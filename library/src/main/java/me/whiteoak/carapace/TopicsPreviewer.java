package me.whiteoak.carapace;

import com.esotericsoftware.minlog.Log;
import java.io.IOException;
import java.util.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
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
class TopicsPreviewer {

    @Getter private List<Topic> topicsList;
    private final Cookies cookies;

    public Status getLastTopics() throws IOException {
	return getNewTopics("act=new&period");
    }

    private Status getNewTopics(String options) throws IOException {
	topicsList = new LinkedList<>();
	String baseUrl = Carapace.BASE_URL + "forum/index.php?" + options;
	Connection con = Jsoup.connect(baseUrl)
		.userAgent(Carapace.USER_AGENT)
		.cookies(cookies.getCookies())
		.timeout(10000);
	Connection.Response resp = con.execute();
	if (resp.statusCode() == 200) {
	    Document get = con.get();
	    String title = get.title();
	    Log.info("carapace", "Got new topics page, title is " + title);
	    Element get1 = get.select(".cent tbody").get(1);
	    final Elements topics = get1.select("tr:gt(0)");
	    for (Element topic : topics) {
		Topic topic1 = parseNewTopic(topic);
		topicsList.add(topic1);
	    }
	    return new Status(StatusType.ONLINE);
	} else {
	    return new Status(StatusType.ERROR, "While trying to read new topics:" + String.valueOf(resp.statusCode()));
	}
    }

    public Status getUnreadTopics() throws IOException {
	return getNewTopics("act=new");
    }

    private Topic parseNewTopic(Element topic) throws NumberFormatException {
	Element mainElement = topic.select(".temtop2").get(0);
	Element body = mainElement.select("a").get(0);
	final String idStr = body.attributes().get("href");
	int id = Integer.parseInt(idStr.substring(idStr.indexOf("id") + 2));
	Element nameElement = body.select("b").get(0);
	String topicName = nameElement.text();
	Element lastPageElement = topic.select(".navpgtem").last();
	final String lastPageStr = lastPageElement.attributes().get("href");
	int lastPage = Integer.parseInt(lastPageStr.substring(lastPageStr.indexOf('-') + 1));

	final Element updatedElement = topic.select(".temtop small").get(0);
	final String updated = updatedElement.text();
	final Topic topic1 = new Topic(id, topicName, lastPage, updated);
	return topic1;
    }
}
