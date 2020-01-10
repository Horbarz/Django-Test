package com.example.djangotest;

import com.example.djangotest.model.AddPostModel;
import com.example.djangotest.model.CategoryModel;
import com.example.djangotest.model.Login;
import com.example.djangotest.model.PostModel;
import com.example.djangotest.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface PostApi {
    String root = "http://192.168.43.168:8000/";
    String base_local = root+"api/v1/";
    String BASE_URL = base_local+"account/";
    String API_URL = root+"api/v1/";
    String POST_URL =  base_local + "post/";


    @POST("register/")
    Call<User> registerationUser (@Body User userModel);

    @POST("api-token-auth/")
    Call<User> login(@Body Login login);

        @GET("post/list/")
    Call<List<PostModel>> getListPost();
//
    @GET("post/{id}/")
    Call<PostModel> getPost(@Path(value = "id", encoded = true) String id);
//
    @POST("add/")
    Call<AddPostModel> addPost(@Header("Authorization")  String authToken, @Body AddPostModel addPostModel);
//
    @GET("profile/list/")
    Call<List<PostModel>> getProfileList(@Header("Authorization")  String authToken);
//
//
    @PUT("profile/edit/{id}/")
    Call<PostModel> updatePost(@Header("Authorization")  String authToken, @Body PostModel postModelUpdate, @Path(value = "id", encoded = true) String id);

//
    @DELETE("profile/delete/{id}/")
    Call<List<PostModel>> deletePost(@Header("Authorization")  String authToken, @Path(value = "id", encoded = true) String id);

//

    @GET("category/list/")
    Call<List<CategoryModel>> getAllCategory();
//
    @GET("category/list/{id}/")
    Call<List<CategoryModel>> getCategoryById(@Path(value = "id", encoded = true) Integer id);
}
