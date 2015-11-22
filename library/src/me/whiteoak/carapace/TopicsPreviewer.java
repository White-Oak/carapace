package me.whiteoak.carapace;

import com.esotericsoftware.minlog.Log;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import me.whiteoak.carapace.exceptions.CarapaceException;
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
class TopicsPreviewer {

    private final Cache cache;

    public List<Topic> getLastTopics() throws IOException {
	return getNewTopics("act=new&period");
    }

    private List<Topic> getNewTopics(String options) throws IOException {
	LinkedList<Topic> topicsList = new LinkedList<>();
	String baseUrl = Carapace.BASE_URL + "forum/index.php?" + options;
	Connection con = Jsoup.connect(baseUrl)
		.userAgent(Carapace.getUserAgent())
		.cookies(cache.getCookies().getCookies())
		.timeout(10000);
	Connection.Response resp = con.execute();
	if (resp.statusCode() == 200) {
	    Document get = resp.parse();
	    String title = get.title();
	    Log.debug("carapace", "Got new topics page, title is " + title);
	    Element get1 = get.select(".cent tbody").get(1);
	    final Elements topics = get1.select("tr:gt(0)");
	    for (Element topic : topics) {
		Topic topic1 = parseNewTopic(topic);
		topicsList.add(topic1);
	    }
	    return topicsList;
	} else {
	    throw new CarapaceException("While trying to read new topics:" + resp.statusCode());
	}
    }

    public List<Topic> getUnreadTopics() throws IOException {
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
	final int indexOf = lastPageStr.indexOf('-');
	int lastPageStartingPost = indexOf < 0 ? 0 : Integer.parseInt(lastPageStr.substring(indexOf + 1));

	final Element updatedElement = topic.select(".temtop small").get(0);
	final String updated = updatedElement.text();
	final Topic topic1 = new Topic(id, topicName, lastPageStartingPost, lastPageStartingPost, updated);
	return topic1;
    }
}
