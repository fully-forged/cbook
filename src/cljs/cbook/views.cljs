(ns cbook.views
  (:require [re-frame.core :as rf]))

(def >evt rf/dispatch)
(def <sub (comp deref rf/subscribe))

(defn ingredient-item [ingredient]
  ^{:key (:id ingredient)} [:tr
                            [:td (:id ingredient)]
                            [:td (:name ingredient)]])

(defn ingredients-list [ingredients]
  [:table.table.is-striped.is-hoverable.is-fullwidth.is-bordered
   [:thead
    [:tr
     [:th "ID"]
     [:th "Name"]]]
   [:tbody
     (map ingredient-item ingredients)]])

(defn create-bar [new-ingredient-name]
  (let [submit-hander (fn [event]
                        (>evt [:create-ingredient])
                        (.preventDefault event))]
    [:form
      {:on-submit submit-hander}
      [:div.field.is-horizontal
        [:div.field-label.is-normal
          [:label.label "Add new"]]
        [:div.field-body
          [:div.field
            [:div.control
              [:input.input {:type "text"
                             :value new-ingredient-name
                             :placeholder "e.g. Cumin"
                             :on-change #(>evt [:update-new-ingredient-name (-> % .-target .-value)])}]]]]]
      [:div.field.is-horizontal
        [:div.field-label]
        [:div.field-body
          [:div.field
            [:div.control
              [:a.button.is-info {:on-click #(>evt [:create-ingredient])}
                                 "Save"]]]]]]))

(defn home-page []
  [:div.container
   [:h1.title "Available ingredients"]
   [ingredients-list (<sub [:ingredients])]
   [create-bar (<sub [:new-ingredient-name])]])

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
