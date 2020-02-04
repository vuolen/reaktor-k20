(ns reaktor-k20.parser
  [:require [clojure.string :as str]])

(defn parse "Takes a control file as a string and returns a map of the properties"
  [input-string]
  (let [[key value] (str/split input-string
                               #":")]
    {(keyword key) (str/trim value)}))
