package task;

import lombok.extern.slf4j.Slf4j;
import service.checking.FinishedWork;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.concurrent.Callable;

@Slf4j
public class TaskTypeSleep implements Callable {
    private static final int BASE_TIME_TO_SLEEP = 100;
    private static final int RANDOM_BOUND_TIME_TO_SLEEP = 10;

    private final String threadName;
    private final int workNum;

    public TaskTypeSleep(String threadName, int workNum) {
        super();
        this.threadName = threadName;
        this.workNum = workNum;
    }

    @Override
    public String toString() {
        return "TaskTypeSleep{" +
                "threadName='" + threadName + '\'' +
                ", workNum=" + workNum +
                '}';
    }

    @Override
    public Object call() throws Exception {
        int timeToSleep = new Random().nextInt(RANDOM_BOUND_TIME_TO_SLEEP) * BASE_TIME_TO_SLEEP;
        Thread.sleep(timeToSleep);
        FinishedWork.getInstance().addFinishElement(threadName, workNum);
        StringBuilder sb = new StringBuilder(100);
        sb.append("date: ")
                .append(LocalDateTime.now())
                .append(" result: ")
                .append(this.toString());
        System.out.println(sb.length());
        System.out.println(sb.toString());
        log.debug(sb.toString());
        return timeToSleep;
    }
}
