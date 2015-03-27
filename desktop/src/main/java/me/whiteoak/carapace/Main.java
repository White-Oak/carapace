package me.whiteoak.carapace;

import java.util.List;

public class Main {

    /**
     * cArAPACE	Annimon jAva PAge CliEnt
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
	Carapace carapace = new Carapace(Oak.OKA);
	carapace.authorize();
	List<Topic> lastTopics = carapace.getLastTopics();
	for (Topic lastTopic : lastTopics) {
	    System.out.println(lastTopic);
	}
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
