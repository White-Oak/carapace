package me.whiteoak.carapace.markup;

/**
 *
 * @author White Oak
 */
public class StyleNode extends Node {

    public StyleNode(TextNode node, String style) {
	super(NodeType.STYLE, getCol(node), style);
    }

}
