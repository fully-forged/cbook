(ns cbook.resolver
  (:require [com.walmartlabs.lacinia.executor :as executor]
            [cbook.db.core :as db]))

(defn- extract-cols [context]
  (mapv name (executor/selections-seq context)))

(defn get-ingredient [context args value]
  (db/get-ingredient {:id (:id args) :cols (extract-cols context)}))

(defn get-ingredients [context args value]
  (db/get-ingredients {:cols (extract-cols context)}))

(defn create-ingredient! [context args value]
  (db/create-ingredient! (select-keys args [:name])))
