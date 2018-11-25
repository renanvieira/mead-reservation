(ns mead-reservation.handler
  (:require [reitit.ring :as reitit-ring]
            [mead-reservation.middleware :refer [middleware]]
            [hiccup.page :refer [include-js include-css html5]]
            [config.core :refer [env]]))

(def mount-target
  [:div#app
   [:h2 "Welcome to mead-reservation"]
   [:p "please wait while Figwheel is waking up ..."]
   [:p "(Check the js console for hints if nothing exсiting happens.)"]])

(defn head []
  [:head
   [:meta {:charset "utf-8"}]
   [:meta {:name    "viewport"
           :content "width=device-width, initial-scale=1"}]

   ;<link rel="icon" href="/favicon.ico?v=1.1">
   [:link {:href "/images/favicon.ico?v=1.0", :rel "icon"}]
   (include-css "https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css")
   (include-css (if (env :dev) "/css/site.css" "/css/site.min.css"))])

(defn loading-page []
  (html5
    (head)
    [:body {:class "body-container"}
     mount-target
     (include-js "/js/app.js")]))

(defn index-handler
  [_request]
  {:status  200
   :headers {"Content-Type" "text/html"}
   :body    (loading-page)})

(def app
  (reitit-ring/ring-handler
    (reitit-ring/router
      [
       ["/" {:get {:handler index-handler}}]
       ["/done" {:get {:handler index-handler}}]
       ["/about" {:get {:handler index-handler}}]]
      {:data {:middleware middleware}})
    (reitit-ring/routes
      (reitit-ring/create-resource-handler {:path "/" :root "/public"})
      (reitit-ring/create-default-handler))))
