package me.whiteoak.carapace.metadata;

/**
 * A value-type containing data about a post.
 *
 * @author White Oak
 */
public class Post {

    private final String nickname;
    private final String username;
    private final String status;
    private final boolean online;
    private final String date;
    private final String text;

    public Post(String nickname, String username, String status, boolean online, String date, String text) {
	this.nickname = nickname;
	this.username = username;
	this.status = status;
	this.online = online;
	this.date = date;
	this.text = text;
    }

    public String getNickname() {
	return nickname;
    }

    public String getUsername() {
	return username;
    }

    public String getStatus() {
	return status;
    }

    public boolean isOnline() {
	return online;
    }

    public String getDate() {
	return date;
    }

    public String getText() {
	return text;
    }

}
