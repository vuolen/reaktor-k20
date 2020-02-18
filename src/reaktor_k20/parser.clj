(ns reaktor-k20.parser
  [:require [clojure.string :as str]])

(defn split-at-first-line
  "Takes a string and splits it into two at the first line break"
  [s]
  (str/split s #"\r?\n" 2))

(defn parse-package-name
  [package-name]
  package-name)

(defn split-extended-description-paragraphs
  "Takes an extended description and splits it into paragraphs"
  [extended-description]
  (str/split extended-description
             #"(?m)^ .$"))

(defn parse-description
  "Takes a package description as a string and returns an array of its paragraphs"
  [description]
  (let [[synopsis extended] (split-at-first-line description)]
    (if extended
      (concat [synopsis]
              (->> extended
                   (split-extended-description-paragraphs)
                   (map str/split-lines)))
      [synopsis])))

(defn parse-package-name-from-dependency
  "Takes a dependency as a string and returns the package name, ignoring version or architecture specifiers"
  [dependency]
  (first (str/split dependency
                    #"\(|\[")))

(defn parse-depends
  "Takes a package dependency list as a string and returns an array of the dependency names"
  [depends]
  (map (fn [dependency]
         (str/trim (parse-package-name-from-dependency dependency)))
       (str/split depends #",|\|")))

(def +field-parsers+
  {:Package parse-package-name
   :Description parse-description
   :Depends parse-depends})

(defn apply-field-parser
  "Takes a field as a key-value pair, finds the field parser that matches the key and applies it to the value"
  [[key value]]
  (when-let [field-parser (key +field-parsers+)]
    {key (field-parser value)}))

(defn split-field-into-key-and-value
  "Takes a field as a string and returns the key as a keyword the and value as a string"
  [field-string]
  (as-> field-string fs
    (str/split fs #":" 2)
    (map str/trim fs)
    (list (keyword (first fs))
          (second fs))))

(defn comment-line?
  [line]
  (str/starts-with? line "#"))

(defn parse-field
  "Takes a field as a string and returns a map of the key/value pairs"
  [field-string]
  (when-not (comment-line? field-string)
    (-> field-string
        split-field-into-key-and-value
        apply-field-parser)))

(defn split-paragraph-into-fields
  "Takes a paragraph as a string and returns an array of field strings"
  [paragraph-string]
  (str/split paragraph-string
             #"\r?\n(?![\t ])"))

(defn parse-paragraph
  "Takes a paragraph as a string and returns a map of the fields"
  [paragraph-string]
  (->> paragraph-string
       split-paragraph-into-fields
       (map parse-field)
       (reduce into)))

(defn depends-on?
  "Returns true if the first package depends on the second one"
  [package1 package2]
  (boolean (some #(= % (:Package package2))
                 (:Depends package1))))

(defn get-reverse-dependency-names
  "Returns the names of the reverse dependencies of the second argument"
  [packages package]
  (->> packages
       (filter #(depends-on? % package))
       (map :Package)))

(defn add-reverse-dependencies
  "Takes an array of packages and adds the Reverse-Depend field in each"
  [packages]
  (map #(assoc %
               :Reverse-Depends
               (get-reverse-dependency-names packages
                                             %))
       packages))

(defn split-file-into-paragraphs
  "Takes a control file and splits it into an array of paragraph strings"
  [file-string]
  (str/split file-string
             #"\n\n+"))

(defn parse-file
  "Takes a control file as a string and returns an array of paragraph maps"
  [input-string]
  (->> input-string
       (split-file-into-paragraphs)
       (map parse-paragraph)
       add-reverse-dependencies
       (map (fn [package]
              {(:Package package) package}))
       (reduce into)))
