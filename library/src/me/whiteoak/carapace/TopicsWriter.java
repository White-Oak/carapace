package me.whiteoak.carapace;

import com.esotericsoftware.minlog.Log;
import java.io.IOException;
import me.whiteoak.carapace.metadata.Topic;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 *
 * @author White Oak
 */
class TopicsWriter {

    private final Topic topic;
    private final Cookies cookies;

    public TopicsWriter(Topic topic, Cookies cookies) {
	this.topic = topic;
	this.cookies = cookies;
    }

    public Status write(String message) throws IOException {
	String baseUrl = Carapace.BASE_URL + "/forum/index.php?act=say&id=" + topic.getId();
	Connection con = Jsoup.connect(baseUrl)
		.userAgent(Carapace.getUserAgent())
		.cookies(cookies.getCookies())
		.data("msg", message)
		.data("submit", "1")
		.method(Connection.Method.POST)
		.timeout(10000);
	Connection.Response resp = con.execute();
	if (resp.statusCode() == 200) {
	    Document get = con.get();
	    Log.debug("carapace", "Wrote a new message, title is: " + get.title());
	    return new Status(StatusType.SHITPOSTING);
	} else {
	    return new Status(StatusType.ERROR, "While trying to write a post:" + String.valueOf(resp.statusCode()));
	}
    }
}
