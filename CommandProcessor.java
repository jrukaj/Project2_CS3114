import java.util.*;
import java.io.*;

/**
 * Parse commands into a list of lists, with each individual list 
 * containing the command as the first object, and the parameters following
 * @author Jonathan Rukaj (jrukaj)
 * @author Drew Wissler
 * @param <E> Generic support
 * @version 1
 */
public class CommandProcessor {

	// File referring to command file, ArrayList to store commands, scanner for parsing
    private File file;
	private ArrayList<ArrayList<String>> commands;
	private Scanner scan;
	
	/** 
	 * CommandProcessor constructor
	 */
	public CommandProcessor() {
		// nothing to initialize
	}
	
	/**
	 * CommandProcessor override
	 * @param filename 
	 *            name of the file
	 */
	public CommandProcessor(String filename) {
		try {
			file = new File(filename);
			scan = new Scanner(file);
		}
		catch (Exception e) {
			System.out.println(
					"The file you are trying to "
					+ "open does not exist bröther. I am sorry my guy.\n");
			e.printStackTrace();
			System.exit(0);
		}
		// Initialize commands list, will be used in helper functions below
		commands = new ArrayList<ArrayList<String>>();
	}
	
	/**
	 * Scans through the input file, adding commands to an ArrayList
	 * @return list with commands
	 */
	public ArrayList<ArrayList<String>> parseCmd() {
		while (scan.hasNextLine()) {
			commands.add(parseLine(scan.nextLine()));
		}
		scan.close();
		return commands;
	}
	
	/**
	 * Parses through an individual line, adding the command and 
	 * parameters to a list.
	 * @param line
	 *            the line to be parsed through
	 * @return list, with command as the first value and 
	 * 		the following values being the parameters
	 */
	public ArrayList<String> parseLine(String line) {
		ArrayList<String> list = new ArrayList<String>();
		// Format strings on the line and add to an array, then add to the ArrayList
		String[] splitted = line.trim().split("\\s+");
		for (String string: splitted) {
			list.add(string);
		}
		return list;
	}
	
}

