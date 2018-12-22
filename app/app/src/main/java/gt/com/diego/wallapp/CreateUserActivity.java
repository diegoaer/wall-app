package gt.com.diego.wallapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import gt.com.diego.wallapp.connections.APIConnection;
import gt.com.diego.wallapp.content.User;

public class CreateUserActivity extends AppCompatActivity {
    private TextInputEditText usernameView;
    private TextInputEditText passwordView;
    private TextInputEditText emailView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);
        usernameView = findViewById(R.id.input_username);
        passwordView = findViewById(R.id.input_password);
        emailView = findViewById(R.id.input_email);
    }

    /**
     * Creates the user
     */
    public void createUser(View view) {
        if (usernameView.getText() != null && passwordView.getText() != null &&
                emailView.getText() != null) {
            User user = new User()
                    .setPassword(passwordView.getText().toString())
                    .setUsername(usernameView.getText().toString())
                    .setEmail(emailView.getText().toString());
            LiveData<String> data = APIConnection.getInstance().createUser(user, this);
            data.observe(this, new Observer<String>() {
                @Override
                public void onChanged(String response) {
                    if (response.equals(""))
                        goToLoginAndFinish();
                }
            });
        }
    }

    /**
     * Creates a login activity and finishes
     */
    private void goToLoginAndFinish() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        Toast.makeText(this, R.string.please_log_in, Toast.LENGTH_SHORT).show();
        finish();
    }
}
