package service.generator;

import lombok.extern.slf4j.Slf4j;
import system.exception.GeneratorException;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class TaskGenerator {
    public static final int THREAD_COUNT = 20;
    public static final int TASK_ON_THREAD = 300;

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
            threadList.add(new SimulationWork(tasksOnThread));
        }
        threadList.parallelStream().forEach(thread -> thread.start());
        return true;
    }

    public boolean stopGenerate() {
        threadList.parallelStream().forEach(thread -> thread.interrupt());
        return true;
    }

}
