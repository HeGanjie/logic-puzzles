(ns xxoo.core
  (:use seesaw.core)
  (:refer-clojure :exclude [==])
  (:use clojure.core.logic)
  (:use clojure.pprint)
  (:require [clojure.core.logic.fd :as fd])
  (:require [clojure.math.combinatorics :as combo])
  (:gen-class))

; http://neverstopbuilding.com/minimax

(defn -main [& args]
  )

(def chess-width 3)

(def xy-indexs
  (for [x (range chess-width) y (range chess-width)]
    [x y]))

(defn init [vars hints]
  (if (seq vars)
    (let [hint (first hints)]
      (all
        (if-not (= :_ hint)
          (== (first vars) hint)
          succeed)
        (init (next vars) (next hints))))
    succeed))

(defn same-counto [vars]
  (conde
    [(== vars [])]
    [(fresh [remain0 remain1]
            (rembero :x vars remain0)
            (rembero :o remain0 remain1)
            (same-counto remain1))]))

(defne existo [coll elem]
       ([[elem . _] _])
       ([[e . tail] _]
         (!= e elem)
         (existo tail elem)))

(defne not-existo [coll elem]
       ([[] _])
       ([[e . tail] _]
         (!= e elem)
         (not-existo tail elem)))

(defne count-limito [coll elem count]
       ([_ _ 0]
         (not-existo coll elem))
       ([[elem . tail] _ _]
         (!= count 0)
         (fresh [dec]
                (fd/+ dec 1 count)
                (count-limito tail elem dec)))
       ([[e . tail] _ _]
         (!= e elem)
         (!= 0 count)
         (count-limito tail elem count)))

(defne exist-permuteo [coll permute]
       ([[e . tail] _]
         (conde
           [(permuteo permute e)]
           [(exist-permuteo tail permute)])))

(defn not-exist-permuteo [coll items]
  (everyg #(not-existo coll %) (combo/permutations items)))

(defn think [chess-state]
  (let [vars (repeatedly (* chess-width chess-width) lvar)
        rows (->> vars (partition chess-width) (map vec) (into []))
        cols (into [] (apply map vector rows))
        frontslash [(nth vars 2) (nth vars 4) (nth vars 6)]
        backslash [(nth vars 0) (nth vars 4) (nth vars 8)]
        groups (concat rows cols [frontslash] [backslash])
        limit (count (filter #(= :x %) chess-state))]
    (println "before think: ") (pprint chess-state)
    (let [rst (run 1 [q]
                   (== q vars)
                   (everyg #(conde [(== % :_)] [(== % :o)] [(== % :x)]) vars)
                   (init vars chess-state)
                   (count-limito vars :o limit)
                   (count-limito vars :x limit)
                   (not-existo groups [:x :x :x])
                   (conde
                     [(existo groups [:o :o :o]) (log "Computer Win!")]
                     [(not-exist-permuteo groups [:o :o :_]) (exist-permuteo groups [:x :x :o]) (log "Defend")]
                     [(not-exist-permuteo groups [:o :o :_]) (not-exist-permuteo groups [:x :x :_]) (log "Random")])
                   )]
      (println "after think: ") (pprint rst)
      (->> rst first (into [])))))

(def chess-status [:_ :_ :_
                   :_ :_ :_
                   :_ :_ :_])

(invoke-later
  (let [f (frame :title "Game"
                 :size [300 :by 300]
                 :content (grid-panel
                            :columns chess-width
                            :items (for [id (range (* chess-width chess-width))]
                                     (button :id (str id) :text "_")))
                 :on-close :dispose)
        btns (select f [:JButton])]
    (.setLocationRelativeTo f nil)
    (.setResizable f false)
    (show! f)
    (listen btns :mouse-released
            (fn [e]
              (when (= "_" (config e :text))
                (config! e :text "x")
                (let [clickingIndex (-> e (config :id) name read-string)
                      result (think (assoc chess-status clickingIndex :x))]
                  (when (seq result)
                    (def chess-status result)
                    (doseq [i (range (* chess-width chess-width))
                            :let [b (nth btns i)
                                  txt (name (nth chess-status i))]]
                      (config! b :text txt)))))))))

