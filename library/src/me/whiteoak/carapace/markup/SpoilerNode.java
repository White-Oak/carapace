package me.whiteoak.carapace.markup;

import java.util.Collection;

/**
 *
 * @author White Oak
 */
public class SpoilerNode extends Node {

    public SpoilerNode(Collection<Node> nodes) {
	super(NodeType.SPOILER, nodes, null);
    }

}
