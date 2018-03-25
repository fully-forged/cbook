(ns ^:figwheel-no-load cbook.app
  (:require [cbook.core :as core]
            [devtools.core :as devtools]))

(enable-console-print!)

(devtools/install!)

(core/init!)
