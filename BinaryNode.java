/**
 * Binary Node class to be used in BST
 * @author Jonathan Rukaj
 * @version 1
 * @param <T> Generic support
 */
public class BinaryNode<T, V> {

    private T element;
    private V value;
    private BinaryNode<T, V> left;
    private BinaryNode<T, V> right;



    /**
     * Creates a node with no children.
     * @param theElement the element to store in this node.
     */
    BinaryNode(T theElement, V theValue) {
        value = theValue;
        element = theElement;
        left = null;
        right = null;
    }


    /**
     * Get the current key value stored in this node.
     * @return the element
     */
    public T getKey() {
        return element;
    }
    
    /**
    * Get the current value stored in this node.
    * @return the element
    */
   public V getValue() {
       return value;
   }
    
    

    // ----------------------------------------------------------
    /**
     * Set the data value stored in this node.
     * @param value the new data value to set
     */
    public void setElement(T key, V valueTwo) {
        element = key;
        value = valueTwo;
    }


    // ----------------------------------------------------------
    /**
     * Get the left child of this node.
     * @return a reference to the left child.
     */
    public BinaryNode<T, V> getLeft() {
        return left;
    }


    // ----------------------------------------------------------
    /**
     * Set this node's left child.
     * @param value the node to point to as the left child.
     */
    public void setLeft(BinaryNode<T, V> value) {
        left = value;
    }


    // ----------------------------------------------------------
    /**
     * Get the right child of this node.
     * @return a reference to the right child.
     */
    public BinaryNode<T, V> getRight() {
        return right;
    }


    // ----------------------------------------------------------
    /**
     * Set this node's right child.
     * @param value the node to point to as the right child.
     */
    public void setRight(BinaryNode<T, V> value) {
        right = value;
    }

    /**
     * Provides an in-order representation of the node
     * @return a string representation of the node
     */
    @Override
    public String toString() {  
        StringBuilder builder = new StringBuilder();
        if (left != null) {
            builder.append(left.toString() + ", ");
        }
        builder.append(element.toString());
        if (right != null) {
            builder.append(", " + right.toString());
        }
        return builder.toString();
    }
    
    public boolean isLeaf() {
    	boolean leaf = false;
    	if (getLeft() == null && getRight() == null) {
    		leaf = true;
    	}
    	return leaf;
    }

}