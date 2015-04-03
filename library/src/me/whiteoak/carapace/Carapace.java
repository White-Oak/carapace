package me.whiteoak.carapace;

import com.esotericsoftware.minlog.Log;
import java.io.IOException;
import java.util.*;
import me.whiteoak.carapace.metadata.*;

/**
 * Main class of a library. Provides access to the whole library.
 *
 * @author White Oak
 */
public class Carapace {

    private final User user;
    private Status lastStatus = new Status(StatusType.IDLE);
    private Cache cache;
    private TopicViewer topicViewer;
    private Authorizator authorizator;

    static final String BASE_URL = "http://annimon.com/";
//    static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.2; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/40.0.2214.93 YaBro";
    static final String USER_AGENT = "Carapace/0.3.1 JSoup/1.8.1 (Java HTML Parser)";
    /**
     * Added to basic Carapace user agent string. <br>
     *
     * Example: {@code
     * Carapace.additonalUserAgent = "Circle/0.1 (Carapace Desktop GUI)";
     * }<br>
     * UA for all connections since then will be "Carapace/0.2 JSoup/1.8.1 (Java HTML Parser) Circle/0.1 (Carapace Desktop GUI)"
     */
    public static String additonalUserAgent = "";

    static {
	Log.DEBUG();
    }

    public User getUser() {
	return user;
    }

    public Status getLastStatus() {
	return lastStatus;
    }

    public Cache getCache() {
	return cache;
    }

    public Carapace(User user) {
	this.user = user;
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
	    try {
		TopicsPreviewer topicsPreviewer = new TopicsPreviewer(cache);
		lastStatus = topicsPreviewer.getUnreadTopics();
		return topicsPreviewer.getTopicsList();
	    } catch (IOException ex) {
		Log.error("carapace", "While trying to logout", ex);
		lastStatus = new Status(StatusType.ERROR, ex.getMessage());
	    }
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
	    try {
		TopicsPreviewer topicsPreviewer = new TopicsPreviewer(cache);
		lastStatus = topicsPreviewer.getLastTopics();
		return topicsPreviewer.getTopicsList();
	    } catch (IOException ex) {
		Log.error("carapace", "While trying to logout", ex);
		lastStatus = new Status(StatusType.ERROR, ex.getMessage());
	    }
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
	    try {
		topicViewer = new TopicViewer(cache.getCookies());
		lastStatus = topicViewer.loadPosts(topic);
		return topicViewer.getPostsList();
	    } catch (IOException ex) {
		Log.error("carapace", "While trying to read a topic", ex);
		lastStatus = new Status(StatusType.ERROR, ex.getMessage());
	    }
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
	    try {
		TopicsWriter topicsWriter = new TopicsWriter(topic, cache.getCookies());
		lastStatus = topicsWriter.write(message);
	    } catch (IOException ex) {
		Log.error("carapace", "While trying to write a message", ex);
		lastStatus = new Status(StatusType.ERROR, ex.getMessage());
	    }
	}
	return lastStatus;
    }

    /**
     * Gets a full list of site's forums (and subforums).
     *
     * @return a list of forums.
     */
    public List<Forum> getForums() {
	if (cache.getCookies() != null) {
	    try {
		ForumsViewer forumsViewer = new ForumsViewer(cache.getCookies());
		lastStatus = forumsViewer.getAllForums();
		return forumsViewer.getForumsList();
	    } catch (IOException ex) {
		Log.error("carapace", "While trying to read all forums", ex);
		lastStatus = new Status(StatusType.ERROR, ex.getMessage());
	    }
	}
	return null;
    }

    static String getUserAgent() {
	return USER_AGENT + (additonalUserAgent.length() == 0 ? "" : (" " + additonalUserAgent));
    }

    /**
     * Authorizes through previously saved {@link Cache} object via getCache(). <br>
     * If cookies expired then the method tries to authorize once again with stored user data in the given cache. <br>
     * If every authorization method fails the method returns null and logs an error message.
     *
     * @param cache previously saved cache object.
     * @return authorized Carapace instance or null if failed to authorized.
     */
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
		return carapace;
	    }
	} catch (IOException ex) {
	    Log.error("carapace", "While trying to apply cache", ex);
	}
	return null;
    }

    public TopicHelper getNewTopicHelper() {
	return new TopicHelper(cache);
    }
}
