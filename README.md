
# clojure-kafka-sse

A minimal Clojure app to expose Kafka topics over SSE.

It demonstrates the configuration needed to connect to the [Apache Kafka service on Heroku](https://www.heroku.com/kafka).  

The SSE aspect is composed from another minimal Clojure library

Both this project and the library make extensive use of Java interop rather than reimplementing as idiomatic Clojure. This is also an idiomatic Clojure approach.

## Running Locally

Make sure you have Clojure installed.  Also, install the [Heroku Toolbelt](https://toolbelt.heroku.com/).

```sh
$ git clone https://github.com/opengrail/heroku-kafka-sse
$ cd heroku-kafka-sse
$ lein repl
user=> (require 'heroku-kafka-sse.web)
user=>(def server (heroku-kafka-sse.web/-main))
```

Your app should now be running on [localhost:5000](http://localhost:5000/).

## Deploying to Heroku

```sh
$ heroku create
$ git push heroku master
$ heroku open
```

or

[![Deploy to Heroku](https://www.herokucdn.com/deploy/button.png)](https://heroku.com/deploy)

