(ns reaktor-k20.htmlgen
  (:use [hiccup.core]
        [hiccup.page])
  [require
   [clojure.string :as str]])

(defn generate-dependency-list
  "Generates html from a package's dependency list"
  [packages dependency-list]
  (when-not (empty? dependency-list)
    [:ul {:class "dependency-list"}
     (map (fn [dependency]
            [:li
             (if (contains? packages
                            dependency)
               [:a {:href dependency}
                dependency]
               dependency)])
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

(defn generate-package
  "Generates html from a package map"
  [packages package]
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
     (when-not (empty? reverse-dependencies)
       (list [:h2 "Reverse dependencies"]
             (generate-dependency-list packages reverse-dependencies))))])

(defn generate-page
  "Takes a title and a body and returns a generic page"
  [title & body]
  (html5
   [:head
    [:title title]
    (include-css "style.css")
    [:meta {:name "viewport"
            :content "width=device-width, initial-scale=1.0"}]
    ]
   [:body
    body]))

(defn generate-index-page
  "Takes a list of packages and returns the index page as a html string"
  [packages]
  (generate-page "Index"
                 [:div
                  [:h1 "Installed packages:"]
                  [:ul
                   (for [name (sort (keys packages))]
                     [:li [:a {:href name} name]])]]))

(defn generate-package-page
  "Takes a single package and returns its page as a html string"
  [packages package]
  (if-not (nil? package)
    (generate-page (:Package package)
                   [:a {:href "/"} "back to index"]
                   (generate-package packages
                                     package))
    (generate-page "Package not found"
                   [:h1 "Package not found"])))
