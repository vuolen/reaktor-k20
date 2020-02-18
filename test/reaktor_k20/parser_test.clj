(ns reaktor-k20.parser-test
  [:require
   [reaktor-k20.parser :refer :all]
   [clojure.test :refer :all]
   [clojure.java.io :as io]
   [clojure.string :as str]])

(deftest parse-file-suite
  (is (coll? (parse-file "Package: TestPackage"))
      "parse-file should return a collection of maps")

  (is (= (-> (parse-file "Package: TestPackage")
             (get "TestPackage")
             :Package)
         "TestPackage")
      "given a package TestPackage, parse-file should return TestPackage as the name")

  (is (= (-> (parse-file "Package: TestPackage1")
             (get "TestPackage1")
             :Package)
         "TestPackage1")
      "given a package TestPackage1, parse-file should return TestPackage1 as the name")

  (is (= (-> (parse-file "Package: \t  WhitespacePackage \t\t  ")
             (get "WhitespacePackage")
             :Package)
         "WhitespacePackage")
      "given a package name with extra whitespace, parse-file should return the package name without whitespace")

  (is (= (get (parse-file "Package: TestPackage2\nDescription: Test description")
              "TestPackage2")
         {:Package "TestPackage2"
          :Description ["Test description"]
          :Reverse-Depends (list)})
      "given a package name and a description, parse-file should return a single package with both")

  (is (= (get (parse-file "Package: Package1\n\nPackage: Package2")
              "Package1")
         {:Package "Package1"
          :Reverse-Depends (list)})
      "given two separate packages, parse-file should return the first package as the first element")

  (is (= (get (parse-file "Package: TestPackage\n#This is a comment\n#Depends: Commented out\nDescription: A description")
              "TestPackage")
         {:Package "TestPackage"
          :Description ["A description"]
          :Reverse-Depends (list)})
      "given a package with a comment line, parse-file should ignore that line")

  (is (= (-> (parse-file "Package: TestPackage\n\n\n")
             (get "TestPackage")
             :Package)
         "TestPackage")
      "given multiple consecutive line breaks, parse-file should ignore them")

  (is (not (contains? (first (parse-file "Package: TestPackage\nFilter: this field"))
                      :Filter))
      "given a field without a parser, parse-file should not include them")
  
  (is (= (-> (parse-file "Package: package1\n\nPackage: package2\nDepends: package1")
             (get "package1")
             :Reverse-Depends
             first)
         "package2")
      "given package2 that depends on package1, parse-file should return package2 in the reverse dependencies of package1")
  )

(deftest parse-field-suite
  (is (= (-> (parse-field "Description: Synopsis\n This line contains a colon:\n it should be ignored")
             :Description
             second
             second)
         " it should be ignored")
      "given a description with a colon in it, parse-field should ignore it"))

(deftest parse-description-suite
  
  (is (= (first (parse-description "Synopsis\n"))
         "Synopsis")
      "given a synopsis, parse-description should have it as the first element")  
  (is (= (first (parse-description "Synopsis\n paragraph\n"))
         "Synopsis")
      "given a synopsis and a paragraph, parse-description should have the synopsis line as the first element")
  (is (= (second (parse-description "Synopsis\n this is a paragraph\n  -with a verbatim line\n and two normal lines"))
         [" this is a paragraph" "  -with a verbatim line" " and two normal lines"])
      "given a synopsis and a paragraph, parse-description should have the paragraph as a second element"))

(deftest parse-depends-suite
  (is (coll? (parse-depends ""))
      "given an empty string, parse-depends should return a collection")

  (is (= (first (parse-depends "foo"))
         "foo")
      "given \"foo\", parse-depends should return \"foo\" as the first element")

  (is (= (second (parse-depends "foo, bar"))
         "bar")
      "given \"foo, bar\", parse-depends should return \"bar\" as the second element")

  (is (= (first (parse-depends "foo (> 1.0.0)"))
         "foo")
      "given \"foo (> 1.0.0)\", parse-depends should return \"foo\" as the first element")

  (is (= (first (parse-depends "foo [amd64]"))
         "foo")
      "given \"foo [amd64]\", parse-depends should return \"foo\" as the first element")

  (is (= (second (parse-depends "foo | bar"))
         "bar")
      "given \"foo | bar\", parse-depends should return \"bar\" as the second element"))
