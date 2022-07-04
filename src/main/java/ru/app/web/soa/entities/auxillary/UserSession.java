package ru.app.web.soa.entities.auxillary;

import javax.servlet.http.HttpSession;
import java.util.Calendar;
import java.util.Date;

public class UserSession
{
    private static final byte ALLOWED_LOGIN_ATTEMPTS = 10;
    private static final String LOGIN_FIRST_FAILURE_TIME = "login_first_failure_time";
    private static final String LOGIN_FAILURE_COUNT_TEXT = "login_failure_count";

    public static boolean numLoginAttemptsExceeded(HttpSession session)
    {
        long delayResettingLoginAttempts = 3600000;
        Integer failureCount = (Integer)session.getAttribute(LOGIN_FAILURE_COUNT_TEXT);

        if(failureCount == null) return false;

        Long firstFailureTimeInMillis = (Long)session.getAttribute(LOGIN_FIRST_FAILURE_TIME);
        Calendar firstFailCalDate = Calendar.getInstance();
        firstFailCalDate.setTimeInMillis(firstFailureTimeInMillis);
        Date firstFailureDate = firstFailCalDate.getTime();
        Date nowDate = Calendar.getInstance().getTime();

        var difference = nowDate.getTime() - firstFailureDate.getTime();

        if(difference > delayResettingLoginAttempts)
        {
            resetFailedLoginAttempts(session);
            return false;
        }

        return failureCount >= ALLOWED_LOGIN_ATTEMPTS;
    }

    public static void addFailedLoginAttempt(HttpSession session)
    {
        Integer failureCount = (Integer)session.getAttribute(LOGIN_FAILURE_COUNT_TEXT);
        if(failureCount == null || failureCount == 0)
        {
            Long nowTimeInMillis = Calendar.getInstance().getTimeInMillis();

            session.setAttribute(LOGIN_FAILURE_COUNT_TEXT, 1);
            session.setAttribute(LOGIN_FIRST_FAILURE_TIME, nowTimeInMillis);
        }
        else
        {
            session.setAttribute(LOGIN_FAILURE_COUNT_TEXT, failureCount + 1);
        }
    }

    public static void resetFailedLoginAttempts(HttpSession session)
    {
        Integer failureCount = (Integer)session.getAttribute(LOGIN_FAILURE_COUNT_TEXT);
        if(failureCount == null) return;

        session.setAttribute(LOGIN_FAILURE_COUNT_TEXT, 0);
        session.removeAttribute(LOGIN_FIRST_FAILURE_TIME);
    }
}
