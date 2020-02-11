(ns reaktor-k20.htmlgen-test
  [:require
   [reaktor-k20.htmlgen :refer :all]
   [clojure.test :refer :all]])

(deftest htmlgen-test-suite
  (let [top-div (generate {:Package "PackageName"})]
    (is (= (first top-div)
           :div)
        "generate should return a top level div")
    (is (= (:class (second top-div))
           "package")
        "generate should return a top level div with the class \"package\"")
    (let [package-div (nth top-div 2)]
      (is (= (first package-div)
             :div)
          "given only a package name, generate should have a child div")
      (is (= (:class (second package-div))
             "package-name")
          "given only a package name, generate should have a child div with the class  \"package-name\"")
      (is (= (nth package-div 2)
             "PackageName")
          "given only a package name, generate should have a child div with the package name as content")))
  (let [top-div (generate {:Description "First line\n These lines\n are part\n of the same paragraph\n .\n This line is preceded by a blank line"})
        description-div (nth top-div 2)]
    (is (= (first description-div)
           :div)
        "given only a description, generate should have a child div")
    (is (= (:class (second description-div))
           "description")
        "given only a description, generate should have a child div with the class \"description\"")
    (is (= (nth description-div 3)
           [:p nil "These lines are part of the same paragraph"])
        "generate should handle paragraph lines in the description")))
