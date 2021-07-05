package io.kall.randomfileutil;

import java.awt.EventQueue;
import java.io.IOException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class Main {
	
	public static void main(String[] args) throws IOException {
		Options options = new Options();
		Option dirOption = new Option("dir", true, "Source directory for randomly selected file");
		dirOption.setRequired(true);
		Option guiOption = new Option("gui", "Graphical UI");
		options.addOption(dirOption);
		options.addOption(guiOption);
		
		CommandLineParser parser = new DefaultParser();
		CommandLine cmdLine;
		try {
			cmdLine = parser.parse(options, args);
			
			String dir = cmdLine.getOptionValue("dir");
			boolean gui = cmdLine.hasOption("gui");
			
			if (!gui)
				new CommandLineApp(dir);
			else{
				EventQueue.invokeLater(new Runnable() {
					@Override
					public void run() {
						try {
							AppWindow frame = new AppWindow(dir);
							frame.setVisible(true);
							frame.openRandom();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
			}
		} catch (ParseException e) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("app", options);
		}
	}
	
}
