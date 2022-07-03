package ru.app.web.soa.enums;

import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

public enum Error
{
    USERDONTEXIST,
    USERALREADYEXIST,
    INVALIDPASSWORD,
    INVALIDUSERNAMELENGTH,
    INVALIDPASSWORDLENGTH;

    public static String getError(Error error)
    {
        String errorText = "Ошибка ";

        Map<String, String> map = new HashMap<>();
        map.put(USERDONTEXIST.name(), errorText + HttpStatus.CONFLICT + " такого пользователя не существует");
        map.put(USERALREADYEXIST.name(), errorText + HttpStatus.CONFLICT + " пользователь с таким именем уже существует");
        map.put(INVALIDPASSWORD.name(), errorText + HttpStatus.CONFLICT + " не верный пароль");
        map.put(INVALIDPASSWORDLENGTH.name(), errorText + HttpStatus.BAD_REQUEST + " Длинна имени пользователя должна быть между 4 и 40 символами");
        map.put(INVALIDPASSWORDLENGTH.name(), errorText + HttpStatus.BAD_REQUEST + " Длинна пароля должна быть не меньше 6 символов");

        return map.get(error.name());
    }
}
