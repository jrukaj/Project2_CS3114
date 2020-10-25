import java.io.IOException;
import java.util.ArrayList;

import com.opencsv.exceptions.CsvException;
/**
 * Class that creates Section objects and executes the commands.
 * @author Jonathan Rukaj
 * @author Drew Wissler
 * @version 1
 */
public class FileExecutor {

	public LoadData loaddata;


	/**
	 * Constructor for file executor
	 */
	public FileExecutor() {
		loaddata = new LoadData();
	}

	/**
	 * Takes in list of list of strings, executing the commands
	 * @param list
	 *            the list of commands
	 * @throws CsvException 
	 * @throws IOException 
	 */
	public void execute(ArrayList<ArrayList<String>> list) throws IOException, CsvException {
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

			case "summarydata":
				loaddata.summarydata();
				break;

			case "search":
				if (cmd.size() == 1) {
                    loaddata.search();
				}
				if (cmd.size() == 2) {
					String date = cmd.get(1);
                    loaddata.search(date);
				}
				if (cmd.size() == 3) {
					String stateName = cmd.get(1).toLowerCase();
					int numRecords = Integer.parseInt(cmd.get(2));
					loaddata.search(stateName, numRecords);

				}
				break;

			case "dumpdata":
				String filename = cmd.get(1);
				loaddata.dumpdata(filename);
				break;			

			} // end of switch
		} // End of for-loop
	} // End of execute
}

/**
 * Commands come like this:
 * [[search, va, 3], [dumpdata], [summarydata]]
 */
