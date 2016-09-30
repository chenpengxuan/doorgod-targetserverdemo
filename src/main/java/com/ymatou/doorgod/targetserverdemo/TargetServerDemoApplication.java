package com.ymatou.doorgod.targetserverdemo;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;

import java.util.concurrent.CountDownLatch;

/**
 * Created by tuwenjie on 2016/9/30.
 */
public class TargetServerDemoApplication {

    public static void main(String[] args ) {

        //指示vertx使用logback记日志
        System.setProperty("vertx.logger-delegate-factory-class-name",
                io.vertx.core.logging.SLF4JLogDelegateFactory.class.getName());

        //当前无需更多配置
        VertxOptions vertxOptions = new VertxOptions();
        Vertx vertx = Vertx.vertx(vertxOptions);

        CountDownLatch latch = new CountDownLatch(1);

        Throwable[] throwables = new Throwable[]{null};

        vertx.deployVerticle(HttpServerVerticle.class.getName(),
                new DeploymentOptions().setInstances(VertxOptions.DEFAULT_EVENT_LOOP_POOL_SIZE),
                result -> {
                    if (result.failed()) {
                        throwables[0] = result.cause();
                    }
                    latch.countDown();
                });

        //等待Verticles部署完成
        try {
            latch.await();
        } catch (InterruptedException e) {
            throwables[0] = e;
        }

        if (throwables[0] != null) {
            throw new RuntimeException("Failed to startup TargetServerDemoApplication", throwables[0]);
        }

        System.out.println( "Target server demo startup..." );
    }

}
