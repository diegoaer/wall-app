package gt.com.diego.wallapp.api;

import java.util.List;

import gt.com.diego.wallapp.content.Post;
import gt.com.diego.wallapp.content.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Interface for the API
 */
public interface APIEndpointInterface {

    /**
     * Gets the posts list from the server
     */
    @GET("posts/")
    Call<List<Post>> postList(@Query("since") int since);

    /**
     * Creates a post
     */
    @POST("posts/")
    Call<Post> createPost(@Body Post post, @Header("Authorization") String auth);

    /**
     * Creates a user
     */
    @POST("users/")
    Call<User> createUser(@Body User user);

    /**
     * Logs in and returns the user token
     */
    @POST("auth/")
    Call<User> tokenAuth(@Body User user);

}
