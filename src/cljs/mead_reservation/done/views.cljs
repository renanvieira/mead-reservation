(ns mead-reservation.done.views)

(defn done-page []
  (fn []
    [:div {:class "container-fluid"}
     [:div {:id "main" :class "row justify-content-center"}
      [:div {:class "mx-auto"}
       [:h1.title "AÊ!"]
       [:iframe {:src "https://giphy.com/embed/7o6oVRTLFI2GY" :frame-border 0 :width "366" :height "480"  }]
       [:p.h3 "Seu hidromel está reservado!"]
       ]]]
    ))