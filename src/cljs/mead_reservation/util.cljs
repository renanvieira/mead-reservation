(ns mead-reservation.util
  (:require [clojure.string :as string]))

(defn to-bullet-list [array]
  (into [:ul] (map #(vector :li %)) array))

(defn validate-email
  [email]
  (let [pattern #"[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?"]
    (and (string? email) (empty? (re-matches pattern email)))))


(defn validate-integer
  "Test if value is below zero. Attribute :include-zero can be used to check if is minor or equal to zero"
  ([value] (validate-integer value {:include-zero true}))
  ([value attrs]
   (if (and (contains? attrs :include-zero) (true? (:include-zero attrs)))
     (and (not (zero? value)) (neg? value))
     (neg? value))))


(defn is_valid_field
  ([validation-method error-map key value]
   (let [err-map error-map
         default-message (list "Campo " [:strong (string/capitalize (name key))] " é obrigatório!")]
     (is_valid_field validation-method err-map key value default-message)))

  ([validation-method error-map key value msg]
   (let [err-map error-map
         msg (vec (conj msg :span))]
     (if (true? (validation-method value))
       (do
         (swap! error-map assoc key msg)
         (boolean false))
       (do
         (swap! error-map dissoc @err-map key)
         (boolean true))
       ))))