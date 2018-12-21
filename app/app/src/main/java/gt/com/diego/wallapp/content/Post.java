package gt.com.diego.wallapp.content;

import java.util.Date;

/** A post in the wall */
public class Post {
    private String content;
    private String user;
    private Date date;

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
}
