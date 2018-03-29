(ns cbook.events
  (:require [cbook.db :as db]
            [cbook.config :as config]
            [re-frame.core :as rf]))

(def standard-interceptors
  [(when config/debug? rf/debug)])

(rf/reg-event-db
  :initialize-db
  standard-interceptors
  (fn [_ _]
    db/default-db))

(rf/reg-event-db
  :set-active-page
  standard-interceptors
  (fn [db [_ page]]
    (assoc db :page page)))

(rf/reg-event-db
  :got-ingredients
  standard-interceptors
  (fn [db [_ {:keys [data errors] :as payload}]]
    (if data
      (assoc db :ingredients (:ingredients data))
      db)))
