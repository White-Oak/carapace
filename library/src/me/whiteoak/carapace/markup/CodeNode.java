package me.whiteoak.carapace.markup;

import java.util.Collection;

/**
 *
 * @author White Oak
 */
public class CodeNode extends Node {

    public CodeNode(Collection<Node> nodes) {
	super(NodeType.CODE, nodes, null);
    }

}
