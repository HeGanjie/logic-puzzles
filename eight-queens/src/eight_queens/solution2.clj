(ns eight-queens.solution2
  (:refer-clojure :exclude [==])
  (:use clojure.core.logic)
  (:require [clojure.core.logic.fd :as fd])
  (:use clojure.pprint)
  (:gen-class))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))

(def chess-width 8)

(def xy-indexs
  (for [x (range chess-width)
        y (range chess-width)]
    [x y]))

(defne all-zeroo [coll]
       ([[0]])
       ([[0 . tail]]
         (all-zeroo tail)))

(defn in?
  "true if seq contains elm"
  [seq elm]
  (some #(= elm %) seq))

(defne check-affectAreao [pair]
       ([[0 _]])
       ([[1 affectArea]]
         (all-zeroo affectArea)))

(defne sumo [coll s]
       ([[] 0])
       ([[s] s])
       ([[a b . tail] _]
         (fresh [c shorter]
                (fd/+ a b c)
                (conso c tail shorter)
                (sumo shorter s))))


(time
  (pprint
    (let [vars (repeatedly (* chess-width chess-width) lvar)
          rows (->> vars (partition chess-width) (map vec) (into []))
          frontslash-group (group-by (partial apply +) xy-indexs)
          backslash-group (group-by (partial apply -) xy-indexs)
          affect-areas (for [[x y] xy-indexs]
                         [(get-in rows [x y])
                          (->>
                            [(nth (partition chess-width xy-indexs) x)
                             (nth (apply map vector (partition chess-width xy-indexs)) y)
                             (->> frontslash-group (filter #(in? (val %) [x y])) first val)
                             (->> backslash-group (filter #(in? (val %) [x y])) first val)]
                            (apply concat) (remove #(= [x y] %)) (map #(get-in rows %)))])]
      (let [res (run 1 [q]
                     (== q vars)
                     (sumo vars chess-width)
                     (everyg check-affectAreao affect-areas)
                     )]
        (map #(partition chess-width %) res))
      )))

