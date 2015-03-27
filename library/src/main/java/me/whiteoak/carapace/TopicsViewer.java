package me.whiteoak.carapace;

import com.esotericsoftware.minlog.Log;
import java.io.IOException;
import java.util.*;
import lombok.Getter;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author White Oak
 */
class TopicsViewer {

    @Getter private List<Post> postsList;

    public Status loadPosts(Topic topic, Map<String, String> cookies) throws IOException {
	postsList = new LinkedList<>();
	String baseUrl = Carapace.BASE_URL + "forum/id" + topic.getId();
	Connection con = Jsoup.connect(baseUrl)
		.userAgent(Carapace.USER_AGENT)
		.cookies(cookies)
		.timeout(10000);
	Connection.Response resp = con.execute();
	if (resp.statusCode() == 200) {
	    Document get = con.get();
	    String title = get.title();
	    Log.info("carapace", "Got another topic page, title is " + title);
	    final Elements users = get.select(".post_mid_l");
	    final Elements posts = get.select(".mainpost");
	    Iterator<Element> postsIterator = posts.iterator();
	    users.stream()
		    .map(user -> user.select("a").get(0).select("b").get(0))
		    .forEach(user -> {
			final String userName = user.text();
//			System.out.println("Username: " + userName);
			Element post = postsIterator.next();
			String text = post.text();
//			System.out.println("Text: " + text);
			postsList.add(new Post(userName, text));
		    });
	    return new Status(StatusType.ONLINE);
	} else {
	    return new Status(StatusType.ERROR, "While trying to read a topic:" + String.valueOf(resp.statusCode()));
	}
    }
}
