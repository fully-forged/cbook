(ns cbook.subs
  (:require [re-frame.core :refer [reg-sub]]))

(reg-sub
  :page
  (fn [db _]
    (:page db)))

(reg-sub
  :ingredients
  (fn [db _]
    (:ingredients db)))
