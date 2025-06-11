package foxiwhitee.hellmod.config;

public class ConfigExcaption extends RuntimeException {
    public ConfigExcaption() {}

    public ConfigExcaption(String message) {
        super(message);
    }

    public ConfigExcaption(String message, Throwable cause) {
        super(message, cause);
    }

    public ConfigExcaption(Throwable cause) {
        super(cause);
    }

    public ConfigExcaption(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
