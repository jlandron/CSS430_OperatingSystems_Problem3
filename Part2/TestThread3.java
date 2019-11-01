public class TestThread3 extends Thread {
    private String testName;

    public TestThread3(String[] args) {
        testName = args[0];
    }

    public void run() {
        switch (testName) {
        case "computation":
            doCompute();
            break;
        case "disk":
            doDiskRead();
            SysLib.cout("Read Finished\n");
            doDiskWrite();
            SysLib.cout("Write Finished\n");
            break;
        default:
            break;
        }
        SysLib.cout(testName + " finished\n");
        SysLib.exit();
    }

    private void doCompute() {
        double ans = 0;
        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            for (int j = 0; j < Integer.MAX_VALUE; j++) {
                ans = Math.pow(Math.sqrt(i) * Math.sqrt(j), 2.0);
            }
            // String s = "iteration " + i + ": ans = " + ans + "\n";
            // SysLib.cout(s);
        }
    }

    private void doDiskRead() {
        byte[] buffer = new byte[512];
        for (int i = 0; i < 1000; i++) {
            SysLib.rawread(i, buffer);
        }
    }

    private void doDiskWrite() {
        byte[] buffer = new byte[512];
        for (int i = 0; i < 1000; i++) {
            SysLib.rawwrite(i, buffer);
        }
    }
}