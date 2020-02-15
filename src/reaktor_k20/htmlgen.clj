(ns reaktor-k20.htmlgen
  [require
   [clojure.string :as str]])

(defn split-at-first-line
  "Takes a string and splits it into two at the first line break"
  [s]
  (str/split s #"\r?\n" 2))

(defn split-description-into-paragraphs
  [description]
  (str/split description #"(?m)^ .$"))

(defn generate-paragraph
  "Takes a paragraph in an extended description and generates a hiccup html form"
  [paragraph]
  [:p {:class "paragraph"}
   (map (fn [line]
          (if (str/starts-with? line "  ")
            [:pre {:class "verbatim"}
             line]
            line))
        (str/split-lines paragraph))])

(defn generate-extended-description
  "Generates html from lines in the extended description"
  [extended-description]
  (map generate-paragraph
       (split-description-into-paragraphs extended-description)))

(defn generate-description
  "Generates html from a description field"
  [description]
  (let [[synopsis extended] (split-at-first-line description)]
    [:div {:class "description"}
     synopsis
     (when extended
       (generate-extended-description extended))]))

(defn generate
  "Generates html from a package map"
  [package]
  [:div {:class "package"}
   (when-let [name (:Package package)]
     [:div {:class "name"} name])
   (when-let [description (:Description package)]
     (generate-description description))])
