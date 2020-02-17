(ns reaktor-k20.htmlgen-test
  [:require
   [reaktor-k20.htmlgen :refer :all]
   [clojure.test :refer :all]
   [hiccup.core :refer [html]]])

;; WARNING: testing html output leads to brittle tests at best, so be careful

(def test-package1 {:Package "TestPackage"
                    :Description ["First line" [" These lines" " are part" " of the same paragraph"] "  This line is preceded by a blank line and displayed verbatim"]
                    :Depends ["package1" "package2"]
                    :Reverse-Depends ["package3" "package4"]})

(def test-package2 {:Package "TestPackage2"
                    :Description ["This is package 2"]
                    :Depends ["packageA" "packageB"]
                    :Reverse-Depends ["packageC" "packageD"]})

(deftest generate-test-suite
  (let [result1 (generate test-package1)
        result2 (generate test-package2)]
    (is (= (first result1)
           :div)
        "generate should return a div")
    
    (is (= (:class (second result1))
           "package")
        "generate should return a div with the class \"package\"")
    
    (is (= (first (nth result1 2))
           :h1)
        "given a package name, generate should have h1 as the first child")
    
    (is (= (:class (second (nth result1 2)))
           "name")
        "given a package name, generate should have a first child with the class \"name\"")
    
    (is (= (get-in result1 [2 2])
           "TestPackage")
        "given a package named TestPackage, generate should have a first child containing that name")
    
    (is (= (get-in result2 [2 2])
           "TestPackage2")
        "given a package named TestPackage2, generate should have a first child containing that name"))
  (is (nil? (nth (generate {:Description "desc"}) 2))
      "given a package with no name, generate should have a first child of nil"))


(deftest generate-description-suite
  (is (= (generate-description [])
         nil)
      "given an empty description, generate-description should return nil")
  (is (= (first (generate-description ["Hello"]))
         :div)
      "given a description with a synopsis line, generate-description should return div")
  (is (vector? (generate-description ["Hello"]))
      "given a description with a synopsis line, generate-description should return a vector")
  (is (= (:class (second (generate-description ["Hello"])))
         "description")
      "given a description with a synopsis line, generate-description should return an element with the class \"description\"")
  (is (= (nth (generate-description ["Hello"])
              2)
         "Hello")
      "given a description with a synopsis line, generate-description should return an element with that line as the first child")
  (is (= (nth (generate-description ["Hello" [" These lines" " are in" " the same paragraph"]])
              3)
         (list [:p {:class "paragraph"}
                (list " These lines"
                      " are in"
                      " the same paragraph")]))
      "given a description with a synopsis and a paragraph, generate-description should return the paragraph in a p element")
  (is (= (nth (generate-description ["Hello" "  This line is verbatim"])
              3)
         (list [:pre {:class "verbatim"}
                "  This line is verbatim"]))
      "given a description with a synopsis and a verbatim line, generate-description should return the verbatim line in a pre element"))


(deftest generate-dependency-list-suite
  (is (= (generate-dependency-list [])
         nil)
      "given no dependencies, generate-dependency-list should return nil")
  (is (= (first (generate-dependency-list ["dependency1"]))
         :div)
      "given a dependency, generate-dependency-list should return a div")
  (is (= (:class (second (generate-dependency-list ["dependency1"])))
         "dependency-list")
      "given a dependency, generate-dependency-list should return an element with the class \"dependency-list\"")
  (is (= (first (-> (generate-dependency-list ["dependency1"])
                    (nth 2)
                    first))
         :a)
      "given a dependency, generate-dependency-list should have an a element as the first child")

  (is (= (-> (generate-dependency-list ["dependency1"])
             (nth 2)
             (nth 0)
             second
             :class)
         "dependency")
      "given a dependency, generate-dependency-list should have an a element with the class \"dependency\" as the first child")

  (is (= (-> (generate-dependency-list ["dependency1"])
             (nth 2)
             (nth 0)
             second
             :href)
         "dependency1")
      "given a dependency, generate-dependency-list should have an element with a href to the dependency as the first element"))
