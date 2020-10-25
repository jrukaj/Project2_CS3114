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
		for (int i = 0; i < splitted.length; i++) {
		    if (splitted[i].equals("-S")) {
		        list.add(splitted[i]);
		        StringBuilder sb = new StringBuilder();
		        int j = 1;
		        while ((i + j) != splitted.length && !isLetter(splitted[i + j])) {
		            sb.append(splitted[i + j]);
		            sb.append(" ");
		            j++;
		        }
		        list.add(sb.toString().substring(0, sb.toString().length() - 1));
		        i += j;
		    }
		    else {
		        list.add(splitted[i]);
		    }
			
		}
		return list;
	}
	/**
     * Determines if a string is a letter for the search method
     * @param str - the string method
     * @return true if it is a letter false if it is not
     */
    private boolean isLetter(String str) {
        return (str.equals("-C") || str.equals("-D") || str.equals("-T") ||
            str.equals("-N") || str.equals("-Q") || str.equals("-S")); 
    }
}