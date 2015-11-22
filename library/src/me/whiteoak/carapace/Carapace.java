package me.whiteoak.carapace;

import com.esotericsoftware.minlog.Log;
import java.io.IOException;
import java.util.List;
import lombok.*;
import me.whiteoak.carapace.exceptions.CarapaceException;
import me.whiteoak.carapace.metadata.*;

/**
 * Main class of a library. Provides access to the whole library.
 *
 * @author White Oak
 */
public class Carapace {

    static final String BASE_URL = "http://annimon.com/";
    static final String USER_AGENT = "Carapace/0.3.2 JSoup/1.8.1 (Java HTML Parser)";
    /**
     * Added to basic Carapace user agent string. <br>
     *
     * Example: {@code
     * Carapace.additonalUserAgent = "Circle/0.1 (Carapace Desktop GUI)";
     * }<br>
     * UA for all connections since then will be "Carapace/0.2 JSoup/1.8.1 (Java HTML Parser) Circle/0.1 (Carapace Desktop GUI)"
     */
    public static String additonalUserAgent = "";
    @Getter(lazy = true, value = AccessLevel.PACKAGE) private static final String userAgent = concatUAs();

    @Getter private final User user;
    @Getter private Cache cache;
    private TopicViewer topicViewer;
    private Authorizator authorizator;

    private static String concatUAs() {
	if (additonalUserAgent == null) {
	    additonalUserAgent = "";
	}
	return USER_AGENT + (additonalUserAgent.length() == 0 ? "" : (" " + additonalUserAgent.trim()));
    }

    /**
     * Authorizes through previously saved {@link Cache} object via getCache(). <br>
     * If cookies expired then the method tries to authorize once again with stored user data in the given cache. <br>
     * If every authorization method fails the method returns null and logs an error message.
     *
     * @param cache previously saved cache object.
     * @return authorized Carapace instance or null if failed to authorized.
     */
    public static Carapace applyCache(@NonNull Cache cache) {
	if (cache.getCookies() == null || cache.getUser() == null || cache.getSettings() == null) {
	    IllegalArgumentException ex = new IllegalArgumentException("Cache is incomplete");
	    Log.error("carapace", "While trying to apply cache", ex);
	    throw ex;
	} else {
	    try {
		Carapace carapace = new Carapace(cache.getUser());
		if (CacheLoader.cacheValid(cache)) {
		    carapace.cache = cache;
		    return carapace;
		} else {
		    Log.info("carapace", "The given cache was invalid.");
		    Log.info("carapace", "But that's OK.");
		    carapace.authorize();
		    return carapace;
		}
	    } catch (IOException ex) {
		Log.error("carapace", "While trying to apply cache", ex);
	    }
	}
	return null;
    }

    static {
	Log.DEBUG();
    }

    public Carapace(User user) {
	this.user = user;
    }

    public boolean authorized() {
	return authorizator != null;
    }

    /**
     * Authorizes using current {@link User}. If no user is specified no actions are performed.
     *
     * @return a status of the performed operation or the last status if no actions are performed.
     * @see User
     * @see Status
     */
    public void authorize() {
	if (user != null) {
	    try {
		authorizator = new Authorizator();
		authorizator.authorize(user);
		cache = CacheLoader.load(user, authorizator.getCookies());
		if (cache == null) {
		    authorizator = null;
		    throw new CarapaceException("Can't load settings");
		}
	    } catch (IOException ex) {
		Log.error("carapace", "While trying to authorize", ex);
		throw new CarapaceException("While trying to authorize", ex);
	    }
	}
    }

    /**
     * Logs out. If no user is specified no actions are performed. <br>
     * Doesn't actually log out as there is no log out feature on annimon.com.
     *
     * @see User
     * @see Status
     */
    public void logout() {
	if (user != null) {
	    try {
		authorizator.logout();
		authorizator = null;
	    } catch (IOException ex) {
		Log.error("carapace", "While trying to logout", ex);
		throw new CarapaceException("While trying to logout", ex);
	    }
	}
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
		return topicsPreviewer.getUnreadTopics();
	    } catch (IOException ex) {
		Log.error("carapace", "While trying to read topics", ex);
		throw new CarapaceException("While trying to read topics", ex);
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
		return topicsPreviewer.getLastTopics();
	    } catch (IOException ex) {
		Log.error("carapace", "While trying to read topics", ex);
		throw new CarapaceException("While trying to read topics", ex);
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
		return topicViewer.loadPosts(topic);
	    } catch (IOException ex) {
		Log.error("carapace", "While trying to read topics", ex);
		throw new CarapaceException("While trying to read topics", ex);
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
    public void writeToTopic(Topic topic, String message) {
	if (cache.getCookies() != null) {
	    try {
		TopicsWriter topicsWriter = new TopicsWriter(topic, cache.getCookies());
		topicsWriter.write(message);
	    } catch (IOException ex) {
		Log.error("carapace", "While trying to write a message", ex);
	    }
	}
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
		return forumsViewer.getAllForums();
	    } catch (IOException ex) {
		Log.error("carapace", "While trying to read all forums", ex);
	    }
	}
	return null;
    }

    public TopicHelper getNewTopicHelper() {
	return new TopicHelper(cache);
    }
}
