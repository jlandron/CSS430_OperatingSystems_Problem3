import java.util.Vector;

class QueueNode {
    private Vector<Integer> tidVector; // using Integer object instead of int

    /**
     * default constructor establishes
     */
    public QueueNode() {
        this.tidVector = new Vector<Integer>();
    }

    /**
     * Puts the process in this queue to sleep.
     * 
     * @return int : method to put a thread to sleep in this queueNode
     */
    public synchronized int sleep() {
        if (this.tidVector.size() == 0) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                // do nothing
            }
        }
        // remove the process at the head of the queue
        return this.tidVector.remove(0);
    }

    /**
     * wakeup a thread belonging to this QueueNodes pid (condition), if the pid had
     * more than one tid, any of the child threads belonging to this pid might be
     * notified to execute
     * 
     * @param tid : tid to add to this vector and potentially notify
     */
    public synchronized void wakeup(int tid) {
        this.tidVector.add(tid);
        this.notifyAll();
    }
}