(ns cbook.test.db.core
  (:require [cbook.db.core :refer [*db*] :as db]
            [luminus-migrations.core :as migrations]
            [clojure.test :refer :all]
            [clojure.java.jdbc :as jdbc]
            [cbook.config :refer [env]]
            [mount.core :as mount]))

(use-fixtures
  :each
  (fn [test-case]
    (mount/start
      #'cbook.config/env
      #'cbook.db.core/*db*)
    (migrations/migrate ["reset"] (select-keys env [:database-url]))
    (jdbc/with-db-transaction [t-conn *db*]
      (jdbc/db-set-rollback-only! t-conn)
      (test-case))))

(deftest test-ingredients
  (let [inserted (db/create-ingredient! {:name "Cinnamon"})
        cinnamon (db/get-ingredient inserted)]
    (is (= "Cinnamon" (:name cinnamon)))
    (is (= [cinnamon] (db/get-ingredients)))))
