package service.consumer.impl;

import lombok.extern.slf4j.Slf4j;
import service.consumer.TaskToTime;
import service.consumer.WorkerMediatorSingleton;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.PriorityBlockingQueue;

@Slf4j
public class WorkerMediatorSingletonImpl implements WorkerMediatorSingleton {
    private static volatile WorkerMediatorSingletonImpl instance = new WorkerMediatorSingletonImpl();
    private static volatile long count = 0L;
    private Queue queue = null;
    private Thread serviceThread;
    private boolean paused = false;

    private WorkerMediatorSingletonImpl() {
        Comparator<TaskToTime> timeComparator = Comparator.
                comparing(TaskToTime::getLocalDateTime)
                .thenComparing(TaskToTime::getOrderTime);
        queue = new PriorityBlockingQueue(10, timeComparator);
        startService();
    }

    public static WorkerMediatorSingletonImpl getInstance() {
        if (instance == null) {
            synchronized (WorkerMediatorSingletonImpl.class) {
                if (instance == null) {
                    instance = new WorkerMediatorSingletonImpl();
                }
            }
        }
        return instance;
    }

    public boolean stopService() {
        serviceThread.interrupt();
        return true;
    }

    public boolean addTask(LocalDateTime localDateTime, Callable callable) {
        if (paused || localDateTime.isAfter(LocalDateTime.now()))
            return false;

        ((PriorityBlockingQueue) queue).add(
                new TaskToTime(localDateTime, callable, LocalDateTime.now())
        );
        return true;
    }

    private boolean startService() {
        serviceThread = new Thread() {
            public void run() {
                while (true) {
                    if (interrupted())
                        break;
                    TaskToTime taskToTime = (TaskToTime) queue.poll();
                    if (taskToTime != null) {
                        if (log.isDebugEnabled()) {
                            System.out.println("Task " + taskToTime.toString());
                            log.debug("Task " + taskToTime.toString());
                        }
                        LocalDateTime taskTime = taskToTime.getLocalDateTime();
                        LocalDateTime nowTime = LocalDateTime.now();
                        boolean testTime = nowTime.isBefore(taskTime);
                        if (testTime) {
                            queue.add(taskToTime);
                        } else {
                            new Thread(() -> {
                                try {
                                    taskToTime.getCallable().call();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    log.error(e.toString());
                                }
                            }).start();
                        }
                    }
                }
            }
        };
        serviceThread.start();

        return true;
    }
}
