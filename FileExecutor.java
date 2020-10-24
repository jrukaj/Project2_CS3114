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
		//System.out.println(list.toString());
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
				
				break;
				
			case "remove":
				String grade = cmd.get(1);
				loaddata.remove(grade);

			case "dumpBST":
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
}

/**
 * Commands come like this:
 * [[search, va, 3], [dumpdata, filename], [summarydata]]
 */
