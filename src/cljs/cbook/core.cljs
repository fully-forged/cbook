(ns cbook.core
  (:require [reagent.core :as r]
            [re-frame.core :as rf]
            [re-graph.core :as re-graph]
            [secretary.core :as secretary]
            [ajax.core :refer [GET POST]]
            [cbook.history :refer [hook-browser-navigation!]]
            [cbook.ajax :refer [load-interceptors!]]
            [cbook.events]
            [cbook.subs]
            [cbook.views]))

(secretary/set-config! :prefix "#")

(secretary/defroute "/" []
  (rf/dispatch [:set-active-page :home])
  (rf/dispatch [:get-ingredients]))

(defn mount-components []
  (rf/clear-subscription-cache!)
  (r/render [#'cbook.views/page] (.getElementById js/document "app")))

(def re-graph-options
  {:ws-url nil
   :http-url "/api"})

(defn init! []
  (rf/dispatch-sync [:initialize-db])
  (rf/dispatch [::re-graph/init re-graph-options])
  (load-interceptors!)
  (hook-browser-navigation!)
  (mount-components))
