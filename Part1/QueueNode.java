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
        return this.tidVector.remove(0);
    }

    
    /** 
     * @param tid : tid to add to this vector and potentially notify
     */
    public synchronized void wakeup(int tid) {
        this.tidVector.add(tid);
        this.notify();
    }    
}