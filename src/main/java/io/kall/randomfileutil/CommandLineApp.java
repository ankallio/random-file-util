package io.kall.randomfileutil;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

public class CommandLineApp {
	
	private static final String ACTION_QUIT = "Q";
	private static final String ACTION_RANDOM = "R";
	private static final String ACTION_RENAME = "N";
	private static final String ACTION_MOVE = "M";
	private static final String ACTION_DELETE = "D";
	private static final String ACTION_FOLDER = "F";
	private static final String ACTION_SEARCH = "S";
	
	private final RandomFileUtil util;
	private final Scanner sc = new Scanner(System.in);
	
	public CommandLineApp(String dir) throws IOException {
		util = new RandomFileUtil();
		openRandomFileIn(dir);
		
		// show options, wait for response
		printFileActions();
		queryFileAction();
	}
	
	private void printFileActions() {
		File file = util.getLastOpenedFile();
		// String dir= util.getLastSearchDir();
		
		System.out.println("| Last opened file");
		System.out.println("| Name:   " + file.getName());
		System.out.println("| Folder: " + file.getParent());
		System.out.println("| Size:   " + FileUtils.byteCountToDisplaySize(file.length()));
		System.out.println("");
		System.out.println("Options:");
		System.out.println("  " + ACTION_QUIT + ": Quit");
		System.out.println("  " + ACTION_RANDOM + ": Open another random file");
		System.out.println("  " + ACTION_RENAME + ": Rename last opened file");
		System.out.println("  " + ACTION_MOVE + ": Move file to another folder");
		System.out.println("  " + ACTION_DELETE + ": Delete last opened file");
		System.out.println("  " + ACTION_FOLDER + ": Open last file's folder");
		System.out.println("  " + ACTION_SEARCH + ": Set filename search term");
		
	}
	
	private void queryFileAction() throws IOException {
		String defaultAction = ACTION_RANDOM;
		System.out.print("Select action [" + defaultAction + "]: ");
		String entry = sc.nextLine().trim();
		if (entry.isEmpty())
			entry = defaultAction;
		parseAndExecute(entry);
	}
	
	private void parseAndExecute(String action) throws IOException {
		if (ACTION_QUIT.equalsIgnoreCase(action)) {
			System.exit(0);
		}
		
		else if (ACTION_RANDOM.equalsIgnoreCase(action)) {
			openRandomFileFromLastSearchDir();
		}
		
		else if (ACTION_RENAME.equalsIgnoreCase(action)) {
			renameFile();
		}
		
		else if (ACTION_MOVE.equalsIgnoreCase(action)) {
			moveFile();
		}
		
		else if (ACTION_DELETE.equalsIgnoreCase(action)) {
			confirmAndDeleteFile();
		}
		
		else if (ACTION_FOLDER.equalsIgnoreCase(action)) {
			util.openLastFileFolder();
		}
		
		else if (ACTION_SEARCH.equalsIgnoreCase(action)) {
			askForSearchTerm();
		}
		
		printFileActions();
		queryFileAction();
	}
	
	private void askForSearchTerm() throws IOException {
		System.out.print("Enter filename search filter: ");
		String filter = sc.nextLine().trim();
		String regex;
		if (filter.isEmpty()) {
			System.out.println("Clearing search filter");
			regex = null;
		} else {
			System.out.println("Changing search filter");
			regex = ".*" + Pattern.quote(filter) + ".*";
		}
		util.setCustomFilenamePattern(regex);
		
		openRandomFileFromLastSearchDir();
	}
	
	private void renameFile() throws IOException {
		File file = util.getLastOpenedFile();
		String defaultBaseName = FilenameUtils.getBaseName(file.getName());
		System.out.println("Enter new base file name for file " + file.getPath());
		
		System.out.print("[" + defaultBaseName + "]: ");
		String baseEntry = sc.nextLine().trim();
		if (baseEntry.isEmpty()) {
			System.out.println("Not renaming file");
			return;
		}
		
		String ext = FilenameUtils.getExtension(file.getName());
		String newFileName = baseEntry + (ext.isEmpty() ? "" : "." + ext);
		
		File newFile = util.renameLastOpenedFile(newFileName);
		System.out.println("Renamed file to: " + newFile.getPath());
	}
	
	private void moveFile() throws IOException {
		File file = util.getLastOpenedFile();
		
		System.out.println("Enter folder path where file " + file.getName() + " will be moved.");
		System.out.print("Path: ");
		String entry = sc.nextLine().trim();
		if (!entry.isEmpty()) {
			util.moveFileToFolder(entry);
		}
	}
	
	private void confirmAndDeleteFile() throws IOException {
		File file = util.getLastOpenedFile();
		System.out.println("Are you sure you want to delete file " + file.getPath() + " ?");
		System.out.print("Yes or No [N]: ");
		String answer = sc.nextLine().trim();
		if ("Y".equalsIgnoreCase(answer) || "Yes".equalsIgnoreCase(answer)) {
			boolean deleted = util.deleteLastOpenedFile();
			if (deleted) {
				System.out.println("Deleted file. Opening another random file.");
				openRandomFileFromLastSearchDir();
			}
		}
	}
	
	private void openRandomFileIn(String dir) throws IOException {
		try {
			util.openRandomFileIn(dir);
		} catch (FilesNotFoundException e) {
			System.out.println("Matching files not found in " + e.getDir());
		}
	}
	
	private void openRandomFileFromLastSearchDir() throws IOException {
		try {
			util.openRandomFileFromLastSearchDir();
		} catch (FilesNotFoundException e) {
			System.out.println("Matching files not found in " + e.getDir());
		}
	}
	
//	public static void main(String[] args) throws IOException {
//		Options options = new Options();
//		Option dirOption = new Option("dir", true, "Source directory for randomly selected file");
//		dirOption.setRequired(true);
//		options.addOption(dirOption);
//		
//		CommandLineParser parser = new DefaultParser();
//		CommandLine cmdLine;
//		try {
//			cmdLine = parser.parse(options, args);
//			
//			String dir = cmdLine.getOptionValue("dir");
//			
//			new CommandLineApp(dir);
//		} catch (ParseException e) {
//			HelpFormatter formatter = new HelpFormatter();
//			formatter.printHelp("app", options);
//		}
//	}
	
}
