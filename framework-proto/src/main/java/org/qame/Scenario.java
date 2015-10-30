package org.qame;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "xtype")
public interface Scenario {
	void run( RunState state ) throws Exception;
}
