package service.generator;

import lombok.extern.slf4j.Slf4j;
import system.exception.GeneratorException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

@Slf4j
public class TaskGenerator {
    public static final int THREAD_COUNT = 10;
    public static final int TASK_ON_THREAD = 20;

    private static final CountDownLatch START_THREAD = new CountDownLatch(TaskGenerator.THREAD_COUNT);


    List<Thread> threadList = new ArrayList<>();

    public TaskGenerator() {
    }

    public boolean startGenerate(int threadCount, int tasksOnThread) throws GeneratorException {
        if (threadCount < 1 || tasksOnThread < 1) {
            StringBuilder sb = new StringBuilder(100);
            sb.append("incorrect params, must be >= 1")
                    .append(" threadCount=")
                    .append(threadCount)
                    .append(" tasksOnThread")
                    .append(tasksOnThread);
            throw new GeneratorException(sb.toString());
        }
        for (int i = 0; i < threadCount; i++) {
            threadList.add(new SimulationWork(tasksOnThread, START_THREAD));
        }

        threadList.forEach(thread -> thread.start());
        return true;
    }

    public boolean stopGenerate() {
        threadList.forEach(thread -> thread.interrupt());
        return true;
    }

}
