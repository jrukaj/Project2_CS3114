import java.io.IOException;
import java.util.ArrayList;

/**
 * Class that creates Section objects and executes the commands.
 * @author Jonathan Rukaj
 * @author Drew Wissler
 * @version 1
 */
public class FileExecutor {

	public LoadDataP2 loaddata;


	/**
	 * Constructor for file executor
	 */
	public FileExecutor() {
		loaddata = new LoadDataP2();
	}

	/**
	 * Takes in list of list of strings, executing the commands
	 * @param list
	 *            the list of commands
	 * @throws CsvException 
	 * @throws IOException 
	 */
	public void execute(ArrayList<ArrayList<String>> list) throws IOException {
		for (ArrayList<String> cmd: list) {

			// Switch the command, doing necessary operations
			// depending on the command
			switch (cmd.get(0).toLowerCase()) {
			    
			case "load":
				String fileName = cmd.get(1);
				try {
					loaddata.load(fileName);
				}
				catch (Exception e) {
					e.printStackTrace();
				}
				break;


			case "search":
				if (cmd.size() == 1) {
				    String[] str = {};
				    loaddata.search(str, str);
				    break;
				}
				String[] commands = new String[4];
				String[] inputs = new String[4];
				int arrayNum = 0;
				int i = 1;
				while (i < cmd.size()) {
				    if (isLetter(cmd.get(i))) {
				        if ((i + 1) == cmd.size()) {
				            commands[arrayNum] = cmd.get(i);
				            arrayNum++;
				            if (isLetter(cmd.get(i + 1))) {
				                continue;
				            }
				        }
				        else if (isLetter(cmd.get(i + 1))) {
				            commands[arrayNum] = cmd.get(i);
                            inputs[arrayNum] = "";
                            arrayNum++;
				        }
				        else {
				            commands[arrayNum] = cmd.get(i);
				            inputs[arrayNum] = cmd.get(i + 1);
				            arrayNum++;
				            i++;
				        }
				    }
				    i++;
				}
				loaddata.search(commands, inputs);
				break;
				
			case "remove":
				String grade = cmd.get(1);
				loaddata.remove(grade);
				break;
				
			case "dumpbst":
				String version = cmd.get(1);
				if (version.equals("1")) {
					loaddata.dumpBST(loaddata.getDateName().getRoot(), "");
				}
				else {
					loaddata.dumpBST(loaddata.getNameDate().getRoot(), "");
				}
				break;					
			} // end of switch
		} // End of for-loop
	} // End of execute
	
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

/**
 * Commands come like this:
 * [[search, va, 3], [dumpdata, filename], [summarydata]]
 */