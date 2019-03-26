package com.home.search.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

/**
 * Created by soncd on 12/02/2019
 */
public class ElasticExecutorService<T> {

    private Logger log = LoggerFactory.getLogger(ElasticExecutorService.class);

    private static ExecutorService executorService = new ThreadPoolExecutor(100, 100,
            0L, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(100, true),
            new ThreadPoolExecutor.CallerRunsPolicy());

    private ElasticService<T> elasticService;

    public ElasticExecutorService() {
    }

    public void setElasticService(ElasticService elasticService) {
        this.elasticService = elasticService;
    }

    public T execute(Object... params) throws Exception {
        try {
            ThreadElasticExecutor elasticExecutor = new ThreadElasticExecutor(params);
            Future<T> future = executorService.submit(elasticExecutor);
            return future.get();
        } catch (Exception e) {
            executorService.shutdownNow();
            log.error(e.getMessage());
            throw new Exception(e);
        }
    }

    private class ThreadElasticExecutor implements Callable<T> {
        private Object params;

        ThreadElasticExecutor(Object... params) {
            this.params = params;
        }

        @Override
        public T call() throws Exception {
            return elasticService.execute(params);
        }
    }

}
