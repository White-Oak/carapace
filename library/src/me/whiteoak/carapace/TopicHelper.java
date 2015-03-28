package me.whiteoak.carapace;

import com.esotericsoftware.minlog.Log;
import java.io.IOException;
import me.whiteoak.carapace.metadata.Topic;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Provides methods for paging topics.
 *
 * @author White Oak
 */
public class TopicHelper {
    //660 | 650 + 10 =   
    //mathcode indiecode posthardcode melodic hardcode 
    //Topic nextPageTopic = carapace.nextPage(topic)
    //TopicHelper th = carapace.getTopicHelper();
    //Topic nextPageTopic = th.nextPage(topic)

    private final Cache cache;

    TopicHelper(Cache cache) {
	this.cache = cache;
    }

    Topic getPagesInfo(Topic topic) throws IOException {
	String baseUrl = String.format("%sforum/id%d",
		Carapace.BASE_URL, topic.getId());
	Connection con = Jsoup.connect(baseUrl)
		.userAgent(Carapace.getUserAgent())
		.cookies(cache.getCookies().getCookies())
		.timeout(10000);
	Connection.Response resp = con.execute();
	if (resp.statusCode() == 200) {
	    Document get = con.get();
	    String title = get.title();
	    Log.debug("carapace", "Got another topic page, title is " + title);

	    final Elements postsData = get.select(".navpg");
	    if (postsData.isEmpty()) {
		return new Topic(topic.getId(), topic.getName(), topic.getCurrentPost(), 0, topic.getUpdated());
	    }

	    Element lastElement = null;
	    for (Element postData : postsData) {
		String text = postData.text();
		if (text.equals(">>")) {
		    String lastPageStr = lastElement.attr("href");
		    final int indexOf = lastPageStr.indexOf('-');
		    int lastPageStartingPost = indexOf < 0 ? 0 : Integer.parseInt(lastPageStr.substring(indexOf + 1));
		    return new Topic(topic.getId(), topic.getName(), topic.getCurrentPost(), lastPageStartingPost, topic.getUpdated());
		}
		lastElement = postData;
	    }
	}
	return null;
    }

    /**
     * Gets a new topic pointing at a next page, if there is one.
     *
     * @param topic a topic
     * @return new topic or null if there are no next pages
     */
    public Topic nextPage(Topic topic) {
	if (topic == null) {
	    return null;
	}
	if (cache != null) {
	    int currentPost = topic.getCurrentPost();
	    int lastPagePost = topic.getLastPagePost();
	    final int pageLinesCount = cache.getSettings().getPageLinesCount();
	    if (lastPagePost < 0) {
		try {
		    topic = getPagesInfo(topic);
		    lastPagePost = topic.getLastPagePost();
		} catch (IOException ex) {
		    Log.error("carapace", "While trying to get topic page info", ex);
		}
	    }
	    if (!isLastPage(topic)) {
		currentPost += pageLinesCount;
		return new Topic(topic.getId(), topic.getName(), currentPost, lastPagePost, topic.getUpdated());
	    }
	}
	return null;
    }

    /**
     * Gets a new topic pointing at a previous page, if there is one.
     *
     * @param topic a topic
     * @return new topic or the given topic if there are no previous pages
     */
    public Topic previousPage(Topic topic) {
	if (topic == null) {
	    return null;
	}
	if (cache != null) {
	    int currentPost = topic.getCurrentPost();
	    if (isFirstPage(topic)) {
		return null;
	    }
	    final int pageLinesCount = cache.getSettings().getPageLinesCount();
	    currentPost -= pageLinesCount;
	    if (currentPost < 0) {
		currentPost = 0;
	    }
	    return new Topic(topic.getId(), topic.getName(), currentPost, topic.getLastPagePost(), topic.getUpdated());
	}
	return null;
    }

    /**
     * Gets page number for a given topic object.
     *
     * @param topic a topic
     * @return page number
     */
    public int getPageNumber(Topic topic) {
	if (topic == null) {
	    return -1;
	}
	if (cache != null) {
	    int currentPost = topic.getCurrentPost();
	    return currentPost / cache.getSettings().getPageLinesCount();
	}
	return -1;
    }

    /**
     * Checks if the given topic is at a last page.
     *
     * @param topic topic
     * @return true if at last page, false — otherwise
     */
    public boolean isLastPage(Topic topic) {
	if (topic == null) {
	    return false;
	}
	int currentPost = topic.getCurrentPost();
	int lastPagePost = topic.getLastPagePost();
	if (lastPagePost < 0) {
	    try {
		topic = getPagesInfo(topic);
		lastPagePost = topic.getLastPagePost();
	    } catch (IOException ex) {
		Log.error("carapace", "While trying to get topic page info", ex);
	    }
	}
	final int pageLinesCount = cache.getSettings().getPageLinesCount();
	return lastPagePost < currentPost + pageLinesCount;
    }

    /**
     * Checks if the given topic is at a starting page.
     *
     * @param topic topic
     * @return true if at starting page, false — otherwise
     */
    public boolean isFirstPage(Topic topic) {
	if (topic == null) {
	    return false;
	}
	int currentPost = topic.getCurrentPost();
	return currentPost == 0;
    }

    /**
     * Gets a new topic pointing at a first page.
     *
     * @param topic a topic
     * @return new topic
     */
    public Topic firstPage(Topic topic) {
	if (topic == null) {
	    return null;
	}
	if (isFirstPage(topic)) {
	    return topic;
	}
	while (!isFirstPage(topic = previousPage(topic)));
	return topic;
    }

    /**
     * Gets a new topic pointing at a last page.
     *
     * @param topic a topic
     * @return new topic
     */
    public Topic lastPage(Topic topic) {
	if (topic == null) {
	    return null;
	}
	if (isLastPage(topic)) {
	    return topic;
	}
	while (!isLastPage(topic = nextPage(topic)));
	return topic;
    }
}
