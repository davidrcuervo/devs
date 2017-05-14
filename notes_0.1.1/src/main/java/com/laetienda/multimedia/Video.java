package com.laetienda.multimedia;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static java.nio.file.StandardOpenOption.READ;
import java.nio.channels.SeekableByteChannel;


/**
 * 
 * @author myself
 * Taken from the blog on internet: <br />
 * <a href="http://www.adrianwalker.org/2012/06/html5-video-pseudosteaming-with-java-7.html">link 1</a> <br />
 * <br />
 * The follow link explains it better: <br /> 
 * <a href="http://www.avajava.com/tutorials/lessons/how-do-i-monitor-the-progress-of-a-file-upload-to-a-servlet.html">link 2</a>
 */
public class Video {
	
	private static final Pattern RANGE_PATTERN = Pattern.compile("bytes=(?<start>\\d*)-(?<end>\\d*)");
	private static final long EXPIRE_TIME = 100 * 60 * 60 * 24;
	private final int BUFFER_LENGTH = 1024 * 16;
	
	Path path;
	int length;
	int start;
	int end;
	int contentLenght;
	
	protected Video(File file) throws MultimediaException{
		
		path = Paths.get(file.getAbsolutePath());
		start = 0;
		
		try{
			length = (int)Files.size(path);
			end = length - 1;
		}catch (IOException ex){
			ex.printStackTrace();
			throw new MultimediaException ("I/O error occurs while gettihg size of: " + path, ex);
		}
	}
	
	public void setRange(String range){
		
		range = range != null ? range : "empty"; 
		Matcher matcher = RANGE_PATTERN.matcher(range);
		
		if(matcher.matches()){
			String startGroup = matcher.group("start");
			start = startGroup.isEmpty() ? start : Integer.valueOf(startGroup);
			start = start < 0 ? 0 : start;
			
			String endGroup = matcher.group("end");
			end = endGroup.isEmpty() ? end : Integer.valueOf(endGroup);
			end = end > length - 1 ? length - 1 : end;
		}
		
		contentLenght = end - start + 1;
	}
	
	public int getBufferLength(){
		return BUFFER_LENGTH;
	}
	
	public String getFileName(){
		return path.getFileName().toString();
	}
	
	public Long getLastModifiedTime() throws MultimediaException{
		Long result = null;
		
		try{
			result = Files.getLastModifiedTime(path).toMillis();
		}catch(IOException ex){
			throw new MultimediaException(" I/O error occured while getting last modified date of " + path.toString(), ex);
		}
		return result;
	}
	
	public long getExpireTime(){
		return System.currentTimeMillis() + EXPIRE_TIME;
	}
	
	public String getContentType() throws MultimediaException{
		String result = null;
		try{
			Files.probeContentType(path);
		}catch(IOException ex){
			throw new MultimediaException("I/O error ocurred while getting type of the content", ex);
		}
		return result;
	}
	
	public SeekableByteChannel getInput(){
		SeekableByteChannel result = null;
		
		try{
			result = Files.newByteChannel(path, READ);
		}catch(IllegalArgumentException ex){
		
		}catch (UnsupportedOperationException ex){
		}catch(FileAlreadyExistsException ex){
		}catch(IOException ex){
			
		}
		
		return result;
	}

	/**
	 * @return the length
	 */
	public int getLength() {
		return length;
	}

	/**
	 * @return the start
	 */
	public int getStart() {
		return start;
	}

	/**
	 * @return the end
	 */
	public int getEnd() {
		return end;
	}

	/**
	 * @return the contentLenght
	 */
	public int getContentLenght() {
		return contentLenght;
	}

	/**
	 * @return the path
	 */
	public Path getPath() {
		return path;
	}
}