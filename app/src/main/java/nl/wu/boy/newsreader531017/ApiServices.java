package nl.wu.boy.newsreader531017;

import com.squareup.okhttp.ResponseBody;

import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;

/**
 * Created by Boy on 9-10-2016.
 */

public interface ApiServices {

    @GET("Articles")
    Call<Result> getArticles();

    @GET("Articles/{id}?count=20")
    Call<Result> getNextArticles(@Path("id") int Id);

    @GET("Articles")
    Call<Result> getArticles(@Header("x-authtoken") String token);

    @GET("Articles/{id}?count=20")
    Call<Result> getNextArticles(@Header("x-authtoken") String token, @Path("id") int Id);

    @PUT("Articles/{id}//like")
    Call<ResponseBody> likeArticle (@Header("x-authtoken") String token, @Path("id") int Id);

    @DELETE("Articles/{id}//like")
    Call<ResponseBody> dislikeArticle (@Header("x-authtoken") String token, @Path("id") int Id);

    @POST("Users/login")
    Call<AuthToken> login(@Body User user);

    @POST("Users/register")
    Call<Message> register(@Body User user);
}
