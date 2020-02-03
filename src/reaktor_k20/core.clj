(ns reaktor-k20.core
  (:use [hiccup.core]))

(defn handler [request]
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body (html [:div "Hello World!"])})
