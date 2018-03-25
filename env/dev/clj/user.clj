(ns user
  (:require [luminus-migrations.core :as migrations]
            [cbook.config :refer [env]]
            [mount.core :as mount]
            [cbook.figwheel :refer [start-fw stop-fw cljs]]
            [cbook.core :refer [start-app]]))

(defn start []
  (mount/start-without #'cbook.core/repl-server))

(defn stop []
  (mount/stop-except #'cbook.core/repl-server))

(defn restart []
  (stop)
  (start))

(defn reset-db []
  (migrations/migrate ["reset"] {:database-url (:config-database-url env)}))

(defn migrate []
  (migrations/migrate ["migrate"] (select-keys env [:database-url])))

(defn rollback []
  (migrations/migrate ["rollback"] (select-keys env [:database-url])))

(defn create-migration [name]
  (migrations/create name (select-keys env [:database-url])))


