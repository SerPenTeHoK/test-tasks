package service.consumer;

import system.exception.WorkMediatoServiceStopedException;
import system.exception.WorkMediatorAlreadyRunException;

import java.time.LocalDateTime;
import java.util.concurrent.Callable;

// ToDo пауза/возврат возможности добавления пар
public interface WorkerMediatorSingleton {

    boolean isWork();

    boolean startService() throws WorkMediatorAlreadyRunException;

    boolean stopService();

    boolean addTask(LocalDateTime localDateTime, Callable callable) throws WorkMediatoServiceStopedException;
}
