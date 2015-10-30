package org.qame;


import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonTypeIdResolver;

@JsonTypeInfo(use = JsonTypeInfo.Id.CUSTOM, include = JsonTypeInfo.As.PROPERTY, property = "xtype")
@JsonTypeIdResolver(value = org.qame.impl.jackson.CheckpointTypeIdResolver.class)
public interface Checkpoint<S extends Scenario> {
	void verify( RunState state, S scenario );
}
