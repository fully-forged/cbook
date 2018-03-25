(ns cbook.env
  (:require [clojure.tools.logging :as log]))

(def defaults
  {:init
   (fn []
     (log/info "\n-=[cbook started successfully]=-"))
   :stop
   (fn []
     (log/info "\n-=[cbook has shut down successfully]=-"))
   :middleware identity})
