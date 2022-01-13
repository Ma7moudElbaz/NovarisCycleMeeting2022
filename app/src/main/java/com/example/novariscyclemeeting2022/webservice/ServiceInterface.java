package com.example.novariscyclemeeting2022.webservice;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

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

    //voting
    @GET("polls")
    Call<ResponseBody> getVoting(@Header("Authorization") String auth, @Query("page") int pageNo);



















    //projects
    @GET("projects")
    Call<ResponseBody> getProjects(@Header("Authorization") String auth, @Query("page") int pageNo,@QueryMap Map<String, String> filters);

    @POST("projects")
    @FormUrlEncoded
    Call<ResponseBody> addProject(@Header("Authorization") String auth, @FieldMap Map<String, String> map);

    @GET("projects/{project_id}")
    Call<ResponseBody> getProjectDetails(@Header("Authorization") String auth, @Path("project_id") int project_id);

    @PUT("projects/{project_id}/done")
    Call<ResponseBody> projectDone(@Header("Authorization") String auth, @Path("project_id") int project_id);

    @PUT("projects/{project_id}/cancel")
    Call<ResponseBody> projectCancel(@Header("Authorization") String auth, @Path("project_id") int project_id);

    //requests
    @GET("requests")
    Call<ResponseBody> getRequests(@Header("Authorization") String auth, @Query("project_id") int project_id, @Query("type_id") int type_id, @Query("page") int pageNo,@QueryMap Map<String, String> filters);

    @GET("requests")
    Call<ResponseBody> getJobOrderRequests(@Header("Authorization") String auth, @Query("status") int status, @Query("project_id") int project_id, @Query("type_id") int type_id, @Query("page") int pageNo);

    @POST("requests")
    @FormUrlEncoded
    Call<ResponseBody> addRequest(@Header("Authorization") String auth, @FieldMap Map<String, String> map);

    @Multipart
    @POST("requests/attaches")
    Call<ResponseBody> addAttach(@Header("Authorization") String auth, @Part List<MultipartBody.Part> files, @Part("request_id") RequestBody request_id);

    @GET("requests/{request_id}")
    Call<ResponseBody> getRequestDetails(@Header("Authorization") String auth, @Path("request_id") int request_id);

    //costs
    @POST("costs")
    @FormUrlEncoded
    Call<ResponseBody> addCost(@Header("Authorization") String auth, @FieldMap Map<String, String> map);

    @POST("costs/update/{cost_id}")
    @FormUrlEncoded
    Call<ResponseBody> editCost(@Header("Authorization") String auth, @Path("cost_id") int cost_id, @FieldMap Map<String, String> map);

    @POST("costs/status")
    @FormUrlEncoded
    Call<ResponseBody> changeCostStatus(@Header("Authorization") String auth, @FieldMap Map<String, String> map);

    //job orders
    @GET("job-orders")
    Call<ResponseBody> getJobOrders(@Header("Authorization") String auth, @Query("project_id") int project_id, @Query("page") int pageNo,@QueryMap Map<String, String> filters);

    @GET("job-orders/{job_order_id}")
    Call<ResponseBody> getJobOrderDetails(@Header("Authorization") String auth, @Path("job_order_id") int job_order_id);

    @POST("job-orders")
    @FormUrlEncoded
    Call<ResponseBody> addJobOrder(@Header("Authorization") String auth, @FieldMap Map<String, String> map);

    @POST("job-orders/status")
    @FormUrlEncoded
    Call<ResponseBody> changeJobOrderStatus(@Header("Authorization") String auth, @FieldMap Map<String, String> map);

    @POST("pos")
    @FormUrlEncoded
    Call<ResponseBody> addPoNumber(@Header("Authorization") String auth, @FieldMap Map<String, String> map);

    //notifications
    @GET("notifications")
    Call<ResponseBody> getNotifications(@Header("Authorization") String auth, @Query("page") int pageNo);

    @GET("unreadNotificationsNumber")
    Call<ResponseBody> getNotificationsNumber(@Header("Authorization") String auth);

    @POST("markAsRead")
    @FormUrlEncoded
    Call<ResponseBody> readNotification(@Header("Authorization") String auth, @FieldMap Map<String, String> map);
}