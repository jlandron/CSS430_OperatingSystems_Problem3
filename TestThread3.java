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
            doDisk();
            break;
        default:
            break;
        }
    }

    private void doCompute() {
        double ans = 0;
        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            ans = Math.pow(Math.sqrt(i) * Math.sqrt(i), 2.0);
        }
    }

    private void doDisk() {
        byte[] buffer = new byte[512];
        for (int i = 0; i < 1000; i++) {
            SysLib.rawread(i, buffer);
            SysLib.rawwrite(i, buffer);
        }
    }
}