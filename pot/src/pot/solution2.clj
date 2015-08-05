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
         (fd/in have (fd/interval 6))
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
           [(== b-slot 0) (!= a-remain 0)])
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

;([5 0] [5 1] [4 1] [4 2] [3 2])

;[5 0] [0 5] (5 0) [4 1]       [0 5] (5 0) [3 2]
;(0 6) [5 1]       [6 0] (0 6) [4 2]       [6 0]

#_(defne acto [p5 p6 act P5 P6]
         ([_ _ :fill-5 [5 0] _]
           (poto p6 6) (== p6 P6)
           (poto p5 5) (!= p5 P5))
         ([_ _ :fill-6 _ [6 0]]
           (poto p5 5) (== p5 P5)
           (poto p6 6) (!= p6 P6))
         ([_ _ :dump-5 [0 5] _]
           (poto p5 5) (!= p5 P5)
           (poto p6 6) (== p6 P6))
         ([_ _ :dump-6 _ [0 6]]
           (poto p5 5) (== p5 P5)
           (poto p6 6) (!= p6 P6))
         ([_ _ :from5to6 _ _]
           (poto p5 5) (poto p6 6)
           (pouro p5 p6 P5 P6))
         ([_ _ :from6to5 _ _]
           (poto p5 5) (poto p6 6)
           (pouro p6 p5 P6 P5)))

#_(defne multi-acto [p5 p6 acts P5 P6]
         ([_ _ [] p5 p6])
         ([_ _ [head-act . tail-act] _ _]
           (fresh [m5 m6]
                  (acto p5 p6 head-act m5 m6)
                  (multi-acto m5 m6 tail-act P5 P6))))

