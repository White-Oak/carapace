package me.whiteoak.carapace;

import com.esotericsoftware.minlog.Log;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import me.whiteoak.carapace.exceptions.CarapaceException;
import me.whiteoak.carapace.markup.Nodes;
import me.whiteoak.carapace.markup.PostParser;
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
@RequiredArgsConstructor
class TopicViewer {

    private final Cookies cookies;

    public List<Post> loadPosts(Topic topic) throws IOException {
	LinkedList<Post> postsList = new LinkedList<>();
	String baseUrl = String.format("%sforum/id%d-%d",
		Carapace.BASE_URL, topic.getId(), topic.getCurrentPost());
	Connection con = Jsoup.connect(baseUrl)
		.userAgent(Carapace.getUserAgent())
		.cookies(cookies.getCookies())
		.timeout(10000);
	Connection.Response resp = con.execute();
	if (resp.statusCode() == 200) {
	    Document get = resp.parse();
	    String title = get.title();
	    Log.debug("carapace", "Got another topic page, title is " + title);
	    final Elements postsData = get.select(".posttable");
	    for (Element postData : postsData) {
		Post post = parsePost(postData);
		postsList.add(post);
	    }
	    return postsList;
	} else {
	    throw new CarapaceException("While trying to read a topic:" + resp.statusCode());
	}
    }

    private Post parsePost(Element postData) {
	final Elements userData = postData.select(".post_mid_l");

	final Element nicknameNext = userData.select(".username").first();
	final String nickname = nicknameNext.text();

	final Element userNameNext = userData.select("a").first();
	String username = userNameNext.attributes().get("href");
	final int indexOfUser = username.indexOf("user/");
	if (indexOfUser != -1) {
	    username = username.substring(indexOfUser + 5);
	} else {
	    username = "yourself";
	}

	final Element statusNext = userData.select("div").first();
	final String status = statusNext.text().trim().substring(2);
	final Element onlineNext = userData.select("span b").first();
	boolean online = false;
	if (onlineNext != null) {
	    online = onlineNext.text().equals("Online");
	}

	final Element postIdElement = postData.select(".post_top_r a").get(0);
	String postIdString = postIdElement.text().substring(1);
	int postId = Integer.parseInt(postIdString);

	final Element textNext = postData.select(".mainpost").first();
	Nodes text = PostParser.parsePostText(textNext);

	final Element nextDate = postData.select(".post_top_l .gray").first();
	final String date = nextDate.text();
	Post post = new Post(postId, nickname, username, status, online, date, text);
	return post;
    }

}
