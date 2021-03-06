package gt.com.diego.wallapp.content;


import java.io.Serializable;

/**
 * A user that can post
 */
public class User implements Serializable {
    private String username;
    private String password;
    private String email;
    private String token;

    public String getUsername() {
        return username;
    }

    public User setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public User setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public User setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getToken() {
        return token;
    }

    public User setToken(String token) {
        this.token = "Token " + token;
        return this;
    }
}
