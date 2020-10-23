import java.io.FileNotFoundException;
import static org.junit.Assert.*; 
import student.TestCase;

public class TestEverything extends student.TestCase {

	private LoadDataP2 load;
	
	public void setUp() {
	    load = new LoadDataP2();
	}
	
	public void testDump() throws FileNotFoundException {
		load.load("Sample_Input1.csv");
		load.dumpBST(load.getDateName().getRoot(), "");
		String output = systemOut().getHistory();
		assertEquals("", output);
	}

}
