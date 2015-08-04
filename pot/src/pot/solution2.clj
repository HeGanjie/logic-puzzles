(ns pot.solution2
  (:refer-clojure :exclude [==])
  (:use clojure.core.logic)
  (:use clojure.pprint)
  (:require [clojure.core.logic.fd :as fd])
  (:gen-class))

(defn -main [& args]
  (println "Hello, World!"))

[5 0] [6 0]                                                 ; full
[0 5] [0 6]                                                 ; empty

(defne pour-outo [before delta after]                  ; [4 1] 4 [0 5], [4 1] 1 [3 2]
       ([[has slot] _ [have inc-slot]]
         (!= delta 0)
         (fd/+ slot delta inc-slot)
         (fd/+ have delta has)))

