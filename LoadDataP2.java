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
     * 
     */
    public void search(String[] commands, String[] inputs) {
        if (commands.length == 0) {
            searchNoParam();
            return;
        }
        if (commands[0].equals("C")) {
            if (inputs[0].isEmpty()) {
                inputs[0] = "0";
            }
            int cases = Integer.parseInt(inputs[0]);
            //keeps track of states with streaks
            int numOfStates = 0;
            //iterator to check all the states in the bst
            java.util.Iterator<Pair<String, String>> iter = bstNameDate.iterator();
            while (iter.hasNext()) {
                Pair<String, String> pair = iter.next();
                
                //used to see if you need to go to the next month
                String str = pair.getK();
                int month = Integer.parseInt(str.substring(4, 6));
                str = str.substring(str.length() - 2, str.length());
                int date = Integer.parseInt(pair.getK());
                int tempDate = 11111111;
                String tempState = "";
                //number of days in a row
                int count = 1;
                //increases after ever date in a row, resets to 1 if in the beginning of the month
                int newCount = 1;
                //true if there is a streak of days with positive cases
                boolean streak = true;
                while (streak) {
                    //if month has 30 days it goes to the next month
                    if ((Integer.parseInt(str) + newCount) > 30 && (month % 2) == 1) { 
                        date = nextMonth(date);
                        newCount = 1;
                        str = "01";
                    }
                    //if month has 30 days it goes to the next month
                    else if ((Integer.parseInt(str) + newCount) > 31 && (month % 2) == 0) {
                        date = nextMonth(date);
                        newCount = 1;
                        str = "01";
                    }

                    BinaryNode<Pair<String, String>, State> node = 
                        bstNameDate.find(new Pair<String,String>(pair.getT(), (date + newCount) + ""));
                    
                    if (node != null && Integer.parseInt(node.getValue().getPositives()) > cases) {
                        tempDate = date + newCount;
                        count++;
                        newCount++;                             
                    }
                    else {
                        streak = false;
                    }
                }
                //streak is true if it is at least a week
                if (count >= 7) {
                    //checks to see if the state name has been printed already
                    if (!tempState.equals(pair.getT())) {
                        System.out.println("State " + pair.getT());
                        tempState = pair.getT();
                        numOfStates++;
                    }
                    //prints date range
                    System.out.println(changeDate(pair.getK()) + " - " + changeDate(tempDate + ""));
                }
                while (count != 1) {
                    iter.next();
                    count--;
                }
            }
            System.out.println(numOfStates + " states have daily numbers of positive cases greater" +
                " than or equal to " + inputs[0] + " for at least 7 days continuously");
        }
        if (commands[0].equals("T")) {
            //iterator to check all the states in the bst
            java.util.Iterator<Pair<String, String>> iter = bstNameDate.iterator();
            String date;
            //if -T # -D #
            if (commands.length != 1) {
                StringBuilder sb = new StringBuilder();
                String temp = inputs[1];
                sb.append(temp.substring(temp.length() - 4, temp.length()));
                sb.append(temp.substring(0, 2));
                sb.append(temp.substring(3, 5));               
                date = sb.toString();
            }
            else {
                date = recentDate();
            }
            int range = Integer.parseInt(inputs[0]);
            
            //gets just the day of the later date
            String justDay = date.substring(date.length() - 2, date.length());
            int justDayInt = Integer.parseInt(justDay);
            
            StringBuilder sb = new StringBuilder();
            sb.append(date.substring(0,4));
            
            //This if statement finds the first date based of the inputs
            if ((justDayInt - range) <= 0) {
                //brings you to beginning of the month
                int temp = range - justDayInt;
                
                int num = Integer.parseInt(date.substring(4, 6));
                
                while (temp > 30) {
                    num--;
                    //months with 31 days
                    if ((num % 2) == 0) {
                        temp -= 32;
                    }
                    //months with 30 days
                    else {
                        temp -= 31;
                    }
                }
                //Integer.parseInt gets rid of the zero so you have to add it back
                if (num < 10) {
                    sb.append("0");
                    sb.append(num - 1);
                }
                else {
                    sb.append(num - 1);
                }
                sb.append(temp);
            }
            else {
                sb.append(date.substring(4, 6));
                if ((justDayInt - range) < 10) {
                    sb.append("0");
                    sb.append(justDayInt - range + 1);
                }
                else {
                    sb.append(justDayInt - range + 1);
                }
            }
            
            String firstDay = sb.toString();
            //stores the states and the average positives
            String[] stateArr = new String[NUMBER_OF_STATES];
            int[] positiveArr = new int[NUMBER_OF_STATES];
            //counters for various purposes
            int dayCounter = 0;
            int stateCounter = 0;
            while (iter.hasNext()) {
                int dateNum = Integer.parseInt(firstDay);
                dayCounter = 0;
                int newCount = 0;
                Pair<String, String> pair = iter.next();
                int month = Integer.parseInt(pair.getK().substring(4, 6));
                //if state has already been looked at the loop just continues
                if (stateCounter != 0 && pair.getT().equals(stateArr[stateCounter - 1])) {
                    continue;
                }
                while (dayCounter < range) {
                    //if month has 30 days it goes to the next month
                    if (((dateNum % 100) + newCount) > 30 && (month % 2) == 1) { 
                        dateNum = nextMonth(dateNum);
                        newCount = 0;
                    }
                    //if month has 30 days it goes to the next month
                    else if (((dateNum % 100) + newCount) > 31 && (month % 2) == 0) {
                        dateNum = nextMonth(dateNum);
                        newCount = 0;
                    }
                    BinaryNode<Pair<String, String>, State> node = 
                        bstNameDate.find(new Pair<String,String>(pair.getT(), (dateNum + newCount) + ""));
                    if (node != null) {
                        positiveArr[stateCounter] += Integer.parseInt(node.getValue().getPositives());
                    }
                    newCount++;
                    dayCounter++;
                }
                //adds positive number and state to same index in different arrays
                positiveArr[stateCounter] = (int)(positiveArr[stateCounter] / range);
                stateArr[stateCounter] = pair.getT();
                stateCounter++;
            }
            //integers and strings will be added to these arrays after being sorted
            int[] sortedPositives = new int[stateCounter];
            String[] sortedStates = new String[stateCounter];
            
            int arrayNum = 0;
            int k = 0;
            //sorts arrays
            while (k < stateCounter && k < 10) {
                int highNum = 0;
                for (int i = 0; i <= stateCounter; i++) {
                    if (positiveArr[i] > highNum) {
                        highNum = positiveArr[i];
                        arrayNum = i;
                    }
                }
                positiveArr[arrayNum] = 0;
                sortedPositives[k] = highNum;
                sortedStates[k] = stateArr[arrayNum];
                k++;
            }
            
            System.out.println("Top " + k + " states with the highest average daily positive cases from " +
                changeDate(firstDay) + " to " + changeDate(date));
            for (int i = 0; i < 10; i++) {
                System.out.println(sortedStates[i] + " " + sortedPositives[i]);
            }
        }
        
    }
    
  
    
    /**
     * Searches the BST for all of the data with the most recent date and prints it out
     */
    private void searchNoParam() {
        java.util.Iterator<Pair<String, String>> iter = bstDateName.iterator();
        String date = recentDate();

        if (!iter.hasNext()) {
            System.out.println("search Failed! No data available");
        }
        State[] stateArr1 = new State[totalRecords];
        int i = 0;
        while (iter.hasNext()) {
            Pair<String, String> pair = iter.next();
            String iterDate = pair.getT();
                if (date.equals(iterDate)) {
                    stateArr1[i] = bstDateName.find(pair).getValue();
                    i++;
                }
        }
        int counter = 0;
        System.out.println("There are " + i + " records on " + changeDate(date));
        System.out.println("state\tpositive\tnegative\thospitalized\tonVentilatorCurrently\tonVentilatorCumulative\t"
                + "recovered\tdataQualityGrade\tdeath");
        System.out.println(stateArr1[0].getHospitalized());
        while (counter < i) {
            System.out.println(stateArr1[counter].getState() + "\t" + changeNumbers(stateArr1[counter].getPositives()) + "\t\t" +
                    changeNumbers(stateArr1[counter].getNegatives() + "\t\t" + changeNumbers(stateArr1[counter].getHospitalized())) + "\t\t" + 
                    changeNumbers(stateArr1[counter].getOnVentilCurr()) + "\t\t\t" + changeNumbers(stateArr1[counter].getOnVentilCum()) + "\t\t\t" + 
                    changeNumbers(stateArr1[counter].getRecovered()) + "\t\t" + stateArr1[counter].getGrade() + "\t\t\t" + 
                    changeNumbers(stateArr1[counter].getDeath()));
            counter++;
        }
    }
    
    
    
    /**
     * Returns the next month in integer form
     * 
     * @param num The integer form of the current form
     * @return The next month in integer form
     */
    
      private int nextMonth(int num) {
          String str = num + "";
          StringBuilder sb = new StringBuilder();
          sb.append(str.substring(0, 4));
          if (Integer.parseInt(str.substring(4,6)) < 9) {
              sb.append("0");
              sb.append(Integer.parseInt(str.substring(4,6)) + 1);
          }
          else {
              sb.append(Integer.parseInt(str.substring(4,6)) + 1);
          }
          sb.append("01");
          
          return Integer.parseInt(sb.toString());
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
     * Finds the most recent date in the data for the search call with no arguments
     * 
     * @return the most recent date
     */
    private String recentDate() {
        java.util.Iterator<Pair<String, String>> iter = bstDateName.iterator();
        String highestDate = iter.next().getT();
        while (iter.hasNext()) {
            String newDate = iter.next().getT();
            if (Integer.valueOf(newDate) > Integer.valueOf(highestDate)) {
                highestDate = newDate;
            }
        }
        return highestDate;
    }
    /**
     * Method that adds commas to numbers for printing
     *
     * @param state string that is getting commas added to it
     * @return string with commas
     */
    private String changeNumbers(String state1) {
        String str = state1;
        Scanner scan = new Scanner(state1);
        String state = scan.next();
        scan.close();
        if (state.length() > 3) {
            StringBuilder sb = new StringBuilder();
            int len2 = state.length() % 3;

            sb.append(state.substring(0, len2));

            while (len2 != state.length()) {
                if (len2 != 0) {
                    sb.append(",");
                }
                sb.append(state.substring(len2, len2 + 3));       
                len2 += 3;
            }
            str = sb.toString();
        }
        return str;
    }
    
    
    /**
     * Performs a dump of the Binary Search Tree
     * @param option: what kind of dump to perform
     */
    public void dumpBST(BinaryNode rootNode, String spacing) {
        if (rootNode == null) {
            System.out.println(spacing + "E");
            return;
        }
        if (!rootNode.isLeaf()) {
            dumpBST(rootNode.getLeft(), spacing + "  ");
        }
        
        
        else {
            System.out.println(spacing + rootNode.getKey() + " " + rootNode.getValue().toString());
            
        }
        if (!rootNode.isLeaf()) {
            dumpBST(rootNode.getRight(), spacing + "  ");
        }
        
    }
    
    public BST<Pair<String, String>, State> getNameDate(){
        return bstNameDate;
    }
    
    public BST<Pair<String, String>, State> getDateName(){
        return bstDateName;
    }


}