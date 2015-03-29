package me.whiteoak.carapace.markup;

import java.util.Collection;

public class QuoteNode extends Node {

    public QuoteNode(Collection<Node> nodes, String author) {
	super(NodeType.QUOTE, nodes, author);
    }

}
