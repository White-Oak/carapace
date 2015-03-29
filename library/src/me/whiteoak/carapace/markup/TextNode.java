package me.whiteoak.carapace.markup;

public class TextNode extends Node {

    public TextNode(String text) {
	super(NodeType.TEXT, null, text);
    }

}
