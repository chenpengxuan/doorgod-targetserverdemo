package com.ymatou.doorgod.targetserverdemo;

import io.vertx.core.http.HttpServerRequest;

import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by tuwenjie on 2016/9/30.
 */
public class HttpReqProcessor implements Runnable {



    private HttpServerRequest req;

    private int consumeTime;

    private int returnBlockSize;

    public HttpReqProcessor( HttpServerRequest req,
                             int consumeTime, int returnBlockSize) {
        this.consumeTime = consumeTime;
        this.returnBlockSize = returnBlockSize;
        this.req = req;
    }

    @Override
    public void run() {
        if ( consumeTime > 0 ) {
            try {
                Thread.sleep(consumeTime);
            } catch (InterruptedException e) {
                //just ignore
            }
        }
        req.response().setChunked(true);
        req.response().setStatusCode(200);
        req.response().headers().set("Content-Type", "text/plain");
        char[] chars = new char[returnBlockSize];
        Arrays.fill(chars, 'a');
        req.response().end("ok:" + new String(chars));
    }
}
