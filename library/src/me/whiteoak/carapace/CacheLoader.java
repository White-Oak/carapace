package me.whiteoak.carapace;

import com.esotericsoftware.minlog.Log;
import java.io.IOException;
import me.whiteoak.carapace.metadata.*;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author White Oak
 */
class CacheLoader {

    private static final String SHOULD_CONTAIN = "Мои настройки";
    private static final String SHOULD_CONTAIN_CACHE_VALID = "Личная анкета";

    public static Cache load(User user, Cookies cookies) throws IOException {
	final String baseUrl = String.format("%sstr/my_set.php", Carapace.BASE_URL);
	final Connection con = Jsoup.connect(baseUrl)
		.userAgent(Carapace.getUserAgent())
		.cookies(cookies.getCookies())
		.timeout(10000);
	final Connection.Response resp = con.execute();
	if (resp.statusCode() == 200) {
	    final Document get = resp.parse();
	    final String title = get.title();
	    Log.debug("carapace", "Got new settings page, title is " + title);
	    if (title.contains(SHOULD_CONTAIN)) {
		final Elements select = get.select("input[name=kmess]");
		if (select.size() > 0) {
		    final Element input = select.get(0);
		    final String valueString = input.attr("value");
		    final int pageLinesCount = valueString.length() > 0 ? Integer.parseInt(valueString) : 10;
		    final Settings settings = new Settings(pageLinesCount);
		    return new Cache(settings, user, cookies);
		}
	    }
	}
	return null;
    }

    public static boolean cacheValid(Cache cache) throws IOException {
	final String baseUrl = String.format("%sstr/anketa.php", Carapace.BASE_URL);
	final Connection con = Jsoup.connect(baseUrl)
		.userAgent(Carapace.getUserAgent())
		.cookies(cache.getCookies().getCookies())
		.timeout(10000);
	final Connection.Response resp = con.execute();
	if (resp.statusCode() == 200) {
	    final Document get = resp.parse();
	    final String title = get.title();
	    Log.debug("carapace", "Trying to check cache validness, title is " + title);
	    if (title.contains(SHOULD_CONTAIN_CACHE_VALID)) {
		return true;
	    }
	}
	return false;
    }
}
