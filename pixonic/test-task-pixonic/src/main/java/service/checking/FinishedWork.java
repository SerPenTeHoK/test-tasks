package service.checking;


import java.util.Comparator;
import java.util.Queue;
import java.util.concurrent.PriorityBlockingQueue;

public class FinishedWork {
    private static FinishedWork instance = new FinishedWork();
    private Queue finishQueue;

    private FinishedWork() {
        Comparator<FinishWorkElement> finishWorkElementComparator = new Comparator<FinishWorkElement>() {
            @Override
            public int compare(FinishWorkElement o1, FinishWorkElement o2) {
                int result = o1.getThreadName().compareTo(o2.getThreadName());
                return result != 0 ? result : Long.compare(o1.getOrder(), o2.getOrder());
            }
        };
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

    public boolean addFinishElement(String threadName, int order) {
        finishQueue.add(new FinishWorkElement(threadName, order));
        return true;
    }

    public void printQueue() {
        finishQueue.forEach(item -> System.out.println(item));
    }

    public boolean testWork(int threadCount, int tasksOnThread) {


        return false;
    }

}
