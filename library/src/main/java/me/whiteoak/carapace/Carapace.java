package me.whiteoak.carapace;

import com.esotericsoftware.minlog.Log;
import java.io.IOException;
import java.util.*;
import lombok.*;

/**
 *
 * @author White Oak
 */
@RequiredArgsConstructor public class Carapace {

    @Getter @Setter @NonNull private User user;
    @Getter private Status lastStatus = new Status(StatusType.IDLE);
    private final Authorizator authorizator = new Authorizator();
    private TopicsPreviewer topicsPreviewer;
    private TopicsViewer topicsViewer;

    static final String BASE_URL = "http://annimon.com/";
//    static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.2; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/40.0.2214.93 YaBro";
    static final String USER_AGENT = "Carapace/0.1 (Windows NT 6.2; WOW64) JSoup/1.8.1 (Java HTML Parser)";

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
		lastStatus = authorizator.authorize(user);
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
     * Gets list of 20 new topics for authorized user. Works only if authorization was performed successfully.
     *
     * @return list of topics or null — if no authorization was performed.
     */
    public List<Topic> getLastTopics() {
	if (authorizator.getCookies() != null) {
	    topicsPreviewer = new TopicsPreviewer();
	    try {
		lastStatus = topicsPreviewer.getLastTopics(authorizator.getCookies());
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
    public List<Post> getTopic(Topic topic) {
	if (authorizator.getCookies() != null) {
	    topicsViewer = new TopicsViewer();
	    try {
		lastStatus = topicsViewer.loadPosts(topic, authorizator.getCookies());
	    } catch (IOException ex) {
		Log.error("carapace", "While trying to logout", ex);
		lastStatus = new Status(StatusType.ERROR, ex.getMessage());
	    }
	    return topicsViewer.getPostsList();
	}
	return null;
    }

    public Status writeToTopic(Topic topic, String message) {
	return lastStatus;
    }
}
