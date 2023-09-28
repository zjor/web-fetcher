# Web Fetcher

A service for loading web page source code. Runs a browser in a headless mode on the background.

Steps to run the app:

1. Start a Standalone
   Chrome: `docker run -d -p 4444:4444 -p 7900:7900 --shm-size="2g" selenium/standalone-chrome:latest`
2. `mvn clean test spring-boot:run`