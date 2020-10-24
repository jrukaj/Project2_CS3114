
public class Helper {

	public void remove(String grade) {
    	System.out.println(grade);
    	removeHelper(bstNameDate, bstDateName, bstGrade, grade);
    	System.out.println(grade);
    }
    
    public void removeHelper(BST tree1, BST tree2, BST tree3, String grade) {
    	int count1 = removeFromSingularTree(tree1, grade, tree1.getRoot());
    	int count2 = removeFromSingularTree(tree2, grade, tree2.getRoot());
    	int count3 = removeFromSingularTree(tree3, grade, tree3.getRoot());
    	int totalCount = count1 + count2 + count3;
    	System.out.println(totalCount + " records with quality grade lower or equal to  " + grade + " have been removed");
    }
    
    private int removeFromSingularTree(BST tree, String grade, BinaryNode rootNode) {
    	int count = 0;
    	if (rootNode == null) {
    		return count;
    	}
    	if (rootNode.getValue().toString().toLowerCase().compareTo(grade.toLowerCase()) <= 0) {
    		tree.remove((Pair)rootNode.getKey(), (State)rootNode.getValue());
    		count++;
    	}
    	
    	removeFromSingularTree(tree, grade, rootNode.getLeft());
    	System.out.println(rootNode.getKey().toString());
    	removeFromSingularTree(tree, grade, rootNode.getRight());
    	return count;
    }
}
