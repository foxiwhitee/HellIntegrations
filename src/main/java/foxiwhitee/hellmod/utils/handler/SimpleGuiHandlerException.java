package foxiwhitee.hellmod.utils.handler;

public class SimpleGuiHandlerException extends RuntimeException {
    public SimpleGuiHandlerException() {}

    public SimpleGuiHandlerException(String message) {
        super(message);
    }

    public SimpleGuiHandlerException(String message, Throwable cause) {
        super(message, cause);
    }

    public SimpleGuiHandlerException(Throwable cause) {
        super(cause);
    }

    public SimpleGuiHandlerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
