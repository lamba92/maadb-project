# MAADB Project

[![N|Solid](https://cldup.com/dTxpPi9lDf.thumb.png)](https://nodesource.com/products/nsolid)

[![Build Status](https://travis-ci.org/joemccann/dillinger.svg?branch=master)](https://travis-ci.org/joemccann/dillinger)

##Overview
This project consists on different type of resources:
- twitter messages: datasets of real twitters associated to a sentiment
- lexical resources:
    - list of words associated to a sentiment
    - list of words or symbols that are related to the sentence

For each word found in the Twitter messages we have three purposes: 
1. list the lexical resources containing each word, so that we can rely upon a unique resource source 
obtained by the fusion of the single resources.
2. count the number of occurrences of each word in the Twitter messages for each emotion.
3. draw a word cloud associated to the most frequent words in each emotion.

### Tech
Tools used to develop:

* [Docker] - 
* [Kotlin] - Programming Language
* [Ktor] - Framework for building asynchronous servers and clients in connected systems
* [Exposed] - Framework to access database with Kotlin
* [KMongo] - Kotlin API for MongoDB

##Our Solution
The solution works on two different type of database:

- SQL (PostgreSQL)
- NoSQL (MongoDB)

In particular, we take advantage of Docker to run our databases on a cluster: three raspberry pi.
Moreover, we created a MongoDB with shard and replication and implemented map reduction to retrieve data and statistics.

###Elaboration
The first step was to get tweets and elaborate them:

[IMAGE]

- remove URL and USERNAME
- remove the hashtags
- remove and create a relative list of emoji and emoticons
- find and replace punctuation with no space
- transform to lower case
- tokenisation
- using the slang words and acronyms file to replace them
- POS Tagging
- stemming
- removing the stop words
- count the stem frequency
- adding the result to a dictionary

### Docker

Docker runs on two different networks (one for SQL, another for NoSQL)
```sh
networks:
  mongo-net:
  postgres-net:
```
This will create the dillinger image and pull in the necessary dependencies. Be sure to swap out `${package.json.version}` with the actual version of Dillinger.

Once done, run the Docker image and map the port to whatever you wish on your host. In this example, we simply map port 8000 of the host to port 8080 of the Docker (or whatever port was exposed in the Dockerfile):

```sh
docker run -d -p 8000:8080 --restart="always" <youruser>/dillinger:${package.json.version}
```

### Get some data

In order to get data or statistics simply do a http request.
The request can be modulated choosing the following route
- sql or nosql : select from which database retrieve data
- data, statistics or worldcloud
- tweet, hashtags, emoticons or emojis : about data type
- sentiment on which restrict the research

E.g.
This query get the anger tweets from nosql database:

```sh
GET http://localhost8080/nosql/data/tweets/anger
```

It is also possible to get some statistics, simply replace the data keyword with statistics:

```sh
GET http://localhost8080/nosql/statistics/tweets/anger
```

License
----
**Free Software, Hell Yeah!**

[Docker]: <https://www.docker.com>
[Kotlin]: <https://kotlinlang.org>
[Ktor]: <https://ktor.io)>
[Exposed]: <https://github.com/JetBrains/Exposed>
[KMongo]: <https://litote.org/kmongo>