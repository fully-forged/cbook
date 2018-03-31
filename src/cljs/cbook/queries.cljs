(ns cbook.queries
  (:require [re-graph.core :as re-graph]))

(def refresh-ingredients
  "{
    ingredients {
      id, name, created_at, updated_at
    }
  }")

(def create-ingredient
  "CreateIngredient($name: String!) {
    CreateIngredient(name: $name) {
      id, name, created_at, updated_at
    }
  }")
