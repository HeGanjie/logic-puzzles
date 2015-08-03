(ns pot.core
  (:refer-clojure :exclude [==])
  (:use clojure.core.logic)
  (:use clojure.pprint)
  (:use clojure.core.logic.protocols)
  (:require [clojure.core.logic.fd :as fd])
  (:gen-class))

(defrecord Pot [^Integer size ^Integer have]
  IUninitialized
  (-uninitialized [_]
    (Pot. nil nil)))

(defn -main [& args]
  (println "Hello, World!"))

(defn dryo [pot]
  (== pot (Pot. (lvar) 0)))

(defn fullo [pot]
  (fresh [s]
         (== pot (Pot. s s))))

(defn fillo [before after]
    (fresh [s n]
           (== before (Pot. s n))
           (!= s n)
           (== after (Pot. s s))))

(defn dumpo [before after]
    (fresh [s n]
           (== before (Pot. s n))
           (!= n 0)
           (== after (Pot. s 0))))

(defn pouro [a b A B]
  (fresh [sa sb n m delta x y]
         (== (Pot. sa n) a) (== (Pot. sb m) b)
         (== (Pot. sa x) A) (== (Pot. sb y) B)
         (fd/in n m x (fd/interval 0 6)) (fd/in y delta (fd/interval 1 6)) ; FIXME: biggest pot size may not be 6
         (fd/eq
           (<= n sa)
           (= x (- n delta)) (= y (+ m delta))
           (<= 0 x) (<= y sb))
         (conde
           [(== x 0) (== y sb)]
           [(== x 0) (!= y sb)]
           [(!= x 0) (== y sb)])))

;act [:fill 5] [:dry 5] [:to 5 6]

(defn acto [a b act A B]
  (fresh [p1 p2 sa sb n m x y]
         (== (Pot. sa n) a) (== (Pot. sb m) b)
         (== (Pot. sa x) A) (== (Pot. sb y) B)
         (conde
           [(== act [:fill p1]) (conde
                                  [(== sa p1) (fillo a A) (== b B)]
                                  [(== sb p1) (fillo b B) (== a A)])]
           [(== act [:dry p1]) (conde
                                 [(== sa p1) (dumpo a A) (== b B)]
                                 [(== sb p1) (dumpo b B) (== a A)])]
           [(== act [:to p1 p2]) (conde
                                   [(== sa p1) (== sb p2) (pouro a b A B)]
                                   [(== sb p1) (== sa p2) (pouro b a B A)])])))

(defn multi-acto [a b acts rs]
  (conde
    [(emptyo acts) (emptyo rs)]
    [(fresh [ha ra hr rr tA tB]
            (firsto acts ha) (resto acts ra)
            (firsto rs hr) (resto rs rr)
            (== hr [tA tB])
            (acto a b ha tA tB)
            (multi-acto tA tB ra rr))]))

(defn limito [acts size limited]
  (conde
    [(emptyo acts)]
    [(fresh [h r nsize]
            (firsto acts h) (resto acts r)
            (conde
              [(firsto h :to) (limito r size nil)]
              [(== h [:fill size]) (!= :fill limited) (limito r size :dry)]
              [(== h [:dry size]) (!= :dry limited) (limito r size :fill)]
              [(== h [:fill nsize]) (!= size nsize) (limito r size limited)]
              [(== h [:dry nsize]) (!= size nsize) (limito r size limited)]
              ))]))

(time
  (pprint
    (run 1 [acts rs]
         (fresh [a b dist x y]
                (== a (Pot. 5 0)) (== b (Pot. 6 0))
                (conso [a b] rs dist) (distincto dist)
                (multi-acto a b acts rs)
                ;(limito acts 5 nil) (limito acts 6 nil)
                ;(appendo f [[{:size 5, :have 5} {:size 6, :have 1}]] rs)
                (appendo (lvar) [[(Pot. 5 x) (Pot. 6 y)]] rs)
                (conde
                  [(== x 3)]
                  [(== y 3)])
                ))))

#_
(run* [x y]
      (acto {:size 5 :have x} {:size 6 :have y}
            [:to 5 6]
            {:size 5 :have 0} {:size 6 :have 6}))

#_
(run* [n m]
      (pouro {:size 5 :have 5} {:size 6 :have 4} {:size 5 :have n} {:size 6 :have m}))
#_
(run* [x y]
      (pouro {:size 6 :have x} {:size 5 :have y} {:size 6 :have 4} {:size 5 :have 5}))



