(ns cbook.views
  (:require [re-frame.core :as rf]
            [re-graph.core :as re-graph]
            [venia.core :as v]))

(def >evt rf/dispatch)
(def <sub (comp deref rf/subscribe))

(def test-query
  (v/graphql-query {:venia/queries [[:ingredients [:id :name]]]}))

(defn load-ingredients! []
  (>evt [::re-graph/query
         test-query
         {}
         [:got-ingredients]]))

(defn load-button []
 [:input {:type "button" :value "load"
          :on-click load-ingredients!}])

(defn home-page []
  [:div.container
   [:h1 "Home"]
   [load-button]])

(def pages
  {:home #'home-page})

(defn nav-link [uri title page]
  [:li.nav-item
   {:class (when (= page (<sub [:page])) "active")}
   [:a.nav-link {:href uri} title]])

(defn navbar []
  [:nav.navbar.navbar-dark.bg-primary.navbar-expand-md
   {:role "navigation"}
   [:button.navbar-toggler.hidden-sm-up
    {:type "button"
     :data-toggle "collapse"
     :data-target "#collapsing-navbar"}
    [:span.navbar-toggler-icon]]
   [:a.navbar-brand {:href "#/"} "cbook"]
   [:div#collapsing-navbar.collapse.navbar-collapse
    [:ul.nav.navbar-nav.mr-auto
     [nav-link "#/" "Home" :home]]]])

(defn page []
  [:div
   [navbar]
   [(pages (<sub [:page]))]])
