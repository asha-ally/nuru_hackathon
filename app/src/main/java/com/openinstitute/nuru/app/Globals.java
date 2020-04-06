package com.openinstitute.nuru.app;

/**
 * Created by oi-dev-01 on 24/12/17.
 */

public interface Globals {

    String CONF_APP_NAME = "com.openinstitute.nuru";
    String msg_no_internet = "Internet not connected!";
    String msg_deal_saved = "Added to Saved Deals.";

    String CONF_POSTS_API_PUSH = "https://nuru.live/dashboard/api/ajbk_post.php";

    String CONF_FILE_UPLOAD = "https://nuru.live/dashboard/api/ajbk_upload.php";
    //public static final String CONF_FILE_UPLOAD = "http://10.0.2.2/oireporting_web/api/ajbk_upload.php";

    int MYINT_1 = 1;

    /*Rage Chips*/
    String[] CONF_POST_TAGS = new String[]{
            "Water Availability",
            "Water Cost",
            "Basic Food Items Availability",
            "Basic Food Items Cost",
            "Discrimination",
            "Disturbance",
            "Restriction of Movement",
            "Suspected COVID-19 Case"
    };

}
