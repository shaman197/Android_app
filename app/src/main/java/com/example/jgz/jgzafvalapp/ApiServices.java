package com.example.jgz.jgzafvalapp;

import com.example.jgz.jgzafvalapp.models.AuthToken;
import com.example.jgz.jgzafvalapp.models.InfoItem;
import com.example.jgz.jgzafvalapp.models.LogItem;
import com.example.jgz.jgzafvalapp.models.Logbook;
import com.example.jgz.jgzafvalapp.models.User;

import java.util.List;

import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by Boy on 11-11-2016.
 */
public interface ApiServices {

    @GET("users/")
    Call<User> login(@Query("Email") String email, @Query("Password") String password);

    @GET("logbooks/user/{userId}/{date}")
    Call<Logbook> getLog(@Path("userId") String id, @Path("date") String date);

    @POST("logbooks/{userId}")
    Call<Logbook> postLog(@Path("userId") String id);

    @PUT("logbooks/{id}/{mood}")
    Call<Logbook> changeMood(@Path("id") String id, @Path("mood") String mood);

    @GET("logbookitems/all/{id}")
    Call<List<LogItem>> getAllLogbookItems(@Path("id") String id);

    @POST("logbookitems")
    Call<LogItem> postLogItem(@Body LogItem item);

    @DELETE("logbookitems/{id}")
    Call<LogItem> deleteLogItem(@Path("id") String id);

    @GET("challanges/")
    Call<User> getChallanges(@Path("id") int Id);

    @GET("articles/")
    Call<List<InfoItem>> getArticleCategory(@Query("category") String category);

    @GET("articles/")
    Call<List<InfoItem>> getMoreArticleCategory(@Query("date") long date, @Query("category") String category);
}

