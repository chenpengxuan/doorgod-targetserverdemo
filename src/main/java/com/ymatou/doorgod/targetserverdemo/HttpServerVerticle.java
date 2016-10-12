package com.ymatou.doorgod.targetserverdemo;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Context;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerRequest;

import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by tuwenjie on 2016/9/5.
 */
public class HttpServerVerticle extends AbstractVerticle {

    public static int SIZE_1K = 1024;
    public static int SIZE_10K = 10240;

    public static String BODY_1K;

    public static String BODY_10K;

    static {
        char[] chars = new char[SIZE_1K];
        Arrays.fill(chars, 'a');

        BODY_1K = new String(chars);

        chars = new char[SIZE_10K ];
        Arrays.fill(chars, 'a');
        BODY_10K = new String(chars);
    }

    @Override
    public void start() throws Exception {

        Context context = vertx.getOrCreateContext();

        HttpServer server = vertx.createHttpServer();

        server.requestHandler(request -> {
            String firstPart = "/";
            if ( request.path().length() > 1 ) {
                firstPart = request.path().substring(1);
                int index = firstPart.indexOf("/");
                if ( index > 0) {
                    firstPart = firstPart.substring(0, index);
                }
            }
            try {
                switch (firstPart) {
                    case "0ms0k":
                        endResponse(request, 0, 0);
                        break;
                    case "0ms1k":
                        endResponse(request, 0, SIZE_1K);
                        break;
                    case "0ms10k":
                        endResponse(request, 0, SIZE_10K);
                        break;
                    case "10ms0k":
                        endResponse(request, 10, 0);
                        break;
                    case "10ms1k":
                        endResponse(request, 10, SIZE_1K);
                        break;
                    case "10ms10k":
                        endResponse(request, 10, SIZE_10K);
                        break;
                    case "50ms0k":
                        endResponse(request, 50, 0);
                        break;
                    case "50ms1k":
                        endResponse(request, 50, SIZE_1K);
                        break;
                    case "50ms10k":
                        endResponse(request, 50, SIZE_10K);
                        break;
                    case "100ms0k":
                        endResponse(request, 100, 0);
                        break;
                    case "100ms1k":
                        endResponse(request, 100, SIZE_1K);
                        break;
                    case "100ms10k":
                        endResponse(request, 100, SIZE_10K);
                    case "200ms0k":
                        endResponse(request, 200, 0);
                        break;
                    case "200ms1k":
                        endResponse(request, 200, SIZE_1K);
                        break;
                    case "200ms10k":
                        endResponse(request, 200, SIZE_10K);
                        break;
                    case "500ms0k":
                        endResponse(request, 500, 0);
                        break;
                    case "500ms1k":
                        endResponse(request, 500, SIZE_1K);
                        break;
                    case "500ms10k":
                        endResponse(request, 500, SIZE_10K);
                        break;
                    case "1000ms0k":
                        endResponse(request, 1000, 0);
                        break;
                    case "1000ms1k":
                        endResponse(request, 1000, SIZE_1K);
                        break;
                    case "1000ms10k":
                        endResponse(request, 1000, SIZE_10K);
                        break;
                    case "2000ms0k":
                        endResponse(request, 2000, 0);
                        break;
                    case "2000ms1k":
                        endResponse(request, 2000, SIZE_1K);
                        break;
                    case "2000ms10k":
                        endResponse(request, 2000, SIZE_10K);
                        break;
                    case "10000ms0k":
                        endResponse(request, 10000, 0);
                        break;
                    case "10000ms1k":
                        endResponse(request, 10000, SIZE_1K);
                        break;
                    case "10000ms10k":
                        endResponse(request, 10000, SIZE_10K);
                        break;
                    default:
                        request.response().setStatusCode(404);
                        request.response().end("Unknown uri:" + request.path());
                }
            } catch (Exception e) {
                request.response().setStatusCode(500);
                request.response().end(e.getLocalizedMessage());
            }
        });

        Properties props = new Properties();
        props.load(HttpServerVerticle.class.getResourceAsStream("/app.properties"));
        server.listen(Integer.valueOf(props.getProperty("vertxServerPort")));
    }


    private void endResponse(HttpServerRequest req, long consumeTime, int bodySize ) {
        if (consumeTime > 0 ) {
            vertx.setTimer(consumeTime, id -> {
                buidResponse(req, bodySize);
            });
        } else {
            buidResponse( req, bodySize);
        }
    }

    private void buidResponse( HttpServerRequest req, int bodySize) {
        req.response().setChunked(true);
        req.response().setStatusCode(200);
        req.response().headers().set("Content-Type", "text/plain");
        req.response().write("ok:" + getBody(bodySize));
        req.response().end();
    }

    private String getBody( int bodySize ) {
        if ( bodySize == SIZE_1K ) {
            return BODY_1K;
        } else if ( bodySize == SIZE_10K) {
            return BODY_10K;
        } else {
            char[] chars = new char[bodySize];
            Arrays.fill(chars, 'a');
            return new String(chars);
        }
    }

}
