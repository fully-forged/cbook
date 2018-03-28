(ns cbook.core
  (:require [reagent.core :as r]
            [re-frame.core :as rf]
            [re-graph.core :as re-graph]
            [venia.core :as v]
            [secretary.core :as secretary]
            [ajax.core :refer [GET POST]]
            [cbook.history :refer [hook-browser-navigation!]]
            [cbook.ajax :refer [load-interceptors!]]
            [cbook.events]
            [cbook.subs]))

(defn nav-link [uri title page]
  [:li.nav-item
   {:class (when (= page @(rf/subscribe [:page])) "active")}
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

(defn home-page []
  [:div.container
   [:h1 "Home"]])

(def pages
  {:home #'home-page})

(defn page []
  [:div
   [navbar]
   [(pages @(rf/subscribe [:page]))]])

(secretary/set-config! :prefix "#")

(secretary/defroute "/" []
  (rf/dispatch [:set-active-page :home]))

(defn mount-components []
  (rf/clear-subscription-cache!)
  (r/render [#'page] (.getElementById js/document "app")))

(def re-graph-options
  {:ws-url nil
   :http-url "/api"})

(def test-query
  (v/graphql-query {:venia/queries [[:GetIngredients [:id]]]}))

(defn init! []
  (rf/dispatch-sync [:initialize-db])
  (rf/dispatch [::re-graph/init re-graph-options])
  (rf/dispatch [::re-graph/query
                test-query
                {}
                [:graphql-test]])
  (load-interceptors!)
  (hook-browser-navigation!)
  (mount-components))
