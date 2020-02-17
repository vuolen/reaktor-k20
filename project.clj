(defproject reaktor-k20 "0.0.1"
  :description "Reaktor 2020 summer job pre-assignment"
  :url "http://reaktor-k20.herokuapp.com/"
  :license {:name "GPLv3"
            :url "https://www.gnu.org/licenses/"}
  :dependencies [[org.clojure/clojure "1.8.0"]
		 [hiccup "1.0.5"]]
  :main ^:skip-aot reaktor-k20.core
  :target-path "target/%s"
  :plugins [[lein-ring "0.12.5"]]
  :ring {:handler reaktor-k20.core/handler
         :port 8000
         :auto-reload? true
         :auto-refresh? true}
  :profiles {:uberjar {:aot :all}
             :dev {:resource-paths ["test/reaktor_k20/"]}}
  :uberjar-name "reaktor-k20-standalone.jar"
  :min-lein-version "2.0.0")
