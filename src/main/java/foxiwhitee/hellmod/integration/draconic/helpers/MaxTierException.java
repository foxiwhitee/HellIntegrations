package foxiwhitee.hellmod.integration.draconic.helpers;

public class MaxTierException extends RuntimeException {
    public MaxTierException() {}

    public MaxTierException(String message) {
        super(message);
    }

    public MaxTierException(String message, Throwable cause) {
        super(message, cause);
    }

    public MaxTierException(Throwable cause) {
        super(cause);
    }

    public MaxTierException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
