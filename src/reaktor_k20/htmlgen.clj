(ns reaktor-k20.htmlgen
  [require
   [clojure.string :as str]])

(defn generate-dependency-list
  "Generates html from a package's dependency list"
  [dependency-list]
  (when-not (empty? dependency-list)
    [:div {:class "dependency-list"}
     (map (fn [dependency]
            [:a {:class "dependency"
                 :href dependency}
             dependency])
          dependency-list)]))

(defn generate-description-paragraph
  "Generates html from a paragraph in a package description"
  [paragraph]
  [:p {:class "paragraph"}
   (seq paragraph)])

(defn generate-description-verbatim
  "Generates html from a verbatim line in a package description"
  [line]
  [:pre {:class "verbatim"}
   line])

(defn generate-description
  "Generates html from a package's description"
  [description]
  (when-not (empty? description)
    [:div {:class "description"}
     (first description)
     (map (fn [element]
            (if (coll? element)
              (generate-description-paragraph
               element)
              (generate-description-verbatim
               element)))
          (rest description))]))

(defn generate-name
  "Generates html from a package name"
  [name]
  [:h1 {:class "name"} name])

(defn generate
  "Generates html from a package map"
  [package]
  (println "GENERATE " (:Package package))
  [:div {:class "package"}
   (when-let [name (:Package package)]
     (generate-name name))
   (when-let [description (:Description package)]
     (generate-description description))
   (when-let [dependencies (:Depends package)]
     (generate-dependency-list dependencies))
   (when-let [reverse-dependencies (:Reverse-Depends package)]
     (generate-dependency-list reverse-dependencies))])
