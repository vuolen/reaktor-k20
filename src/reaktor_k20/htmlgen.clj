(ns reaktor-k20.htmlgen)

(defn generate
  "Takes a package and returns a hiccup html"
  [package]
  [:div {:class "package"}
   (when-let [name (:Package package)]
     [:div {:class "package-name"} name])])
