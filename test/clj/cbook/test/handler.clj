(ns cbook.test.handler
  (:require [cbook.db.core :refer [*db*] :as db]
            [luminus-migrations.core :as migrations]
            [clojure.test :refer :all]
            [clojure.java.jdbc :as jdbc]
            [ring.mock.request :refer :all]
            [cbook.handler :refer :all]
            [cheshire.core :refer [parse-string]]
            [cbook.config :refer [env]]
            [mount.core :as mount]))

(def get-ingredients-payload
  "{\"query\":\"query { GetIngredients {\n      id\n     }\n   }\",\"variables\":{}}")

(defn get-json-body [response]
  (parse-string (:body response)))

(deftest test-app
  (testing "main route"
    (let [response (app (request :get "/"))]
      (is (= 200 (:status response)))))

  (testing "not-found route"
    (let [response (app (request :get "/invalid"))]
      (is (= 404 (:status response)))))

  (testing "graphql endpoint"
    (let [response (app (request :post "/api" get-ingredients-payload))]
      (is (= ["data"] (keys (get-json-body response))))
      (is (= 200 (:status response))))))

(defn migrate [test-case]
  (mount/start
    #'cbook.config/env
    #'cbook.db.core/*db*)
  (migrations/migrate ["reset"] (select-keys env [:database-url])))

(defn clear [test-case]
  (jdbc/with-db-transaction [t-conn *db*]
    (jdbc/db-set-rollback-only! t-conn)
    (test-case)))

(use-fixtures :once migrate)
(use-fixtures :each clear)
