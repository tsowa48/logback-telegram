package gcg.logback;

import ch.qos.logback.classic.spi.ILoggingEvent;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Telegram {
    private static final String TELEGRAM_BASE = "https://api.telegram.org/bot";
    private static TelegramOptions telegramOptions;

    private static ConcurrentLinkedQueue<ILoggingEvent> events = new ConcurrentLinkedQueue<>();
    private static SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss.SSS");

    public static synchronized void init(final TelegramOptions options) {
        telegramOptions = options;
        new Thread(Telegram::handler).start();
    }

    private static void handler() {
        while (true) {
            if (!events.isEmpty()) {
                final ILoggingEvent event = events.poll();
                String text = telegramOptions.getApp() + ": " + format.format(new Date(event.getTimeStamp())) + " [" + event.getLevel().levelStr + "] " + event.getFormattedMessage();
                try {
                    URL url = new URL(TELEGRAM_BASE + telegramOptions.getKey() + "/sendMessage");
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("POST");
                    con.setDoOutput(true);
                    con.setDoInput(true);
                    Map<String, String> arguments = new HashMap<>();
                    arguments.put("chat_id", telegramOptions.getChat());
                    arguments.put("text", text);
                    StringJoiner sj = new StringJoiner("&");
                    for (Map.Entry<String, String> entry : arguments.entrySet()) {
                        sj.add(URLEncoder.encode(entry.getKey(), "UTF-8") + "="
                                + URLEncoder.encode(entry.getValue(), "UTF-8"));
                    }
                    byte[] out = sj.toString().getBytes(StandardCharsets.UTF_8);
                    con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
                    con.connect();
                    OutputStream os = con.getOutputStream();
                    os.write(out);
                    os.flush();
                    InputStream is = con.getInputStream();
                    int len = is.available();
                    byte[] buf = new byte[len];
                    is.read(buf);
                    if (!new String(buf).contains("\"ok\":true")) {
                        throw new Exception("error send message");
                    }
                    con.disconnect();
                } catch (Exception ex) {

                }
            }
        }
    }

    public static Long captureEvent(final ILoggingEvent event) {
        events.add(event);
        return event.getTimeStamp();
    }
}
