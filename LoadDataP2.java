import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * 
 */

/**
 * @author Drew
 *
 */
public class LoadDataP2 {
  //The BST that stores all the states
    private BST<State> bst;
    //The array that stores state abbreviations
    private String[] state_abvs;
    //The total number of states in the US
    private static final int NUMBER_OF_STATES = 59;
    //The total number of records
    private int totalRecords = 0;
    //Array of the state names
    String[] stateNames = new String[NUMBER_OF_STATES];
    
    /**
     * Constructor for the LoadData object
     */
    public LoadDataP2() {
        bst = new BST<State>();
        state_abvs = new String[NUMBER_OF_STATES];
    }
    
    
    /**
     * Parses through the data and stores each line into a "state" which
     * is then stored in a BST based on the name of the state. Data is updated 
     * or not used based on certain qualifications
     * 
     * @param textFile - The name of the file
     * @throws FileNotFoundException if there is on file found
     */
    public void load(String textFile) throws FileNotFoundException {      
        File fileName = new File(textFile);
        makeStateArray("States_Abbreviations.txt");        

        File f = new File("State_Names.txt");
        Scanner scan = new Scanner(f);
        for (int i = 0; i < NUMBER_OF_STATES; i++) {
            stateNames[i] = scan.nextLine();
        }
        scan.close();
        
        if (!fileName.exists()) {
            System.out.println("File " + textFile + " was not found");
            return;          
        }

        Scanner scanner = new Scanner(fileName);

        scanner.nextLine();
        int tempRecords = 0;
        while(scanner.hasNext()) {
            String str = scanner.next();
            if (str.length() == 9) {
                continue;
            }
            String[] dataArr = str.split(",");
            State state = new State();
                     
            
            //boolean to see if the data is valid
            boolean validData = false;
            //Checks to see if state, quality grade, or date exists
            if (!dataArr[0].isEmpty() && !dataArr[1].isEmpty() &&
                    !dataArr[2].isEmpty() && dataArr[0].length() == 8) {
                validData = true;
                state.stateInfo(dataArr); 
            }

            //boolean to see if the name is valid
            boolean validName = false;

            int i = 0;
            //Checks to see if any states have invalid name
            while (i < NUMBER_OF_STATES && validData) {
                if (state_abvs[i].equals(state.getState())) {
                    validName = true;
                    break;
                }
                i++;
            }

            
            //boolean that is true if state has the same abbreviation and date as another 
            //state in the bst
            boolean notSameName = true;

            java.util.Iterator<State> iter = bst.iterator();

            //checks to see if other state in bst has same abbreviation and date and updates
            //the data if needed
            while (iter.hasNext() && validData) {
                State iterState = iter.next();
                if (iterState.getState().equals(state.getState()) &&
                        iterState.getDate().equals(state.getDate())) {
                    notSameName = false;
                    if (isGradeBetter(state, iterState)) {
                        System.out.println("Data has been updated for " + state.getState() +
                                " " + changeDate(state.getDate()));
                        state.insert(bst);
                        bst.remove(iterState);
                        tempRecords++;
                        break;
                    }
                    else {
                        if (compareStates(iterState, state)) {
                            tempRecords++;
                            System.out.println("Data has been updated for the missing " +
                                    "data in " + state.getState());
                        }
                        else {
                            System.out.println("Low quality data rejected for " + state.getState());
                        }
                    }
                }
            }

            //print statements 
            if (validName && validData && notSameName) {
                tempRecords++;
                state.insert(bst);
            }
            else if (!validData) {
                System.out.println("Discard invalid record");
            }
            else if (!validName) {
                System.out.println("State of " + state.getState() + " does "
                        + "not exist!");
            }            
        }
        scanner.close();
        System.out.println("Finished loading " + fileName + " file");
        System.out.println(tempRecords + " records have been loaded"); 
        totalRecords += tempRecords;
    }
    
    /**
     * Compares the data quality between two states
     * 
     * @param state1 A state being compared
     * @param state2 A state being compared
     * @return true if first state has a better grade
     */
    private boolean isGradeBetter(State state1, State state2) {
        int state1Int = 0;
        int state2Int = 0;
        switch (state1.getGrade()) {
        case "A+": 
            state1Int = 9;
            break;
        case "A":
            state1Int = 8;
            break;
        case "B+":
            state1Int = 7;
            break;
        case "B": 
            state1Int = 6;
            break;
        case "C+":
            state1Int = 5;
            break;
        case "C":
            state1Int = 4;
            break;
        case "D+":
            state1Int = 3;
            break;
        case "D":
            state1Int = 2;
            break;
        case "F":
            state1Int = 1;
            break;
        default:
            state1Int = 0;
            break;
        }
        switch (state2.getGrade()) {
        case "A+": 
            state2Int = 9;
            break;
        case "A":
            state2Int = 8;
            break;
        case "B+":
            state2Int = 7;
            break;
        case "B": 
            state2Int = 6;
            break;
        case "C+":
            state2Int = 5;
            break;
        case "C":
            state2Int = 4;
            break;
        case "D+":
            state2Int = 3;
            break;
        case "D":
            state2Int = 2;
            break;
        case "F":
            state2Int = 1;
            break;
        default:
            state2Int = 0;
            break;
        }

        return (state1Int > state2Int);
    }

    /**
     * Creates an array of the states and territories used to compare with
     * the states in the data file
     * 
     * @param textFile - Text file with state abbreviations
     * @throws FileNotFoundException if the file is not found
     */
    private void makeStateArray(String textFile) throws FileNotFoundException {
        File file = new File(textFile);
        Scanner scan = new Scanner(file);
        int i = 0;
        while (scan.hasNext()) {           
            state_abvs[i] = scan.next();
            i++;
        }
        scan.close();
    }

    /**
     * Compares the values of two states to see if anything needs to be updated
     * 
     * @param better - The state with the better quality grade
     * @param worse - The state with the worse quality grade
     * @return true if any values were changed
     */
    private boolean compareStates(State better, State worse) {
        boolean isChanged = false;    

        if (better.getPositives().isEmpty() && !worse.getPositives().isEmpty()) {
            better.setPositives(worse.getPositives());
            isChanged = true;
        }
        if (better.getNegatives().isEmpty() && !worse.getNegatives().isEmpty()) {
            better.setNegatives(worse.getNegatives());
            isChanged = true;
        }
        if (better.getHospitalized().isEmpty() && !worse.getHospitalized().isEmpty()) {
            better.setHospitalized(worse.getHospitalized());
            isChanged = true;
        }
        if (better.getOnVentilCurr().isEmpty() && !worse.getOnVentilCurr().isEmpty()) {
            better.setOnVentilCurr(worse.getOnVentilCurr());
            isChanged = true;
        }
        if (better.getOnVentilCum().isEmpty() && !worse.getOnVentilCum().isEmpty()) {
            better.setOnVentilCum(worse.getOnVentilCum());
            isChanged = true;
        }
        if (better.getRecovered().isEmpty() && !worse.getRecovered().isEmpty()) {
            better.setRecovered(worse.getRecovered());
            isChanged = true;
        }
        if (better.getDeath().isEmpty() && !worse.getDeath().isEmpty()) {
            better.setDeath(worse.getDeath());
            isChanged = true;
        }

        return isChanged;
    }

    /**
     * Changes the date to a readable form
     * @param str - The date from the data
     * @return The readable date
     */
    private String changeDate(String str) {
        String day, month, year;
        year = str.substring(0,4);
        month = str.substring(4,6);
        day = str.substring(6,8);

        return month + "/" + day + "/" + year;

    }
}
