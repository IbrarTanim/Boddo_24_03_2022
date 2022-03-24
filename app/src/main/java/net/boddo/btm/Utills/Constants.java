package net.boddo.btm.Utills;

public interface Constants {
    //    for api calling keys
    String LOGGED_IN_FIRST_TIME_TODAY = "yes";
    String USER_NAME = "user_name";
    String FIRST_NAME = "first_name";
    String FULL_NAME = "full_name";
    String PASSWORD = "password";
    String EMAIL = "email";
    String DATE_OF_BIRTH = "date_of_birth";
    String GENDER = "gender";
    String AGE = "age";
    int LOADING_SIGNUP_DURATION = 1500;
    String isLetsGoClicked = "isSkippedOrLetsGoButtonClicked";
    //shared Preference
    String USER_ID = "user_id";
    String IS_LOGGED_IN = "is_logged_in";
    String ACCESS_TOKEN = "access_token";
    String FCM_TOKEN = "fcm_token";
    String IS_CHECKED = "remember_checkbox";


    //chat room sharedpreference

    String LAST_CHAT_ROOM = "last_chat_room";

    String GLOBAL = "Global";
    String BANGLADESH = "bangladesh";
    String LOVE = "Love";
    String FRIENDS = "Friends";
    String DATE = "Date";
    String PRIVATE_MESSAGE = "private";
    String MESSAGE_REQUEST = "message_request";
    String COUNTRY = "country";
    String TYPE = "type";
    String CHAT_TYPE_ROOM = "chatroom";
    String MATCHED = "matched";
    String UNMATCHED = "unmatch";
    String FEVORITE = "fevorite";
    String VISITOR = "visitor";
    String REQUEST_STRING = "$2y$12$5C05oYdYo0ZJkLZZVZNulu5GhjEnwDXOeJrh2yev4rWYgF04qSjeK";
    public static final String FAVORITE_VIEW_TYPE = "favorite";


    //website related
    String BASE_URL = "https://bluetigermobile.com/palup/apis/";
    int SECRET_KEY = 52525;

    //data transfer between activity key names
    String USER_DETAILS = "user_details";

    //full photo blog attributes
    String PHOTO_BLOG_PHOTO_URL = "photo_url";
    String PHOTO_BLOG_PHOTO_ID = "photo_id_of_photo_blog";
    String POHOT_BLOG_PHOTO_DESCRIPTION = "description_of_photo_blog_photo";
    String POHOTO_BLOG_PHOTO_LIKE_AMOUNT = "total_like_amount_on_photo_blog";
    String PHOTO_BLOG_PHOTO_COMMENT_AMOUNT = "total_comments_on_photo_blog";
    String PHOTO_BLOG_MATCHED_AMOUNT = "total_matched_showing_in_photo_blog";


    //broad cast and receiver for bottom sheet in full photo view activity
    String UPDATE_UI_FOR_CHAT = "UPDATE";

    String REQUEST_ACCEPT = "PrivateChatActivity";
    String REQUEST_FOR_PRIVATE_MESSAGE_IMAGE_UPLOAD_SUCCESS = "PrivateChatActivity";
    String CHAT_LIST = "active_chat_list";
    String IMAGE_REGEX = "https://bluetigermobile.com/palup/apis/images/";
    String CHAT_REQUEST_ACCEPTED = "chat_request_accepted";
    String CONVERSATION_ID = "conversation_id";
    String RECEIVER_NAME = "receiver_name";
    String FULL_SCREEN_IMAGE = "full_image_view";
    String CHAT_ROOM = "chat_room";
    String GLOBAL_MESSAGE = "ChatRoomActivity";
    String OTHER_USER_ID = "other_user_id";

    String KICK_OUT_FROM_CHAT_ROOM = "kick_out_from_chat_room";
    String BANNED = "banned";
    String ENTERED_INTO_CHAT_ROOM = "entered_into_chat_room";
    String LEFT_FROM_CHAT_ROOM = "left_from_chat_room";
    String UNLOCK_FAVORITE_IMAGE = "unlock_favorite_image";
    String UPDATE_PALUP_POINT = "update_palup_point";

    //Google AdsMob
    String ADMOB_APP_ID = "ca-app-pub-3940256099942544~3347511713";
    String INTERSTITIAL_AD = "ca-app-pub-3940256099942544/1033173712";


    String WARN = "Warn";
    String DISABLED = "disable";
    String BLOCKED = "blocked";
    String UNBLOCKED = "unblocked";
    String RESET_CID = "reset_cid";
    String PALUP_PLUS = "palup_plus";
    String NEW_MESSAGE_RECEIVED = "NEW_MESSAGE";
    String NEW_MESSAGE_REQUEST = "MESSAGE_request";

    String NEW_MESSAGE = "MESSAGE";
    String LIKE_FAV_VISITOR = "LIKE_FAV_VISITOR";
    String MESSAGE_COUNT = "MESSAGE_COUNT";
    String SUCCESS = "success";
    String GET_CHAT_LIST = "chatlist";
    String REQUEST_COUNT = "REQUEST_COUNT";
    String GET_REQUEST_LIST = "REQUEST_chatlist";
    String LIKE_FAV_SHOW = "LIKE_FAV_SHOW";
    String PHOTO_BLOG_NOTIFICATION = "PHOTO_BLOG_NOTIFICATION";
    String PHOTO_BLOG = "bpconfirm";
    String TOP_PHOTO_BLOG = "confirmtop";
    String SET_PHOTO_BLOGE_COUNT = "SET_PHOTO_BLOGE_COUNT";
    String MESSAGE_DOT_INCREASE = "dot";
    String PRIVERY_AND_POLICY = "http://bluetigermobile.com/palup/privacy-policy.html";

    //login activity

    String FORGOT_PASSWORD = "forgot_password";
    String CREATE_NEW_ACCOUNT = "create_new_account";


    String IS_PHOTO_VIEWED_BEFORE = "is_viewed";


    //photoblog

    String SCROLLED_ITEM_COUNT = "scrolled_items_count";

    String SCROLLED_COUNT = "scrolled_count";
    String SCROLLED_BROAD_CAST = "bd.com.palup.www.Fragment.action.scrolledup";;
    String USER_PROFILE_PHOTO = "user_profile_photo";
    String DEFAULT_PROFILE_IMAGE = "http://grandpamslp.bluetigermobile.com/public/images/default.jpg";
}
