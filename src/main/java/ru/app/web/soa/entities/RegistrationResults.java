package ru.app.web.soa.entities;

import java.util.ArrayList;
import java.util.List;

public class RegistrationResults
{
    private boolean result;
    private List<String> errors;

    public RegistrationResults()
    {
        result = false;
        errors = new ArrayList<>();
    }
}
