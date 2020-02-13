(ns reaktor-k20.htmlgen
  [require
   [clojure.string :as str]])

;; (defn description-paragraph-line
;;   "Lines starting with a single space are part of a paragraph. Successive lines of this form will be word-wrapped when displayed. The leading space will usually be stripped off. The line must contain at least one non-whitespace character."
;;   [html string]
;;   (let [trimmed (str/trim string)]
;;     (if (= (first (last html))
;;            :p)
;;       (update-in html
;;                  [(- (count html) 1) 2]
;;                  str " " trimmed)
;;       (conj html [:p nil trimmed]))))

(defn split-at-first-line
  "Takes a string and splits it into two at the first line break"
  [s]
  (str/split s #"\r?\n" 2))

(defn generate-description
  "Takes a description as a string and generates a hiccup html form"
  [description]
  (let [[first-line rest-lines] (split-at-first-line description)]
    [:div {:class "description"}
     first-line]))

(defn generate
  "Takes a map and returns a html string"
  [package]
  [:div {:class "package"}
   (when-let [name (:Package package)]
     [:div {:class "name"} name])
   (when-let [description (:Description package)]
     (generate-description description))])
