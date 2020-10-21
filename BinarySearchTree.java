/**
 * BST interface
 * @author Jonathan Rukaj
 * @version 1
 * @param <T> generic support
 */
public interface BinarySearchTree<T> {
    
    /**
     * Insert
	 * @param x
	 *            object to be inserted
	 */
    public  void insert(T x);

	/**
	 * Remove
	 * @param x
	 *            object to remove
	 */
    public void remove(T x);

    /**
     * Find  
     * @param x
     *            object to find
     * @return reference to found object
     */
    public T find(T x);

    /**
     * Make tree empty  
     */
    public void makeEmpty();

    /**
     * Is empty   
     * @return true if tree is empty
     */
    public boolean isEmpty();
    

}
