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
	//http://annimon.com/auto.php?id=id&p=p
	String baseUrl = String.format("http://annimon.com/auto.php?id=%d&p=%s",
		user.getId(), user.getPassword());
	Connection con = Jsoup.connect(baseUrl).userAgent(
		"Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/535.21 (KHTML, like Gecko) Chrome/19.0.1042.0 Safari/535.21").timeout(10000);
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
	String baseUrl = "http://annimon.com/exit.php";
	Connection con = Jsoup.connect(baseUrl).userAgent(
		"Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/535.21 (KHTML, like Gecko) Chrome/19.0.1042.0 Safari/535.21").timeout(10000);
	Connection.Response resp = con.execute();
	if (resp.statusCode() == 200) {
	    Document get = con.get();
	    String title = get.title();
	    Log.info("carapace", "Logged out, title is " + title);
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
