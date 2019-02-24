package service.checking;


import lombok.extern.slf4j.Slf4j;
import service.consumer.impl.WorkerMediatorSingletonImpl;
import service.generator.TaskGenerator;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
public class FinishedWork {
    private static FinishedWork instance = new FinishedWork();
    List<FinishWorkElement> resultList = null;
    private Queue finishQueue;
    private AtomicBoolean isChanged = new AtomicBoolean(true);

    private FinishedWork() {
        Comparator<FinishWorkElement> finishWorkElementComparator = Comparator
                .comparing(FinishWorkElement::getThreadName)
                .thenComparing(FinishWorkElement::getOrderInThread);
        finishQueue = new PriorityBlockingQueue(100, finishWorkElementComparator);
    }

    public static FinishedWork getInstance() {
        if (instance == null) {
            synchronized (FinishedWork.class) {
                if (instance == null) {
                    instance = new FinishedWork();
                }
            }
        }
        return instance;
    }

    public boolean addFinishElement(String threadName, int order,
                                    LocalDateTime needStartTime,
                                    LocalDateTime addingToQueueTime,
                                    LocalDateTime factSendToRunTime,
                                    long counter) {
        finishQueue.add(new FinishWorkElement(threadName, order,
                needStartTime,
                addingToQueueTime,
                factSendToRunTime,
                counter));
        this.isChanged.set(true);
        return true;
    }

    public boolean addFinishElement(String threadName, int order) {
        finishQueue.add(new FinishWorkElement(threadName, order));
        this.isChanged.set(true);
        return true;
    }

    public void printQueue() {
        fillResultListIfNeed();
        resultList.forEach(finishWorkElement ->
                printToLogAndOut(finishWorkElement.toString())
        );
        printToLogAndOut("Reject task: " + WorkerMediatorSingletonImpl.getInstance().getRejectByTimeCounter());
    }

    public boolean testWork(int threadCount, int tasksOnThread) {
        fillResultListIfNeed();
        if (resultList.size() != TaskGenerator.THREAD_COUNT * TaskGenerator.TASK_ON_THREAD) {
            return false;
        }
        for (int i = 1; i < resultList.size(); i++) {
            if (resultList.get(i).getCounterNum() - resultList.get(i - 1).getCounterNum() != 1) {
                return false;
            }
        }
        return true;
    }

    public boolean getMetaDataFromResult() {
        fillResultListIfNeed();

        long countMoreZero = 0;
        long avgDelay = 0;
        for (int i = 0; i < resultList.size(); i++) {
            FinishWorkElement element = resultList.get(i);
            // ToDo понять ошибку: -999 ошибка вычисления наносекунд:
            // 2019-02-25T02:15:51.999 и 2019-02-25T02:15:52
            long systemDelay = element.getFactSendToRunTime().getNano() -
                    element.getNeedStartTime().getNano();
            systemDelay = systemDelay / 1_000_000;
            printToLogAndOut("Num: " + element.getCounterNum() + " System delay start(ms) = " + systemDelay);
            if(systemDelay > 0){
                avgDelay += systemDelay;
                countMoreZero++;
            }
        }

        if(countMoreZero != 0)
            printToLogAndOut("Average Delta start: " + avgDelay/countMoreZero);
        else
            printToLogAndOut("Haven't element > 0");
        
        return true;
    }

    private void fillResultListIfNeed() {
        if (resultList == null || isChanged.get()) {
            resultList = fillArrayByQueue(finishQueue);
        }
    }

    private void printToLogAndOut(String msg) {
        msg = ">>> Finish LOG " + msg;
        System.out.println(msg);
        log.debug(msg);
    }

    private List<FinishWorkElement> fillArrayByQueue(Queue inQueue) {
        List<FinishWorkElement> finishWorkElements = new ArrayList();
        finishWorkElements.addAll(inQueue);
        Comparator<FinishWorkElement> finishWorkElementComparatorByCounter = Comparator
                .comparing(FinishWorkElement::getCounterNum);
        finishWorkElements.sort(finishWorkElementComparatorByCounter);
        return finishWorkElements;
    }
}
