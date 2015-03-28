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
	pagirovanie();
    }

    private static void pagirovanie() {
	Carapace carapace = new Carapace(Oak.OKA);
	carapace.authorize();
	Topic pogodaTopic = new Topic(3280);
	TopicHelper topicHelper = carapace.getTopicHelper();
	Topic nextPage;
	while ((nextPage = topicHelper.nextPage(pogodaTopic)) != null) {
	    pogodaTopic = nextPage;
	}
	System.out.println(pogodaTopic.getLastPagePost());
	System.out.println(pogodaTopic.getCurrentPost());
	List<Post> topicPosts = carapace.getTopicPosts(pogodaTopic);
	topicPosts.forEach(post -> System.out.println(post.getText()));
	while ((nextPage = topicHelper.previousPage(pogodaTopic)) != pogodaTopic) {
	    pogodaTopic = nextPage;
	}
	System.out.println(pogodaTopic.getLastPagePost());
	System.out.println(pogodaTopic.getCurrentPost());
	topicPosts = carapace.getTopicPosts(pogodaTopic);
	topicPosts.forEach(post -> System.out.println(post.getText()));
    }

    private static void getPogoda() {
	//	reemeekDemo();
	Carapace carapace = new Carapace(Oak.OKA);
	carapace.authorize();

	carapace = Carapace.applyCache(carapace.getCache());

	Topic pogodaTopic = new Topic(3280);
	List<Post> topicPosts = carapace.getTopicPosts(pogodaTopic);
	topicPosts.forEach(post -> System.out.print(post.getText()));
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
	    Status writeStatus = carapace.writeToTopic(pogodaTopic,
		    "Привет! Это снова ока! " + ((char) 30) + "У нас тут ветер дует сильно, и холодно стало :(");
	    System.out.println(writeStatus);
	}
    }
}
