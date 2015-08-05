(ns xxoo.core
  (:use seesaw.core)
  (:gen-class))

(defn -main [& args]
  )

(invoke-later
  (let [f (frame :title "Game",
                 :size [300 :by 300]
                 :content (grid-panel
                            :columns 3
                            :items (repeatedly 9 button)),
                 :on-close :dispose)]
    (.setLocationRelativeTo f nil)
    (show! f)
    (listen (select f [:JButton]) :mouse-clicked (fn [e] (config! e :text "x")))))





