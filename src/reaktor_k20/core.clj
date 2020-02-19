(ns reaktor-k20.core  
  (:require [clojure.java.io :as io]
            [ring.middleware.resource :refer [wrap-resource]]
            [reaktor-k20.parser :as parser :refer [parse-file]]
            [reaktor-k20.htmlgen :as htmlgen :refer [generate-index-page
                                                     generate-package-page]]))

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
  (let [package (get-package-from-uri (:uri request))]
    {:status (if (nil? package) 404 200)
     :headers {"Content-Type" "text/html"}
     :body (generate-package-page packages
                                  package)}))

(defn serve-index
  [request]
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body (generate-index-page packages)})

(def handler
  (wrap-resource (fn [request]
                   (if (= (:uri request)
                          "/")
                     (serve-index request)
                     (serve-package-site request)))
                 "css"))
