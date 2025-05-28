package io.cloudapp.observabilites.opentelemetry.demo;

import io.opentelemetry.instrumentation.annotations.WithSpan;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.Future;

@Service
public class AsyncService {
    @Async("myAsyncThreadPool")
    @WithSpan
    public Future<String> asyncMethod1() {
        System.out.println("asyncMethod1");
        try {
            asyncInnerFunc();
            Thread.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("asyncMethod1 done");
        return new AsyncResult<>("asyncMethod1");
    }
    
    @WithSpan
    public void asyncInnerFunc() {
        System.out.println("asyncInnerFunc");
    }
    
    @WithSpan
    public void innerRaise() throws Exception {
        throw new Exception("my exception");
    }
    
    @Async("myAsyncThreadPool")
    @WithSpan
    public Future<String> asyncMethod2() {
        System.out.println("asyncMethod2");
        try {
            Thread.sleep(3);
            
            innerRaise();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        System.out.println("asyncMethod2 done");
        return new AsyncResult<>("asyncMethod2");
    }
}