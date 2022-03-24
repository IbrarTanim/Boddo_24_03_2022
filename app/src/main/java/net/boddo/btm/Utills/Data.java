package net.boddo.btm.Utills;

import android.app.ProgressDialog;

import net.boddo.btm.Model.ProfileImageLoader;
import net.boddo.btm.Model.User;

import java.util.List;

public class Data {

    //chatroom
    public static boolean isChatRoomAdmin = false;
    //end of chatroom

    public static String[] languages;
    public static ProgressDialog pd;

    public static int UnseenMessageCount = 0;
    public static int RequestMessageCount = 0;

    public static int PhotoBlogCount = 0;
    public static int TopPhotoCount = 0;

    public static int LikeCount = 0;
    public static int FevoriteCount = 0;
    public static int VisitorCount = 0;
    //own data
    public static String userId = "";
    public static String userName = "";
    public static String userFirstName = "";
    public static String userAge = "";
    public static String userDateOfBirgh = "";
    public static String userCountry = "";
    public static String userGender = "";
    public static String userMoto = "";
    public static boolean isUserEmailVerified;
    public static String userAccountCreated = "";
    public static String userEyeColor = "";

    public static String userEmail = "";
    public static String userHabits = "";
    public static String userHairColor = "";
    public static String userHeight = "";
    public static String userHomeTown = "";
    public static boolean isUserActive;
    public static String isUserSuspended;
    public static boolean isPalupPlusSubcriber;
    public static String userLookingFor = "";
    public static String userPalupPoint = "";
    public static String userPersonality = "";
    public static String userProfession = "";
    public static String userRelationShip = "";
    public static String doUserSmooke="";
    public static String userAboutMe = "";
    public static String userLocation = "";
    public static String userEducation = "";
    public static String userLanguage = "";
    public static String whoLikedMe = "";
    public static String country="";
    public static String likeCountValue="0";


    public static void saveLoggedInData(User user) {
        //don't change anything here
        Data.userName = (user.getUserName() == null) ? "" : user.getUserName();
        Data.userDateOfBirgh = (user.getDateOfBirth() == null) ? "" : user.getDateOfBirth();
        Data.userId = (user.getUserId() == null) ? "" : user.getUserId();
        Data.userAboutMe = (user.getAbout() == null) ? "" : user.getAbout();
        Data.userLanguage = (user.getLanguage() == null) ? "" : user.getLanguage();
        Data.userEducation = (user.getEducation() == null) ? "" : user.getEducation();
        Data.userLookingFor = (user.getLookingFor() == null) ? "" : user.getLookingFor();
        Data.userGender = (user.getGender() == null) ? "" : user.getGender();
        Data.userProfession = (user.getProfession() == null) ? "" : user.getProfession();
        Data.userRelationShip = (user.getRelationship() == null) ? "" : user.getRelationship();
        Data.userFirstName = (user.getFirstName() == null) ? "" : user.getFirstName();
        Data.isUserActive = (user.getIsActive() == null) ? false : Boolean.getBoolean(user.getIsActive());
        Data.doUserSmooke = (user.getSmoking() == null) ? "" : user.getSmoking();
        Data.userHabits = (user.getHabits() == null) ? "" : user.getHabits();
        Data.userEyeColor = (user.getEyeColor() == null) ? "" : user.getEyeColor();
        Data.userCountry = (user.getCurrentLocation() == null) ? "" : user.getCurrentLocation();
        Data.userHomeTown = (user.getHometown() == null) ? "" : user.getHometown();
        Data.isPalupPlusSubcriber = (user.getIsPulupPlusSubcriber() == null) ? false : Boolean.getBoolean(user.getIsPulupPlusSubcriber());
        Data.isUserSuspended = (user.getIsSuspended() == "") ? "0" : user.getIsSuspended();
        Data.userPalupPoint = (user.getPalupPoint() == null) ? "0" : user.getPalupPoint();
        Data.userHairColor = (user.getHairColor() == null) ? "" : user.getHairColor();
        Data.userHeight = (user.getHeight() == null) ? "" : user.getHeight();
        Data.userMoto = (user.getMoto() == null) ? "" : user.getMoto();
        Data.userLocation = (user.getCurrentLocation() == null) ? "" : user.getCurrentLocation();
        Data.userAccountCreated = (user.getAccountCreated() == null) ? "" : user.getAccountCreated();
        Data.isUserEmailVerified = (user.getIsEmailVerified() == null) ? false: Boolean.getBoolean(user.getIsEmailVerified());
        //
    }


    public static String[] getLanguages(){

        if (!Data.userLanguage.equals("")){
            languages = Data.userLanguage.split(",");
        }
        return languages;
    }
    // Other User Data

    public static List<ProfileImageLoader.ProfileImageInfo> othersImageList;

    public static String otherUserId = "";
    public static String otherUserName = "";
    public static String otherUserFirstName = "";
    public static String otherUserAge = "";
    public static String otherUserDateOfBirgh = "";
    public static String otherUserCountry = "";
    public static String otherUserGender = "";
    public static String otherUserMoto = "";
    public static boolean isOtherUserEmailVerified;
    public static String otherUserAccountCreated = "";
    public static String otherUserEyeColor = "";

    public static String otherUserEmail = "";
    public static String otherUserHabits = "";
    public static String otherUserHairColor = "";
    public static String otherUserHeight = "";
    public static String otherUserHomeTown = "";
    public static boolean isOtherUserActive;
    public static String isOtherUserSuspended;
    public static boolean isOtherUserPalupPlusSubcriber;
    public static String otherUserLookingFor = "";
    public static String otherUserPalupPoint = "";
    public static String otherUserPersonality = "";
    public static String otherUserProfession = "";
    public static String otherUserRelationShip = "";
    public static String doOtherUserSmooke="";
    public static String otherUserAboutMe = "";
    public static String otherUserLocation = "";
    public static String otherUserEducation = "";
    public static String otherUserLanguage = "";
    public static String otherProfilePhoto="";

    //liked and loved
    public static String isLoved = "";
    public static String isFavorite = "";
    public static String isMatched = "";


    public static void saveOthersUserData(User user){

        Data.otherUserName = (user.getUserName() == null) ? "" : user.getUserName();
        Data.otherUserDateOfBirgh = (user.getDateOfBirth() == null) ? "" : user.getDateOfBirth();
        Data.otherUserId = (user.getUserId() == null) ? "" : user.getUserId();
        Data.otherUserAboutMe = (user.getAbout() == null) ? "" : user.getAbout();
        Data.otherUserLanguage = (user.getLanguage() == null) ? "" : user.getLanguage();
        Data.otherUserEducation = (user.getEducation() == null) ? "" : user.getEducation();
        Data.otherUserLookingFor = (user.getLookingFor() == null) ? "" : user.getLookingFor();
        Data.otherUserGender = (user.getGender() == null) ? "" : user.getGender();
        Data.otherUserProfession = (user.getProfession() == null) ? "" : user.getProfession();
        Data.otherUserRelationShip = (user.getRelationship() == null) ? "" : user.getRelationship();
        Data.otherUserFirstName = (user.getFirstName() == null) ? "" : user.getFirstName();
        Data.isOtherUserActive = (user.getIsActive() == null) ? false : Boolean.getBoolean(user.getIsActive());
        Data.doOtherUserSmooke = (user.getSmoking() == null) ? "" : user.getSmoking();
        Data.otherUserHabits = (user.getHabits() == null) ? "" : user.getHabits();
        Data.otherUserEyeColor = (user.getEyeColor() == null) ? "" : user.getEyeColor();
        Data.otherUserCountry = (user.getCurrentLocation() == null) ? "" : user.getCurrentLocation();
        Data.otherUserHomeTown = (user.getHometown() == null) ? "" : user.getHometown();
        Data.isOtherUserPalupPlusSubcriber = (user.getIsPulupPlusSubcriber() == null) ? false : Boolean.getBoolean(user.getIsPulupPlusSubcriber());
        Data.isOtherUserSuspended = (user.getIsSuspended() == "") ? "0" : user.getIsSuspended();
        Data.otherUserPalupPoint = (user.getPalupPoint() == null) ? "" : user.getPalupPoint();
        Data.otherUserHairColor = (user.getHairColor() == null) ? "" : user.getHairColor();
        Data.otherUserHeight = (user.getHeight() == null) ? "" : user.getHeight();
        Data.otherUserMoto = (user.getMoto() == null) ? "" : user.getMoto();
        Data.otherUserLocation = (user.getCurrentLocation() == null) ? "" : user.getCurrentLocation();
        Data.otherProfilePhoto = (user.getProfilePhoto()==null)?"":user.getProfilePhoto();

    }



    //SUNNY SULTAN VERIABLES
    public static String profilePhoto = "";
    //conversion ID
    public static String conversationID="0";

    //adapter update
    public static boolean isUpdatePhotoBlogUi = false;
    public static int adapterPositionOfPhotoBlog;

    //notification on
    public static boolean isNotificationOn = true;

    public static List<String> chatRoomEntered = null;

    public static String chatRoomShowAdminAndUser = "";

    public static int STATUS_BAR_HEIGHT = 0;

}
