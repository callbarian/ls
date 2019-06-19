package edu.handong.csee.java;

import edu.handong.csee.java.MyComparator;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;


import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;


import static java.nio.file.LinkOption.NOFOLLOW_LINKS;

public class LsCommand {

	/*
	private String longFormat;
	private String appendCharacter;
	private String listAllFiles;
	private String sortByTime;
	private String printSize;
	private boolean help;

	
	public LsCommand() {
		longFormat = "defaultValue";
		appendCharacter = "defaultValue";
		listAllFiles = "defaultValue"
		
	}
	*/
	
	public void runLsCommand(String[] args) {
		
		Options options = createOptions();
		if(parseOptions(options, args)) {
			
		}
	

		
		
		
	}
	
	private boolean parseOptions(Options options, String[] args) {
		CommandLineParser parser = new DefaultParser();
		
		try {

			
			CommandLine cmd = parser.parse(options, args);
			
			String currentPath = System.getProperty("user.dir");
		//	String[] pathArray = currentPath.split("\\");
			
			
		//	Path path = Paths.get(System.getProperty("user.dir"));
			
			File file = new File(currentPath);
			String[] allFiles = file.list();
			ArrayList<String> allFilesList = new ArrayList<String>();
			ArrayList<String> allFilesListCopy;
			
			for(String string : allFiles) 
				allFilesList.add(string);
			
			if(cmd.hasOption("a")||cmd.hasOption("f")){
				
				if(cmd.hasOption("f")) {
					
				}
				else{
				 	Collections.sort(allFilesList);
				}
				
			}
			else{
				
				
				int i = allFiles.length - 1;
				while(i>=0){
					if(allFiles[i].charAt(0)=='.') {
						allFilesList.remove(i);
					}
					i--;
				}
			//	allFiles = null;
			//	allFiles = excludeHidden.toArray(allFiles);
				
			}
			
			allFilesListCopy = (ArrayList<String>) allFilesList.clone();
			
			if(cmd.hasOption("F")) {
				
				int i = allFilesList.size() - 1;
				
				while(i>=0) {
					if(allFilesList.get(i).contains(".bat")||
							allFilesList.get(i).contains(".app")) {
						allFilesList.set(i,allFilesList.get(i)+"*");
					}
					
					if(!allFilesList.get(i).contains("."))
						allFilesList.set(i,allFilesList.get(i)+"/");
					
					i--;
				}
				
			}
			
			
			if(cmd.hasOption("t")) {
				List<Double> timeInDouble = new ArrayList<Double>();
				
				for(String string : allFilesListCopy) {
					Path path = Paths.get(string);
					FileTime fileTime = (FileTime)Files.getLastModifiedTime(path, NOFOLLOW_LINKS);
					
					StringBuilder sb = new StringBuilder(fileTime.toString());
					sb.deleteCharAt(sb.indexOf("-"));
					sb.deleteCharAt(sb.indexOf("-"));
					sb.deleteCharAt(sb.indexOf("T"));
					sb.deleteCharAt(sb.indexOf(":"));
					sb.deleteCharAt(sb.indexOf(":"));
					sb.deleteCharAt(sb.length()-1);
					
					timeInDouble.add(Double.parseDouble(sb.toString()));
					
				}
				
				HashMap<Double,String> mapFiles = new HashMap<Double,String>();
				HashMap<Double,String> mapFilesCopy = new HashMap<Double,String>();
				
				for(int i=0; i<allFilesListCopy.size(); i++) {
					mapFiles.put(timeInDouble.get(i),allFilesList.get(i));
					mapFilesCopy.put(timeInDouble.get(i), allFilesListCopy.get(i));
				}
				
				Collections.sort(timeInDouble);
				
				ArrayList<String> sortedFiles = new ArrayList<String>();
				ArrayList<String> sortedFilesCopy = new ArrayList<String>();
				
				for(int i=0; i<allFilesListCopy.size(); i++) {
					sortedFiles.add(mapFiles.get(timeInDouble.get(i)));
					sortedFilesCopy.add(mapFilesCopy.get(timeInDouble.get(i)));
				}
				
				allFilesList.clear();
				allFilesListCopy.clear();
				
				allFilesList = sortedFiles;
				allFilesListCopy = sortedFilesCopy;
				
			}
			
			
		
				Iterator<String> iterator = allFilesListCopy.iterator();
				for(String string : allFilesList) {
					
					
					Path path = Paths.get(iterator.next());
				
					String format = "%-20s%-35s%s\n";
					String formatElse = "%-20s%s\n";
					if(cmd.hasOption("h")) {
						System.out.printf(format,string,(FileTime)Files.getLastModifiedTime(path, NOFOLLOW_LINKS)
								,Files.getAttribute(path, "basic:size", NOFOLLOW_LINKS));
					
					}
					else {
						
						System.out.printf(formatElse,string,(FileTime)Files.getLastModifiedTime(path, NOFOLLOW_LINKS));
					}
					
				}
				
				if(cmd.hasOption("H")) {
					printHelp(options);
				}
				
				
				
			
			/*
			longFormat = cmd.getOptionValue("l");
			appendCharacter = cmd.getOptionValue("F");
			listAllFiles = cmd.getOptionValue("a");
			sortByTime = cmd.getOptionValue("t");
			printSize = cmd.getOptionValue("h");
			help = cmd.hasOption("H");
			*/

			
		} catch (Exception e) {
			printHelp(options);
			return(false);
			
		}

		return true;
	}
	
	private Options createOptions() {
		Options options = new Options();

		// add options by using OptionBuilder
		/*
		options.addOption(Option.builder("l").longOpt("longFormat")
				.desc("long format, displaying Unix file types, permissions, "
						+ "number of hard links, owner, group, size, last-modified date and filename")
				.build());
		
		options.addOption(Option.builder("F").longOpt("appendCharacter")
				.desc(" appends a character revealing the nature of a file, for example,"
						+ " * for an executable, or / for a directory. Regular files have no suffix.")
				.build());
		
		options.addOption(Option.builder("a").longOpt("listAllFiles")
				.desc(" lists all files in the given directory, including those whose names start with \".\" "
						+ "(which are hidden files in Unix). By default, these files are excluded from the list.")
				.build());
		
		options.addOption(Option.builder("t").longOpt("sortByTime")
				.desc("sort the list of files by modification time.")
				.build());
		
		options.addOption(Option.builder("h").longOpt("printSize")
				.desc("print sizes in human readable format. (e.g., 1K, 234M, 2G, etc.)")
				.build());
			
		options.addOption(Option.builder("H").longOpt("Help")
		        .desc("Help")
		        .build());
		*/
		
		options.addOption("f",false,null);
		options.addOption("F",false,null);
		options.addOption("a",false,null);
		options.addOption("t",false,null);
		options.addOption("h",false,null);
		
		options.addOption(Option.builder("H").longOpt("Help")
		        .desc("Help")
		        .build());
		
		return options;
	}
	private void printHelp(Options options) {
		// automatically generate the help statement
		HelpFormatter formatter = new HelpFormatter();
		String header = "LsCommand";
		String footer = "";
		formatter.printHelp("LsCommand", header, options, footer, true);
	}


}
