package gt.com.diego.wallapp.api;

import java.util.List;

import gt.com.diego.wallapp.content.Post;
import gt.com.diego.wallapp.content.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Interface for the API
 */
public interface APIEndpointInterface {

    /**
     * Gets the posts list from the server
     */
    @GET("posts/")
    Call<List<Post>> postList();

    /**
     * Creates a post
     */
    @POST("posts/")
    Call<Post> createPost(@Body Post post);

    /**
     * Creates a user
     */
    @POST("user/")
    Call<User> createUser(@Body User user);

}
