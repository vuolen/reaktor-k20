(ns reaktor-k20.parser-test
  [:require
   [reaktor-k20.parser :refer :all]
   [clojure.test :refer :all]
   [clojure.java.io :as io]
   [clojure.string :as str]])

(deftest parser-test

  (is (coll? (parse "Package: TestPackage"))
      "parse should return a collection of maps")

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

  (is (= (parse "Package: Package1\n\nPackage: Package2")
         '({:Package "Package1"}
           {:Package "Package2"}))
      "parse should parse multiple paragraphs")

  (is (= (parse "Package: TestPackage\n#This is a comment\nDescription: A description")
         '({:Package "TestPackage"
            :Description "A description"}))
      "parse should ignore comment lines")

  (is (= (parse "Package: TestPackage\n\n\n")
         '({:Package "TestPackage"}))
      "parse should ignore trailing newlines")


  (let [result (parse (slurp (io/file (io/resource "status.real"))))]
    (is (some #(and (= (:Package %)
                       "libio-string-perl")
                    (str/starts-with? (:Depends %)
                                      "perl"))
              result)
        "parse should return libio-string-perl from status.real")
    (is (some #(and (= (:Package %)
                       "libslf4j-java")
                    (= (:Priority %)
                       "optional")
                    (str/ends-with? (:Description %)
                                    "JDK14LoggerAdapter.")
                    (= (count %)
                       12))
              result)
        "parse should return libslf4j-java from status.real")))
