package service.consumer.impl;

import lombok.extern.slf4j.Slf4j;
import service.consumer.TaskToTime;
import service.consumer.WorkerMediatorSingleton;
import system.exception.WorkMediatoServiceStopedException;
import system.exception.WorkMediatorAlreadyRunException;
import task.TaskTypeSleep;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
// ToDo ограничение количества потоков
public class WorkerMediatorSingletonImpl implements WorkerMediatorSingleton {
    private static volatile WorkerMediatorSingletonImpl instance = new WorkerMediatorSingletonImpl();
    private static volatile Thread serviceThread = null;
    private Queue queue = null;
    private volatile boolean isServiceWork = false;
    private AtomicLong counter = new AtomicLong(0);
    private AtomicLong rejectByTimeCounter = new AtomicLong(0);
    private Random rnd = new Random();

    private WorkerMediatorSingletonImpl() {
        Comparator<TaskToTime> timeComparator = Comparator.
                comparing(TaskToTime::getLocalDateTime)
                .thenComparing(TaskToTime::getOrderTime);
        queue = new PriorityBlockingQueue(100, timeComparator);
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

    public AtomicLong getRejectByTimeCounter() {
        return rejectByTimeCounter;
    }

    public boolean isWork() {
        return isServiceWork;
    }

    public boolean startService() throws WorkMediatorAlreadyRunException {
        if (isWork()) {
            throw new WorkMediatorAlreadyRunException("The service is already running");
        } else {
            return internalStartService();
        }
    }

    public boolean stopService() {
        if (serviceThread != null) {
            synchronized (WorkerMediatorSingletonImpl.class) {
                if (serviceThread != null) {
                    serviceThread.interrupt();
                    serviceThread = null;
                }
                isServiceWork = false;
            }
        }
        return true;
    }

    public boolean addTask(LocalDateTime taskStartLocalDateTime, Callable callable) throws WorkMediatoServiceStopedException {
        if (!isServiceWork)
            throw new WorkMediatoServiceStopedException("Service doesn't run");
        LocalDateTime nowLocalDateTime = LocalDateTime.now();
        if (taskStartLocalDateTime.isBefore(nowLocalDateTime)) {
            rejectByTimeCounter.incrementAndGet();
            return false;
        }

        if (log.isDebugEnabled()) {
            TaskTypeSleep tts = (TaskTypeSleep) callable;
            tts.setNeedStartTime(taskStartLocalDateTime);
            tts.setAddingToQueueTime(nowLocalDateTime);
        }

        ((PriorityBlockingQueue) queue).add(
                new TaskToTime(taskStartLocalDateTime, callable, nowLocalDateTime, counter.getAndIncrement())
        );
        return true;
    }

    // ToDo не бесконечный цикл, какую-нибудь алтернативу,
    // ToDo правильную проверку времени
    // Предполагаю, что есть отедельный флаг, говорящий о том, что
    // система перегружена loadFactor > N и надо выполнять заранее таски сделано: rnd.nextBoolean();
    private boolean internalStartService() {
        if (serviceThread == null) {
            serviceThread = new Thread() {
                public void run() {
                    while (true) {
                        if (interrupted())
                            break;
                        // ToDo подумать как бороться с перекладыванием из пустого в порожное
                        // и не сломать первый пришёл тот и выполнится
                        TaskToTime taskToTime = (TaskToTime) queue.poll();
                        if (taskToTime != null) {
                            LocalDateTime taskTime = taskToTime.getLocalDateTime();
                            LocalDateTime nowTime = LocalDateTime.now();
                            boolean testTime = nowTime.isBefore(taskTime);
                            if (testTime) {
                                boolean loadFactor = false;//rnd.nextBoolean();
                                if (loadFactor)
                                    SendToRun(taskToTime);
                                else
                                    queue.add(taskToTime);
                            } else {
                                SendToRun(taskToTime);
                            }
                        }
                    }
                }
            };
            serviceThread.start();
            isServiceWork = true;
        }
        return isServiceWork;
    }

    //ToDo Ограничить максимальное количество потоков, а-ля ThreadPool, с верхней границей.
    private void SendToRun(TaskToTime taskToTime) {
        new Thread(() -> {
            try {
                if (log.isDebugEnabled()) {
                    System.out.println("Run callable for task: " + taskToTime.toString());
                    log.debug("Run callable for task: " + taskToTime.toString());
                    TaskTypeSleep tts = (TaskTypeSleep) taskToTime.getCallable();
                    tts.setCounter(taskToTime.getCounterNum());
                    tts.setFactSendToRunTime(LocalDateTime.now());
                }
                taskToTime.getCallable().call();
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(e.toString());
                log.error(e.toString());
            }
        }).start();
    }
}
