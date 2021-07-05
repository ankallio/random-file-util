package io.kall.randomfileutil;

import java.awt.Desktop;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RandomFileUtil {
	
	private static final Logger logger = LoggerFactory.getLogger(RandomFileUtil.class);
	
	private static final String[] ignoredFilenameExtensions = { "jar", "db", "exe", "bat", "cmd", "ini", "lnk", "" };
	private static final String[] ignoredFileNames = { "folder.jpg" };
	
	private Pattern customFilenamePattern = null;
	
	private File lastOpenedFile;
	private String lastSearchDir;
	
	public RandomFileUtil() {
		
	}
	
	public void setCustomFilenamePattern(String customFilenameRegex) {
		if (customFilenameRegex != null && !customFilenameRegex.isEmpty())
			customFilenamePattern = Pattern.compile(customFilenameRegex, Pattern.CASE_INSENSITIVE);
		else
			customFilenamePattern = null;
	}
	
	public File getLastOpenedFile() {
		return lastOpenedFile;
	}
	
	public String getLastSearchDir() {
		return lastSearchDir;
	}
	
	public void setLastSearchDir(String lastSearchDir) {
		this.lastSearchDir = lastSearchDir;
	}
	
	public void openRandomFileIn(String directoryPath) throws IOException, FilesNotFoundException {
		File file = findRandomFile(directoryPath, true);
		if (file != null) {
			openFile(file);
			lastOpenedFile = file;
			lastSearchDir = directoryPath;
		}
	}
	
	public void openRandomFileFromLastSearchDir() throws IOException, FilesNotFoundException {
		openRandomFileIn(lastSearchDir);
	}
	
	public void openRandomFileInLastDirectory() throws IOException, FilesNotFoundException {
		openRandomFileIn(lastOpenedFile.getParent());
	}
	
	public File renameLastOpenedFile(String newFileName) throws IOException {
		File newFile = new File(lastOpenedFile.getParentFile(), newFileName);
		FileUtils.moveFile(lastOpenedFile, newFile);
		lastOpenedFile = newFile;
		return newFile;
	}
	
	public boolean deleteLastOpenedFile() {
		return FileUtils.deleteQuietly(lastOpenedFile);
	}
	
	public void openLastFileFolder() throws IOException {
		openFile(lastOpenedFile.getParentFile());
	}
	
	public void moveFileToFolder(String targetFolderPath) throws IOException {
		File targetFile = new File(targetFolderPath, lastOpenedFile.getName());
		FileUtils.moveFile(lastOpenedFile, targetFile);
		lastOpenedFile = targetFile;
	}
	
	/**
	 * Finds a random file in given directory. If directoryPath is null or empty, current directory is used. Some
	 * filetypes are always passed.
	 * 
	 * @param directoryPath
	 * @throws IOException
	 * @throws FilesNotFoundException
	 */
	private File findRandomFile(String directoryPath, boolean recursive) throws IOException, FilesNotFoundException {
		if (directoryPath == null || directoryPath.trim().length() == 0)
			directoryPath = ".";
		
		// confirm directory
		File targetFile = new File(directoryPath);
		if (!targetFile.exists() || !targetFile.isDirectory()) {
			throw new FileNotFoundException("Given file not a directory or does not exist.");
		}
		targetFile = targetFile.getCanonicalFile();
		
		// find all files under directory
		File[] files = targetFile.listFiles(new FileFilter() {
			
			@Override
			public boolean accept(File file) {
				if (file.isFile()) {
					String fileExt = FilenameUtils.getExtension(file.getName());
					for (String extension : ignoredFilenameExtensions) {
						if (fileExt.equalsIgnoreCase(extension))
							return false;
					}
				}
				for (String fn : ignoredFileNames) {
					if (file.getName().equalsIgnoreCase(fn))
						return false;
				}
				if (customFilenamePattern != null && file.isFile()
						&& !customFilenamePattern.matcher(file.getName()).matches()) {
					return false;
				}
				return true;
			}
		});
		
		if (files == null || files.length == 0) {
			throw new FilesNotFoundException(directoryPath);
		}
		
		// choose random file
		
		// Randomise files array
		Random r = new Random();
		List<File> fileList = Arrays.asList(files);
		Collections.shuffle(fileList, r);
		
		// Go thru shuffled files. If directory, search in it. If not found in directory, try next file/dir. If nothing
		// found, throw exception.
		for (File randomFile : fileList) {
			if (recursive && randomFile.isDirectory()) {
				try {
					return findRandomFile(randomFile.getPath(), true);
				} catch (FilesNotFoundException e) {
					continue;
				}
			} else {
				// open selected file using default application
				return randomFile;
			}
		}
		throw new FilesNotFoundException(directoryPath);
	}
	
	private static void openFile(File file) throws IOException {
		logger.info("Opening file {}", file);
		Desktop.getDesktop().open(file);
	}
}
