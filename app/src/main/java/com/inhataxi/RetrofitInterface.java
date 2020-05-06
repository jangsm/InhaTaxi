package com.inhataxi;

import com.inhataxi.response.ChattingRoomResponse;
import com.inhataxi.response.LoginResponse;
import com.inhataxi.response.MakeRoomResponse;
import com.inhataxi.response.SignUpResponse;
import com.inhataxi.response.SuperResponse;
import com.inhataxi.response.TestDetailResponse;
import com.inhataxi.response.TestListResponse;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Retrofit에서 사용하는 통신 인터페이스
 */
public interface RetrofitInterface {


    //GET API Example
    @GET("test")
    Observable<TestListResponse> getTestDataList();

    //GET API Path Variable Example
    @GET("test/{testNo}")
    Observable<TestDetailResponse> getTestData(@Path("testNo") int testNo);

    //POST API Body Data Example
    @POST("test")
    Observable<SuperResponse> postTestData(@Body RequestBody params);

    @POST("text")
    Observable<SuperResponse> postText(@Body RequestBody params);

    @POST("/inha/getRoom")
    Call<ChattingRoomResponse> postChatRoom(@Body RequestBody params);

    @POST("/inha/signUp")
    Call<SignUpResponse> postSignUp(@Body RequestBody params);

    @POST("/inha/room")
    Call<MakeRoomResponse> postMakeRoom(@Body RequestBody params);

    @POST("/inha/login")
    Call<LoginResponse> postLogin(@Body RequestBody params);

//    Mixed Example
//    @POST("test/{v1}/{v2}")
//    Observable<SuperResponse> postTestData(@Path("v1") String v1,
//                                                 @Path("v2") String v2,
//                                                 @Body RequestBody params);



    // 수동 로그인 (토큰 발급)
    @POST("/ask/register")
    Call<SuperResponse> postAsk(@Body RequestBody params);

}
