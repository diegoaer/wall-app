package gt.com.diego.wallapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import gt.com.diego.wallapp.connections.APIConnection;
import gt.com.diego.wallapp.content.Post;
import gt.com.diego.wallapp.content.User;

public class MainActivity extends AppCompatActivity {
    private final int LOGIN_RESULT = 0;
    private WallAdapter wallAdapter;
    private RecyclerView wallContainer;
    private WallViewModel wallViewModel;
    private SwipeRefreshLayout refreshLayout;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        wallContainer = findViewById(R.id.wall_container);
        wallViewModel = ViewModelProviders.of(this).get(MainActivity.WallViewModel.class);
        refreshLayout = findViewById(R.id.swipeRefresh);

        refreshLayout.setOnRefreshListener(new OnRefreshWallListener());

        initWallRecyclerView();
        observeData();
    }

    /**
     * Initilizes the RecylerView for the wall
     */
    private void initWallRecyclerView() {
        wallAdapter = new WallAdapter(wallViewModel.posts);
        wallContainer.setLayoutManager(new LinearLayoutManager(this));
        wallContainer.setHasFixedSize(true);
        wallContainer.setAdapter(wallAdapter);
        wallContainer.setItemAnimator(new DefaultItemAnimator());
    }

    /**
     * Observes changes to the wallViewModel's postList
     */
    private void observeData() {
        wallViewModel.newPostList.observe(this, new Observer<List<Post>>() {
            /**
             * Adds the new posts to the posts and tells the wallAdapter that there are new changes
             * @param newPosts the new posts
             */
            @Override
            public void onChanged(@Nullable List<Post> newPosts) {
                if (newPosts != null) {
                    wallViewModel.posts.addAll(newPosts);
                    wallAdapter.notifyDataSetChanged();
                }
                refreshLayout.setRefreshing(false);
            }
        });
    }

    /**
     * Starts a CreatePostActivity
     */
    public void openCreatePostActivity(View view) {
        Intent intent = new Intent(this, CreatePostActivity.class);
        intent.putExtra("user", user);
        startActivity(intent);
    }

    /**
     * When the app starts/resumes update posts
     */
    @Override
    protected void onStart() {
        super.onStart();
        wallViewModel.updatePosts();
        refreshLayout.setRefreshing(true);
    }

    /**
     * Get the logged user from the Login activity
     *
     * @see android.preference.PreferenceManager.OnActivityResultListener
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case LOGIN_RESULT:
                if (data != null && resultCode == Activity.RESULT_OK) {
                    user = (User) data.getSerializableExtra("user");
                }
                break;
        }
    }

    /**
     * Starts the login activity
     */
    private void startLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivityForResult(intent, LOGIN_RESULT);
    }

    /**
     * Starts the create user activity
     */
    private void startCreateUserActivity() {
        Intent intent = new Intent(this, CreateUserActivity.class);
        startActivityForResult(intent, LOGIN_RESULT);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.auth_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.login:
                startLoginActivity();
                return true;
            case R.id.create_user:
                startCreateUserActivity();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Holds the postList
     */
    public static class WallViewModel extends ViewModel {
        private final MutableLiveData<List<Post>> newPostList;
        private final List<Post> posts;

        public WallViewModel() {
            newPostList = new MutableLiveData<>();
            posts = new ArrayList<>();
        }

        private void updatePosts() {
            APIConnection.getInstance().getPosts(newPostList);
        }
    }

    private class OnRefreshWallListener implements SwipeRefreshLayout.OnRefreshListener {

        /**
         * Called when a swipe gesture triggers a refresh.
         */
        @Override
        public void onRefresh() {
            wallViewModel.updatePosts();
        }
    }
}
