package com.nablanet.aula31;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ExecutorFactory {

    public static Executor executor;

    public static Executor getInstanceSingleThreadExecutor() {
        if (executor == null)
            executor = Executors.newSingleThreadExecutor();
        return executor;
    }

}
