(ns reaktor-k20.parser-test
  [:require
   [reaktor-k20.parser :refer :all]
   [clojure.test :refer :all]])

(is (list? (parse "Package: TestPackage"))
    "parse should return a list of maps")

(is (= (parse "Package: TestPackage")
       '({:Package "TestPackage"}))
    "parse should return a list that contains a map with a key value pair")

(is (= (parse "Description: desc")
       '({:Description "desc"}))
    "parse should return a list that contains a map with the correct key value pair")

(is (= (parse "Package: \t  WhitespacePackage \t\t  ")
       '({:Package "WhitespacePackage"}))
    "parse should ignore whitespace around the value")
