(ns clojure-getting-started.web
  (:require [aleph.http :as http]
            [aleph.netty :as netty]
            [compojure.route :as route]
            [compojure.core :as compojure :refer [GET]]
            [ring.middleware.params :as params]
            [manifold.stream :as s]
            [environ.core :refer [env]]
            [clojure-getting-started.producer :as producer]
            [clojure-getting-started.heroku-kafka :as heroku]))

(defn env-or-default [env-var-name default]
  (if-let [env-var (env env-var-name)] env-var default))

(def ^:private CONSUME_LATEST -1)

(def ^:private TOPIC (env-or-default :sse-proxy-topic "simple-proxy-topic"))

(defn dummy-handler
  [request]
  (println "Dummy request" request)
  "Dummy Handler did it's job on" request)

(defn sse-handler-using-heroku
  "Stream SSE data from the Kafka topic"
  [request]
  (println "Starting to run a request" request)
  (let [topic-name (get (:params request) "topic" TOPIC)
        offset (get (:headers request) "last-event-id" CONSUME_LATEST)
        event-filter-regex (get (:params request) "filter[event]" ".*")

        _ (producer/produce-constantly! topic-name) ; not normal, just for demo - also produce!!

        ch (heroku/heroku-kafka->sse-ch topic-name offset event-filter-regex)]
    {:status  200
     :headers {"Content-Type"  "text/event-stream;charset=UTF-8"
               "Cache-Control" "no-cache"}
     :body    (s/->source ch)}))

(def handler
  (params/wrap-params
    (compojure/routes
      (GET "/" [] "Hello")
      (GET "/dummy" [] dummy-handler)
      (GET "/kafka-sse" [] dummy-handler)
      (route/not-found "No such page."))))

(defn -main [& [port]]
  (let [port (Integer. (or port (env :port) 5000))
        server (http/start-server handler {:port port})]
    (netty/wait-for-close server)))

;; For interactive development:
;; (.stop server)
;; (def server (-main))