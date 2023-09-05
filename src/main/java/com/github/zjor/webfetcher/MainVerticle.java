package com.github.zjor.webfetcher;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MainVerticle extends AbstractVerticle {

    @Override
    public void start() throws Exception {
        var router = Router.router(vertx);
        router.post("/api/download").handler(ctx -> {
            // TODO: parse request body
            ctx.response().end("Hello world");
        });

        vertx.createHttpServer()
                .requestHandler(router)
                .listen(8080)
                .onSuccess(server -> {
                    log.info("Server started at http://localhost:{}/", server.actualPort());
                });
    }

    public static void main(String[] args) throws Exception {
        var vertx = Vertx.vertx();
        vertx.deployVerticle(new MainVerticle());
    }
}
