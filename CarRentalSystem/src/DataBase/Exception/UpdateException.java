package DataBase.Exception;

public class UpdateException extends Exception {
    public enum ErrorCode {
        noError,
        carLeased,
        itemNotExit,
        itemInformationChanged,
        sqlException,
        retryTimeExceeded,
    }

    public ErrorCode error;
    private String reason;

    public UpdateException(String reason, ErrorCode err) {
        super(reason);
        this.reason = reason;
        error = err;
    }

    public String getReason() {
        return reason;
    }
}
