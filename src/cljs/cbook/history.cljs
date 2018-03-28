(ns cbook.history
  (:require [goog.history.EventType :as HistoryEventType]
            [goog.events :as events]
            [secretary.core :as secretary])
  (:import goog.History))

(defn hook-browser-navigation! []
  (doto (History.)
    (events/listen
      HistoryEventType/NAVIGATE
      (fn [event]
        (secretary/dispatch! (.-token event))))
    (.setEnabled true)))
