(ns reaktor-k20.parser-test
  [:require
   [reaktor-k20.parser :refer :all]
   [clojure.test :refer :all]])

(is (list? (parse "Package: TestPackage"))
    "parse should return a list of maps")

(is (= (parse "Package: TestPackage")
       '({:Package "TestPackage"}))
    "parse should return a list that contains a map with a key value pair")

(is (= (parse "Package: TestPackage1")
       '({:Package "TestPackage1"}))
    "parse should return a list that contains a map with the correct key value pair")

(is (= (parse "Package: \t  WhitespacePackage \t\t  ")
       '({:Package "WhitespacePackage"}))
    "parse should ignore whitespace around a simple value")

(is (= (parse "Description: This package is a package\n It does this and that\n not including that")
       '({:Description "This package is a package\n It does this and that\n not including that"}))
    "parse should parse description as multiline")
