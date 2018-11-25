(ns mead-reservation.home-page.views
  (:require [mead-reservation.util :as util]
            [reagent.core :as reagent :refer [atom]]
            [reagent-forms.core :refer [bind-fields]]))

(def ^:private form-data (atom {:name nil :email nil :quantity 1}))
(def ^:private errors (atom {}))

(defn toggle-alert [error_map]
  (fn []
    (if (not (empty? error_map))
      (if (> (count error_map) 0)
        [:div.alert.alert-danger {:field :alert} (util/to-bullet-list (vals error_map))]))))

(defn check-required-fields [reservation]
  (let [name (:name @reservation)
        email (:email @reservation)
        quantity (:quantity @reservation)
        doc form-data
        error-map errors
        is_form_valid (atom false)]

    (if (util/is_valid_field empty? error-map :name name)
      (reset! is_form_valid true)
      (reset! is_form_valid false))

    (if (util/is_valid_field util/validate-integer error-map :quantity quantity)
      (reset! is_form_valid true)
      (reset! is_form_valid false))

    (if (util/is_valid_field empty? error-map :email email)
      (do
        (reset! is_form_valid true)
        (if (util/is_valid_field util/validate-email error-map :email email (list "Campo " [:strong "Email"] " inválido!"))
          (reset! is_form_valid true)
          (reset! is_form_valid false)))
      (reset! is_form_valid false))


    (if (true? @is_form_valid)
      (do
        (swap! doc assoc-in [:email] email)
        (swap! doc assoc-in [:name] name)
        (swap! doc assoc-in [:quantity] quantity)
        ;(set! (.-href js/window.location) "done")
        ))))

(defn form-template []
  (let [reservation form-data
        quantity (:quantity @form-data)]
    (fn []
      [:form
       [:div.form-group
        [:label "Nome:"]
        [:input {:id            :name
                 :name          :name
                 :class         "form-control"
                 :type          :text
                 :required      true
                 :default-value (:name @form-data)
                 :on-change     #(swap! form-data assoc :name (-> % .-target .-value))
                 }]]
       [:div.form-group
        [:label "Email:"]
        [:input {:id            :email
                 :name          :email
                 :class         "form-control"
                 :type          :email
                 :required      true
                 :default-value (:email @form-data)
                 :on-change     #(swap! form-data assoc :email (-> % .-target .-value))
                 }]]
       [:div.form-group
        [:label {:style {:marginRight "5px"}} "Quantidade:"]
        [:label (:quantity @form-data)]
        [:input {:id            :quantity
                 :name          :quantity
                 :class         "form-control"
                 :type          :range
                 :min           1
                 :max           10
                 :required      true
                 :default-value (:quantity @form-data)
                 :on-change     #(swap! form-data assoc :quantity (-> % .-target .-value))
                 }]]

       [:div.form-group
        [:button..btn.btn-primary {:type     "button"
                                   :on-click #(check-required-fields reservation)
                                   } "Enviar!"]]])))



;; -------------------------
;; Page Component
;; -------------------------

(defn home-page []
  (let [doc form-data
        error errors]
    (fn []
      [:div {:class "container-fluid"}
       [:div {:id "main" :class "row justify-content-center"}
        [:div {:class "mx-auto"}
         [:h1 ""]
         [bind-fields form-template doc]
         [(toggle-alert @error)]]]])))