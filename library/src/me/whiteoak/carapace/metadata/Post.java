package me.whiteoak.carapace.metadata;

import me.whiteoak.carapace.markup.Nodes;

/**
 * A value-type containing data about a post.
 *
 * @author White Oak
 */
public class Post {

    private final int id;
    private final String nickname;
    private final String username;
    private final String status;
    private final boolean online;
    private final String date;
    private final Nodes text;

    public Post(int id, String nickname, String username, String status, boolean online, String date, Nodes text) {
	this.id = id;
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

    public Nodes getText() {
	return text;
    }

    public int getId() {
	return id;
    }

}
