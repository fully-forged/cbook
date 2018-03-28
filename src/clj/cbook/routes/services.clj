(ns cbook.routes.services
  (:require [com.walmartlabs.lacinia.util :refer [attach-resolvers]]
            [com.walmartlabs.lacinia.schema :as schema]
            [com.walmartlabs.lacinia :as lacinia]
            [clojure.data.json :as json]
            [clojure.edn :as edn]
            [ring.util.http-response :refer :all]
            [compojure.api.sweet :refer :all]
            [cbook.db.core :as db]))

(defn get-ingredient [context args value]
  (db/get-ingredient (select-keys args [:id])))

(defn get-ingredients [context args value]
  (db/get-ingredients))

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

(defn execute-request [body]
  (let [json-body (json/read-str body)
        query (get json-body "query")]
    (-> (lacinia/execute compiled-schema query nil nil)
        (json/write-str))))

(defapi service-routes
  (POST "/api" [:as {body :body}]
    (ok (execute-request (slurp body)))))
