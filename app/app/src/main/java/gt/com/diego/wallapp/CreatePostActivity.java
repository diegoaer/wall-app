package gt.com.diego.wallapp;

import android.os.Bundle;
import android.text.Editable;
import android.view.View;

import com.google.android.material.textfield.TextInputEditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import gt.com.diego.wallapp.connections.APIConnection;
import gt.com.diego.wallapp.content.Post;
import gt.com.diego.wallapp.content.User;

public class CreatePostActivity extends AppCompatActivity {
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);
        user = new User("admin", "admin");
        LiveData<User> data = APIConnection.getInstance().tokenAuth(user);
        data.observe(this, new Observer<User>() {
            @Override
            public void onChanged(User loggedUser) {
                user.setToken(loggedUser.getToken());
            }
        });
    }

    public void postAndClose(View view) {
        Editable content = ((TextInputEditText) findViewById(R.id.input_content)).getText();
        if (content != null) {
            APIConnection.getInstance().postPost(new Post(content.toString()), user.getToken());
            finish();
        }
    }
}
