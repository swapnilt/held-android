package com.held.retrofit.response;

/**
 * Created by swapnil on 1/10/15.
 */
public class FriendData {

    User fromUser;
    User toUser;
    String date;
    String rid;

    public String getProfilePic(){
        return  toUser.getProfilePic();
    }

    public String getDisplayName(){
        return toUser.getDisplayName();
    }

    public String getJoinDate(){
        return toUser.getJoinDate();
    }

}
