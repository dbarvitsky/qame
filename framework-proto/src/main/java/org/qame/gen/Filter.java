package org.qame.gen;

public interface Filter {
	boolean isAccepted( Object value );
	Object filter( Object value ) throws Exception;
}
