(ns reaktor-k20.htmlgen-test
  [:require
   [reaktor-k20.htmlgen :refer :all]
   [clojure.test :refer :all]])

(def test-package1 {:Package "TestPackage"
                    :Description "First line\n These lines\n are part\n of the same paragraph\n .\n  This line is preceded by a blank line and displayed verbatim"
                    :Depends ["package1" "package2"]
                    :Reverse-Depends ["package3" "package4"]})

(def test-package2 {:Package "TestPackage2"
                    :Description "This is package 2"
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
           :div)
        "given a package name, generate should have a div as the first child")
    (is (= (:class (second (nth result1 2)))
           "name")
        "given a package name, generate should have a first child with the class \"name\"")
    (is (= (get-in result1 [2 2])
           "TestPackage")
        "given a package named TestPackage, generate should have a first child containing that name")
    (is (= (get-in result2 [2 2])
           "TestPackage2")
        "given a package named TestPackage2, generate should have a first child containing that name")
    (is (= (first (nth result1 3))
           :div)
        "given a package description, generate should have a div as the second child")
    (is (= (:class (second (nth result1 3)))
           "description")
        "given a package description, generate should have a second child with the class \"description\""))
  (is (nil? (nth (generate {:Description "desc"}) 2))
      "given a package with no name, generate should have a first child of nil")
  (is (nil? (nth (generate {:Package "TestPackage"}) 3))
      "given a package with no description, generate should have a second child of nil")

  (is (= (get-in (generate {:Description "Synopsis\n Next line"}) [3 2])
         "Synopsis")
      "given a description with the first line of \"Synopsis\", generate should have \"Synopsis\" as the first child of the description div")
  (is (= (get-in (generate {:Description "Synopsis2\n Next line"}) [3 2])
         "Synopsis2")
      "given a description with the first line of \"Synopsis2\", generate should have \"Synopsis2\" as the first child of the description div"))

