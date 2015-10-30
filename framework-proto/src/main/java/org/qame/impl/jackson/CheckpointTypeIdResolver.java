package org.qame.impl.jackson;

import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.databind.DatabindContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.jsontype.impl.TypeIdResolverBase;

public class CheckpointTypeIdResolver extends TypeIdResolverBase {

	@Override public JavaType typeFromId(DatabindContext context, String id) {
		try {
			Class<?> type = null;
			if ("call".equals(id)) 
				type = org.qame.impl.IndirectCheckpoint.class;
			else 
				type = context.getTypeFactory().findClass(id);
			
			return context.getTypeFactory().constructType(type);
		} catch (Exception e){
			return null;
		}
	}
	public String idFromValue(Object value) {
		Class<?> type = value == null ? Object.class : value.getClass(); 
		return type.getName();
	}

	public String idFromValueAndType(Object value, Class<?> suggestedType) {
		return value == null ? suggestedType.getClass().getName() : value.getClass().getName();
	}

	public Id getMechanism() {
		return Id.CLASS;
	}
}
