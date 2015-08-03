(ns eight-queens.core
  (:refer-clojure :exclude [==])
  (:use clojure.core.logic)
  (:require [clojure.core.logic.fd :as fd])
  (:use clojure.pprint)
  (:gen-class))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))

(def chess-width 4)

(def xy-indexs
  (for [x (range chess-width)
        y (range chess-width)]
    [x y]))

(def gen-permute-base
  (memoize
    (fn [len]
      (into [1] (-> len dec (repeat 0))))))

(defn not-contain [x coll]
  (project [x coll]
           (== true (not-any? #(= x %) coll))))

(defn contain-once [x coll]
  (project [x coll]
           (== true (->> coll (filter #(= x %)) count (= 1)))))

(defne all-zeroo [coll]
       ([[0]])
       ([[0 . tail]]
         (all-zeroo tail)))

(defn one-onceo [coll]
  (fresh [remain]
         (rembero 1 coll remain)
         (all-zeroo remain)))

(defn not-contain-more-than-once-one [coll]
  (conde
    [(all-zeroo coll)]
    [(one-onceo coll)]))     ; todo: occer chess-width times

(time
  (pprint
    (let [vars (repeatedly (* chess-width chess-width) lvar)
          rows (->> vars (partition chess-width) (map vec) (into []))
          cols (apply map vector rows)
          frontslash (map (fn [xy-coll]
                            (map #(get-in rows %) xy-coll))
                          (->> xy-indexs (group-by (partial apply +)) (sort-by key) vals))
          backslash (map (fn [xy-coll]
                           (map #(get-in rows %) xy-coll))
                         (->> xy-indexs (group-by (partial apply -)) (sort-by key) vals))]
      (let [res (run 3 [q]
                     (== q vars)
                     (everyg one-onceo rows)
                     (everyg one-onceo cols)
                     (everyg not-contain-more-than-once-one frontslash)
                     (everyg not-contain-more-than-once-one backslash)
                     )]
        (map #(partition chess-width %) res))
      )))

