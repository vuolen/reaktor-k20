(ns reaktor-k20.parser-test
  [:require
   [reaktor-k20.parser :refer :all]
   [clojure.test :refer :all]])

(is (list? (parse "Package: TestPackage"))
    "parse should return a list of maps")

(is (= (parse "Package: TestPackage")
       '({:Package "TestPackage"}))
    "parse should parse a simple field")

(is (= (parse "Package: TestPackage1")
       '({:Package "TestPackage1"}))
    "parse should parse a simple field correctly")

(is (= (parse "Package: \t  WhitespacePackage \t\t  ")
       '({:Package "WhitespacePackage"}))
    "parse should ignore whitespace around a simple field value")

(is (= (parse "Description: This package is a package\n It does this and that\n not including that")
       '({:Description "This package is a package\n It does this and that\n not including that"}))
    "parse should support multiline fields")

(is (= (parse "Package: TestPackage2\nDescription: Test description")
       '({:Package "TestPackage2"
          :Description "Test description"}))
    "parse should parse paragraphs")
