package com.alibaba.cloudapp.observabilites.opentelemetry.demo;

import com.alibaba.cloudapp.api.observabilities.InvokeCount;
import com.alibaba.cloudapp.api.observabilities.MetricCollection;
import com.alibaba.cloudapp.api.observabilities.TraceService;
import io.opentelemetry.context.Scope;
import io.opentelemetry.instrumentation.annotations.SpanAttribute;
import io.opentelemetry.instrumentation.annotations.WithSpan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.servlet.http.HttpServletRequest;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@MetricCollection(serviceName = "demo-service")
@EnableScheduling
public class DemoController {
    
    private final Logger logger = LoggerFactory.getLogger(DemoController.class);
    
    ExecutorService executor = Executors.newFixedThreadPool(3);
    
    @Autowired
    AsyncService asyncService;
    
    @Autowired
    DemoMetricsHolder demoMetricsHolder;
    
    @Autowired
    TraceService traceService;
    
    // for counter
    AtomicLong syncCounter = new AtomicLong(10);
    AtomicLong asyncCounter = new AtomicLong(30);
    Random random = new Random(5);
    
    private Double getRandomDouble() {
        return random.nextDouble();
    }
    
    @GetMapping("/logging")
    public void logging(HttpServletRequest request) {
        logger.warn("=====WARNING TEST=====");
        logger.error("=====ERROR TEST=====");
        logger.debug("=====DEBUG TEST=====");
    }
    
    @GetMapping("/sync")
    public void sync(HttpServletRequest request) {
        logger.info("start sync invoke trace...");
        logger.info(syncInvoke());
        logger.info(syncInvokeWithTags("simpleTag"));
        logger.info("sync invoke trace done.");
        demoMetricsHolder.setMyCounter(syncCounter.getAndIncrement());
        bizfunction();

        logger.info("finish count metric");
    }

    @WithSpan("biz")
    private void bizfunction() {

    }

    @Scheduled(fixedRate = 5000)
    public void schedulerGauge() {
        demoMetricsHolder.setMyGauge(getRandomDouble());
        demoMetricsHolder.setMyHistogram(getRandomDouble());
    }
    
    @GetMapping("/invokeCount/{tag1}/{tag2}")
    @InvokeCount(name="sample.sync.invoke.counter",
            attributes = "args_1=args[1];args_2=args[2]")
    public void invoke(HttpServletRequest request,
                       @PathVariable String tag1,
                       @PathVariable String tag2) {
        logger.info("invokeCount metric tester...");
    }
    
    @WithSpan
    private String syncInvoke() {
        logger.info("sync invoke executing...");
        demoMetricsHolder.setMyAsyncCounter(asyncCounter.getAndIncrement());
        demoMetricsHolder.setMyAsyncGauge(getRandomDouble());
        return "syncInvoke finished";
    }
    
    @WithSpan
    private String syncInvokeWithTags(@SpanAttribute("myTag") String tag) {
        logger.info("sync invoke with tags {} executing...", tag);
        return "syncInvokeWithTags finished";
    }
    
    @GetMapping("/async")
    public void async(HttpServletRequest request)
            throws ExecutionException, InterruptedException {
        logger.info("start async invoke trace...");
        
        // Executor.submit
        logger.info("start to trace Executor.submit");
        Future<String> res = executor.submit(callableTask());
        logger.info("finish execute callableTask.call, result: {}",
                    res.get());
        
        // Spring AsyncTask
        logger.info("start to trace spring AsyncTask");
        asyncService.asyncMethod1();
        Future<String> res2 = asyncService.asyncMethod2();
        logger.info("finish execute spring AsyncTask, result: {}", res2.get());
        
        // CompletableFuture
        logger.info("start to trace CompletableFuture.supplyAsync");
        CompletableFuture.supplyAsync(this::myFunctionForCompletable)
                         .thenAccept(r -> logger.info(
                                 "CompletableFuture done, result: {}",
                                 r));
        logger.info("finish execute CompletableFuture.supplyAsync");
        
        // Mono
        logger.info("start to trace mono async");
        String monoResult = monoSubscriber(monoPublisher());
        logger.info("finish execute mono async, result: {}", monoResult);
        
        logger.info("finish async invoke trace");
    }
    
    @WithSpan
    private String myFunctionForCompletable() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "myCompletable test";
    }
    
    public Callable callableTask() {
        return new Callable() {
            @Override
            @WithSpan
            public Future<String> call() throws Exception {
                logger.info("start callable.call executing...");
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                logger.info("callable.call done");
                return new AsyncResult<>("callableTask done");
            }
        };
    }
    
    @WithSpan
    public Mono<String> monoPublisher() {
        logger.info("start monoPublisher...");
        return Mono.just("monoPublisher").delayElement(Duration.ofSeconds(1));
    }
    
    @WithSpan
    public String monoSubscriber(Mono m) {
        logger.info("monoSubscriber waiting...");
        return (String)m.block();
    }
    
    @GetMapping("/{tag}")
    public String invokeRemote(HttpServletRequest request,
                               @PathVariable String tag) {
        if (tag == null || tag.isEmpty()) {
            return "";
        }
        logger.info("traffic label before inject: {}",
                    traceService.getBaggageUserDataValue("myData"));
        Map<String, String> userData = new HashMap<>();
        userData.put("myData", tag);
        try (Scope ignored = traceService.withBaggageUserData(userData)){
            spanWithAndTagsAndAttributes(tag);
        }
        return tag;
    }
    
    @WithSpan
    private void spanWithAndTagsAndAttributes(@SpanAttribute("myTag") String tag) {
        logger.info("spanWithAndTagsAndAttributes with tag: {}", tag);
        logger.info("getValue from baggage: {}",
                    traceService.getBaggageUserDataValue("myData"));
    }
}