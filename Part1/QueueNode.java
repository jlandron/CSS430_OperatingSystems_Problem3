import java.util.Vector;

class QueueNode {
    private Vector<Integer> tidVector; // using Integer object instead of int

    
    /** 
     * @return 
     */
    public QueueNode() {
        this.tidVector = new Vector<Integer>();
    }

    
    /** 
     * @return int
     */
    public synchronized int sleep() {
        if (this.tidVector.size() == 0) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                // do nothing
            }
        }
        return this.tidVector.remove(0);
    }

    
    /** 
     * @param tid
     */
    public synchronized void wakeup(int tid) {
        this.tidVector.add(tid);
        this.notify();
    }
}