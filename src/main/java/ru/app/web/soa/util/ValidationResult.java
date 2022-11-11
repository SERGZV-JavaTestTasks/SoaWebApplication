package ru.app.web.soa.util;

public class ValidationResult
{
    boolean passed;
    String error;

    public boolean getPassed() { return passed; }
    public void setPassed(boolean passed) { this.passed = passed; }
    public String getError() { return error; }
    public void setError(String error) { this.error = error; }
}
