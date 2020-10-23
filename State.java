/**
 * Class that stores one line of data in the data file
 * 
 * @author Drew Wissler 906106464
 * @author Jonathan Rukaj
 * @version 29.09.2020
 */
public class State implements Comparable<State> {
    //State abbreviation 
    private String stateAbv;
    //Date of record
    private String date;
    //# of positives
    private String positives;
    //# of negatives
    private String negatives;
    //# of people hospitalized
    private String hospitalized;
    //# of people currently on a ventilator
    private String onVentilCurr;
    //# of people cumulatively on a ventilator
    private String onVentilCum;
    //# of people recovered
    private String recovered;
    // Data Quality Grade
    private String grade;
    //# of people who have died
    private String death;
    
    /**
     * New State Object
     */
    public State() {
        // nothing to initialize
    } 
    
    /**
     * Stores info into the state
     * 
     * @param dataArr Array of Strings with state values
     * @return The state with filled values
     */
    public State stateInfo(String[] dataArr) {
        date = dataArr[0];
        stateAbv = dataArr[1];
        positives = dataArr[2];
        negatives =dataArr[3];
        hospitalized = dataArr[4];
        onVentilCurr = dataArr[5];
        onVentilCum = dataArr[6];
        recovered = dataArr[7];
        grade = dataArr[8];
        death = dataArr[9];
        return this;
    }
    /**
     * Compares 2 objects for the BST
     * 
     * @param state The state being compared
     */
    public int compareTo(State state) {
        if (this.getState().compareTo(state.getState()) > 0) {
            return 1;
        }
        else if (this.getState().compareTo(state.getState()) < 0) {
            return -1;
        }
        else {
            return 0;
        }
    }
    
    /**
     * Returns date
     * @return date
     */
    public String getDate() {
        return date;
    } 
    /**
     * Returns state Abbreviation
     * @return state Abbreviation
     */
    public String getState() {
        return stateAbv;
    }
    /**
     * Returns number of positives
     * @return number of positives
     */
    public String getPositives() {
        return positives;
    } 
    /**
     * Returns number of negatives
     * @return number of negatives
     */
    public String getNegatives() {
        return negatives;
    } 
    /**
     * Returns number of people hospitalized
     * @return number of people hospitalized
     */
    public String getHospitalized() {
        return hospitalized;
    } 
    /**
     * Returns number of people on Ventilation currently
     * @return number of people on Ventilation currently
     */
    public String getOnVentilCurr() {
        return onVentilCurr;
    }
    /**
     * Returns number of people on Ventilation cumulatively
     * @return number of people on Ventilation cumulatively
     */
    public String getOnVentilCum() {
        return onVentilCum;
    }
    /**
     * Returns number of people who recovered
     * @return number of people who recovered
     */
    public String getRecovered() {
        return recovered;
    }
    /**
     * Returns the data quality grade
     * @return data quality grade
     */
    public String getGrade() {
        return grade;
    }
    /**
     * Returns number of people who died
     * @return number of people who died
     */
    public String getDeath() {
        return death;
    }
    /**
     * Sets the date
     */
    public void setDate(String str) {
        date = str;
    } 
    /**
     * Sets the state abbreviation
     */
    public void setState(String str) {
        stateAbv = str;
    } 
    /**
     * Sets the positive cases
     */
    public void setPositives(String str) {
        positives = str;
    } 
    /**
     * Sets the negative cases
     */
    public void setNegatives(String str) {
        negatives = str;
    } 
    /**
     * Sets the number of people hospitalized
     */
    public void setHospitalized(String str) {
        hospitalized = str;
    } 
    /**
     * Sets the number of people on a ventilation currently
     */
    public void setOnVentilCurr(String str) {
        onVentilCurr = str;
    } 
    /**
     * Sets the number of people on a ventilation cummulatively
     */
    public void setOnVentilCum(String str) {
        onVentilCum = str;
    } 
    /**
     * Sets the number of people who are recovered
     */
    public void setRecovered(String str) {
        recovered = str;
    } 
    /**
     * Sets the quality grade
     */
    public void setGrade(String str) {
        grade = str;
    }
    /**
     * Sets the number of people who died
     */
    public void setDeath(String str) {
        death = str;
    } 
    
    public String toString() {
    	return death;
    }
}
