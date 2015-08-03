(ns floors.core
  (:refer-clojure :exclude [==])
  (:use clojure.core.logic)
  (:use clojure.pprint)
  (:gen-class))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))

[:Baker :Cooper :Fletcher :Miller :Smith]
; :Baker < 5
; 1 < :Cooper
; 1 < :Fletcher < 5
; :Miller - :Cooper = 1
; 1 < Abs(:Smith - :Flether)
; 1 < Abs(:Cooper - :Flether)

(defne nexto [ls base next]
       ([[base next . _] _ _])
       ([[head . tail] _ _]
         (nexto tail base next)))

(defne not-nexto [ls base elem]
       ([[base] _ _])
       ([[base next . tail] _ _]
         (!= next elem))
       ([[head . tail] _ _]
         (not-nexto tail base elem)))

(defn not-nexto2 [ls base elem]
       (fresh [next]
              (nexto ls base next)
              (!= next elem)))

#_
(pprint
  (run 5 [q]
       (not-nexto2 [:Baker :Cooper :Fletcher :Miller :Smith] q :Smith)))

(pprint
  (run* [q]
       (fresh [f1 f2 f3 f4 f5]
              (permuteo [:Baker :Cooper :Fletcher :Miller :Smith] q)
              (== q [f1 f2 f3 f4 f5])
              (!= f5 :Baker) (!= f1 :Cooper)
              (!= f1 :Fletcher) (!= f5 :Fletcher)
              ;(nexto q :Cooper :Miller)
              (not-nexto q :Smith :Fletcher) (not-nexto q :Fletcher :Smith)
              (not-nexto q :Fletcher :Cooper) (not-nexto q :Cooper :Fletcher)
              )))