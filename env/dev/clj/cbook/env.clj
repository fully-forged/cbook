(ns cbook.env
  (:require [selmer.parser :as parser]
            [clojure.tools.logging :as log]
            [cbook.dev-middleware :refer [wrap-dev]]))

(def defaults
  {:init
   (fn []
     (parser/cache-off!)
     (log/info "\n-=[cbook started successfully using the development profile]=-"))
   :stop
   (fn []
     (log/info "\n-=[cbook has shut down successfully]=-"))
   :middleware wrap-dev})
