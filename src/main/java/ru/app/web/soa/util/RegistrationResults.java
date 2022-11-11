package ru.app.web.soa.util;

import java.util.ArrayList;
import java.util.List;

public class RegistrationResults
{
    private boolean successful;
    private List<String> errors;

    public RegistrationResults()
    {
        successful = false;
        errors = new ArrayList<>();
    }

    public boolean getSuccessful() { return successful; }
    public void setSuccessful(boolean successful) { this.successful = successful; }
    public void addError(String error) { errors.add(error); }
    public List<String> getErrors() { return errors; }
    public void addErrors(List<String> errors) { this.errors.addAll(errors); }
}
