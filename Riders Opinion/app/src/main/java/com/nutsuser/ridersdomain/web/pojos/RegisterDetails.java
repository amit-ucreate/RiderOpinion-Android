package com.nutsuser.ridersdomain.web.pojos;

/**
 * Created by admin on 19-01-2016.
 */
public class RegisterDetails {

    String userId;
    String accessToken;
    String isNewUser;
    String userName;
    String user_image;
    String profile_image;;
    String nickName;
    public String getUserProfileImage() {
        return user_image;
    }

    public void setUserProfileImage(String profile_image) {
        this.user_image = profile_image;
    }


    public String getUserBannerImage() {
        return profile_image;
    }

    public void setUserBannerImage(String user_image) {
        this.profile_image = user_image;
    }

    public String getIsNewUser() {
        return isNewUser;
    }

    public void setIsNewUser(String isNewUser) {
        this.isNewUser = isNewUser;
    }

    public String getUserId() {
        return userId;
    }
    public String getUserName() {
        return nickName;
    }

    public void setUserName(String User) {
        this.nickName = User;
    }

//    public String getUserName() {
//        return userName;
//    }
//
//    public void setUserName(String User) {
//        this.userName = User;
//    }


    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

}
