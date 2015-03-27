package me.whiteoak.carapace;

import java.util.LinkedList;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

/**
 *
 * @author White Oak
 */
@RequiredArgsConstructor public class ForumsViewer {

    private final Cookies cookies;
    @Getter private LinkedList<Subforum> forumsList;

    public void getForums() {
	forumsList = new LinkedList<>();
	String baseUrl = String.format("%sforum/", Carapace.BASE_URL);
	Connection con = Jsoup.connect(baseUrl)
		.userAgent(Carapace.USER_AGENT)
		.cookies(cookies.getCookies())
		.timeout(10000);
    }
}
