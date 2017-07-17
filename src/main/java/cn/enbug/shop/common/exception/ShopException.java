package cn.enbug.shop.common.exception;

/**
 * 异常
 *
 * @author Hu Wenqiang
 * @version 1.0.0
 * @since 1.0.0
 */
@SuppressWarnings("serial")
public class ShopException extends RuntimeException {

    /**
     * @see java.lang.RuntimeException#RuntimeException()
     */
    public ShopException() {

    }

    /**
     * @param message message
     * @see java.lang.RuntimeException#RuntimeException()
     */
    public ShopException(String message) {
        super(message);
    }

    /**
     * @param message message
     * @param cause   cause
     * @see java.lang.RuntimeException#RuntimeException()
     */
    public ShopException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * @param cause cause
     * @see java.lang.RuntimeException#RuntimeException()
     */
    public ShopException(Throwable cause) {
        super(cause);
    }

    /**
     * @param message            message
     * @param cause              cause
     * @param enableSuppression  enable suppression
     * @param writableStackTrace writable stack trace
     * @see java.lang.RuntimeException#RuntimeException()
     */
    public ShopException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
