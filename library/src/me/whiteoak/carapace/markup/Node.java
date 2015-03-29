package me.whiteoak.carapace.markup;

import java.util.Collection;
import java.util.LinkedList;

/**
 *
 * @author White Oak
 */
public abstract class Node {

    public static LinkedList<Node> getCol(Node text) {
	LinkedList<Node> linkedList = new LinkedList<>();
	linkedList.add(text);
	return linkedList;
    }

    private final Collection<Node> nodes;
    private final NodeType type;
    private final String text;

    public Node(NodeType type, Collection<Node> nodes, String text) {
	this.nodes = nodes;
	this.type = type;
	this.text = text;
    }

    public Collection<Node> getNodes() {
	return nodes;
    }

    public NodeType getType() {
	return type;
    }

    public String getText() {
	return text;
    }

    @Override
    public String toString() {
	return getClass().getName() + "\n" + text;
    }
}
