package gcg.logback;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.UnsynchronizedAppenderBase;

public final class TelegramAppender extends UnsynchronizedAppenderBase<ILoggingEvent> {
    private TelegramOptions options = new TelegramOptions();
    private Level minimumEventLevel = Level.ERROR;

    @Override
    public void start() {
        super.start();
        Telegram.init(options);
    }

    @Override
    protected void append(ILoggingEvent eventObject) {
        if (eventObject.getLevel().isGreaterOrEqual(minimumEventLevel)) {
            Telegram.captureEvent(eventObject);
        }
    }

    public TelegramOptions getOptions() {
        return options;
    }

    public void setOptions(TelegramOptions options) {
        this.options = options;
    }

    public Level getMinimumEventLevel() {
        return minimumEventLevel;
    }

    public void setMinimumEventLevel(Level minimumEventLevel) {
        this.minimumEventLevel = minimumEventLevel;
    }
}
