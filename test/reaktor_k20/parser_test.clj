(ns reaktor-k20.parser-test
  [:require
   [reaktor-k20.parser :refer :all]
   [clojure.test :refer :all]
   [clojure.java.io :as io]
   [clojure.string :as str]])

(deftest parser-test-suite
  (is (coll? (parse "Package: TestPackage"))
      "givenparse should return a collection of maps")

  (is (= (parse "Package: TestPackage")
         '({:Package "TestPackage"}))
      "given a package TestPackage, parse should return TestPackage as the name")

  (is (= (parse "Package: TestPackage1")
         '({:Package "TestPackage1"}))
      "given a package TestPackage1, parse should return TestPackage1 as the name")

  (is (= (parse "Package: \t  WhitespacePackage \t\t  ")
         '({:Package "WhitespacePackage"}))
      "given a package name with extra whitespace, parse should return the package name without whitespace")

  (is (= (parse "Description: This package is a package\n It does this and that\n not including that")
         '({:Description "This package is a package\n It does this and that\n not including that"}))
      "given a description that spans multiple lines, parse should return every line")

  (is (= (parse "Package: TestPackage2\nDescription: Test description")
         '({:Package "TestPackage2"
            :Description "Test description"}))
      "given a package name and a description, parse should return a single package with both")

  (is (= (parse "Package: Package1\n\nPackage: Package2")
         '({:Package "Package1"}
           {:Package "Package2"}))
      "given two separate packages, parse should return two separate packages with correct fields")

  (is (= (parse "Package: TestPackage\n#This is a comment\n#Depends: Commented out\nDescription: A description")
         '({:Package "TestPackage"
            :Description "A description"}))
      "given a package with a comment line, parse should ignore that line")

  (is (= (parse "Package: TestPackage\n\n\n")
         '({:Package "TestPackage"}))
      "given multiple consecutive line breaks, parse should ignore them"))
