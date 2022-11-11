package ru.app.web.soa.util.enums;

import org.springframework.http.HttpStatus;

import java.util.EnumMap;
import java.util.Map;

public enum Message
{
    USER_DONT_EXIST,
    USER_ALREADY_EXIST,
    INVALID_USERNAME_LENGTH,
    INVALID_PASSWORD_LENGTH,
    INVALID_PASSWORD,
    EXCEEDED_LOGIN_ATTEMPTS,
    SUCCESSFUL_LOGIN,
    NO_SUCH_PET,
    PET_ADDED,
    PET_EDITED,
    PET_DELETED;

    public String getMessage()
    {
        Map<Message, String> map = new EnumMap<>(Message.class);
        map.put(USER_DONT_EXIST, "Such a user does not exist");
        map.put(USER_ALREADY_EXIST, "Such a user already exists");
        map.put(INVALID_USERNAME_LENGTH, "The length of the username must be between 4 and 40 characters");
        map.put(INVALID_PASSWORD_LENGTH, "The password must be at least 6 characters long");
        map.put(INVALID_PASSWORD, "Invalid password");
        map.put(EXCEEDED_LOGIN_ATTEMPTS, "Exceeded the number of login attempts, try again later");
        map.put(SUCCESSFUL_LOGIN, "You have successfully logged in");
        map.put(NO_SUCH_PET, "No such pet was found");
        map.put(PET_ADDED, "The pet has been added successfully");
        map.put(PET_EDITED, "The pet has been successfully edited");
        map.put(PET_DELETED,  "The pet has been successfully deleted");

        return map.get(this);
    }
}
