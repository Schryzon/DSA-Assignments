package Objects;

/** KHLogger: Separate logging utility for Kingdom Hearts ADT demo. */
public class KHLogger {
    
    /**
     * Output a log message.
     * @param msg The text to print
     */
    public static void log(String msg) {
        System.out.println(msg);
    }

    /**
     * Output a log message with a decorative header.
     * @param title Header title
     */
    public static void section(String title) {
        System.out.println("\n--- " + title + " ---\n");
    }
}