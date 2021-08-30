package sit.int222.cfan.exceptions;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ExceptionResponse {

    public static enum ERROR_CODE {
        FOODMENU_DOES_NOT_EXIST(1001),
        FOODMENU_FOODNAME_PUBLISH_ALREADY_EXIST(1002),
        FOODMENU_FOODNAME_PERSONAL_ALREADY_EXIST(1003),

        USER_DOES_NOT_EXIST(2001),
        USER_EMAIL_ALREADY_EXIST(2002),
        USER_USERNAME_ALREADY_EXIST(2003),
        USER_EMAIL_DOES_NOT_EXIST(2004),
        USER_PASSWORD_INCORRECT(2005),
        USER_UNAUTHORIZED(2006),
        USER_NO_PROFILE_IMAGE(2007),
        USER_INCORRECT_ID(2008),
        USER_EMAIL_INCORRECT(2009),
        USER_USERNAME_INCORRECT(2010),

        FILE_SUBMITTED_NOT_FOUND(3001),
        FILE_CAN_NOT_SAVE(3002);
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
