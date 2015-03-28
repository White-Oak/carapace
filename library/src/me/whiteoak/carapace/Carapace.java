package me.whiteoak.carapace;

import com.esotericsoftware.minlog.Log;
import java.io.IOException;
import java.util.*;
import lombok.*;
import me.whiteoak.carapace.metadata.*;

/**
 * Main class of a library. Provides access to the whole library.
 *
 * @author White Oak
 */
@RequiredArgsConstructor public class Carapace {

    @Getter private final User user;
    @Getter private Status lastStatus = new Status(StatusType.IDLE);
    @Getter private Cache cache;
    private TopicViewer topicViewer;
    private Authorizator authorizator;

    static final String BASE_URL = "http://annimon.com/";
//    static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.2; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/40.0.2214.93 YaBro";
    static final String USER_AGENT = "Carapace/0.1 JSoup/1.8.1 (Java HTML Parser)";
    public static String additonalUserAgent = "";

    static {
	Log.DEBUG();
    }

    /**
     * Authorizes using current {@link User}. If no user is specified no actions are performed.
     *
     * @return a status of the performed operation or the last status if no actions are performed.
     * @see User
     * @see Status
     */
    public Status authorize() {
	if (user != null) {
	    try {
		authorizator = new Authorizator();
		lastStatus = authorizator.authorize(user);
		cache = CacheLoader.load(user, authorizator.getCookies());
		if (cache == null) {
		    lastStatus = new Status(StatusType.ERROR, "Can't load settings.");
		    authorizator = null;
		}
	    } catch (IOException ex) {
		Log.error("carapace", "While trying to authorize", ex);
		lastStatus = new Status(StatusType.ERROR, ex.getMessage());
	    }
	}
	return lastStatus;
    }

    /**
     * Logs out. If no user is specified no actions are performed. <br>
     * Doesn't actually log out as there is no log out feature on annimon.com.
     *
     * @return a status of the performed operation or the last status if no actions are performed.
     * @see User
     * @see Status
     */
    public Status logout() {
	if (user != null) {
	    try {
		lastStatus = authorizator.logout();
	    } catch (IOException ex) {
		Log.error("carapace", "While trying to logout", ex);
		lastStatus = new Status(StatusType.ERROR, ex.getMessage());
	    }
	}
	return lastStatus;
    }

    /**
     * Gets list of unread topics for authorized user. Works only if authorization was performed successfully.
     *
     * @return list of topics or null — if no authorization was performed.
     */
    public List<Topic> getUnreadTopics() {
	if (cache.getCookies() != null) {
	    TopicsPreviewer topicsPreviewer = new TopicsPreviewer(authorizator.getCookies());
	    try {
		lastStatus = topicsPreviewer.getUnreadTopics();
	    } catch (IOException ex) {
		Log.error("carapace", "While trying to logout", ex);
		lastStatus = new Status(StatusType.ERROR, ex.getMessage());
	    }
	    return topicsPreviewer.getTopicsList();
	}
	return null;
    }

    /**
     * Gets list of last topics for authorized user. Works only if authorization was performed successfully.
     *
     * @return list of topics or null — if no authorization was performed.
     */
    public List<Topic> getLastTopics() {
	if (cache.getCookies() != null) {
	    TopicsPreviewer topicsPreviewer = new TopicsPreviewer(cache.getCookies());
	    try {
		lastStatus = topicsPreviewer.getLastTopics();
	    } catch (IOException ex) {
		Log.error("carapace", "While trying to logout", ex);
		lastStatus = new Status(StatusType.ERROR, ex.getMessage());
	    }
	    return topicsPreviewer.getTopicsList();
	}
	return null;
    }

    /**
     * Gets a most appropriate data for given topic metadata.
     *
     * @param topic a metadata for a topic to be loaded
     * @return a list of posts
     */
    public List<Post> getTopicPosts(Topic topic) {
	if (cache.getCookies() != null) {
	    topicViewer = new TopicViewer(cache.getCookies());
	    try {
		lastStatus = topicViewer.loadPosts(topic);
	    } catch (IOException ex) {
		Log.error("carapace", "While trying to read a topic", ex);
		lastStatus = new Status(StatusType.ERROR, ex.getMessage());
	    }
	    return topicViewer.getPostsList();
	}
	return null;
    }

    /**
     * Writes a message to a given topic. Message is sent as-is.
     *
     * @param topic topic to be sent to.
     * @param message message to be sent.
     * @return a status of the performed operation or the last status if no actions are performed.
     */
    public Status writeToTopic(Topic topic, String message) {
	if (cache.getCookies() != null) {
	    TopicsWriter topicsWriter = new TopicsWriter(topic, cache.getCookies());
	    try {
		lastStatus = topicsWriter.write(message);
	    } catch (IOException ex) {
		Log.error("carapace", "While trying to write a message", ex);
		lastStatus = new Status(StatusType.ERROR, ex.getMessage());
	    }
	}
	return lastStatus;
    }

    public List<Forum> getForums() {
	if (cache.getCookies() != null) {
	    ForumsViewer forumsViewer = new ForumsViewer(cache.getCookies());
	    try {
		lastStatus = forumsViewer.getAllForums();
	    } catch (IOException ex) {
		Log.error("carapace", "While trying to read all forums", ex);
		lastStatus = new Status(StatusType.ERROR, ex.getMessage());
	    }
	    return forumsViewer.getForumsList();
	}
	return null;
    }

    static String getUserAgent() {
	return USER_AGENT + (additonalUserAgent.length() == 0 ? "" : (" " + additonalUserAgent));
    }

    public static Carapace applyCache(Cache cache) {
	try {
	    Carapace carapace = new Carapace(cache.getUser());
	    if (CacheLoader.cacheValid(cache)) {
		carapace.cache = cache;
		return carapace;
	    } else {
		Log.info("carapace", "The given cache was invalid.");
		Log.info("carapace", "But that's OK.");
		Status authorize = carapace.authorize();
		if (authorize.getType() != StatusType.AUTHORIZED) {
		    Log.error("carapace", "While trying to authorize", new RuntimeException(authorize.getMessage().toString()));
		    return null;
		}
	    }
	} catch (IOException ex) {
	    Log.error("carapace", "While trying to apply cache", ex);
	}
	return null;
    }
}
