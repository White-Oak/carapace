package me.whiteoak.carapace.metadata;

/**
 * A value-type describing authorization data for a user.
 *
 * @author White Oak
 */
public class User {

    public User(int id, String password) {
	this.id = id;
	this.password = password;
    }

    public String getPassword() {
	return password;
    }

    public int getId() {
	return id;
    }

    private final int id;
    private final String password;
}
