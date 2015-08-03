(ns boats.core
  (:refer-clojure :exclude [==])
  (:use clojure.core.logic)
  (:use clojure.pprint)
  (:gen-class))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))

[:Moore :Downing :Hall :Barnacle :Parker]                   ; father
[:Lorna :Melissa :Rosalind :Gabrielle :Mary]                ; boat's name
['(lvar) '(lvar) '(lvar) :Melissa '(lvar)]                  ; daughter's name
; Gabrielle's father's boat's name is Parker's daughter'name

;who is Lorna's father

(defne same-indexo [ls1 ls2 elem1 elem2]
       ([[elem1 . _] [elem2 . _] _ _])
       ([[head1 . tail1] [head2 . tail2] _ _]
         (same-indexo tail1 tail2 elem1 elem2)))

(pprint
  (run* [q]
        (fresh [boats g1 g2 g3 g5]
               (== boats [:Lorna :Melissa :Rosalind :Gabrielle :Mary])
               (== q [g1 g2 g3 :Melissa g5])
               (permuteo boats q)
               (!= g1 :Lorna) (!= g2 :Melissa) (!= g3 :Rosalind) (!= g5 :Mary)
               (same-indexo boats q g5 :Gabrielle)
               ;(== g1 :Mary)
               )))
