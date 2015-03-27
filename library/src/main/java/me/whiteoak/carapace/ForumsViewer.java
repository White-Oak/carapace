package me.whiteoak.carapace;

import com.esotericsoftware.minlog.Log;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author White Oak
 */
@RequiredArgsConstructor public class ForumsViewer {

    private final Cookies cookies;
    @Getter private List<Forum> forumsList;

    public Status getAllForums() throws IOException {
	forumsList = new LinkedList<>();
	String baseUrl = String.format("%sforum/", Carapace.BASE_URL);
	Connection con = Jsoup.connect(baseUrl)
		.userAgent(Carapace.USER_AGENT)
		.cookies(cookies.getCookies())
		.timeout(10000);
	Connection.Response resp = con.execute();
	if (resp.statusCode() == 200) {
	    Document get = con.get();
	    String title = get.title();
	    Log.debug("carapace", "Got new index forum page, title is " + title);
	    final Elements forums = get.select(".forums");
	    List<Subforum> subForumsList = new LinkedList<>();
	    for (Element forum : forums) {
		Elements nameNext = forum.select(".frazd a");
		String name = nameNext.text();
		Elements subforums = forum.select("table tr:gt(0)");
		for (Element subforum : subforums) {
		    final Element subForumNameNext = subforum.select(".razfor:gt(0) a").get(0);
		    final String subForumName = subForumNameNext.text();
		    String temp = subForumNameNext.attr("href");
		    final String subForumId = temp.substring(temp.indexOf("id") + 2);

		    final Element topicsCountNext = subforum.select(".razfor2").get(0);
		    final int topicsCount = Integer.parseInt(topicsCountNext.text());

		    final Element updatedTopicNext = subforum.select(".razfor3").get(0);
		    final Element updatedNext = updatedTopicNext.select("a").get(0);

		    final String lastPageStr = updatedNext.attr("href");
		    final int indexOf = lastPageStr.indexOf('-');
		    final int topicId = Integer.parseInt(lastPageStr.substring(lastPageStr.indexOf("id") + 2));
		    int lastPage = indexOf < 0 ? 0 : Integer.parseInt(lastPageStr.substring(indexOf + 1));

		    final String topicName = updatedNext.text();
		    Topic topic = new Topic(topicId, topicName, lastPage, null);
		    Subforum subForum = new Subforum(subForumName, topicsCount, topic);
		    subForumsList.add(subForum);
		}

	    }
	    return new Status(StatusType.ONLINE);
	} else {
	    return new Status(StatusType.ERROR, "While trying to read new topics:" + String.valueOf(resp.statusCode()));
	}
    }
}
