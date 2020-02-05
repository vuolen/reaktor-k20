(ns reaktor-k20.parser
  [:require [clojure.string :as str]])

(defn parse-field
  "Takes a field as a string and returns a map of the key/value pairs"
  [field-string]
  (let [[key value] (str/split field-string
                               #":")]
    {(keyword key)
     (str/trim value)}))

(defn parse-paragraph
  "Takes a paragraph as a string and returns a map of the fields' key/value pairs"
  [paragraph-string]
  (reduce into
          (map
           parse-field
           (str/split paragraph-string
                      #"\r?\n(?![\t ])"))))

(defn parse
  "Takes a control file as a string and returns a map of the properties"
  [input-string]
  (list (parse-paragraph input-string)))
