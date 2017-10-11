(defproject clojure-training "0.1.0-SNAPSHOT"
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/math.combinatorics "0.1.1"]
                 [org.clojure/data.xml "0.0.8"]
                 [org.clojure/data.json "0.2.6"]
                 [org.clojure/core.async "0.2.374"]
                 [jline/jline "2.14.1"]
                 [org.clojure/test.check "0.9.0"]]
  :exclusions [org.clojure/clojure]
  :plugins [[lein-cljsbuild "1.0.3" :exclusions [org.clojure/clojure]]]
  :local-repo "repository")
