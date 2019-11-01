import java.util.*;

public class Scheduler extends Thread {
    private Vector<TCB> queueOne;
    private Vector<TCB> queueTwo;
    private Vector<TCB> queueThree;
    private int timeSlice;

    private static final int DEFAULT_TIME_SLICE = 1000;

    // New data added to p161
    private boolean[] tids; // Indicate which ids have been used
    private static final int DEFAULT_MAX_THREADS = 10000;

    // A new feature added to p161
    // Allocate an ID array, each element indicating if that id has been used
    private int nextId = 0;

    /**
     * @param maxThreads
     */
    private void initTid(int maxThreads) {
        tids = new boolean[maxThreads];
        for (int i = 0; i < maxThreads; i++)
            tids[i] = false;
    }

    /**
     * A new feature added to p161 Search an available thread ID and provide a new
     * thread with this ID
     * 
     * @return int
     */

    private int getNewTid() {
        for (int i = 0; i < tids.length; i++) {
            int tentative = (nextId + i) % tids.length;
            if (tids[tentative] == false) {
                tids[tentative] = true;
                nextId = (tentative + 1) % tids.length;
                return tentative;
            }
        }
        return -1;
    }

    /**
     * A new feature added to p161 Return the thread ID and set the corresponding
     * tids element to be unused
     *
     * @param tid
     * @return boolean
     */

    private boolean returnTid(int tid) {
        if (tid >= 0 && tid < tids.length && tids[tid] == true) {
            tids[tid] = false;
            return true;
        }
        return false;
    }

    /**
     * method used to get TCB using multilevel scheduling system
     * 
     * @return TCB
     */
    public TCB getMyTcb() {
        Thread myThread = Thread.currentThread(); // Get my thread object
        synchronized (queueOne) { // top tier queue
            for (int i = 0; i < queueOne.size(); i++) {
                TCB tcb = (TCB) queueOne.elementAt(i);
                Thread thread = tcb.getThread();
                if (thread == myThread) // if this is my TCB, return it
                    return tcb;
            }
        }
        synchronized (queueTwo) { // second tier queue
            for (int i = 0; i < queueTwo.size(); i++) {
                TCB tcb = (TCB) queueTwo.elementAt(i);
                Thread thread = tcb.getThread();
                if (thread == myThread) // if this is my TCB, return it
                    return tcb;
            }
        }
        synchronized (queueThree) { // third tier queue
            for (int i = 0; i < queueThree.size(); i++) {
                TCB tcb = (TCB) queueThree.elementAt(i);
                Thread thread = tcb.getThread();
                if (thread == myThread) // if this is my TCB, return it
                    return tcb;
            }
        }
        return null;
    }

    /**
     * A new feature added to p161 Return the maximal number of threads to be
     * spawned in the system
     * 
     * @return int
     */

    public int getMaxThreads() {
        return tids.length;
    }

    /**
     * @return
     */
    public Scheduler() {
        timeSlice = DEFAULT_TIME_SLICE;
        queueOne = new Vector<>();
        queueTwo = new Vector<>();
        queueThree = new Vector<>();
        initTid(DEFAULT_MAX_THREADS);
    }

    /**
     * @param quantum
     * @return
     */
    public Scheduler(int quantum) {
        timeSlice = quantum;
        queueOne = new Vector<>();
        queueTwo = new Vector<>();
        queueThree = new Vector<>();
        initTid(DEFAULT_MAX_THREADS);
    }

    /**
     * A new feature added to p161 // A constructor to receive the max number of
     * threads to be spawned
     * 
     * @param quantum
     * @param maxThreads
     * @return
     */

    public Scheduler(int quantum, int maxThreads) {
        timeSlice = quantum;
        queueOne = new Vector<>();
        queueTwo = new Vector<>();
        queueThree = new Vector<>();
        initTid(maxThreads);
    }

    private void schedulerSleep() {
        try {
            Thread.sleep(timeSlice);
        } catch (InterruptedException e) {
        }
    }

    /**
     * @param milliseconds
     */
    private void schedulerSleep(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
        }
    }

    /**
     * A modified addThread of p161 example
     * 
     * @param t
     * @return TCB
     */

    public TCB addThread(Thread t) {
        // t.setPriority( 2 ); //removed for P2

        TCB parentTcb = getMyTcb(); // get my TCB and find my TID
        int pid = (parentTcb != null) ? parentTcb.getTid() : -1;
        int tid = getNewTid(); // get a new TID
        if (tid == -1)
            return null;
        TCB tcb = new TCB(t, tid, pid); // create a new TCB
        // thread safe operation

        queueOne.add(tcb);

        return tcb;
    }

    /**
     * A new feature added to p161 // Removing the TCB of a terminating thread
     * 
     * @return boolean
     */

    public boolean deleteThread() {
        TCB tcb = getMyTcb();
        if (tcb != null)
            return tcb.setTerminated();
        else
            return false;
    }

    /**
     * @param milliseconds
     */
    public void sleepThread(int milliseconds) {
        try {
            sleep(milliseconds);
        } catch (InterruptedException e) {
        }
    }

    /**
     * A modified run of p161 implemented for thread interface
     */

    public void run() {
        while (true) {
            try {
                runQueueOne();
                runQueueTwo();
                runQueueThree();
            } catch (NullPointerException e1) {
            } catch (NoSuchElementException e2) {
            }
        }
    }

    private void runQueueOne() {
        Thread current = null;
        TCB currentTCB;

        while (queueOne.size() > 0) {
            // get the next TCB and its thread
            synchronized (queueOne) {
                currentTCB = (TCB) queueOne.firstElement();
            }
            current = currentTCB.getThread();
            if (current != null) {
                current.start();
            } else {
                continue;

            }

            schedulerSleep(timeSlice / 2);
            // System.out.println("* * *Level 1 Context Switch * * * ");
            if (currentTCB.getTerminated()) {
                remove(currentTCB, 1);
                continue;
            }
            synchronized (queueOne) {
                if (current != null && current.isAlive()) {
                    current.suspend();
                    // current.setPriority(2); //replaced for P2
                }
                queueOne.remove(currentTCB); // rotate this TCB to second queue
                queueTwo.add(currentTCB);
            }
        }
    }

    private void runQueueTwo() {
        Thread current = null;
        TCB currentTCB;

        while (queueTwo.size() > 0) {
            // get the next TCB and its thread
            synchronized (queueTwo) {
                currentTCB = (TCB) queueTwo.firstElement();
            }
            if (currentTCB.getTerminated()) {
                remove(currentTCB, 2);
                continue;
            }
            current = currentTCB.getThread();
            if (current != null) {
                current.resume();
            } else {
                continue;
            }

            schedulerSleep(timeSlice / 2);

            if (queueOne.size() > 0) {
                current.suspend();
                // System.out.println("* * * Level 2 Context Switch * * * ");
                runQueueOne();
                current.resume();
            }
            if (currentTCB.getTerminated()) {
                remove(currentTCB, 2);
                continue;
            }
            schedulerSleep(timeSlice / 2);
            // System.out.println("* * * Level 2 Context Switch * * * ");
            if (currentTCB.getTerminated()) {
                remove(currentTCB, 2);
                continue;
            }
            synchronized (queueTwo) {
                if (current != null && current.isAlive()) {
                    current.suspend();
                    // current.setPriority(2); //replaced for P2
                }
                queueTwo.remove(currentTCB); // rotate this TCB to the third queue
                queueThree.add(currentTCB);
            }
        }

    }

    private void runQueueThree() {
        Thread current = null;
        TCB currentTCB;

        while (queueThree.size() > 0) {

            // get the next TCB and its thread
            synchronized (queueThree) {
                currentTCB = (TCB) queueThree.firstElement();
            }
            if (currentTCB.getTerminated()) {
                remove(currentTCB, 3);
                continue;
            }
            current = currentTCB.getThread();
            if (current != null) {
                current.resume();
            } else {
                continue;
            }
            // tell scheduler to sleep a total of 4 times to achieve total time of
            // 2*timeSlice
            for (int i = 0; i < 3; i++) {
                schedulerSleep(timeSlice / 2);
                if (queueOne.size() > 0 || queueTwo.size() > 0) {
                    current.suspend();
                    // System.out.println("* * * Level 3 Context Switch * * * ");
                    runQueueOne();
                    runQueueTwo();
                    current.resume();
                }
                if (currentTCB.getTerminated()) {
                    remove(currentTCB, 3);
                    continue;
                }
            }
            schedulerSleep(timeSlice / 2);
            if (currentTCB.getTerminated()) {
                remove(currentTCB, 3);
                continue;
            }
            // System.out.println("* * * Level 3 Context Switch * * * ");
            synchronized (queueThree) {
                if (current != null && current.isAlive()) {
                    current.suspend();
                }
                // this rotation keeps the heap of the queue from getting stuck in a single
                // process
                queueThree.remove(currentTCB); // rotate this TCB to the end
                if (queueThree.size() <= 1) {
                    queueThree.add(currentTCB);
                } else {
                    queueThree.add(1, currentTCB);
                }
            }
        }
    }

    private void remove(TCB tcb, int queueNum) {
        switch (queueNum) {
        case 1:
            synchronized (queueOne) {
                queueOne.remove(tcb);
                returnTid(tcb.getTid());
            }
            break;
        case 2:
            synchronized (queueTwo) {
                queueTwo.remove(tcb);
                returnTid(tcb.getTid());
            }
            break;
        case 3:
            synchronized (queueThree) {
                queueThree.remove(tcb);
                returnTid(tcb.getTid());
            }
            break;
        default:
            break;
        }
    }
}
