package org.qame;

import com.fasterxml.jackson.databind.node.ObjectNode;

public abstract class Evaluator {
	public abstract ObjectNode evaluate( ObjectNode current );
}
