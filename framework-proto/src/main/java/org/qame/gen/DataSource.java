package org.qame.gen;

public interface DataSource {
	Object load( java.net.URI uri );
	String getSchema();
}
