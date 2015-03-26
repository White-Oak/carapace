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
public class TopicViewer {

    @Getter private List<Topic> topicsList;

    public Status getLastTopics(Map<String, String> cookies) throws IOException {
	topicsList = new LinkedList<>();
	String baseUrl = "http://annimon.com/forum/index.php?act=new";
	Connection con = Jsoup.connect(baseUrl)
		.userAgent("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/535.21 (KHTML, like Gecko) Chrome/19.0.1042.0 Safari/535.21")
		.cookies(cookies)
		.timeout(10000);
	Connection.Response resp = con.execute();
	if (resp.statusCode() == 200) {
	    Document get = con.get();
	    String title = get.title();
	    Log.info("carapace", "Got new topics page, title is " + title);
	    final Elements topics = get.select(".temtop2");
//	    System.out.println(topics.size());
	    //System.out.println(topics.text());
	    topics.stream()
		    .map(topic -> topic.select("a").get(0))
		    .forEach(body -> {
			final String idStr = body.attributes().get("href");
			int id = Integer.parseInt(idStr.substring(idStr.indexOf("id") + 2));
//			System.out.println("ID: " + id);
			Element nameElement = body.select("b").get(0);
			String topicName = nameElement.text();
//			System.out.println("Name: " + topicName);
			topicsList.add(new Topic(id, topicName));
		    });
	    return new Status(StatusType.ONLINE);
	} else {
	    return new Status(StatusType.ERROR, "While trying to read new topics:" + String.valueOf(resp.statusCode()));
	}
    }
}
