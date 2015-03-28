package me.whiteoak.carapace.grabber;

import com.mongodb.MongoClient;
import com.mongodb.client.*;
import java.util.List;
import me.whiteoak.carapace.*;
import me.whiteoak.carapace.metadata.Post;
import me.whiteoak.carapace.metadata.Topic;
import org.bson.Document;

public class Main {

    /**
     * cArAPACE	Annimon jAva PAge CliEnt
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) throws InterruptedException {
	MongoClient mongoClient = new MongoClient();
	MongoDatabase database = mongoClient.getDatabase("forum");
	MongoCollection<Document> collection = database.getCollection("posts");
	FindIterable<Document> find = collection.find();
	for (Document find1 : find) {
	    System.out.println(find1);
	}
    }

    private static void parsePogoda() throws InterruptedException {
	Carapace.additonalUserAgent = " Crawler/0.1 (Forum grabber)";
	Carapace carapace = new Carapace(Oak.OKA);
	carapace.authorize();

	MongoClient mongoClient = new MongoClient();
	MongoDatabase database = mongoClient.getDatabase("forum");
	MongoCollection<Document> collection = database.getCollection("posts");
	collection.drop();
//	Topic topic = new Topic(175734);
	Topic topic = new Topic(3280);
	TopicHelper th = carapace.getNewTopicHelper();
	do {
	    List<Post> topicPosts = carapace.getTopicPosts(topic);
	    topicPosts.stream().map(post -> new Document()
		    .append("id", post.getId())
		    .append("date", post.getDate())
		    .append("username", post.getUsername())
		    .append("nickname", post.getNickname())
		    .append("status", post.getStatus())
		    .append("text", post.getText()))
		    .forEach(collection::insertOne);
	    System.out.println(collection.count());
	    Thread.sleep(200L);
	} while ((topic = th.nextPage(topic)) != null);
    }

}
