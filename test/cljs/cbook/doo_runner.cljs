(ns cbook.doo-runner
  (:require [doo.runner :refer-macros [doo-tests]]
            [cbook.core-test]))

(doo-tests 'cbook.core-test)

