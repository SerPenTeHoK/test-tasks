package service.generator;

import lombok.extern.slf4j.Slf4j;
import service.consumer.impl.WorkerMediatorSingletonImpl;
import task.TaskTypeSleep;

import java.time.LocalDateTime;
import java.util.Random;

@Slf4j
public class SimulationWork extends Thread {
    private static final int TIME_DELAY_SEC = 60;
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
            localDateTime = localDateTime.withSecond(random.nextInt(TIME_DELAY_SEC));
            TaskTypeSleep taskTypeSleep = new TaskTypeSleep(Thread.currentThread().getName(), i);
            if (workerMediator.addTask(localDateTime, taskTypeSleep)) {
                System.out.println("Task add by thread:" + Thread.currentThread().getName());
                log.debug("Task add by thread:" + Thread.currentThread().getName());
            } else {
                System.out.println("Task doesn't add by thread:" + Thread.currentThread().getName());
                log.debug("Task doesn't add by thread:" + Thread.currentThread().getName());
            }
        }

    }
}
