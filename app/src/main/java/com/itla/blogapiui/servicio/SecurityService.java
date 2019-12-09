package com.itla.blogapiui.servicio;

import com.itla.blogapiui.entidades.Login;
import com.itla.blogapiui.entidades.Post;
import com.itla.blogapiui.entidades.Regist;
import com.itla.blogapiui.entidades.User;

import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.HeaderMap;
import retrofit2.http.Headers;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface SecurityService {

@POST("login")
public Call<User> Login(@Body Login login);

@POST("register")
public Call<Regist> Register(@Body Regist register);

@GET("post")
public Call<List<Post>> getPosts(@HeaderMap Map<String, String> headers);

/* LIKE Y DISLIKE */
@PUT("post/{id}/like")
Call<Void> likePost(@Path("id") int id, @HeaderMap Map<String, String> headers);

@DELETE("post/{id}/like")
Call<Void> dislikePost(@Path("id") int id, @HeaderMap Map<String, String> headers);

}