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
        user = (User) getIntent().getSerializableExtra("user");
    }

    public void postAndClose(View view) {
        Editable content = ((TextInputEditText) findViewById(R.id.input_content)).getText();
        if (content != null) {
            LiveData<Integer> data = APIConnection
                    .getInstance()
                    .postPost(new Post().setContent(content.toString()), user.getToken(), this);
            data.observe(this, new Observer<Integer>() {
                @Override
                public void onChanged(Integer integer) {
                    if (integer == 201) finish();
                }
            });
        }
    }
}
