package service.consumer;

import java.time.LocalDateTime;
import java.util.concurrent.Callable;

public interface WorkerMediatorSingleton {
    boolean stopService();

    boolean addTask(LocalDateTime localDateTime, Callable callable);
}
