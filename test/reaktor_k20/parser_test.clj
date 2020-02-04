(ns reaktor-k20.parser-test
  [:require
   [reaktor-k20.parser :refer :all]
   [clojure.test :refer :all]])

(is (map? (parse "Package: TestPackage"))
    "parse should return a map")

(is (= (parse "Package: TestPackage")
       {:Package "TestPackage"})
    "parse should return a map with a key value pair")

(is (= (parse "Description: desc")
       {:Description "desc"})
    "parse should return a map with the correct key value pair")

(is (= (parse "Package: \t  WhitespacePackage \t\t  ")
       {:Package "WhitespacePackage"})
    "parse should ignore whitespace around the value")

(is (= (parse "Package: pack\nDescription: desc\r\nVersion: vers")
       {:Package "pack"
        :Description "desc"
        :Version "vers"})
    "parse should handle multiple fields on their own lines")

(is (= (parse "Package: pack\nDescription: descline1\n descline2\nVersion: vers")
       {:Package "pack"
        :Description "descline1\n descline2"
        :Version "vers"})
    "parse should handle folded fields")
