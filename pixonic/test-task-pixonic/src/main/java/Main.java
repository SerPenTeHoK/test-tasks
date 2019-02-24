import lombok.extern.slf4j.Slf4j;
import service.checking.FinishedWork;
import service.consumer.impl.WorkerMediatorSingletonImpl;
import service.generator.TaskGenerator;
import system.exception.GeneratorException;
import system.exception.WorkMediatorAlreadyRunException;

@Slf4j
public class Main {
    private static final int BASE_WAIT = 1000;
    private static final int BASE_WAIT_THREAD = 400;
    private static final int BASE_WAIT_TASK = 200;
    private static final int START_WAIT = BASE_WAIT + BASE_WAIT_THREAD * TaskGenerator.THREAD_COUNT;
    private static final int FINISH_WAIT = BASE_WAIT +
            BASE_WAIT_TASK * TaskGenerator.TASK_ON_THREAD * TaskGenerator.THREAD_COUNT;

    public static void main(String[] args) throws InterruptedException, WorkMediatorAlreadyRunException {
        printToOutAndLog("Start!");
        WorkerMediatorSingletonImpl worker = WorkerMediatorSingletonImpl.getInstance();
        printToOutAndLog("Start worker service");
        worker.startService();
        Thread.sleep(setMaxTime(START_WAIT));
        TaskGenerator taskGenerator = new TaskGenerator();
        Thread.sleep(setMaxTime(START_WAIT));
        try {
            taskGenerator.startGenerate(TaskGenerator.THREAD_COUNT, TaskGenerator.TASK_ON_THREAD);
            printToOutAndLog("Finish taskGenerator startGenerate");
        } catch (GeneratorException e) {
            e.printStackTrace();
            log.error(e.toString());
        }
        printToOutAndLog("Wait work");
        Thread.sleep(setMaxTime(FINISH_WAIT));
        printToOutAndLog("Task stopGenerate tasks");
        taskGenerator.stopGenerate();
        Thread.sleep(setMaxTime(FINISH_WAIT)*2);
        printToOutAndLog("Task WorkerMediatorSingletonImpl stopService");
        worker.stopService();
        printToOutAndLog("Wait stop worker service");
        Thread.sleep(setMaxTime(FINISH_WAIT));
        if (log.isDebugEnabled()) {
            printToOutAndLog("Task FinishedWork printQueue");
            FinishedWork.getInstance().printQueue();
            printToOutAndLog("Task FinishedWork testWork");
            if(FinishedWork.getInstance().testWork(TaskGenerator.THREAD_COUNT, TaskGenerator.TASK_ON_THREAD))
                printToOutAndLog("All task finished");
            else
                printToOutAndLog("Not all task finished");
            printToOutAndLog("MetaData:");
            FinishedWork.getInstance().getMetaDataFromResult();
            printToOutAndLog("Finish!");
        }
    }

    private static void printToOutAndLog(String msg) {
        msg = ">>>>>>>>>>>>> Main LOG: " + msg;
        System.out.println(msg);
        log.debug(msg);
    }

    private static int setMaxTime(int delay){
        return delay > 10000 ? 10000 : delay;
    }
}
