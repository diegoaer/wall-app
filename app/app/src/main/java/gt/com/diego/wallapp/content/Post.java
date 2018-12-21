package gt.com.diego.wallapp.content;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A post in the wall
 */
public class Post {
    private Integer id;
    private String content;
    private String user;
    private Date date;

    public Post(String content) {
        user = ""; // The user cant be None
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public Post setId(int id) {
        this.id = id;
        return this;
    }

    public String getContent() {
        return content;
    }

    public Post setContent(String content) {
        this.content = content;
        return this;
    }

    public String getUser() {
        return user;
    }

    public Post setUser(String user) {
        this.user = user;
        return this;
    }

    public Date getDate() {
        return date;
    }

    public Post setDate(Date date) {
        this.date = date;
        return this;
    }

    public String getFormattedDate() {
        DateFormat format = SimpleDateFormat.getDateTimeInstance();
        return format.format(date);
    }
}
