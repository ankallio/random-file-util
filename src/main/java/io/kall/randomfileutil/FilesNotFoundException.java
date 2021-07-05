package io.kall.randomfileutil;

@SuppressWarnings("serial")
public class FilesNotFoundException extends Exception {
	
	private final String dir;
	
	public FilesNotFoundException(String dir) {
		this.dir = dir;
	}
	
	public String getDir() {
		return dir;
	}
}
