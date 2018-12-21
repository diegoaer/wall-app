package gt.com.diego.wallapp.connections;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import gt.com.diego.wallapp.api.APIEndpointInterface;
import gt.com.diego.wallapp.content.Post;
import gt.com.diego.wallapp.content.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Handles the connection
 */
public class APIConnection {
    private static APIConnection instance;
    private String BASE_URL = "http://10.0.2.2:8000/";
    private APIEndpointInterface apiService;
    private int lastUpdate;

    /**
     * Creates the the apiService object
     */
    private APIConnection() {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        apiService = retrofit.create(APIEndpointInterface.class);
    }

    /**
     * Returns the instance of APIConnection (Singleton)
     *
     * @return the APIConnection instance
     */
    public static APIConnection getInstance() {
        if (instance == null)
            instance = new APIConnection();
        return instance;
    }

    /**
     * Logs a user in
     *
     * @param user the user to be logged in
     */
    public LiveData<User> tokenAuth(User user) {
        final MutableLiveData<User> responseToken = new MutableLiveData<>();
        apiService.tokenAuth(user).enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull
                    Response<User> response) {
                if (response.isSuccessful()) {
                    responseToken.setValue(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                Log.e("API", t.getMessage());
                t.printStackTrace();
            }
        });
        return responseToken;
    }

    /**
     * Gets the posts from the server and adds it to the LiveData
     */
    public void getPosts(final MutableLiveData<List<Post>> responseData) {
        apiService.postList(lastUpdate).enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(@NonNull Call<List<Post>> call,
                                   @NonNull Response<List<Post>> response) {
                if (response.isSuccessful() && response.body() != null &&
                        response.body().size() > 0) {
                    List<Post> responseBody = response.body();
                    lastUpdate = responseBody.get(responseBody.size() - 1).getId();
                    responseData.setValue(responseBody);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Post>> call, @NonNull Throwable t) {
                Log.e("API", t.getMessage());
                t.printStackTrace();
            }
        });
    }

    /**
     * Creates a new post in the server
     *
     * @param post the post to be created
     * @param auth the authentication string
     */
    public void postPost(Post post, String auth) {
        apiService.createPost(post, auth).enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                Log.e("API", response.code() + "");
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {
                Log.e("API", t.getMessage());
                t.printStackTrace();
            }
        });
    }

}
