(defproject clojure-getting-started "1.0.0-SNAPSHOT"
  :description "Demo Clojure web app"
  :url "http://clojure-getting-started.herokuapp.com"
  :license {:name "Eclipse Public License v1.0"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [compojure "1.5.1" :exclusions [org.clojure/tools.reader]]
                 [aleph "0.4.2-alpha8"]
                 [com.github.jkutner/env-keystore "0.1.2"]
                 [com.opengrail/kafka-sse-clj "0.1.1"]
                 [org.slf4j/slf4j-simple "1.7.6"]]
  :min-lein-version "2.0.0"
  :plugins [[environ/environ.lein "0.3.1"]]
  :hooks [environ.leiningen.hooks]
  :uberjar-name "clojure-getting-started-standalone.jar"
  :profiles {:production {:env {:production true}}}
  :pedantic? :warn)
