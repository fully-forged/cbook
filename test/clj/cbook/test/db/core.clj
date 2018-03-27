(ns cbook.test.db.core
  (:require [cbook.db.core :refer [*db*] :as db]
            [luminus-migrations.core :as migrations]
            [clojure.test :refer :all]
            [clojure.java.jdbc :as jdbc]
            [cbook.config :refer [env]]
            [mount.core :as mount]))

(use-fixtures
  :once
  (fn [f]
    (mount/start
      #'cbook.config/env
      #'cbook.db.core/*db*)
    (migrations/migrate ["reset"] (select-keys env [:database-url]))
    (f)))

(deftest test-users
  (jdbc/with-db-transaction [t-conn *db*]
    (jdbc/db-set-rollback-only! t-conn)
    (let [inserted (db/create-ingredient! {:name "Cinnamon"})
          cinnamon (db/get-ingredient inserted)]
      (is (= "Cinnamon" (:name cinnamon)))
      (is (= [cinnamon] (db/get-ingredients))))))
