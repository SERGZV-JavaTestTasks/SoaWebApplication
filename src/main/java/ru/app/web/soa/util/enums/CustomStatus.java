package ru.app.web.soa.util.enums;

public enum CustomStatus
{
    OK(200, "OK"),
    BAD_REQUEST(400, "Bad Request"),
    NOT_FOUND(404, "Not Found"),
    CONFLICT(409, "Conflict"),
    EXCEPTION(500, "Internal Server Error");

    private final int code;
    private final String message;

    CustomStatus(int code, String message)
    {
        this.code = code;
        this.message = message;
    }

    public int getCode() { return code; }
    public String getMessage() { return message; }
}
