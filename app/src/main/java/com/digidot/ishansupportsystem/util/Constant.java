package com.digidot.ishansupportsystem.util;


/**
 * Created by riddhi.parkhiya on 1/24/2018.
 */

public class Constant {
    // Splash screen timer
    public static int SPLASH_TIME_OUT = 3000;
    // for google
    public static final int RC_SIGN_IN = 9001;
    public static String startDatetime="";
    public static String endDateTime="";
    public static String catId="";

    public static final String PREF_KEY_LOGIN="login";
    public static final boolean LOGIN=false;

    public static final String PREF_KEY_USER_ID="userId";
    public static final String PREF_KEY_USERNAME="username";

    public static final String INTENT_PARAM_EVENT_ID="eventId";
    public static final String INTENT_PARAM_BUTTON_ID="buttonId";
    public static final String INTENT_PARAM_ALBUM_ID="albumID";
    public static final String INTENT_PARAM_ALBUM_NAME="albumName";
    public static final String INTENT_PARAM_EVENT_DATA="strEventData";
    public static final String PREF_KEY_NOTIFICATION_ACTIVATION_KEY="notificationActivationFlag";
    /*drawer name*/
    public static CharSequence FRAGMNET_HOME="Home";
    public static CharSequence FRAGMNET_NOTIFICATIONS="Notifications";
    public static CharSequence FRAGMNET_COMPANY_DETAILS ="Company Details";
    public static CharSequence FRAGMNET_PHOTO_GALLERY="Gallery";
    public static CharSequence FRAGMNET_CONTEST="Contest";
    public static CharSequence FRAGMNET_EVENT_DETAILS="Notification Details";
    public static CharSequence FRAGMNET_CONTECT_US="Contact Us";
    public static CharSequence FRAGMNET_SETTINGS="Settings";


    public static String PREF_KEY_MENU_LIST="menuList";
    public static String BASE_URL = "http://webapi.digidott.com/DigidottWebService.asmx/";
    public static String INTENT_PARAM_URL="url";
    public static final String PROJECT_NAME="ISHAN_SUPPORT";
    public static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    public static final String PHONE_PATTERN="\\d{10}";
    public static final String AGE_PATTERN="\\d{2}";

    public static final String PREF_KEY_REG_ID="regId";
    public static final String PREF_KEY_NAME="name";
    public static final String PREF_KEY_AGE="age";
    public static final String PREF_KEY_PHONE="phone";
    public static final String PREF_KEY_EMAIL="email";
    public static final String PREF_KEY_GENDER="gender";
    public static final String PREF_KEY_OTP="OTP";
    public static final String PREF_KEY_LOGIN_TYPE="loginType";
    public static final String PREF_KEY_WEBSITE_LINK="websitelink";
    public static final String PREF_KEY_API_LINK="apilink";

    /*Login type*/
    public static final String loginTypeFacebook="FaceBook";
    public static final String loginTypeGoogle="Google";
    public static final String loginTypeMobile="Mobile";

    public static final String INTENT_PARAM_NOTIFICATION_ID="notificationId";
    public static final String INTENT_PARAM_NOTIFICATION_MSG="notificationMsg";
    public static final String INTENT_PARAM_NOTIFICATION_DATETIME="notificationDateTime";
    public static final String INTENT_PARAM_NOTIFICATION_CAT_ID="notificationCatId";

    public static final String MAP_LOACTION_LINK="https://goo.gl/maps/Wg7w9be2XKs";
    public static final String INDOOR_MAP_LINK="Images/Ground-Map.jpg";
    public static final String VOLLEY_SCHEDULE_LINK="Images/VolleyBall.pdf";
    public static final String CRICKET_LINK="Images/Cricket.pdf";

    public static final String INTENT_PARAM_QUE="quizQue";
    public static final String INTENT_PARAM_QUE_ID="quizQueId";
    public static final String INTENT_PARAM_OPT_A="optA";
    public static final String INTENT_PARAM_OPT_B="optB";
    public static final String INTENT_PARAM_OPT_C="optC";
    public static final String INTENT_PARAM_OPT_D="optD";

    public static String CURRENT_LOADED_FRAGMENT="Home";


}
