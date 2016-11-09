(defproject clojure-getting-started "1.0.0-SNAPSHOT"
  :description "Demo Clojure web app"
  :url "http://clojure-getting-started.herokuapp.com"
  :license {:name "Eclipse Public License v1.0"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [compojure "1.4.0"]
                 [aleph "0.4.1"]
                 [ring/ring-jetty-adapter "1.4.0"]
                 [environ "1.1.0"]
                 [com.github.jkutner/env-keystore "0.1.2"]
                 [com.opengrail/kafka-sse-clj "0.1.0"]
                 ; only for the demo ... these libs are needed to run the Kafka Producer
                 [org.apache.kafka/kafka_2.10 "0.10.0.1"]
                 [org.apache.kafka/kafka-clients "0.10.0.1"]]
  :min-lein-version "2.0.0"
  :plugins [[environ/environ.lein "0.3.1"]]
  :hooks [environ.leiningen.hooks]
  :uberjar-name "clojure-getting-started-standalone.jar"
  :profiles {:production {:env {:production true}}}
  :pedantic? :warn)
