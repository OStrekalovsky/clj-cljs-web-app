(ns demo.backend
  (:require
    [compojure.handler :refer :all]
    [compojure.core :refer :all]
    [compojure.route :as route]
    [ring.adapter.jetty :refer (run-jetty)]
    [ring.util.response :as resp]
    )
  )

(require 'compojure.route)
(defn gen-answer [id sleep-time]
  (Thread/sleep sleep-time)
  {:body (str "Hello " id ". I sleep " sleep-time)}
  )

(defroutes app*
           (route/resources "/" )
           (GET "/:id" [id] (gen-answer id 1000))
           (GET "/list/" [] {:body "nothing here"})
           (route/not-found "Sorry, there's nothing here.")
           )

(def app (compojure.handler/site app*) )

(defn -main []
  (run-jetty #'app {:port 3000 :join? false}))

