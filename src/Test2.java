import java.util.Date;

class Test2 extends Thread {

	public void run() {
		String[] args1 = SysLib.stringToArgs("TestThread2 a 500 0");
		String[] args2 = SysLib.stringToArgs("TestThread2 b 600 0");
		String[] args3 = SysLib.stringToArgs("TestThread2 c 700 0");
		String[] args4 = SysLib.stringToArgs("TestThread2 d 800 0");
		String[] args5 = SysLib.stringToArgs("TestThread2 e 1000 0");
		String[] args6 = SysLib.stringToArgs("TestThread2 f 2000 0");
		String[] args7 = SysLib.stringToArgs("TestThread2 g 2500 0");
		String[] args8 = SysLib.stringToArgs("TestThread2 h 3000 0");
		String[] args9 = SysLib.stringToArgs("TestThread2 i 3500 0");
		String[] args10 = SysLib.stringToArgs("TestThread2 j 4000  0");
		SysLib.exec(args1);
		SysLib.exec(args2);
		SysLib.exec(args3);
		SysLib.exec(args4);
		SysLib.exec(args5);
		SysLib.exec(args6);
		SysLib.exec(args7);
		SysLib.exec(args8);
		SysLib.exec(args9);
		SysLib.exec(args10);
		for (int i = 0; i < 5; i++)
			SysLib.join();
		// SysLib.cout( "Test2 finished\n" );
		SysLib.exit();
	}
}
