package me.whiteoak.carapace;

import com.esotericsoftware.minlog.Log;
import java.io.IOException;
import java.util.Map;
import lombok.Getter;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 *
 * @author White Oak
 */
class Authorizator {

    @Getter private Map<String, String> cookies;

    public Status authorize(User user) throws IOException {
	String baseUrl = String.format(Carapace.BASE_URL + "auto.php?id=%d&p=%s",
		user.getId(), user.getPassword());
	Connection con = Jsoup.connect(baseUrl)
		.userAgent(Carapace.USER_AGENT)
		.timeout(10000);
	Connection.Response resp = con.execute();
	if (resp.statusCode() == 200) {
	    Document get = con.get();
	    String title = get.title();
	    Log.info("carapace", "Authorized, title is " + title);
	    this.cookies = resp.cookies();
	    return new Status(StatusType.AUTHORIZED, title);
	} else {
	    return new Status(StatusType.ERROR, "While trying to login:" + String.valueOf(resp.statusCode()));
	}
    }

    public Status logout() throws IOException {
	String baseUrl = Carapace.BASE_URL + "exit.php";
	Connection con = Jsoup.connect(baseUrl)
		.cookies(cookies)
		.userAgent(Carapace.USER_AGENT)
		.timeout(10000);
	Connection.Response resp = con.execute();
	if (resp.statusCode() == 200) {
	    Document get = con.get();
	    String title = get.title();
	    Log.info("carapace", "Logged out, title is " + title);
	    cookies = null;
	    return new Status(StatusType.IDLE, title);
	} else {
	    return new Status(StatusType.ERROR, "While trying to logout:" + String.valueOf(resp.statusCode()));
	}
    }

    @Deprecated
    public static int resolveId(String login) throws IOException {
	Document doc = Jsoup.connect("http://annimon.com/user/" + login).get();
	String title = doc.title();
	if (title.contains("404")) {
	    return -1;
	}
	return -1;
    }
}
