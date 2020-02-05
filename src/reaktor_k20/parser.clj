(ns reaktor-k20.parser
  [:require [clojure.string :as str]])

(defn parse "Takes a control file as a string and returns a map of the properties"
  [input-string]
  (list (reduce into (map
                      (fn [field-string]
                        (let [[key value] (str/split field-string
                                                     #":")]
                          {(keyword key) (str/trim value)}))
                      (str/split input-string
                                 #"\r?\n(?![\t ])")))))
