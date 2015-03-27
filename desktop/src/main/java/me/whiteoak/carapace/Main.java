package me.whiteoak.carapace;

import java.util.*;

public class Main {

    /**
     * cArAPACE	Annimon jAva PAge CliEnt
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
	User user = new User(969, "SUPERSECRETPASSWORD");
	Carapace carapace = new Carapace(user);

	Status authorizeStatus = carapace.authorize();
	System.out.println(authorizeStatus);
	if (authorizeStatus.getType() == StatusType.AUTHORIZED) {
	    List<Topic> unreadTopics = carapace.getUnreadTopics();

	    Topic topic = unreadTopics.get(new Random().nextInt(unreadTopics.size()));
	    carapace.getTopicPosts(topic);
	}
//	List<Topic> lastTopics = carapace.getLastTopics();
//	lastTopics.toArray(new Topic[]{});

//	Topic topic = lastTopics.get(0);
//	Topic pogodaTopic = new Topic(3280);
//	pogodaTopic.setPostsToSkip(650);
//	List<Post> list = carapace.getTopicPosts(pogodaTopic);
//	for (Post topic1 : list) {
//	    System.out.println(topic1);
//	}
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
