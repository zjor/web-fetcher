# Web Fetcher

A service for loading web page source code. Runs a browser in a headless mode on the background.
When submitting a scrape request it saves it to the memory structures (map, queue) and provides generated UUID to the consumer.
When queue is not empty, it takes next in the queue request, updates the status to 'processing',
downloads requested source page and stores the page to backblaze.
Once successfully stored, it updates the request status to 'ready'.
If the request fails, it updates the status to 'failed' and stores the error message.


Steps to run the app:

1. Start a Standalone
   Chrome: `docker run -d -p 4444:4444 -p 7900:7900 --shm-size="2g" selenium/standalone-chrome:latest`
2. `mvn clean test spring-boot:run`