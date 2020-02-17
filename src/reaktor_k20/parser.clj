(ns reaktor-k20.parser
  [:require [clojure.string :as str]])

(def +key-value-delimiter+
  #":")

(def +field-delimiter+
  #"\r?\n(?![\t ])")

(def +paragraph-delimiter+
  #"\n\n+")

(defn split-at-first-line
  "Takes a string and splits it into two at the first line break"
  [s]
  (str/split s #"\r?\n" 2))

(defn parse-package-name
  [package-name]
  package-name)

(defn parse-description
  "Takes a package description as a string and returns an array of its paragraphs"
  [description]
  (let [[synopsis extended] (split-at-first-line description)]
    (if extended
      [synopsis
       (flatten
        (map (fn [paragraph]
               (str/split-lines paragraph))
             (str/split extended #"(?m)^ .$")))]
      [synopsis])))

(defn parse-package-from-dependency
  "Takes a dependency as a string and returns the package name, ignoring version or architecture specifiers"
  [dependency]
  (first (str/split dependency
                    #"\(|\[")))

(defn parse-depends
  "Takes a package dependency list as a string and returns an array of the dependency names"
  [depends]
  (map (fn [dependency]
         (str/trim (parse-package-from-dependency dependency)))
       (str/split depends #",|\|")))

(def +field-parsers+
  {:Package parse-package-name
   :Description parse-description
   :Depends parse-depends})

(defn comment-line?
  [line]
  (str/starts-with? line "#"))

(defn parse-field
  "Takes a field as a string and returns a map of the key/value pairs"
  [field-string]
  (when-not (comment-line? field-string)
    (let [[key value] (str/split field-string
                                 +key-value-delimiter+)]
      (when-let [field-parser (get +field-parsers+
                                   (keyword key))]
        {(keyword key)
         (field-parser (str/trim value))}))))

(defn parse-paragraph
  "Takes a paragraph as a string and returns a map of the fields"
  [paragraph-string]
  (reduce into
          (map parse-field
               (str/split paragraph-string
                          +field-delimiter+))))

(defn get-package-names-that-depend-on
  "Returns the names of the packages that depend on the second argument"
  [packages package]
  (map
   :Package
   (filter #(some (fn [p]
                    (= p
                       (:Package package)))
                  (:Depends %))
           packages)))

(defn add-reverse-dependencies
  "Takes an array of packages and adds the Reverse-Depend field in each"
  [packages]
  (map (fn [package]
         (assoc package
                :Reverse-Depends
                (get-package-names-that-depend-on packages
                                                  package)))
       packages))

(defn parse
  "Takes a control file as a string and returns an array of paragraphs"
  [input-string]
  (add-reverse-dependencies (map parse-paragraph
                                 (str/split input-string
                                            +paragraph-delimiter+))))


