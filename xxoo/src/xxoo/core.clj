(ns xxoo.core
  (:use seesaw.core)
  (:refer-clojure :exclude [==])
  (:use clojure.core.logic)
  (:use clojure.pprint)
  (:require [clojure.core.logic.fd :as fd])
  (:gen-class))

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

(defn think [chess-state]
  (let [vars (repeatedly (* chess-width chess-width) lvar)
        rows (->> vars (partition chess-width) (map vec) (into []))
        cols (into [] (apply map vector rows))
        frontslash [(nth vars 2) (nth vars 4) (nth vars 6)]
        backslash [(nth vars 0) (nth vars 4) (nth vars 8)]]
    (println "before think: ") (pprint chess-state)
    (let [rst (run 1 [q]
                   (== q vars)
                   (everyg #(conde [(== % :_)] [(== % :x)] [(== % :o)]) vars)
                   (init vars chess-state))]
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
                  (def chess-status result)
                  (doseq [i (range (* chess-width chess-width))
                          :let [b (nth btns i)
                                txt (name (nth chess-status i))]]
                    (config! b :text txt))))))))

