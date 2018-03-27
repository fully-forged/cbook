(ns cbook.test.handler
  (:require [clojure.test :refer :all]
            [ring.mock.request :refer :all]
            [cbook.handler :refer :all]
            [cheshire.core :refer [parse-string]]
            [mount.core :as mount]))

(use-fixtures
  :once
  (fn [f]
    (mount/start #'cbook.config/env
                 #'cbook.handler/app)
    (f)))

(def get-ingredients-payload
  "{ GetIngredients {
      id
     }
   }")

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
