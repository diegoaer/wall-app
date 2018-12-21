package gt.com.diego.wallapp;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import gt.com.diego.wallapp.content.Post;

/**
 * Adapter of a wall (list of posts)
 */
public class WallAdapter extends RecyclerView.Adapter<WallAdapter.PostViewHolder> {
    private List<Post> posts;

    public WallAdapter(List<Post> posts) {
        this.posts = posts;
    }

    /**
     * @see RecyclerView.Adapter#onCreateViewHolder
     */
    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardView view = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.section_wall_post, parent, false);
        return new PostViewHolder(view);
    }

    /**
     * @see RecyclerView.Adapter#onBindViewHolder
     */
    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        holder.bindData(posts.get(position));
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return posts.size();
    }

    class PostViewHolder extends RecyclerView.ViewHolder {
        private TextView dateTextView;
        private TextView usernameTextView;
        private TextView contentTextView;

        PostViewHolder(View itemView) {
            super(itemView);
            usernameTextView = itemView.findViewById(R.id.username);
            contentTextView = itemView.findViewById(R.id.content);
            dateTextView = itemView.findViewById(R.id.date);
        }

        void bindData(final Post post) {
            usernameTextView.setText(post.getUser());
            dateTextView.setText(post.getFormattedDate());
            contentTextView.setText(post.getContent());
        }
    }
}
