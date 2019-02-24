import lombok.extern.slf4j.Slf4j;
import service.checking.FinishedWork;
import service.consumer.impl.WorkerMediatorSingletonImpl;
import service.generator.TaskGenerator;
import system.exception.GeneratorException;

@Slf4j
public class Main {
    private static final int THREAD_COUNT = 2;
    private static final int TASK_ON_THREAD = 100;

    public static void main(String[] args) throws InterruptedException {
        printToLog("Task Executer");
        TaskGenerator taskGenerator = new TaskGenerator();
        try {
            taskGenerator.startGenerate(THREAD_COUNT, TASK_ON_THREAD);
            printToLog("Finish taskGenerator.startGenerate");
        } catch (GeneratorException e) {
            e.printStackTrace();
            log.error(e.toString());
        }
        Thread.sleep(1000);
        printToLog("Task stopGenerate");
        taskGenerator.stopGenerate();
        printToLog("Task WorkerMediatorSingletonImpl.stopService");
        WorkerMediatorSingletonImpl.getInstance().stopService();
        printToLog("Task FinishedWork.testWork");
        FinishedWork.getInstance().testWork(THREAD_COUNT, TASK_ON_THREAD);
        printToLog("Task FinishedWork.printQueue");
        FinishedWork.getInstance().printQueue();
    }

    private static void printToLog(String msg) {
        msg = ">>>>>>>>>>>>> Main LOG" + msg;
        System.out.println(msg);
        log.error(msg);
    }
}
