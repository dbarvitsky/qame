package org.qame.impl;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.qame.Checkpoint;
import org.qame.Scenario;

public class ReflectedCheckpointFactory implements CheckpointFactory {
	private Class<?>[] cpParameters;
	private Class<?> cpClass;
	private Constructor<?> cpCtor;
	private Method cpMethod;
	private Object[] args;
	
	@SuppressWarnings("unchecked")
	public <S extends Scenario> Checkpoint<S> createCheckpoint() throws ReflectiveOperationException {
		return (Checkpoint<S>) 
				(cpMethod != null 
					? cpMethod.invoke(null, args)
					: cpCtor.newInstance(args));
	}
	
	public Class<?>[] getArgTypes() {
		return cpParameters;
	}
	
	public Object[] getArgs() {
		return args;
	}
	
	public ReflectedCheckpointFactory setArgs( Object... args ) {
		this.args = args;
		return this;
	}
	
	public String getExpression() {
		return cpClass.getName()+"#"+(cpMethod != null ? cpMethod.getName() : "");
	}
	
	public ReflectedCheckpointFactory setExpression( String expr, int nArgs ) throws ClassNotFoundException {
		if (args != null) nArgs = args.length;
		if (expr == null) throw new java.lang.IllegalArgumentException("Parameter \"expr\" is required");
		expr = expr.trim();
		if (expr.length() == 0) throw new java.lang.IllegalArgumentException("Parameter \"expr\" is required");
		int pos = expr.lastIndexOf('#');
		if (pos >= expr.length()-1) { expr = expr.substring(0,pos); pos = -1; }
		else if (pos == 0) { expr = expr.substring(1,expr.length()-1); pos = -1; }
		if (pos < 0) {
			cpClass = Class.forName(expr);
			for (Constructor<?> m : cpClass.getConstructors()) {
				if (
						Modifier.isPublic(m.getModifiers()) && 
						(nArgs < 0 || m.getParameterTypes().length == nArgs)
				) {
					if (cpCtor != null) 
						throw new java.lang.InstantiationError("Class "+cpClass.getName()+" has more than one constructor with "+nArgs+" parameters.");
					cpCtor = m;
				}
			}
			cpMethod = null;
			if (cpCtor == null)
				throw new java.lang.InstantiationError("Class "+cpClass.getName()+" does not have a constructor with "+nArgs+" parameters.");
			cpParameters = cpCtor.getParameterTypes();

		} else {
			String className = expr.substring(0,pos);
			String methodName = expr.substring(pos+1);
			cpClass = Class.forName(className);
			for (Method m : cpClass.getMethods()) {
				if (
						m.getName().equals(methodName) &&
						Modifier.isPublic(m.getModifiers()) && 
						Modifier.isStatic(m.getModifiers()) && 
						Checkpoint.class.isAssignableFrom(m.getReturnType()) &&
						(nArgs < 0 || m.getParameterTypes().length == nArgs)) {
					if (cpMethod != null) 
						throw new java.lang.InstantiationError("Class "+cpClass.getName()+" has more than one method "+methodName+" with "+nArgs+" parameters.");
					cpMethod = m;
				}
			}
			cpCtor = null;
			if (cpMethod == null)
				throw new java.lang.InstantiationError("Class "+cpClass.getName()+" does not have a static method "+methodName+" with "+nArgs+" parameters.");
			cpParameters = cpMethod.getParameterTypes();
		}
		return this;
	}
}
