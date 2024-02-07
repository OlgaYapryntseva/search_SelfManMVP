# SelfMan Search Server

This is a minimalistic search engine built for the "SelfMan" project.

It is powered by Google Maps API, Palm2 API, ElasticSearch and Jsoup.

## How to start?

<b>First</b>, start ElasticSearch locally. The easiest way is to use the Docker image. You can download and run the container with the below command: 

<pre>docker run -d --name es762 -p 9200:9200 -e "discovery.type=single-node" elasticsearch:7.17.0</pre>

<u>IMPORTANT!</u> Ensure that the version of your ElasticSearch server is 7.17.0.

To ensure that the local ElasticSearch is up and running, just open this <a href="http://localhost:9200/">link</a>.

The server uses two indexes:
<ul>
<li><i>keywords</i></li>
To see documents contained in the index open in browser: 
<pre>localhost:9200/keywords/_search</pre>
<li><i>resources</i> (cached web pages content with additional information on provider)</li>
To see documents contained in the index open in browser: 
<pre>localhost:9200/resources/_search</pre>
</ul>

<b>Second</b>, add your Google Maps and Palm2 API keys into application.properties file.

<b>Third</b>, run the Spring Boot server. The application will be available on port 8080.

## Documentation 

All endpoints are available in Swagger documentation. Once the server is up and running, just open this  <a href="http://localhost:8080/swagger-ui/index.html#/">link</a>

## Further steps and WIP
- Add Docker containerization for the server
- Add search with filters
- Handle localization of cached resources

## Full tech stack
 - Java 17
 - Spring Boot 3.0
 - Maven
 - ElasticSearch
 - Jsoup
 - Swagger
 - Lombok



