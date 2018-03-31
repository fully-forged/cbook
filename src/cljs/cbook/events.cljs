(ns cbook.events
  (:require [cbook.db :as db]
            [cbook.config :as config]
            [re-frame.core :as rf]
            [re-graph.core :as re-graph]
            [cbook.queries :as q]))

(def standard-interceptors
  [(when config/debug? rf/debug)])

(def refresh-ingredients-evt
  [::re-graph/query q/refresh-ingredients {} [:got-ingredients]])

(defn create-ingredient-evt [name]
  [::re-graph/mutate q/create-ingredient {:name name} [:created-ingredient]])

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
  :update-new-ingredient-name
  standard-interceptors
  (fn [db [_ new-ingredient-name]]
    (assoc db :new-ingredient-name new-ingredient-name)))

(rf/reg-event-fx
  :get-ingredients
  standard-interceptors
  (fn [cofx event]
    {:db (:db cofx)
     :dispatch refresh-ingredients-evt}))

(rf/reg-event-db
  :got-ingredients
  standard-interceptors
  (fn [db [_ {:keys [data errors] :as payload}]]
    (if data
      (assoc db :ingredients (:ingredients data))
      db)))

(rf/reg-event-fx
  :create-ingredient
  standard-interceptors
  (fn [cofx _]
    (let [db (:db cofx)
          new-ingredient-name (:new-ingredient-name db)]
      {:db (assoc db :new-ingredient-name "")
       :dispatch (create-ingredient-evt new-ingredient-name)})))

(rf/reg-event-db
  :created-ingredient
  standard-interceptors
  (fn [db [_ {:keys [data errors] :as payload}]]
    (if data
      (let [created-ingredient (:CreateIngredient data)]
        (update db :ingredients #(conj % created-ingredient)))
      db)))
