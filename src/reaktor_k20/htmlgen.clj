(ns reaktor-k20.htmlgen
  [require
   [clojure.string :as str]])

;; (defn split-at-first-line
;;   "Takes a string and splits it into two at the first line break"
;;   [s]
;;   (str/split s #"\r?\n" 2))

;; (defn split-description-into-paragraphs
;;   [description]
;;   (str/split description #"(?m)^ .$"))

;; (defn generate-paragraph
;;   "Takes a paragraph in an extended description and generates a hiccup html form"
;;   [paragraph]
;;   [:p {:class "paragraph"}
;;    (map (fn [line]
;;           (if (str/starts-with? line "  ")
;;             [:pre {:class "verbatim"}
;;              line]
;;             line))
;;         (str/split-lines paragraph))])

;; (defn generate-extended-description
;;   "Generates html from lines in the extended description"
;;   [extended-description]
;;   (map generate-paragraph
;;        (split-description-into-paragraphs extended-description)))

;; (defn generate-description
;;   "Generates html from a description field"
;;   [description]
;;   (let [[synopsis extended] (split-at-first-line description)]
;;     [:div {:class "description"}
;;      synopsis
;;      (when extended
;;        (generate-extended-description extended))]))

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
  [:div {:class "name"} name])

(defn generate
  "Generates html from a package map"
  [package]
  [:div {:class "package"}
   (when-let [name (:Package package)]
     (generate-name name))
   (when-let [description (:Description package)]
     (generate-description description))
   (when-let [dependencies (:Depends package)]
     (generate-dependency-list dependencies))
   (when-let [reverse-dependencies (:Reverse-Depends package)]
     (generate-dependency-list reverse-dependencies))])
