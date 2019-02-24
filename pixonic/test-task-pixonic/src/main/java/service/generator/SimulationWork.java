package service.generator;

import lombok.extern.slf4j.Slf4j;
import service.consumer.impl.WorkerMediatorSingletonImpl;
import system.exception.WorkMediatoServiceStopedException;
import task.TaskTypeSleep;

import java.time.LocalDateTime;
import java.util.Random;

@Slf4j
public class SimulationWork extends Thread {
    // Must: FIXED_DELAY_SEC + RANDOM_DELAY_SEC <= 60, because it's second
    private static final int FIXED_DELAY_SEC = 40;
    private static final int RANDOM_DELAY_SEC = 20;

    private final int countTask;
    private final WorkerMediatorSingletonImpl workerMediator = WorkerMediatorSingletonImpl.getInstance();

    private Random random = new Random();

    public SimulationWork(int countTask) {
        super();
        this.countTask = countTask;
    }

    @Override
    public void run() {
        for (int i = 0; i < countTask; i++) {
            if (Thread.interrupted())
                break;
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
