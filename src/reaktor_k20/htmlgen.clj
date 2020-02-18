(ns reaktor-k20.htmlgen
  [require
   [clojure.string :as str]])

(defn generate-dependency-list
  "Generates html from a package's dependency list"
  [packages dependency-list]
  (when-not (empty? dependency-list)
    [:div {:class "dependency-list"}
     (map (fn [dependency]
            (if (contains? packages
                           dependency)
              [:a {:class "dependency"
                   :href dependency}
               dependency]
              dependency))
          dependency-list)]))

(defn generate-description-verbatim
  "Generates html from a verbatim line in a package description"
  [line]
  [:pre {:class "verbatim"}
   line])

(defn verbatim-line?
  "Takes a string and returns true if it should be displayed verbatim"
  [line]
  (str/starts-with? line "  "))

(defn generate-description-paragraph
  "Generates html from a paragraph in a package description"
  [paragraph]
  [:p {:class "paragraph"}
   (map (fn [line]
          (if (verbatim-line? line)
            (generate-description-verbatim line)
            line))
        paragraph)])

(defn generate-description
  "Generates html from a package's description"
  [description]
  (when-not (empty? description)
    [:div {:class "description"}
     [:b (first description)]
     (map generate-description-paragraph
          (rest description))]))

(defn generate-name
  "Generates html from a package name"
  [name]
  [:h1 {:class "name"} name])

(defn generate
  "Generates html from a package map"
  [packages package]
  (println "GENERATE " (:Package package))
  [:div {:class "package"}
   (when-let [name (:Package package)]
     (generate-name name))
   (when-let [description (:Description package)]
     (list [:h2 "Description"]
           (generate-description description)))
   (when-let [dependencies (:Depends package)]
     (list [:h2 "Dependencies"]
           (generate-dependency-list packages dependencies)))
   (when-let [reverse-dependencies (:Reverse-Depends package)]
     (list [:h2 "Reverse dependencies"]
           (generate-dependency-list packages reverse-dependencies)))])

(defn generate-index
  [packages]
  [:ul
   (for [name (sort (keys packages))]
     [:li [:a {:href name} name]])])
