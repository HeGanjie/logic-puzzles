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

(defne poto [coll size]
       ([[have slot] _]
         (fd/+ have slot size)))

(defne pour-outo [before delta after]                       ; [4 1] 4 [0 5], [4 1] 1 [3 2]
       ([[has slot] _ [have inc-slot]]
         (!= delta 0)
         (fd/+ slot delta inc-slot)
         (fd/+ have delta has)))

; 从 [0 5] 怎么到 [3 2] -> 得到 [4 2]
; 从 [0 6] 怎么得到 [4 2] -> 得到 [1 4]
; 从 [0 5] 怎么得到 [1 4] -> 得到 [2 4] / [1 5]
; ……

(defne pouro [a b A B]                                      ; [5 0] [0 6] -> [0 5] [5 1]
       ([_ _ [a-remain _] [_ b-slot]]
         (conde
           [(== a-remain 0)]
           [(== b-slot 0)])
         (fresh [delta]
                (pour-outo a delta A)
                (pour-outo B delta b))))

(defne exchangeo [precondition need]
       ([_ [have slot]]
         (fresh [pot2]
                (conde
                  [(fd/+ have slot 5) (poto pot2 6)]
                  [(fd/+ have slot 6) (poto pot2 5)])
                (conde
                  [(pouro [(lvar) 0] precondition need pot2)]
                  [(pouro precondition [0 (lvar)] pot2 need)])
                )))

(defne iterateo [fo coll]
       ([_ [a b]]
         (fo a b))
       ([_ [a . tail]]
         (fresh [b]
                (firsto tail b)
                (fo a b)
                (iterateo fo tail))))

(time
  (pprint
    (run 1 [q]
         (distincto q)
         (conde
           [(firsto q [0 5])] [(firsto q [5 0])]
           [(firsto q [0 6])] [(firsto q [6 0])])
         (appendo (lvar) [[3 (lvar)]] q)
         (iterateo exchangeo q))))