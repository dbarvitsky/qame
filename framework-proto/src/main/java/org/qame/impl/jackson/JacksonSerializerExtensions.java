package org.qame.impl.jackson;

import org.qame.impl.IndirectCheckpoint;

import com.fasterxml.jackson.databind.module.SimpleModule;

@SuppressWarnings("serial")
public class JacksonSerializerExtensions extends SimpleModule {

	@SuppressWarnings("unchecked")
	public JacksonSerializerExtensions()
	{
		this.addDeserializer(IndirectCheckpoint.class, new ReflectedCheckpointDeserializer());
	}
}
