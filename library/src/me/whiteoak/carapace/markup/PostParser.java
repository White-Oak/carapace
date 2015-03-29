package me.whiteoak.carapace.markup;

import java.util.LinkedList;
import java.util.List;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author White Oak
 */
public class PostParser {

    public static Nodes parsePostText(Element post) {
	List<org.jsoup.nodes.Node> childNodes = post.childNodes();
	List<Node> nodes = new LinkedList<>();
	for (org.jsoup.nodes.Node childNode : childNodes) {
	    final Node parseNode = parseNode(childNode);
	    if (parseNode != null) {
		nodes.add(parseNode);
	    }
	}
	return new Nodes(nodes);
    }

    private static Node parseNode(org.jsoup.nodes.Node node) {
	if (node instanceof Element) {
	    Element element = (Element) node;
	    String tagName = element.tagName();
	    switch (tagName) {
		case "span": {
		    String classs = element.attr("class");
		    switch (classs) {
			case "quote":
			    return parseQuoteElement(element);
			case "spoiler":
			    //@working name of spoiler
			    List<Node> nodes = parseSpoilerElement(element);
			    return new SpoilerNode(nodes);
			default:
			    String style = element.attr("style");
			    if (style.contains("bold")) {
				return new StyleNode(new TextNode(element.text()), "bold");
			    } else if (style.contains("underlined")) {
				return new StyleNode(new TextNode(element.text()), "underlined");
			    } else if (style.contains("line-through")) {
				return new StyleNode(new TextNode(element.text()), "line-through");
			    } else if (style.contains("italic")) {
				return new StyleNode(new TextNode(element.text()), "italic");
			    } else if (style.contains("color")) {
				return new StyleNode(new TextNode(element.text()), "color: " + style.substring(6));
			    } else if (style.contains("background")) {
				return new StyleNode(new TextNode(element.text()), "background: " + style.substring(11, 18));
			    } else {
				return new TextNode(element.html());
			    }
		    }
		}
		case "br":
		    return new LineFeedNode();
		case "div": {
		    String classs = element.attr("class");
		    switch (classs) {
			case "backcode":
			    return parseCodeElement(element);
			default:
			    return new TextNode(element.html());
		    }
		}
		case "img": {
		    String alt = element.attr("alt");
		    return new SmileNode(alt);
		}
		case "script":
		    return null;
		case "a": {
		    String href = element.attr("href");
		    if (href.contains("javascript")) {
			return null;
		    }
		    return new LinkNode(new TextNode(element.text()), href);
		}
		default:
		    return new TextNode(element.html());
	    }
	} else if (node instanceof org.jsoup.nodes.TextNode) {
	    org.jsoup.nodes.TextNode textNode = (org.jsoup.nodes.TextNode) node;
	    return (new TextNode(textNode.text()));
	} else {
	    return new TextNode(node.toString());
	}
    }

    private static List<Node> parseSpoilerElement(Element element) {
	List<org.jsoup.nodes.Node> childNodes = element.childNodes();
	List<Node> nodes = new LinkedList<>();
	for (org.jsoup.nodes.Node childNode : childNodes) {
	    nodes.add(parseNode(childNode));
	}
	return nodes;
    }

    private static QuoteNode parseQuoteElement(Element element) {
	List<org.jsoup.nodes.Node> childNodes = element.childNodes();
	List<Node> nodes = new LinkedList<>();

	int start = 0;
	final org.jsoup.nodes.Node get = childNodes.get(0);
	String author = null;
	if (get instanceof Element) {
	    final Element name = (Element) get;
	    if (name.tagName().equals("small")) {
		author = (name.getAllElements().get(0)).text();
		author = author.substring(author.indexOf("Цитата") + 7, author.length() - 1);
		start = 2;
	    }
	}
	for (int i = start; i < childNodes.size(); i++) {
	    org.jsoup.nodes.Node node = childNodes.get(i);
	    nodes.add(parseNode(node));
	}
	nodes.add(new LineFeedNode());
	return new QuoteNode(nodes, author);
    }

    private static CodeNode parseCodeElement(Element element) {
	Elements select = element.select("li");
	List<Node> nodes = new LinkedList<>();
	for (Element li : select) {
	    String text = li.select("div").get(0).text();
	    nodes.add(new TextNode(text));
	    nodes.add(new LineFeedNode());
	}
	return new CodeNode(nodes);
    }
}
