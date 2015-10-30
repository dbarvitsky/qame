package org.qame.impl.jackson;

import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;

import org.qame.Checkpoint;
import org.qame.Scenario;
import org.qame.impl.ReflectedCheckpointFactory;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonTokenId;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

public class ReflectedCheckpointDeserializer<S extends Scenario> extends JsonDeserializer<Checkpoint<S>> {
	@Override
	public Checkpoint<S> deserialize(JsonParser p, DeserializationContext ctxt)
			throws IOException, JsonProcessingException {

		if (p.getCurrentTokenId() != JsonTokenId.ID_FIELD_NAME) 
			throw ctxt.mappingException("Syntax error - expected \"function\" field");
		if (!"function".equals(p.getCurrentName()))
			throw ctxt.mappingException("Syntax error - expected \"function\" field");
		
		p.nextToken();
		String expr = p.getText();
		p.nextToken();

		TreeNode node = p.readValueAsTree();
		int nodeSize = 0;
		Iterator<JsonNode> jsIterator = null;
		
		if (p.getCurrentTokenId() == JsonTokenId.ID_FIELD_NAME)
		{
			 if (!"args".equals(p.getCurrentName()))
				throw ctxt.mappingException("Syntax error - expected \"args\" field");
			p.nextToken();

			if (node.isArray()) {
				nodeSize = node.size();
				jsIterator = ((ArrayNode)node).iterator();
			} else {
				nodeSize = 1;
				jsIterator = Arrays.asList( new JsonNode[] { (JsonNode) node }).iterator();
			}
		} else {
			nodeSize = 0;
			jsIterator = Arrays.asList( new JsonNode[]{}).iterator();
		}
		
		try {
			ReflectedCheckpointFactory factory = 
					new ReflectedCheckpointFactory()
					.setExpression(expr, nodeSize );
			Class<?>[] types = factory.getArgTypes();
			Object[] args = new Object[types.length];
			int i = 0;
			while ( jsIterator.hasNext()) {
				args[i] = p.getCodec().treeToValue(jsIterator.next(), types[i]);
				i++;
			}
			factory.setArgs(args);
			return factory.createCheckpoint();
		} catch (ReflectiveOperationException e) {
			throw ctxt.mappingException("Cannot deserialize");
		} finally {
			if (p.getCurrentTokenId() == JsonTokenId.ID_END_OBJECT)
				p.nextToken();
		}
	}

}
