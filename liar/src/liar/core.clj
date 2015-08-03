(ns liar.core
  (:refer-clojure :exclude [==])
  (:use clojure.core.logic)
  (:use clojure.pprint)
  (:gen-class))

(defn -main
  [& args]
  (println "Hello, World!"))

; Betty : I'm 3th, Ketty 2nd
; Ayser : I'm 1st, John 2nd
; John  : I'm 3th, Ayser 5th
; Ketty : I'm 2nd, Mary 4th
; Mary  : I'm 4th, Betty 1st
; 1 true 1 false
; get real rank

(def income [[[:Betty 3] [:Ketty 2]]
             [[:Ayser 1] [:John 2]]
             [[:John 3] [:Ayser 5]]
             [[:Ketty 2] [:Mary 4]]
             [[:Mary 4] [:Betty 1]]])

(defna checkTrueo [t ts fs]
       ([_ [] []])
       ([[name index] [[head-t-name head-t-index] . tail-ts] [[head-f-name head-f-index] . tail-fs]]
         (conde
           [(== name head-t-name) (== index head-t-index)]
           [(!= name head-t-name) (!= index head-t-index)])
         (conde
           [(== name head-f-name) (!= index head-f-index)]
           [(!= name head-f-name)])
         (checkTrueo t tail-ts tail-fs)))

(defna checkFalseo [f ts fs]
       ([_ [] []])
       ([[name index] [[head-t-name head-t-index] . tail-ts] [[head-f-name head-f-index] . tail-fs]]
         (conde
           [(== name head-t-name) (!= index head-t-index)]
           [(!= name head-t-name)])
         (checkFalseo f tail-ts tail-fs)))

(defnu infero [incomes trues falses]
       ([_ [] []])
       ([[[t f] . tail-income] [t . tail-ts] [f . tail-fs]]
         (checkTrueo t tail-ts tail-fs)
         (checkFalseo f tail-ts tail-fs)
         (infero tail-income tail-ts tail-fs))
       ([[[f t] . tail-income] [t . tail-ts] [f . tail-fs]]
         (checkTrueo t tail-ts tail-fs)
         (checkFalseo f tail-ts tail-fs)
         (infero tail-income tail-ts tail-fs)))

(time
  (pprint (run 3 [q]
               (fresh [t1 t2 t3 t4 t5]
                      (== q [t1 t2 t3 t4 t5])
                      (infero income q (lvar)))
               )))

