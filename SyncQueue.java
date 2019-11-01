
public class SyncQueue {
    private QueueNode[] queue;
    private final int DEFAULT_COND = 20;
    private final int DEFAULT_PID = 0;

    
    /** 
     * @return 
     */
    public SyncQueue() {
        makeQueue(DEFAULT_COND);
    }

    
    /** 
     * @param condMax
     * @return 
     */
    public SyncQueue(int condMax) {
        makeQueue(condMax);
    }

    
    /** 
     * @param condMax
     */
    private void makeQueue(int condMax) {
        queue = new QueueNode[condMax];
        for (int i = 0; i < condMax; i++) {
            queue[i] = new QueueNode();
        }
    }

    
    /** 
     * @param condition
     * @return int
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
        }
        return false;
    }
}