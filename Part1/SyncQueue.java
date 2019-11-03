
public class SyncQueue {
    private QueueNode[] queue;
    private final int DEFAULT_COND = 20;
    private final int DEFAULT_PID = 0;

    /**
     * Default constructor
     */
    public SyncQueue() {
        makeQueue(DEFAULT_COND);
    }

    /**
     * override constructor 
     * 
     * @param condMax : max number of conditions Queue can hold
     */
    public SyncQueue(int condMax) {
        makeQueue(condMax);
    }

    /**
     * Queue initialization method called by constructors 
     * 
     * @param condMax : max number of conditions Queue can hold
     */
    private void makeQueue(int condMax) {
        queue = new QueueNode[condMax];
        for (int i = 0; i < condMax; i++) {
            queue[i] = new QueueNode();
        }
    }

    /**
     * @param condition : pid passed in
     * @return int : tid of thread that was put to sleep
     */
    public int enqueueAndSleep(int condition) {
        int retVal = -1;
        if (condition >= 0 && condition < queue.length) {
            retVal = queue[condition].sleep();
        }
        return retVal;

    }

    /**
     * @param condition
     * @return boolean
     */
    public boolean dequeueAndWakeup(int condition) {
        return dequeueAndWakeup(condition, DEFAULT_PID);
    }

    /**
     * @param condition
     * @param tid
     * @return boolean
     */
    public boolean dequeueAndWakeup(int condition, int tid) {
        if (condition >= 0 && condition < queue.length) {
            queue[condition].wakeup(tid);
            return true;
        }
        return false;
    }
}