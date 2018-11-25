(ns ^:figwheel-no-load mead-reservation.dev
  (:require
    [mead-reservation.core :as core]
    [devtools.core :as devtools]))

(devtools/install!)

(enable-console-print!)

(core/init!)
