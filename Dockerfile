FROM openjdk:17

ARG VCS_REF
LABEL maintainer="Sergey Royz <zjor.se@gmail.com>" \
  org.label-schema.vcs-ref=$VCS_REF \
  org.label-schema.vcs-url="git@github.com:zjor/web-fetcher.git "

EXPOSE 8080

ADD "target/web-fetcher.jar" "service.jar"

CMD ["sh", "-c", "java --enable-preview -jar service.jar"]