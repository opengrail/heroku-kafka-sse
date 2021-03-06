(ns heroku-kafka-sse.heroku-kafka
  (:require
    [environ.core :refer [env]]
    [clojure.string :as str]
    [com.opengrail.kafka-sse.core :as sse])
  (:import (org.apache.kafka.clients CommonClientConfigs)
           (java.net URI)
           (com.github.jkutner EnvKeyStore)
           (org.apache.kafka.common.config SslConfigs)))

(defn- env-or-fail
  [env-var-name]
  (if-let [env-var (env env-var-name)]
    env-var
    (throw (RuntimeException. (str "Must set environment variable: " env-var-name)))))

(defn- kafka-ssl-config
  []
  (let [_ (env-or-fail :kafka-trusted-cert)
        _ (env-or-fail :kafka-client-cert)
        _ (env-or-fail :kafka-client-cert-key)
        env-trust-store (EnvKeyStore/createWithRandomPassword "KAFKA_TRUSTED_CERT")
        env-key-store (EnvKeyStore/createWithRandomPassword "KAFKA_CLIENT_CERT_KEY" "KAFKA_CLIENT_CERT")
        trust-store (.storeTemp env-trust-store)
        key-store (.storeTemp env-key-store)]
    {SslConfigs/SSL_TRUSTSTORE_TYPE_CONFIG        (.type env-trust-store)
     SslConfigs/SSL_TRUSTSTORE_LOCATION_CONFIG    (.getAbsolutePath trust-store)
     SslConfigs/SSL_TRUSTSTORE_PASSWORD_CONFIG    (.password env-trust-store)
     SslConfigs/SSL_KEYSTORE_TYPE_CONFIG          (.type env-key-store)
     SslConfigs/SSL_KEYSTORE_LOCATION_CONFIG      (.getAbsolutePath key-store)
     SslConfigs/SSL_KEYSTORE_PASSWORD_CONFIG      (.password env-key-store)
     CommonClientConfigs/SECURITY_PROTOCOL_CONFIG "SSL"}))

(defn- kafka-connection-config
  "Return a Java Properties object that contains the broker configuration"
  []
  (if-let [hps-broker-url (env :kafka-plaintext-url)]
    ; only possible in Heroku Private Spaces
    {CommonClientConfigs/BOOTSTRAP_SERVERS_CONFIG hps-broker-url}

    ; if not in Heroku Private Space, brokers must always be accessed using SSL
    (let [broker-url (env-or-fail :kafka-url)
          brokers (map #(str (.getHost (URI. %)) ":" (.getPort (URI. %))) (str/split broker-url #","))
          broker-config {CommonClientConfigs/BOOTSTRAP_SERVERS_CONFIG (str/join "," brokers)}
          ssl-config (kafka-ssl-config)]
      (merge broker-config ssl-config))))

(defn heroku-kafka->sse-ch
  [topic-name offset event-filter]
  (let [heroku-brokers (kafka-connection-config)
        consumer (sse/sse-consumer topic-name offset heroku-brokers)
        transducer (comp (filter #(sse/name-matches? event-filter (or (.key %) "")))
                         (map sse/consumer-record->sse))]
    (sse/kafka-consumer->sse-ch consumer transducer)))