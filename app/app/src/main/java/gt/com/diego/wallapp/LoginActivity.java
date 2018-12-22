package gt.com.diego.wallapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.textfield.TextInputEditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import gt.com.diego.wallapp.connections.APIConnection;
import gt.com.diego.wallapp.content.User;

public class LoginActivity extends AppCompatActivity {
    private User user;
    private TextInputEditText usernameView;
    private TextInputEditText passwordView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        usernameView = findViewById(R.id.input_username);
        passwordView = findViewById(R.id.input_password);
    }

    /**
     * Logs in the user
     */
    public void login(View view) {
        if (usernameView.getText() != null && passwordView.getText() != null) {
            user = new User()
                    .setUsername(usernameView.getText().toString())
                    .setPassword(passwordView.getText().toString());
            LiveData<User> data = APIConnection.getInstance().tokenAuth(user);
            data.observe(this, new Observer<User>() {
                @Override
                public void onChanged(User loggedUser) {
                    user.setToken(loggedUser.getToken());
                    setResultAndFinish();
                }
            });
        }
    }

    /**
     * Sends the user to the previous activity and finishes
     */
    private void setResultAndFinish() {
        Intent intent = new Intent();
        intent.putExtra("user", user);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }
}
