
# clojure-kafka-sse

A minimal Clojure app to expose Kafka topics over SSE.

It demonstrates the configuration needed to connect to the [Apache Kafka service on Heroku](https://www.heroku.com/kafka).  

The SSE aspect is composed from another minimal Clojure library

Both this project and the library make extensive use of Java interop rather than reimplementing as idiomatic Clojure. This is also an idiomatic Clojure approach.

## Running Locally

Make sure you have Clojure installed.  Also, install the [Heroku Toolbelt](https://toolbelt.heroku.com/).

You need to run a local Kafka instance.

You can download [directly from the Apache site](https://kafka.apache.org/downloads) or use the [packages from the good folks at Confluent](https://www.confluent.io/download/) could be more convenient if you are just starting out.

If you already have a Kafka cluster provisioned in Heroku (not in a private space), you can connect directly to that over SSL. Once you create your app it will have a bunch of configuration variables that you can also set on your development environment. **You would be placing certificates in your environment so you should be confident that the machine is reasonably secure. Do this at your own risk!**

```sh
$ git clone https://github.com/opengrail/heroku-kafka-sse
$ cd heroku-kafka-sse
$ lein repl
user=> (require 'heroku-kafka-sse.web)
user=>(def server (heroku-kafka-sse.web/-main))
```

Your app should now be running on [localhost:5000](http://localhost:5000/).

## Deploying to Heroku - this will cost real money

You will need to have a Kafka cluster provisioned in Heroku. There are **not any free** options.

```sh
$ heroku create
$ git push heroku master
$ heroku open
```

or

[![Deploy to Heroku](https://www.herokucdn.com/deploy/button.png)](https://heroku.com/deploy)

