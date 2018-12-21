package gt.com.diego.wallapp;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import gt.com.diego.wallapp.connections.APIConnection;
import gt.com.diego.wallapp.content.Post;

public class MainActivity extends AppCompatActivity {
    private WallAdapter wallAdapter;
    private RecyclerView wallContainer;
    private WallViewModel wallViewModel;
    private SwipeRefreshLayout refreshLayout;

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
            }
        });
    }

    public void openCreatePostActivity(View view) {
        Intent intent = new Intent(this, CreatePostActivity.class);
        startActivity(intent);
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
            updatePosts();
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
            refreshLayout.setRefreshing(false);
        }
    }
}
