package com.digidot.ishansupportsystem.util;


/**
 * Created by riddhi.parkhiya on 1/24/2018.
 */

public class Constant {
    // Splash screen timer
    public static int SPLASH_TIME_OUT = 3000;
    public static final String PREF_KEY_LOGIN = "login";

    public static final String TICKET_STATUS_OPEN = "Open";
    public static final String TICKET_STATUS_CLOSE = "Closed";
    public static final String TICKET_STATUS_DEPENDENCY = "Dependency";

    public static final String PREF_KEY_USER_ID = "userId";
    public static final String PREF_KEY_USERNAME = "username";

    public static final String INTENT_PARAM_TICKET_ID = "ticketId";
    public static final String INTENT_PARAM_TICKET_NO = "ticketNo";
    public static final String INTENT_PARAM_TICKET_DATE = "ticketDate";
    public static final String INTENT_PARAM_TICKET_FAULT = "ticketFault";
    public static final String INTENT_PARAM_TICKET_DESCRIPTION = "ticketDescription";
    public static final String INTENT_PARAM_TICKET_STATUS = "ticketStatus";
    public static final String INTENT_PARAM_TICKET_DEPENDENCY_CODE = "dependencyCode";
    public static final String INTENT_PARAM_TICKET_RESOLUTION_CODE = "resolutionCode";
    public static final String INTENT_PARAM_TICKET_BROAD_CATEGORY = "broadCategory";

    public static String BASE_URL = "http://webapi.digidott.com/DigidottWebService.asmx/";
    public static final String PROJECT_NAME = "ISHAN_SUPPORT";

    public static CharSequence FRAGMNET_HOME = "Home";
    public static CharSequence FRAGMNET_NOTIFICATIONS = "Notifications";
    public static CharSequence FRAGMNET_TICKET = "Ticket";
    public static CharSequence FRAGMENT_REPORTS = "Reports";
    public static CharSequence FRAGMNET_SETTINGS = "Settings";

    public static final String PREF_KEY_WEBSITE_LINK = "websitelink";
    public static final String PREF_KEY_API_LINK = "apilink";

    public static final String TICKET_STATUS = "ticket_status";
}
