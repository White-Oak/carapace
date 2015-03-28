package me.whiteoak.carapace;

import java.util.Map;

/**
 *
 * @author White Oak
 */
class Cookies {

    private final Map<String, String> cookies;

    public Cookies(Map<String, String> cookies) {
	this.cookies = cookies;
    }

    public Map<String, String> getCookies() {
	return cookies;
    }

}
