package com.inhataxi.activities.chat_room;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.inhataxi.LoadingDialog;
import com.inhataxi.R;
import com.inhataxi.RetrofitInterface;
import com.inhataxi.activities.BaseActivity;
import com.inhataxi.model.ChatRoom;
import com.inhataxi.model.ChatRoomItem;
import com.inhataxi.response.ChattingRoomResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.inhataxi.IngaTaxiApp.*;

public class ChatRoomActivity extends BaseActivity {

    ArrayList<ChatRoomItem> mArrayChat;
    RecyclerView mRvChatRoom;
    ChatRoomAdapter mChatRoomAdapter;
    Context mContext;
    Intent mIntent;
    LoadingDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = getWindow().getDecorView();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (view != null) {
                view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
        }
        setContentView(R.layout.activity_chat_room);
        mContext = this;
        this.initialize();
        try {
            this.postChattingRoom();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    void initialize() {

        mDialog = new LoadingDialog(mContext);
        mArrayChat = new ArrayList<>();
        mChatRoomAdapter = new ChatRoomAdapter(mContext, mArrayChat);

        mRvChatRoom = findViewById(R.id.rv_chat_room);
        mRvChatRoom.setAdapter(mChatRoomAdapter);

        mIntent = getIntent();
    }

    public void onClick(View view) {
        switch (view.getId()) {

        }
    }

    void postChattingRoom() throws JSONException {
        final JSONObject params = new JSONObject();

        params.put("startLongitude", Double.valueOf(mIntent.getExtras().getString("startLatitude")));
        params.put("startLatitude", Double.valueOf(mIntent.getExtras().getString("startLongitude")));
        params.put("endLongitude", Double.valueOf(mIntent.getExtras().getString("endLatitude")));
        params.put("endLatitude", Double.valueOf(mIntent.getExtras().getString("endLongitude")));
/*
        Log.d("로그", "startLogitude: "+Double.valueOf(mIntent.getExtras().getString("startLongitude")));
        Log.d("로그", "startLatitude: "+Double.valueOf(mIntent.getExtras().getString("startLatitude")));
        Log.d("로그", "endLongitude: "+Double.valueOf(mIntent.getExtras().getString("endLongitude")));
        Log.d("로그", "endLatitude: "+Double.valueOf(mIntent.getExtras().getString("endLatitude")));
*/

        if (mIntent.getExtras().getInt("type") == 1) {
            params.put("type", 1);
        } else if (mIntent.getExtras().getInt("type") == 2) {
            params.put("type", 2);
        }
/*
        params.put("startLongitude", 37.4650456);
        params.put("startLatitude", 126.6785137);
        params.put("endLongitude", 37.4500263);
        params.put("endLatitude", 126.6512993);
        params.put("type", 1);
*/
        //로딩 다이얼로그
        mDialog.show();

        final RetrofitInterface retrofitInterface = getRetrofit(mContext).create(RetrofitInterface.class);
        retrofitInterface.postChatRoom(RequestBody.create(params.toString(), MEDIA_TYPE_JSON)).enqueue(new Callback<ChattingRoomResponse>() {
            @Override
            public void onResponse(@NonNull final Call<ChattingRoomResponse> call,
                                   @NonNull final Response<ChattingRoomResponse> response) {
//                hideProgressDialog();

                Log.d("로그", params.toString());
                ChattingRoomResponse chattingRoomResponse = response.body();
                if (chattingRoomResponse == null) {
                    showCustomToast("응답 없음");
                }

                if (chattingRoomResponse.isSuccess()) {
                    //목록 가져오기 성공
                    for (int i = 0; i < chattingRoomResponse.getResult().size(); i++) {
                        ChatRoom room = chattingRoomResponse.getResult().get(i);
                        ChatRoomItem temp = new ChatRoomItem(room.getName(), room.getDept(), room.getCount(), room.getTime(), room.getDistance(),
                                room.getDeparture(), room.getDestination(), room.getImageUrl(), room    .getUrl());

                        Log.d("로그",room.getUrl());
                        mArrayChat.add(temp);
                        //mArrayChat.add(temp);
                        //mArrayChat.add(temp);
                    }
                    mChatRoomAdapter.notifyDataSetChanged();

                } else {
                    showCustomToast("목록을 불러오지 못했습니다.");
                }
                //로딩 끝
                mDialog.dismiss();
            }

            @Override
            public void onFailure(@NonNull final Call<ChattingRoomResponse> call,
                                  @NonNull final Throwable throwable) {
                //mDialog.dismiss();
                showCustomToast("연결 실패");
            }
        });
    }

    public String curTime() {
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        String now_txt = format.format(date);
        int a = Integer.parseInt(now_txt);
        String adaybeforetoday = String.valueOf(a - 1);
        return adaybeforetoday;
    }

}
