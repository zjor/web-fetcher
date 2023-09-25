# Web Fetcher

A service for loading web page source code. Runs a browser in a headless mode on the background.
When submitting a scrape request it saves it to the memory structures (map, queue) and provides generated UUID to the consumer.
The scheduled task picks up a job from the queue and processes it with a small period delay.

Steps to run the app:

1. Start a Standalone
   Chrome: `docker run -d -p 4444:4444 -p 7900:7900 --shm-size="2g" selenium/standalone-chrome:latest`
2. `mvn clean test spring-boot:run`