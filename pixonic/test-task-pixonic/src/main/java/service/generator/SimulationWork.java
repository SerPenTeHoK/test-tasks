package service.generator;

import lombok.extern.slf4j.Slf4j;
import service.consumer.impl.WorkerMediatorSingletonImpl;
import system.exception.WorkMediatoServiceStopedException;
import task.TaskTypeSleep;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

@Slf4j
public class SimulationWork extends Thread {
    // Must: FIXED_DELAY_SEC + RANDOM_DELAY_SEC <= 60, because it's second
    private static final int FIXED_DELAY_SEC = 40;
    private static final int RANDOM_DELAY_SEC = 20;

    private final int countTask;
    private final WorkerMediatorSingletonImpl workerMediator = WorkerMediatorSingletonImpl.getInstance();
    CountDownLatch startThread;
    private Random random = new Random();

    public SimulationWork(int countTask, CountDownLatch startThread) {
        super();
        this.countTask = countTask;
        this.startThread = startThread;
    }

    @Override
    public void run() {
        startThread.countDown();
        try {
            startThread.await();
            if (log.isDebugEnabled()) {
                System.out.println("Start test thread name:" + Thread.currentThread().getName() + " start time" + LocalDateTime.now());
                log.debug("Start test thread name:" + Thread.currentThread().getName() + " start time" + LocalDateTime.now());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < countTask; i++) {
            if (Thread.interrupted())
                break;
            try {
                Thread.sleep(50 * random.nextInt(10));
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
            LocalDateTime localDateTime = LocalDateTime.now();
            localDateTime = localDateTime.withSecond(FIXED_DELAY_SEC + random.nextInt(RANDOM_DELAY_SEC));
            TaskTypeSleep taskTypeSleep = new TaskTypeSleep(Thread.currentThread().getName(), i);
            boolean resultAdd = false;
            try {
                resultAdd = workerMediator.addTask(localDateTime, taskTypeSleep);
            } catch (WorkMediatoServiceStopedException e) {
                e.printStackTrace();
                log.error(e.toString());
            }
            if (log.isDebugEnabled()) {
                if (resultAdd) {
                    System.out.println("Task add by thread:" + Thread.currentThread().getName());
                    log.debug("Task add by thread:" + Thread.currentThread().getName());
                } else {
                    System.out.println("Task doesn't add by thread:" + Thread.currentThread().getName());
                    log.debug("Task doesn't add by thread:" + Thread.currentThread().getName());
                }
            }
        }
    }
}
