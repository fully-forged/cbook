FROM java:8-alpine
MAINTAINER Your Name <you@example.com>

ADD target/uberjar/cbook.jar /cbook/app.jar

EXPOSE 3000

CMD ["java", "-jar", "/cbook/app.jar"]
