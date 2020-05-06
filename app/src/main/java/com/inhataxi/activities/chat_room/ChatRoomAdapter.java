package com.inhataxi.activities.chat_room;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.inhataxi.R;
import com.inhataxi.RetrofitInterface;
import com.inhataxi.activities.ChatActivity;
import com.inhataxi.model.ChatRoomItem;
import com.inhataxi.response.ChattingRoomResponse;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.inhataxi.IngaTaxiApp.MEDIA_TYPE_JSON;
import static com.inhataxi.IngaTaxiApp.getRetrofitMethod;
import static com.inhataxi.activities.LoginActivity.userName;

public class ChatRoomAdapter extends RecyclerView.Adapter<ChatRoomAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<ChatRoomItem> mChatList;

    // 아이템 뷰를 저장하는 뷰홀더 클래스.
    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivChatRoomProfile;
        TextView tvChatRoomMaker;
        TextView tvChatRoomNum;
        TextView tvChatRoomDistance;
        TextView tvChatRoomDeparture;
        TextView tvChatRoomDestination;
        TextView tvChatRoomJoin;

        ViewHolder(View itemView) {
            super(itemView);

            ivChatRoomProfile = itemView.findViewById(R.id.iv_chat_room_profile);
            tvChatRoomMaker = itemView.findViewById(R.id.tv_chat_room_name);
            tvChatRoomNum = itemView.findViewById(R.id.tv_chat_room_member_num);
            tvChatRoomDistance = itemView.findViewById(R.id.tv_chat_room_distance);
            tvChatRoomDeparture = itemView.findViewById(R.id.tv_chat_room_departure);
            tvChatRoomDestination = itemView.findViewById(R.id.tv_chat_room_destination);
            tvChatRoomJoin = itemView.findViewById(R.id.tv_chat_roomt_join);

        }
    }

    // 생성자에서 데이터 리스트 객체를 전달받음.
    public ChatRoomAdapter(Context mContext, ArrayList<ChatRoomItem> mChatList) {
        this.mContext = mContext;
        this.mChatList = mChatList;
    }

    @NonNull
    @Override
    public ChatRoomAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.item_chat_room_list, parent, false);
        ChatRoomAdapter.ViewHolder vh = new ChatRoomAdapter.ViewHolder(view);

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final ChatRoomItem chatRoomItem = mChatList.get(position);
        holder.tvChatRoomMaker.setText(chatRoomItem.getName());
        holder.tvChatRoomNum.setText(String.valueOf(chatRoomItem.getCount()));
        int distance = (int) (chatRoomItem.getDistance() * 1000);
        holder.tvChatRoomDistance.setText(String.valueOf(distance).concat(" m"));
        holder.tvChatRoomDeparture.setText(chatRoomItem.getDeparture());
        holder.tvChatRoomDestination.setText(chatRoomItem.getDestination());

        Glide.with(mContext).load(chatRoomItem.getImageUrl()).placeholder(R.drawable.ic_profile_basic).into(holder.ivChatRoomProfile);

        holder.ivChatRoomProfile.setBackground(new ShapeDrawable(new OvalShape()));
        holder.ivChatRoomProfile.setClipToOutline(true);

        holder.tvChatRoomJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(mContext, ChatActivity.class);
                intent.putExtra("chatName", chatRoomItem.getUrl());
                intent.putExtra("userName", userName);
                mContext.startActivity(intent);
                //채팅방 참가
            }
        });

    }

    // getItemCount() - 전체 데이터 갯수 리턴.
    @Override
    public int getItemCount() {
        return mChatList.size();
    }

}
