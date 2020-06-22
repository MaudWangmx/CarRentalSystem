package DataBase.Exception;

public class DeleteException extends Exception{
    public enum ErrorCode {
        noError,
        itemNotExist,
        carLeased,
        itemInformationChanged,
        sqlException,
        retryTimeExceeded,
    }

    public ErrorCode error;
    private String reason;

    public DeleteException(String reason, ErrorCode err) {
        super(reason);
        this.reason = reason;
        error = err;
    }

    public String getReason() {
        return reason;
    }
}
