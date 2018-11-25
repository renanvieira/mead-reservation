(ns mead-reservation.core
  (:require [reagent.core :as reagent :refer [atom]]
            [reagent.session :as session]
            [reitit.frontend :as reitit]
            [clerk.core :as clerk]
            [accountant.core :as accountant]
            [mead-reservation.home-page.views :refer [home-page form-data]]
            [mead-reservation.about.views :refer [about-page]]
            [mead-reservation.done.views :refer [done-page]]
            [mead-reservation.util :as util]))

;; -------------------------
;; Routes

(def router
  (reitit/router
    [["/" :index]
     ["/about" :about]
     ["/done" :done]]))

(defn path-for [route & [params]]
  (if params
    (:path (reitit/match-by-name router route params))
    (:path (reitit/match-by-name router route))))

(path-for :about)
(path-for :done)

;; -------------------------
;; Translate routes -> page components

(defn page-for [route]
  (case route
    :index #'home-page
    :about #'about-page
    :done #'done-page))


;; -------------------------
;; Page mounting component

(defn current-page []
  (fn []
    (let [page (:current-page (session/get :route))]
      [:div
       [:header
        [:div {:class "navbar navbar-light bg-light"}
         [:span {:class "navbar-brand mb-0 h1"}
          [:img {:src "/images/mead.png" :alt "Hidromel do Papudo" :title "Hidromel do Papudo" }]]]]
       [page]
       [:footer.fixed-bottom
        [:div.navbar.navbar-light.bg-light
         [:p.mx-auto "made with a lot of beer in mind using "
          [:a {:href "https://clojure.org/"} "Clojure"]
          ", "
          [:a {:href "https://clojurescript.org/"} "Clojurescript"]
          " and "
          [:a {:href "https://github.com/reagent-project"} "Reagent"] "."]]
        ]])))

;; -------------------------
;; Initialize app

(defn mount-root []
  (reagent/render [current-page] (.getElementById js/document "app")))

(defn init! []
  (clerk/initialize!)
  (accountant/configure-navigation!
    {:nav-handler
     (fn [path]
       (let [match (reitit/match-by-path router path)
             current-page (:name (:data match))
             route-params (:path-params match)]
         (reagent/after-render clerk/after-render!)
         (session/put! :route {:current-page (page-for current-page)
                               :route-params route-params})
         (clerk/navigate-page! path)
         ))
     :path-exists?
     (fn [path]
       (boolean (reitit/match-by-path router path)))})
  (accountant/dispatch-current!)
  (mount-root))
