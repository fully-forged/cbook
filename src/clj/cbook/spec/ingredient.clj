(ns cbook.spec.ingredient
  (:require [clojure.spec.alpha :as s]
            [clj-time.spec]))

(s/def ::id uuid?)
(s/def ::name string?)
(s/def ::created_at :clj-time.spec/date-time)
(s/def ::updated_at :clj-time.spec/date-time)

(s/def ::ingredient
  (s/keys :req-un [::id ::name ::created_at ::updated_at]))
