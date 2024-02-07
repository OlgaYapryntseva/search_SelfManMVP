# Dockerfile for Spring application

FROM eclipse-temurin:21-jre

ENV SPRING_DATA_ELASTICSEARCH_CLUSTER_NAME=docker-cluster \
    SPRING_DATA_ELASTICSEARCH_CLUSTER_NODES=elasticsearch:9200 \
    SPRING_DATA_ELASTICSEARCH_REPOSITORIES_ENABLED=true \
    MAPS_API_KEY=AIzaSyCXzYDHGRZerNSa_35Nh7dkmssSUjd5M9Q \
    PALM2_API_KEY=AIzaSyBtVgHzGfsk3IGX6b_c87Xf9ivnrfHkVEY

WORKDIR /app

COPY ./target/selfman_search-0.0.1-SNAPSHOT.jar ./selfman_search.jar

EXPOSE 8080

CMD ["java", "-jar", "/app/selfman_search.jar"]
