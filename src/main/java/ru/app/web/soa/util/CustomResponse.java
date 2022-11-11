package ru.app.web.soa.util;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import ru.app.web.soa.util.enums.CustomStatus;
import ru.app.web.soa.util.enums.Message;

import java.util.Collection;

@Getter
public class CustomResponse<T>
{
    private final int code;
    private final String message;
    private String messageClarification;
    private Collection<T> responseList;

    public CustomResponse(Collection<T> response, CustomStatus customStatus)
    {
        this.code = customStatus.getCode();
        this.message = customStatus.getMessage();
        this.responseList = response;
    }

    public CustomResponse(Collection<T> response, CustomStatus customStatus, Message message)
    {
        this.code = customStatus.getCode();
        this.message = customStatus.getMessage();
        this.responseList = response;
        this.messageClarification = message.getMessage();
    }

    public CustomResponse(Collection<T> response, CustomStatus customStatus, String messageClarification)
    {
        this.code = customStatus.getCode();
        this.message = customStatus.getMessage();
        this.responseList = response;
        this.messageClarification = messageClarification;
    }

    public void setMessageClarification(String messageClarification)
    {
        this.messageClarification = messageClarification;
    }

    public void setMessageClarification(Message message)
    {
        this.messageClarification = message.getMessage();
    }
}
