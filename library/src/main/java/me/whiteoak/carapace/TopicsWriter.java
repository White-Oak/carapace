package me.whiteoak.carapace;

import com.esotericsoftware.minlog.Log;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 *
 * @author White Oak
 */
@RequiredArgsConstructor
class TopicsWriter {

    private final Topic topic;
    private final Cookies cookies;

    public Status write(String message) throws IOException {
	String baseUrl = Carapace.BASE_URL + "/forum/index.php?act=say&id=" + topic.getId();
	Connection con = Jsoup.connect(baseUrl)
		.userAgent(Carapace.USER_AGENT)
		.cookies(cookies.getCookies())
		.data("msg", message)
		.data("submit", "1")
		.method(Connection.Method.POST)
		.timeout(10000);
	Connection.Response resp = con.execute();
	if (resp.statusCode() == 200) {
	    Document get = con.get();
	    Log.info("carapace", "Wrote a new message, title is: " + get.title());
	    return new Status(StatusType.SHITPOSTING);
	} else {
	    return new Status(StatusType.ERROR, "While trying to write a post:" + String.valueOf(resp.statusCode()));
	}
    }
}
