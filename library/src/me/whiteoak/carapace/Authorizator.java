package me.whiteoak.carapace;

import com.esotericsoftware.minlog.Log;
import java.io.IOException;
import lombok.Getter;
import me.whiteoak.carapace.exceptions.CarapaceException;
import me.whiteoak.carapace.metadata.User;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 *
 * @author White Oak
 */
class Authorizator {

    @Getter private Cookies cookies;

    public void authorize(User user) throws IOException {
	String baseUrl = String.format(Carapace.BASE_URL + "auto.php?id=%d&p=%s",
		user.getId(), user.getPassword());
	Connection con = Jsoup.connect(baseUrl)
		.userAgent(Carapace.getUserAgent())
		.timeout(10000);
	Connection.Response resp = con.execute();
	if (resp.statusCode() == 200) {
	    Document get = resp.parse();
	    String title = get.title();
	    Log.debug("carapace", "Authorized with id " + user.getId() + ", title is " + title);
	    if (title.contains("Вход")) {
		throw new CarapaceException("Given user is not permitted to enter the site: check id and password.");
	    }
	    this.cookies = new Cookies(resp.cookies());
	} else {
	    throw new CarapaceException("While trying to login:" + String.valueOf(resp.statusCode()));
	}
    }

    public void logout() throws IOException {
	String baseUrl = Carapace.BASE_URL + "exit.php";
	Connection con = Jsoup.connect(baseUrl)
		.cookies(cookies.getCookies())
		.userAgent(Carapace.getUserAgent())
		.data("submit", "Да, выйти")
		.method(Connection.Method.POST)
		.timeout(10000);
	Connection.Response resp = con.execute();
	if (resp.statusCode() == 200) {
	    Document get = resp.parse();
	    String title = get.title();
	    Log.debug("carapace", "Logged out, title is " + title);
	    cookies = null;
	} else {
	    throw new CarapaceException("While trying to logout:" + String.valueOf(resp.statusCode()));
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
