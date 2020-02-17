(ns reaktor-k20.parser-test
  [:require
   [reaktor-k20.parser :refer :all]
   [clojure.test :refer :all]
   [clojure.java.io :as io]
   [clojure.string :as str]])

(deftest parse-suite
  (is (coll? (parse "Package: TestPackage"))
      "parse should return a collection of maps")

  (is (= (-> (parse "Package: TestPackage")
             first
             :Package)
         "TestPackage")
      "given a package TestPackage, parse should return TestPackage as the name")

  (is (= (-> (parse "Package: TestPackage1")
             first
             :Package)
         "TestPackage1")
      "given a package TestPackage1, parse should return TestPackage1 as the name")

  (is (= (-> (parse "Package: \t  WhitespacePackage \t\t  ")
             first
             :Package)
         "WhitespacePackage")
      "given a package name with extra whitespace, parse should return the package name without whitespace")

  (is (= (first (parse "Package: TestPackage2\nDescription: Test description"))
         {:Package "TestPackage2"
          :Description ["Test description"]
          :Reverse-Depends (list)})
      "given a package name and a description, parse should return a single package with both")

  (is (= (first (parse "Package: Package1\n\nPackage: Package2"))
         {:Package "Package1"
          :Reverse-Depends (list)})
      "given two separate packages, parse should return the first package as the first element")

  (is (= (first (parse "Package: TestPackage\n#This is a comment\n#Depends: Commented out\nDescription: A description"))
         {:Package "TestPackage"
          :Description ["A description"]
          :Reverse-Depends (list)})
      "given a package with a comment line, parse should ignore that line")

  (is (= (-> (parse "Package: TestPackage\n\n\n")
             first
             :Package)
         "TestPackage")
      "given multiple consecutive line breaks, parse should ignore them")

  (is (not (contains? (first (parse "Package: TestPackage\nFilter: this field"))
                      :Filter))
      "given a field without a parser, parse should not include them")
  
  ;; (is (= (-> (parse "Package: package1\n\nPackage: package2\nDepends: package1")
  ;;            first
  ;;            :Reverse-Depends
  ;;            first)
  ;;        "package2")
  ;;     "given package2 that depends on package1, parse should return package2 in the reverse dependencies of package1")
  )

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
