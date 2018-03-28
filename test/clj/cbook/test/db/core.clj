(ns cbook.test.db.core
  (:require [cbook.db.core :refer [*db*] :as db]
            [luminus-migrations.core :as migrations]
            [clojure.test :refer :all]
            [clojure.java.jdbc :as jdbc]
            [cbook.config :refer [env]]
            [mount.core :as mount]))

(deftest test-ingredients
  (let [inserted (db/create-ingredient! {:name "Cinnamon"})
        cinnamon (db/get-ingredient inserted)]
    (is (= "Cinnamon" (:name cinnamon)))
    (is (= [cinnamon] (db/get-ingredients)))))

(defn reset-db! [test-case]
  (mount/start
    #'cbook.config/env
    #'cbook.db.core/*db*)
  (jdbc/with-db-transaction [t-conn *db*]
    (migrations/migrate ["reset"] (select-keys env [:database-url]))
    (jdbc/db-set-rollback-only! t-conn)
    (test-case)))

(use-fixtures :each reset-db!)
