(defproject demo "0.1.0-SNAPSHOT"
  :plugins [
            [lein-cljsbuild "1.1.5"]
            [lein-ancient "0.6.10"]
            [lein-figwheel "0.5.8"]]

  :hooks [leiningen.cljsbuild]

  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/clojurescript "1.9.293"]

                 [com.taoensso/timbre "4.8.0"]
                 [reagent "0.6.0"]
                 [re-frame "0.9.1"]
                 [cljs-ajax "0.6.0"]

                 [ring/ring-core "1.6.1"]
                 [ring/ring-jetty-adapter "1.6.1"]
                 [compojure "1.6.0"]
                 ]

  :main demo.backend
  :cljsbuild {:builds
              [
               {:id           :dev
                :figwheel     {:on-jsload "demo.ui/render"}
                :source-paths ["src/cljs"]
                :compiler     {:output-dir    "resources/public/js/compiled"
                               :output-to     "resources/public/js/compiled/demo.js"
                               :optimizations :none
                               :pretty-print  true
                               }
                }
               ]
              }
  )

