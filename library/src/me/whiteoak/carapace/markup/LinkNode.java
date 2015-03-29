package me.whiteoak.carapace.markup;

import java.util.LinkedList;

/**
 *
 * @author White Oak
 */
public class LinkNode extends Node {

    public LinkNode(TextNode text, String adress) {
	super(NodeType.LINK, getCol(text), adress);
    }


}
