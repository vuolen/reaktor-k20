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
          "given a package name, generate should have a child div")
      (is (= (:class (second package-div))
             "package-name")
          "given a package name, generate should have a child div with the class  \"package-name\"")
      (is (= (nth package-div 2)
             "PackageName")
          "given a package name, generate should have a child div with the package name as content"))))
