(ns heroku-kafka-sse.web
  (:require [aleph.http :as http]
            [aleph.netty :as netty]
            [compojure.route :as route]
            [compojure.core :as compojure :refer [GET]]
            [ring.middleware.params :as params]
            [manifold.stream :as s]
            [environ.core :refer [env]]
            [heroku-kafka-sse.heroku-kafka :as kafka]))

(def ^:private CONSUME_LATEST -1)

(def ^:private TOPIC (or (env :sse-proxy-topic) "simple-proxy-topic"))


; TODO add a little style / context and maybe have the stream in a scrolling window

(defn kafka->sse
  "Stream SSE data from the Kafka topic"
  [request]
  (let [topic-name (get (:params request) "topic" TOPIC)
        offset (get (:headers request) "last-event-id" CONSUME_LATEST)
        event-filter-regex (get (:params request) "filter[event]" ".*")
        ch (kafka/heroku-kafka->sse-ch topic-name offset event-filter-regex)]
    {:status  200
     :headers {"Content-Type"  "text/event-stream;charset=UTF-8"
               "Cache-Control" "no-cache"}
     :body    (s/->source ch)}))

(def handler
  (params/wrap-params
    (compojure/routes
      (GET "/" [] "Get your SSE via /sse")
      (GET "/sse" [] kafka->sse)
      (route/not-found "No such page."))))

(defn -main [& [port]]
  (let [port (Integer. (or port (env :port) 5000))
        server (http/start-server handler {:port port})]
    (netty/wait-for-close server)))