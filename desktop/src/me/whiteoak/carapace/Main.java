package me.whiteoak.carapace;

import java.util.*;
import me.whiteoak.carapace.metadata.*;

public class Main {

    /**
     * cArAPACE	Annimon jAva PAge CliEnt
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
//	reemeekDemo();
	Carapace carapace = new Carapace(Oak.OKA);

	Status authorizeStatus = carapace.authorize();
	System.out.println(authorizeStatus);
	if (authorizeStatus.getType() == StatusType.AUTHORIZED) {
	    List<Forum> forums = carapace.getForums();
	    forums.forEach(System.out::println);
	}

    }

    private static void reemeekDemo() {
	User user = new User(969, "SUPERSECRETPASSWORD");
	Carapace carapace = new Carapace(Oak.OKA);

	Status authorizeStatus = carapace.authorize();
	System.out.println(authorizeStatus);
	if (authorizeStatus.getType() == StatusType.AUTHORIZED) {
	    List<Topic> unreadTopics = carapace.getUnreadTopics();

	    final int index = new Random().nextInt(unreadTopics.size());
	    Topic topic = unreadTopics.get(index);
	    List<Post> topicPosts = carapace.getTopicPosts(topic);

	    topicPosts.forEach(System.out::println);
	}
    }

    public static void demo() {
	Carapace carapace = new Carapace(Oak.OKA);
	Status authorizeStatus = carapace.authorize();
	if (authorizeStatus.getType() == StatusType.AUTHORIZED) {
	    Topic pogodaTopic = new Topic(3280);
	    Status writeStatus = carapace.writeToTopic(pogodaTopic, "Привет! Это ока! У нас тут погода прекрасная.");
	    System.out.println(writeStatus);
	}
    }
}
