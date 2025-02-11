import java.util.logging.Handler;
import java.util.logging.LogRecord;

/**
 *
 * @author saman
 */
public class DungeonClerkOutputHandler extends Handler {
    public void publish(LogRecord r) {
        String d=r.getMessage().replace("\n", "<br>");
        Clerk.write(Clerk.view(),d);

    }
    public void flush() {
        System.out.flush();
    }
    public void close() {
        // IDK
    }
}
