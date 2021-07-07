package task;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import service.checking.FinishedWork;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.concurrent.Callable;

@Slf4j
@Data
public class TaskTypeSleep implements Callable {
    private static final int BASE_TIME_TO_SLEEP = 1000;
    private static final int RANDOM_BOUND_TIME_TO_SLEEP = 10;

    private final String threadName;
    private final int workNum;
    private LocalDateTime needStartTime;
    private LocalDateTime addingToQueueTime;
    private LocalDateTime factSendToRunTime;
    private long counter;


    public TaskTypeSleep(String threadName, int workNum) {
        super();
        this.threadName = threadName;
        this.workNum = workNum;
    }

    @Override
    public Object call() throws Exception {
        int timeToSleep = new Random().nextInt(RANDOM_BOUND_TIME_TO_SLEEP) * BASE_TIME_TO_SLEEP;
        Thread.sleep(timeToSleep);
        if (log.isDebugEnabled()) {
            FinishedWork.getInstance().addFinishElement(threadName, workNum,
                    needStartTime, addingToQueueTime, factSendToRunTime,
                    counter);
        } else {
            FinishedWork.getInstance().addFinishElement(threadName, workNum);
        }

        StringBuilder sb = new StringBuilder(240);
        sb.append("date: ")
                .append(LocalDateTime.now())
                .append(" result: ")
                .append(this.toString());
        System.out.println(sb.toString());
        log.debug(sb.toString());
        return timeToSleep;
    }
}
