package me.whiteoak.carapace;

import me.whiteoak.carapace.metadata.User;

/**
 * An object used to store cache data through several runs of application.
 *
 * Cookies of the site are long-lived, so there is no need to authorize every application start.
 *
 * @author White Oak
 */
public class Cache {

    private final Settings settings;
    private final User user;
    private final Cookies cookies;

    Cache(Settings settings, User user, Cookies cookies) {
	this.settings = settings;
	this.user = user;
	this.cookies = cookies;
    }

    Settings getSettings() {
	return settings;
    }

    User getUser() {
	return user;
    }

    Cookies getCookies() {
	return cookies;
    }

}
