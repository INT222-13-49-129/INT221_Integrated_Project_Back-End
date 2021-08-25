package sit.int222.cfan.exceptions;

public class BaseException extends RuntimeException{

    private static final long serialVersionUID = 39901776523317234L;
    ExceptionResponse.ERROR_CODE errorCode;
    public BaseException(ExceptionResponse.ERROR_CODE errorCode, String s) {
        super(s);
        this.errorCode = errorCode;
    }
    public ExceptionResponse.ERROR_CODE getErrorCode() {
        return this.errorCode;
    }
}
