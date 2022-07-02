package ru.app.web.soa.enums;

import java.util.HashMap;
import java.util.Map;

public enum PetType
{
    CAT,
    DOG,
    PENGUIN,
    GIRAFFE;

    // This method is needed in case someone wants to swap the enum in places,
    // the same number for this enum remains in the database
    public static int getDatabaseId(PetType category)
    {
        Map<String, Integer> map = new HashMap<>();
        map.put(CAT.name(), 0);
        map.put(DOG.name(), 1);
        map.put(PENGUIN.name(), 2);
        map.put(GIRAFFE.name(), 3);

        return map.get(category.name());
    }
}
