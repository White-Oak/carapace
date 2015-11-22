package me.whiteoak.carapace;

import com.esotericsoftware.minlog.Log;
import java.io.IOException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class TopicHelper {
    //660 | 650 + 10 =   
    //mathcode indiecode posthardcode melodic hardcode 
    //Topic nextPageTopic = carapace.nextPage(topic)
    //TopicHelper th = carapace.getTopicHelper();
    //Topic nextPageTopic = th.nextPage(topic)

    @NonNull private final Cache cache;

    Topic getPagesInfo(@NonNull Topic topic) throws IOException {
	String baseUrl = String.format("%sforum/id%d",
		Carapace.BASE_URL, topic.getId());
	Connection con = Jsoup.connect(baseUrl)
		.userAgent(Carapace.getUserAgent())
		.cookies(cache.getCookies().getCookies())
		.timeout(10000);
	Connection.Response resp = con.execute();
	if (resp.statusCode() == 200) {
	    Document get = resp.parse();
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
    public Topic nextPage(@NonNull Topic topic) {
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
	return null;
    }

    /**
     * Gets a new topic pointing at a previous page, if there is one.
     *
     * @param topic a topic
     * @return new topic or the given topic if there are no previous pages
     */
    public Topic previousPage(@NonNull Topic topic) {
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

    /**
     * Gets page number for a given topic object.
     *
     * @param topic a topic
     * @return page number
     */
    public int getPageNumber(@NonNull Topic topic) {
	int currentPost = topic.getCurrentPost();
	return currentPost / cache.getSettings().getPageLinesCount();
    }

    /**
     * Checks if the given topic is at a last page.
     *
     * @param topic topic
     * @return true if at last page, false — otherwise
     */
    public boolean isLastPage(@NonNull Topic topic) {
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
    public boolean isFirstPage(@NonNull Topic topic) {
	int currentPost = topic.getCurrentPost();
	return currentPost == 0;
    }

    /**
     * Gets a new topic pointing at a first page.
     *
     * @param topic a topic
     * @return new topic
     */
    public Topic firstPage(@NonNull Topic topic) {
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
    public Topic lastPage(@NonNull Topic topic) {
	if (isLastPage(topic)) {
	    return topic;
	}
	while (!isLastPage(topic = nextPage(topic)));
	return topic;
    }

    /**
     * Sets a starting post for a given topic. Starting post is floored down to the closest (pageLinesCount * x), where x is a page number.
     *
     * @param topic a topic
     * @param startingPost desired post to start a page with
     * @return a new topic
     */
    public Topic setStartingPost(@NonNull Topic topic, int startingPost) {
	return setPage(topic, startingPost / cache.getSettings().getPageLinesCount());
    }

    /**
     * Sets a page for a given topic. No checks are made.
     *
     * @param topic a topic
     * @param page a desired page to start with
     * @return a new topic
     */
    public Topic setPage(@NonNull Topic topic, int page) {
	return new Topic(topic.getId(), topic.getName(),
		page * cache.getSettings().getPageLinesCount(),
		topic.getLastPagePost(), topic.getUpdated());
    }
}
