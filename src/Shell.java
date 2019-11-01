import java.util.Arrays;

/**
 * @author Joshua Landron
 * @version 9/29/2019
 * @see ThreadOS, CSS430 Fall 2019 program 0 Shell assignment
 * 
 *      regex information found at
 *      https://stackoverflow.com/questions/4416425/how-to-split-string-with-some-separator-but-without-removing-that-separator-in-j
 */

public class Shell extends Thread {
    /**
     * Default Constructor override
     * 
     * prints standard startup prompts
     */
    public Shell() {
        SysLib.cout("Shell Started.\n");
        SysLib.cout("Type 'exit' to close the shell.\n\n");
    }

    /**
     * run method inherited form java thread class.
     * 
     * This is the main method called by ThreadOS when a new shell is created.
     * 
     */
    public void run() {
        int shellLineNumber = 0;
        while (true) {
            shellLineNumber++;
            SysLib.cout("Shell[" + shellLineNumber + "]% ");
            // create a new buffer and convert standard input to a string
            StringBuffer localBuffer = new StringBuffer();
            SysLib.cin(localBuffer);
            String lineOfCommands = new String(localBuffer);

            // check if empty line was passed
            if (lineOfCommands.length() == 0) {
                continue;
            }

            // split the lineOfCommands into substrings while keeping the separator
            // using positive lookahead Regex
            String[] commands = lineOfCommands.split("((?<=;)|(?<=&))");
            // check if user entered exit (only checks first commands for exit)
            // todo: implement more robust exit conditions
            if (commands[0].toLowerCase().equals("exit")) {
                SysLib.cout(" Exiting \n");
                break;
            }

            for (String command : commands) {

                if (command.length() == 0) {
                    continue;
                }
                String[] args = SysLib.stringToArgs(command);
                // check delimiter for ';' or '&'
                if (args[args.length - 1].equals(";")) {
                    args = Arrays.copyOf(args, args.length - 1);
                    sequentialExecution(args);
                } else if (args[args.length - 1].equals("&")) {
                    args = Arrays.copyOf(args, args.length - 1);
                    concurrentExecution(args);
                } else {
                    sequentialExecution(args);
                }
            }
        }
        SysLib.exit();
    }

    /**
     * commands passed to this method will run right away
     * 
     * @param args
     */
    private void concurrentExecution(String[] args) {
        int execId = SysLib.exec(args);
        if (execId < 0) {
            SysLib.cerr("Invalid Command\n");
        }
    }

    /**
     * commands passed to this method will wait on the ThreadOS to have a running
     * thread continue before executing.
     * 
     * @param args
     */
    private void sequentialExecution(String[] args) {
        int execId = SysLib.exec(args);
        if (execId < 0) {
            SysLib.cerr("Invalid Command\n");
            return;
        }
        // get initial join thread number and put this execution on hold
        int joinId = SysLib.join();
        if (joinId < 0) {
            SysLib.cerr("Unable to join\n");
            return;
        }
        // check through each join to make sure the thread joins at the right time
        while (joinId != execId) {
            joinId = SysLib.join();
            if (joinId < 0) {
                SysLib.cerr("Unable to join\n");
            }
        }
    }

    /**
     * This method will not be used in ThreadOS
     * 
     * @param args
     */
    // public static void main(String[] args) {
    // // TODO Auto-generated method stub
    // Shell shell = new Shell();
    // shell.run();
    // }
}