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
    private TopicViewer topicViewer;

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
     * Logs out. If no user is specified no actions are performed.
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

    public List<Topic> getLastTopics() {
	topicViewer = new TopicViewer();
	try {
	    lastStatus = topicViewer.getLastTopics(authorizator.getCookies());
	} catch (IOException ex) {
	    Log.error("carapace", "While trying to logout", ex);
	    lastStatus = new Status(StatusType.ERROR, ex.getMessage());
	}
	return topicViewer.getTopicsList();
    }

}
