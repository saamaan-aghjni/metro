import java.util.logging.Handler;
import java.util.logging.LogRecord;

/**
 *
 * @author saman
 */
public class DungeonConsoleOutputHandler extends Handler {
    public void publish(LogRecord r) {
        System.out.println(r.getMessage());
    }
    public void flush() {
        System.out.flush();
    }
    public void close() {
        // IDK
    }
}
