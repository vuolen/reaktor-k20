(ns reaktor-k20.parser
  [:require [clojure.string :as str]])

(defn parse-field "Takes a field as a string and returns a map of the key/value pair"
  [field-string]
  (let [[key value] (str/split field-string #":")]
    {(keyword key) (str/trim value)}))

(defn combine-maps "Takes an array of maps and combines them into one large map"
  [arr]
  (reduce into arr))

(defn parse "Takes a control file as a string and returns a map of the properties"
  [input-string]
  (combine-maps (map parse-field
                     (str/split-lines input-string))))
