(ns clojure-getting-started.producer
  (:require [clojure.core.async :as async :refer [>! <! go-loop chan close! timeout]]
            [clojure-getting-started.heroku-kafka :as heroku-kafka])
  (:import (org.apache.kafka.clients.producer ProducerRecord KafkaProducer)
           (org.apache.kafka.common.serialization StringSerializer)))

(def ^:private marshalling-config {"key.serializer"   (.getCanonicalName StringSerializer)
                                   "value.serializer" (.getCanonicalName StringSerializer)})

(defn kafka-produce
  [producer topic-name k v]
  (.send producer (ProducerRecord. topic-name k v)))

(defn produce-constantly!
  [topic-name]
  (let [heroku-brokers (heroku-kafka/kafka-connection-config)
        producer (KafkaProducer. (merge marshalling-config heroku-brokers))
        keep-alive-millis 5000]
    (go-loop [rando 0]
      (let [_ (<! (timeout keep-alive-millis))]
        (kafka-produce producer topic-name "rando-event" (str "{\"id\" " rando " \"message\" \"Hello SSE\"}"))
        (recur (rand-int keep-alive-millis))))))