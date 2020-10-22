/**
 * BST interface
 * @author Jonathan Rukaj
 * @version 1
 * @param <T> generic support
 */
public interface BinarySearchTree<T, V> {
    
    /**
     * Insert
     * @param x
     *            object to be inserted
     */
    public  void insert(T x, V y);

    /**
     * Remove
     * @param x
     *            object to remove
     */
    public void remove(T x, V y);

    /**
     * Find  
     * @param x
     *            object to find
     * @return reference to found object
     */
    public BinaryNode<T, V> find(T x, V v);

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