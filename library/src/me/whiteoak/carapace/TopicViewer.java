package me.whiteoak.carapace;

import com.esotericsoftware.minlog.Log;
import java.io.IOException;
import java.util.*;
import me.whiteoak.carapace.metadata.Post;
import me.whiteoak.carapace.metadata.Topic;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author White Oak
 */
class TopicViewer {

    private List<Post> postsList;
    private final Cookies cookies;

    public TopicViewer(Cookies cookies) {
	this.cookies = cookies;
    }

    public List<Post> getPostsList() {
	return postsList;
    }

    public Status loadPosts(Topic topic) throws IOException {
	postsList = new LinkedList<>();
	String baseUrl = String.format("%sforum/id%d-%d",
		Carapace.BASE_URL, topic.getId(), topic.getCurrentPost());
	Connection con = Jsoup.connect(baseUrl)
		.userAgent(Carapace.getUserAgent())
		.cookies(cookies.getCookies())
		.timeout(10000);
	Connection.Response resp = con.execute();
	if (resp.statusCode() == 200) {
	    Document get = con.get();
	    String title = get.title();
	    Log.debug("carapace", "Got another topic page, title is " + title);
	    final Elements postsData = get.select(".posttable");
	    for (Element postData : postsData) {
		Post post = parsePost(postData);
		postsList.add(post);
	    }
	    return new Status(StatusType.ONLINE);
	} else {
	    return new Status(StatusType.ERROR, "While trying to read a topic:" + String.valueOf(resp.statusCode()));
	}
    }

    private Post parsePost(Element postData) {
	final Elements userData = postData.select(".post_mid_l");
	final Element nicknameNext = userData.select(".username").first();
	final String nickname = nicknameNext.text();
	final Element userNameNext = userData.select("a").first();
	String username = userNameNext.attributes().get("href");
	username = username.substring(username.indexOf("user/") + 5);
	final Element statusNext = userData.select("div").first();
	final String status = statusNext.text().trim().substring(2);
	final Element onlineNext = userData.select("span b").first();
	final boolean online = onlineNext.text().equals("Online");

	final Element textNext = postData.select(".mainpost").first();
	String text = textNext.html();
	text = text.replaceAll("<br>", "[new line]");
	text = Jsoup.parse(text).text();
	text = text.replaceAll("\\[new line\\]", "\n");

	final Element nextDate = postData.select(".post_top_l .gray").first();
	final String date = nextDate.text();
	Post post = new Post(nickname, username, status, online, date, text);
	return post;
    }
}
