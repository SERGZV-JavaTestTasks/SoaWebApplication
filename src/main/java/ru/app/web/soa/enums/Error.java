package ru.app.web.soa.enums;

import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

public enum Error
{
    USER_DONT_EXIST,
    USER_ALREADY_EXIST,
    INVALID_PASSWORD,
    INVALID_USERNAME_LENGTH,
    INVALID_PASSWORD_LENGTH,
    EXCEEDED_LOGIN_ATTEMPTS;

    public static String getError(Error error)
    {
        String errorText = "Ошибка ";

        Map<String, String> map = new HashMap<>();
        map.put(USER_DONT_EXIST.name(), errorText + HttpStatus.CONFLICT + " такого пользователя не существует");
        map.put(USER_ALREADY_EXIST.name(), errorText + HttpStatus.CONFLICT + " пользователь с таким именем уже существует");
        map.put(INVALID_PASSWORD.name(), errorText + HttpStatus.CONFLICT + " не верный пароль");
        map.put(INVALID_PASSWORD_LENGTH.name(), errorText + HttpStatus.BAD_REQUEST + " длинна имени пользователя должна быть между 4 и 40 символами");
        map.put(INVALID_PASSWORD_LENGTH.name(), errorText + HttpStatus.BAD_REQUEST + " длинна пароля должна быть не меньше 6 символов");
        map.put(EXCEEDED_LOGIN_ATTEMPTS.name(), errorText + HttpStatus.CONFLICT + " превышенно количество попыток входа, попробуйте позже");

        return map.get(error.name());
    }
}
