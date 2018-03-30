(ns cbook.queries
  (:require [re-graph.core :as re-graph]
            [venia.core :as v]))

(def refresh-ingredients
  (v/graphql-query {:venia/queries [[:ingredients [:id :name :created_at :updated_at]]]}))
