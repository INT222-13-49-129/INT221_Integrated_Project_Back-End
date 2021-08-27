package sit.int222.cfan.exceptions;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ExceptionResponse {

    public static enum ERROR_CODE {
        FOODMENU_DOES_NOT_EXIST(1001),

        USER_DOES_NOT_EXIST(2001),
        USER_EMAIL_ALREADY_EXIST(2002),
        USER_USERNAME_ALREADY_EXIST(2003),
        USER_EMAIL_DOES_NOT_EXIST(2004),
        USER_PASSWORD_INCORRECT(2005),
        USER_UNAUTHORIZED(2006);
        private int value;
        ERROR_CODE(int value) {
            this.value = value;
        }
        public int getValue() {
            return value;
        }
    }

    private ERROR_CODE errorCode;
    private int status;
    private String message;
    private LocalDateTime dateTime;

    public ExceptionResponse(ERROR_CODE errorCode, String message, LocalDateTime dateTime) {
        this.errorCode = errorCode;
        this.status = errorCode.value;
        this.message = message;
        this.dateTime = dateTime;
    }
}
