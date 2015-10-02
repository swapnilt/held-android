package com.held.adapters;


import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.held.activity.ChatActivity;
import com.held.activity.ParentActivity;
import com.held.activity.R;
import com.held.retrofit.response.Objects;
import com.held.retrofit.response.SearchUserResponse;
import com.held.retrofit.response.User;
import com.held.utils.AppConstants;
import com.squareup.picasso.Picasso;

import java.util.List;
import com.held.retrofit.response.FriendData;

public class FriendsAdapter extends RecyclerView.Adapter {

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;

    private ParentActivity mActivity;
    private List<FriendData> mFriendList;
    private boolean mIsLastPage;
    private String mOwnerDisplayName;
    private GestureDetector mPersonalGestureDetector;

    public FriendsAdapter(ParentActivity activity, List<FriendData> friendList, boolean isLastPage) {
        mActivity = activity;
        mFriendList = friendList;
        mIsLastPage = isLastPage;
        mPersonalGestureDetector = new GestureDetector(mActivity, new PersonalChatListener());
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View v = LayoutInflater.from(mActivity).inflate(R.layout.row_friends_list, parent, false);
            return new FriendViewHolder(v);
        } else {
            View v = LayoutInflater.from(mActivity).inflate(R.layout.layout_progress_bar, parent, false);
            return new ProgressViewHolder(v);
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof FriendViewHolder) {
            final FriendData friend  = mFriendList.get(position);
            FriendViewHolder viewHolder = (FriendViewHolder) holder;
            String picUrl = AppConstants.BASE_URL + friend.getProfilePic();
            Picasso.with(mActivity).load(picUrl).into(viewHolder.mProfilePic);

            viewHolder.mUserName.setText(friend.getDisplayName());

            //String date[] = friend.getJoinDate().split(" ");
            viewHolder.mTimeTxt.setText(friend.getJoinDate());

            viewHolder.mProfilePic.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    mOwnerDisplayName = friend.getDisplayName();
                    return mPersonalGestureDetector.onTouchEvent(motionEvent);
                }
            });
        } else {
            ProgressViewHolder viewHolder = (ProgressViewHolder) holder;
            if (mIsLastPage) {
                viewHolder.mIndicationTxt.setVisibility(View.VISIBLE);
                viewHolder.progressBar.setVisibility(View.GONE);
            } else {
                viewHolder.progressBar.setVisibility(View.VISIBLE);
                viewHolder.mIndicationTxt.setVisibility(View.GONE);
                viewHolder.progressBar.setIndeterminate(true);
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        return mFriendList.size() == position ? TYPE_FOOTER : TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        return mFriendList.size() + 1;
    }

    public static class FriendViewHolder extends RecyclerView.ViewHolder {

        private ImageView mProfilePic;
        private TextView mUserName, mUserDetail, mTimeTxt;

        public FriendViewHolder(View itemView) {
            super(itemView);
            mProfilePic = (ImageView) itemView.findViewById(R.id.FRIEND_profile_pic);
            mUserName = (TextView) itemView.findViewById(R.id.FRIEND_name);
            mUserDetail = (TextView) itemView.findViewById(R.id.FRIEND_description);
            mTimeTxt = (TextView) itemView.findViewById(R.id.FRIEND_time_txt);
        }
    }

    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        private ProgressBar progressBar;
        private TextView mIndicationTxt;

        public ProgressViewHolder(View v) {
            super(v);
            progressBar = (ProgressBar) v.findViewById(R.id.progressBar);
            mIndicationTxt = (TextView) v.findViewById(R.id.indication_txt);
        }
    }

    public void setFriendList(List<FriendData> friendList, boolean isLastPage) {
        mFriendList = friendList;
        mIsLastPage = isLastPage;
        notifyDataSetChanged();
    }

    private class PersonalChatListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onDown(MotionEvent e) {

            return true;
        }

        // event when double tap occurs
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            Bundle bundle = new Bundle();
            bundle.putString("owner_displayname", mOwnerDisplayName);
            mActivity.perform(AppConstants.LAUNCH_PERSONAL_CHAT_SCREEN, bundle);
            return true;
        }
    }
}
