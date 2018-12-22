package gt.com.diego.wallapp.connections;

import android.content.Context;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import gt.com.diego.wallapp.R;
import gt.com.diego.wallapp.api.APIEndpointInterface;
import gt.com.diego.wallapp.content.Post;
import gt.com.diego.wallapp.content.User;
import okhttp3.ResponseBody;
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

    public LiveData<String> createUser(User user, final Context context) {
        final MutableLiveData<String> creationResponse = new MutableLiveData<>();
        apiService.createUser(user).enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                if (response.isSuccessful()) {
                    creationResponse.setValue("");
                } else if (response.errorBody() != null) {
                    createDetailedErrorToast(context, response.errorBody());
                } else {
                    createErrorToast(context, response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                createErrorToast(context, t.getMessage());
            }
        });
        return creationResponse;
    }

    /**
     * Logs a user in
     *
     * @param user the user to be logged in
     */
    public LiveData<User> tokenAuth(User user, final Context context) {
        final MutableLiveData<User> responseToken = new MutableLiveData<>();
        apiService.tokenAuth(user).enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull
                    Response<User> response) {
                if (response.isSuccessful()) {
                    responseToken.setValue(response.body());
                } else if (response.errorBody() != null) {
                    createDetailedErrorToast(context, response.errorBody());
                } else {
                    createErrorToast(context, response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                createErrorToast(context, t.getMessage());
            }
        });
        return responseToken;
    }

    /**
     * Gets the posts from the server and adds it to the LiveData
     */
    public void getPosts(final MutableLiveData<List<Post>> responseData, final Context context) {
        apiService.postList(lastUpdate).enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(@NonNull Call<List<Post>> call,
                                   @NonNull Response<List<Post>> response) {
                if (response.isSuccessful() && response.body() != null &&
                        response.body().size() > 0) {
                    List<Post> responseBody = response.body();
                    lastUpdate = responseBody.get(responseBody.size() - 1).getId();
                    responseData.setValue(responseBody);
                } else {
                    responseData.setValue(null);
                    if (!response.isSuccessful())
                        createErrorToast(context, response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Post>> call, @NonNull Throwable t) {
                responseData.setValue(null);
                createErrorToast(context, t.getMessage());
            }
        });
    }

    /**
     * Creates a new post in the server
     *
     * @param post the post to be created
     * @param auth the authentication string
     */
    public LiveData<Integer> postPost(Post post, String auth, final Context context) {
        final MutableLiveData<Integer> responseCode = new MutableLiveData<>();
        apiService.createPost(post, auth).enqueue(new Callback<Post>() {
            @Override
            public void onResponse(@NonNull Call<Post> call, @NonNull Response<Post> response) {
                if (response.code() != 201) responseCode.setValue(response.code());
                else createErrorToast(context, response.message());
            }

            @Override
            public void onFailure(@NonNull Call<Post> call, @NonNull Throwable t) {
                createErrorToast(context, t.getMessage());
            }
        });
        return responseCode;
    }

    /**
     * Creates a simple toast with an error message
     *
     * @param context the context in which to display the toast
     * @param message the extra message that will be displayed in the toast
     */
    private void createErrorToast(Context context, String message) {
        String toastMessage = context.getString(R.string.an_error_occurred, message);
        Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT).show();
    }

    /**
     * Creates a toast for specific errors
     *
     * @param context      the context in which to display the toast
     * @param responseBody the body of the error response
     */
    private void createDetailedErrorToast(Context context, @NonNull ResponseBody responseBody) {
        try {
            String errorBody = responseBody.string();
            JsonParser parser = new JsonParser();
            JsonObject jsonObject = parser.parse(errorBody).getAsJsonObject();
            Set<Map.Entry<String, JsonElement>> entries = jsonObject.entrySet();
            Map.Entry<String, JsonElement> entry = entries.iterator().next();
            String message;
            if (entry.getValue().isJsonArray())
                message = entry.getValue().getAsJsonArray().get(0).getAsString();
            else
                message = entry.getValue().getAsString();
            String fieldWithIssue = "";
            if (!entry.getKey().equals("non_field_errors"))
                fieldWithIssue = entry.getKey();
            String toastMessage = fieldWithIssue + message;
            Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
