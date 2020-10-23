import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * 
 */

/**
 * @author Drew Wissler
 *
 */
public class LoadDataP2 {
    //The BST that stores <Pair<Name, Date>, records>
    private BST<Pair<String, String>, State> bstNameDate;
    //The BST that stores <Pair<Date, Name>, records>
    private BST<Pair<String, String>, State> bstDateName;
    //The BST that stores <Pair<Grade ,<Pair<Name, Date>>, records>
    private BST<Pair<String, Pair<String, String>>, State> bstGrade;
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
        bstNameDate = new BST<Pair<String, String>, State>();
        bstDateName = new BST<Pair<String, String>, State>();
        bstGrade = new BST<Pair<String, Pair<String, String>>, State>();
        state_abvs = new String[NUMBER_OF_STATES];
    }
    
    public BST<Pair<String, String>, State> getNameDate(){
    	return bstNameDate;
    }
    
    public BST<Pair<String, String>, State> getDateName(){
    	return bstDateName;
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
            
            //iterator to check on the certain criteria for updating or discarding data
            //only need to iterate one bst
            java.util.Iterator<Pair<String, String>> iterNameDate = bstNameDate.iterator();

            while (iterNameDate.hasNext() && validData) {
                Pair<String, String> iterPair = new Pair<String,String>(state.getState(), state.getDate());
                BinaryNode<Pair<String, String>, State> node = bstNameDate.find(iterPair);
                
                if (node == null) {
                    break;
                }
                else {
                    notSameName = false;
                    State temp = node.getValue();
                    if (isGradeBetter(state, temp)) {
                        System.out.println("Data has been updated for " + state.getState() +
                            " " + changeDate(state.getDate()));
                        tempRecords++;
                        
                        Pair<String, String> tempPairDateName = node.getKey();
                        tempPairDateName = new Pair<String, String>(
                            tempPairDateName.getK(), tempPairDateName.getT());
                        
                        Pair<String, Pair<String, String>> tempPairGrade = 
                            new Pair<String, Pair<String, String>>(temp.getGrade(), iterPair);
                        
                        bstNameDate.remove(node.getKey(), node.getValue());
                        bstDateName.remove(tempPairDateName, node.getValue());
                        bstGrade.remove(tempPairGrade, node.getValue());
                        
                        bstNameDate.insert(new Pair<String, String>(state.getState(), state.getDate()), state);
                        bstDateName.insert(tempPairDateName, state);
                        bstGrade.insert(tempPairGrade, state);
                        break;
                    }
                    else {
                        if (compareStates(temp, state)) {
                            System.out.println("Data has been updated for the missing " +
                                "data in " + state.getState());
                            tempRecords++;
                            break;
                        }
                        else {
                            System.out.println("Low quality data rejected for " + state.getState());
                            break;
                        }
                    }
                } 
            }
            
            //if the date and state has not been added yet
            if (validName && validData && notSameName) {
                bstNameDate.insert(new Pair<String, String>(state.getState(), state.getDate()), state);
                bstDateName.insert(new Pair<String, String>(state.getDate(), state.getState()), state);
                bstGrade.insert(new Pair<String, Pair<String, String>>(state.getGrade(),
                    new Pair<String, String>(state.getState(), state.getDate())), state);
                tempRecords++;
            }
            //if the record is invalid
            else if (!validData) {
                System.out.println("Discard invalid record");
            }
            //if the state name does not exist
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
    
    /**
     * Performs a dump of the Binary Search Tree
     * @param option: what kind of dump to perform
     */
    public void dumpBST(BinaryNode rootNode, String spacing) {
    	if (!rootNode.isLeaf()) {
    		dumpBST(rootNode.getLeft(), spacing + "  ");
    	}
    	
    	if (rootNode == null) {
    		System.out.println(spacing + "E");
    		return;
    	}
    	else {
    		System.out.println(spacing + rootNode.getKey() + rootNode.getValue());
    		
    	}
    	if (!rootNode.isLeaf()) {
    		dumpBST(rootNode.getRight(), spacing + "  ");
    	}
    	
    }

}