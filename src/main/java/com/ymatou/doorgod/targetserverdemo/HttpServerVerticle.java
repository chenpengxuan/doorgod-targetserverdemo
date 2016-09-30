package com.ymatou.doorgod.targetserverdemo;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;

import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by tuwenjie on 2016/9/5.
 */
public class HttpServerVerticle extends AbstractVerticle {

    private static ExecutorService executor = new ThreadPoolExecutor(10, 100,
            0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<Runnable>(1000000));

    @Override
    public void start() throws Exception {

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
                        executor.submit(new HttpReqProcessor(request, 0, 0));
                        break;
                    case "0ms1k":
                        executor.submit(new HttpReqProcessor(request, 0, 512));
                        break;
                    case "10ms0k":
                        executor.submit(new HttpReqProcessor(request, 10, 0));
                        break;
                    case "10ms1k":
                        executor.submit(new HttpReqProcessor(request, 10, 512));
                        break;
                    case "50ms0k":
                        executor.submit(new HttpReqProcessor(request, 50, 0));
                        break;
                    case "50ms10k":
                        executor.submit(new HttpReqProcessor(request, 50, 512));
                        break;
                    case "100ms0k":
                        executor.submit(new HttpReqProcessor(request, 100, 0));
                        break;
                    case "100ms1k":
                        executor.submit(new HttpReqProcessor(request, 100, 512));
                        break;
                    case "200ms0k":
                        executor.submit(new HttpReqProcessor(request, 200, 0));
                        break;
                    case "200ms1k":
                        executor.submit(new HttpReqProcessor(request, 200, 512));
                        break;
                    case "500ms0k":
                        executor.submit(new HttpReqProcessor(request, 500, 0));
                        break;
                    case "500ms1k":
                        executor.submit(new HttpReqProcessor(request, 500, 512));
                        break;
                    case "1000ms0k":
                        executor.submit(new HttpReqProcessor(request, 1000, 0));
                        break;
                    case "1000ms1k":
                        executor.submit(new HttpReqProcessor(request, 1000, 512));
                        break;
                    case "10000ms0k":
                        executor.submit(new HttpReqProcessor(request, 10000, 0));
                        break;
                    case "10000ms1k":
                        executor.submit(new HttpReqProcessor(request, 10000, 512));
                        break;
                    default:
                        request.response().setStatusCode(500);
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

}
