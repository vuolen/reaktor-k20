(ns reaktor-k20.core
  (:use [hiccup.core]
        [hiccup.page])
  (:require [clojure.java.io :as io]
            [reaktor-k20.parser :as parser :refer [parse-file]]
            [reaktor-k20.htmlgen :as htmlgen :refer [generate]]))

(defn read-mock-file
  "Reads the status.real mock file"
  []
  (slurp (io/resource "status.real")))

(defn read-dpkg-status
  "Reads /var/lib/dpkg/status, returns nil if not successful"
  []
  (try
    (slurp "/var/lib/dpkg/status")
    (catch Exception e nil)))

(defn read-real-or-mock-file
  "Reads the control file, or if not successful reads mock file"
  []
  (or (read-dpkg-status)
      (read-mock-file)))

(defonce packages (->> (read-real-or-mock-file)
                       parser/parse-file))

(defn get-package-from-uri
  "Returns the package from the uri. Returns nil if not found"
  [uri]
  (get packages
       (subs uri
             1)))

(defn serve-package-site
  [request]
  (if-let [package (get-package-from-uri (:uri request))]
    {:status 200
     :headers {"Content-Type" "text/html"}
     :body (html5 [:a {:href "/"} "back to index"]
                  (htmlgen/generate packages
                                    package))}
    {:status 404
     :headers {"Content-Type" "text/html"}
     :body (html5 [:h1 "Package not found"])}))

(defn serve-index
  [request]
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body (html5 (htmlgen/generate-index packages))})

(defn handler [request]
  (if (= (:uri request)
         "/")
    (serve-index request)
    (serve-package-site request))
  )
