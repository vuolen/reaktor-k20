(ns reaktor-k20.parser
  [:require [clojure.string :as str]])

(defn split-file-to-paragraphs
  "Takes a control file as a string and returns an array of the paragraphs"
  [input]
  (str/split input
             #"\n\n+"))

(defn split-paragraph-to-fields
  "Takes a paragraph as a string and returns an array of the fields"
  [input]
  (str/split input
             #"\r?\n(?![\t ])"))

(defn split-field
  [input]
  (str/split input
             #":"))

(defn parse-field
  "Takes a field as a string and returns a map of the key/value pairs"
  [field-string]
  (let [[key value] (split-field field-string)]
    (when-not (str/starts-with? key "#")
      {(keyword key)
       (str/trim value)})))

(defn parse-paragraph
  "Takes a paragraph as a string and returns a map of the fields"
  [paragraph-string]
  (reduce into
          (map parse-field
               (split-paragraph-to-fields paragraph-string))))

(defn parse
  "Takes a control file as a string and returns an array of paragraphs"
  [input-string]
  (map parse-paragraph
       (split-file-to-paragraphs input-string)))
