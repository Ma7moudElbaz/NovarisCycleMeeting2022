package com.novartis.winnovators.webservice;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ServiceInterface {

    //Auth
    @POST("auth/login")
    @FormUrlEncoded
    Call<ResponseBody> login(@FieldMap Map<String, String> map);

    @POST("auth/reset-password")
    @FormUrlEncoded
    Call<ResponseBody> resetPassword(@FieldMap Map<String, String> map);

    //agenda
    @GET("agenda/{day}")
    Call<ResponseBody> getAgenda(@Header("Authorization") String auth, @Path("day") int day);

    //posts
    @GET("posts")
    Call<ResponseBody> getPosts(@Header("Authorization") String auth, @Query("page") int pageNo);

    @GET("posts/{post_id}")
    Call<ResponseBody> getSinglePost(@Header("Authorization") String auth, @Path("post_id") int post_id);

    @POST("posts/{post_id}/make-comment")
    @FormUrlEncoded
    Call<ResponseBody> addComment(@Header("Authorization") String auth, @Path("post_id") int post_id, @Field("content") String content);

    @POST("post/{post_id}/likes")
    Call<ResponseBody> addLike(@Header("Authorization") String auth, @Path("post_id") int post_id);

    //voting
    @GET("polls")
    Call<ResponseBody> getVoting(@Header("Authorization") String auth, @Query("page") int pageNo);

    @GET("polls/{voting_id}")
    Call<ResponseBody> getSingleVoting(@Header("Authorization") String auth, @Path("voting_id") int voting_id);

    @POST("polls")
    @FormUrlEncoded
    Call<ResponseBody> submitAnswer(@FieldMap Map<String, String> map);

    //notifications
    @GET("notifications")
    Call<ResponseBody> getNotifications(@Header("Authorization") String auth, @Query("page") int pageNo);

    //gm_messaged
    @GET("gm-message")
    Call<ResponseBody> getGmMessage(@Header("Authorization") String auth);


}