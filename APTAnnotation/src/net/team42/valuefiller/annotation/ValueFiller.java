package net.team42.valuefiller.annotation;

public @interface ValueFiller {
	public static final int SOURCETYPE_HTTP = 1;
	public static final int SOURCETYPE_FILE = 2;

	String path();

	String charset() default "UTF-8";

	int sourceType() default SOURCETYPE_FILE;

	int maxLength() default Integer.MAX_VALUE;

}
