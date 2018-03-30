(ns cbook.views
  (:require [re-frame.core :as rf]
            [re-graph.core :as re-graph]
            [cbook.queries :as q]))

(def >evt rf/dispatch)
(def <sub (comp deref rf/subscribe))
(defn >gqevt [query args cback]
  (>evt [::re-graph/query
         query
         args
         cback]))

(defn refresh-button []
  [:div.navbar-item
   [:button.button
     {:on-click (>gqevt q/refresh-ingredients {} [:got-ingredients])}
     "Refresh"]])

(defn ingredient-item [ingredient]
  ^{:key (:id ingredient)} [:tr
                            [:td (:id ingredient)]
                            [:td (:name ingredient)]
                            [:td (:created_at ingredient)]
                            [:td (:updated_at ingredient)]])

(defn ingredients-list [ingredients]
  [:table.table.is-striped.is-hoverable.is-fullwidth.is-bordered
   [:thead
    [:tr
     [:th "ID"]
     [:th "Name"]
     [:th "Created at"]
     [:th "Updated at"]]]
   [:tbody
     (map ingredient-item ingredients)]])

(defn home-page []
  [:div.container
   [:h1.title "Available ingredients"]
   [ingredients-list (<sub [:ingredients])]
   [:nav.navbar
    [:div.navbar-menu
      [:div.navbar-end
       [refresh-button]]]]])

(def pages
  {:home #'home-page})

(defn nav-link [uri title page]
  [:a.navbar-item
   {:class (when (= page (<sub [:page])) "active")
    :href uri}
   title])

(defn navbar []
  [:nav.navbar.is-primary {:role "navigation"}
    [:div.navbar-brand
      [nav-link "#/" "Home" :home]]])

(defn page []
  [:div
   [navbar]
   [(pages (<sub [:page]))]])
