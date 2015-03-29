package me.whiteoak.carapace.markup;

import java.util.Collection;

/**
 *
 * @author White Oak
 */
public final class Nodes {

    private final Collection<Node> nodes;

    public Nodes(Collection<Node> nodes) {
	this.nodes = nodes;
    }

    @Override
    public String toString() {
	return addNodes(nodes, 0).toString();
    }

    private CharSequence addNodes(Collection<Node> nodes, int tabs) {
	StringBuilder stringBuilder = new StringBuilder();
	for (Node node : nodes) {
	    for (int i = 0; i < tabs; i++) {
		stringBuilder.append('\t');
	    }
	    stringBuilder
		    .append(node.getType())
		    .append(": ");
	    if (node.getText() != null) {
		stringBuilder.append(node.getText());
	    }
	    if (node.getNodes() != null) {
		stringBuilder.append(System.lineSeparator());
		stringBuilder.append(addNodes(node.getNodes(), tabs + 1));
	    }
	    stringBuilder.append(System.lineSeparator());
	}
	return stringBuilder.delete(stringBuilder.length() - System.lineSeparator().length(), stringBuilder.length());
    }

    private CharSequence getTextOnly(Node node) {
	StringBuilder stringBuilder = new StringBuilder();
	if (node.getType() == NodeType.TEXT && node.getText() != null) {
	    stringBuilder.append(node.getText());
	}
	if (node.getNodes() != null) {
	    for (Node node1 : node.getNodes()) {
		stringBuilder.append(getTextOnly(node1));
	    }
	}
	return stringBuilder;
    }

    public Collection<Node> getNodes() {
	return nodes;
    }

    public CharSequence getTextOnly() {
	StringBuilder stringBuilder = new StringBuilder();
	for (Node node : nodes) {
	    stringBuilder.append(getTextOnly(node));
	}
	return stringBuilder;
    }

}
