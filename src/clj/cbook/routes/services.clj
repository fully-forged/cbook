(ns cbook.routes.services
  (:require [com.walmartlabs.lacinia.util :refer [attach-resolvers]]
            [com.walmartlabs.lacinia.schema :as schema]
            [com.walmartlabs.lacinia :as lacinia]
            [com.walmartlabs.lacinia.executor :as executor]
            [clojure.data.json :as json]
            [clojure.edn :as edn]
            [clojure.string :as str]
            [ring.util.http-response :refer :all]
            [compojure.api.sweet :refer :all]
            [cbook.db.core :as db]))

(defn get-ingredient [context args value]
  (let [cols (mapv name (executor/selections-seq context))]
    (db/get-ingredient {:id (:id args) :cols cols})))

(defn get-ingredients [context args value]
  (let [cols (mapv name (executor/selections-seq context))]
    (db/get-ingredients {:cols cols})))

(defn create-ingredient! [context args value]
  (db/create-ingredient! (select-keys args [:name])))

(def compiled-schema
  (-> "resources/graphql/schema.edn"
      slurp
      edn/read-string
      (attach-resolvers {:get-ingredient get-ingredient
                         :get-ingredients get-ingredients
                         :create-ingredient! create-ingredient!})
      schema/compile))

(defn unwrap-vars
  [query]
  (:variables (json/read-str query :key-fn keyword)))

(defn variable-map
  "Reads the `variables` query parameter, which contains a JSON string
  for any and all GraphQL variables to be associated with this request.
  Returns a map of the variables (using keyword keys)."
  [raw-query request-method query-params]
  (case request-method
    :get (let [vars (:variables query-params)]
           (if-not (str/blank? vars)
             (json/read-str vars :key-fn keyword)
             {}))
    :post (unwrap-vars raw-query)))

(defn unwrap-query
  [query content-type]
  (case content-type
    "application/json" (:query (json/read-str query :key-fn keyword))
    "application/graphql" query))

(defn extract-query
  [request]
  (case (:request-method request)
    :get (get-in request [:query-params :query])
    :post (slurp (:body request))
    :else ""))

(defn ^:private graphql-handler
  "Accepts a GraphQL query via GET or POST, and executes the query.
  Returns the result as text/json."
  [compiled-schema]
  (let [context {:cache (atom {})}]
    (fn [request]
      (let [raw-query (extract-query request)
            query (unwrap-query raw-query (:content-type request))
            vars (variable-map raw-query (:request-method request) (:query-params request))
            result (lacinia/execute compiled-schema query vars context)
            status (if (-> result :errors seq)
                     400
                     200)]
        {:status status
         :headers {"Content-Type" "application/json"}
         :body (json/write-str result)}))))

(defapi service-routes
  (let [query-handler (graphql-handler compiled-schema)]
    (POST "/api" request (query-handler request))))
