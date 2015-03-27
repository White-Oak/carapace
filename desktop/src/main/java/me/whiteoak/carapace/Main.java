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
//	List<Topic> lastTopics = carapace.getLastTopics();
//	lastTopics.forEach(System.out::println);
	Topic pogodaTopic = new Topic(3280);
	pogodaTopic.setPostsToSkip(650);
	List<Post> topic = carapace.getTopic(pogodaTopic);
	for (Post topic1 : topic) {
	    System.out.println(topic1);
	}
//	carapace.writeToTopic(pogodaTopic, "Привет! Это ока! У нас тут погода прекрасная.");
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
