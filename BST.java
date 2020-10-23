/**
 * Binary Search Tree class
 * @author Jonathan Rukaj
 *
 * @param <T> Generic support
 * @version 1
 */
public class BST<T extends Comparable<? super T>, V extends Comparable<? super V>> implements 
    Iterable<T>, BinarySearchTree<T, V> {

    private BinaryNode<T, V> root;
     
     
    private int size;
  


    /**
     * Constructs an empty tree.
     */
    public BST() {
        root = null;
        size = 0;
    }
    
     
    /**
     * Inserts object
     * @param x 
     *            the object to be inserted
     */
    public void insert(T x, V y) {
        root = insert(x, y, root); 
        size++;
    }


    /**
     * Remove the specified value from the tree.
     *
     * @param x
     *            the item to remove.     
     */
    public void remove(T x, V y) { 
        if (x != null) {
            root = remove(x, y, root);
            size--;
        }          
    }
    //Got rid of update score
    
    public BinaryNode<T, V> find(T x) {
        return find(x, root);
    }


    /**
     * Make the tree logically empty.
     */
    public void makeEmpty() {
        root = null;
        size = 0;
    }


    /**
     * Test if the tree is logically empty.
     *
     * @return true if empty, false otherwise.
     */
    public boolean isEmpty() {
        return root == null;
    }
    /**
     * Internal method to insert a value into a subtree.
     *
     * @param x
     *            the item to insert.
     * @param node
     *            the node that roots the subtree.
     * @return the new root of the subtree.
     */
    private BinaryNode<T, V> insert(T x, V v, BinaryNode<T, V> node) {
        if (node == null) {
            return new BinaryNode<T, V>(x, v);
        }
        else if (x.compareTo(node.getKey()) < 0) {
            node.setLeft(insert(x, v, node.getLeft()));
        }
        else if (x.compareTo(node.getKey()) > 0) {
            node.setRight(insert(x, v, node.getRight()));
        }
        else {
            if (v.compareTo(node.getValue()) < 0) {
                node.setLeft(insert(x, v, node.getLeft()));
            }
            else if (v.compareTo(node.getValue()) > 0) {
                node.setRight(insert(x, v, node.getRight()));
            }
        }
        return node;
    }
  

    /**
     * Internal method to remove a specified item from a subtree.
     *
     * @param x
     *            the item to remove.
     * @param node
     *            the node that roots the subtree.
     * @return the new root of the subtree.
     * 
     */
    private BinaryNode<T, V> remove(T x, V y, BinaryNode<T, V> node) {
         // This local variable will contain the new root of the subtree,
         // if the root needs to change.
        BinaryNode<T, V> result = node;

         // If there's no more subtree to examine
        if (node == null) {
             //throw new ItemNotFoundException(x.toString());
        }

         // if value should be to the left of the root
        if (x.compareTo(node.getKey()) < 0) {
            node.setLeft(remove(x, y, node.getLeft()));
        } 
         // if value should be to the right of the root
        else if (x.compareTo(node.getKey()) > 0) {
            node.setRight(remove(x, y, node.getRight()));
        }
        else if (x.compareTo(node.getKey()) == 0 && y.compareTo(node.getValue()) != 0) {
            if (y.compareTo(node.getValue()) < 0) {
                node.setLeft(remove(x, y, node.getLeft()));
            }
            else if (y.compareTo(node.getValue()) > 0) {
                node.setRight(remove(x, y, node.getRight()));
            }
        }
         // If value is on the current node
        else
        {
             // If there are two children
            if (node.getLeft() != null && node.getRight() != null) {

                BinaryNode<T, V> min = minValue(node.getRight());
                T t = min.getKey();
                V v = min.getValue();
                BinaryNode<T, V> temp = remove(min.getKey(), min.getValue(), node);
                node.setElement(t, v);
                
                result = node;
            }
             // If there is only one child on the left
            else if (node.getLeft() != null) { 
                result = node.getLeft();
            }
             // If there is only one child on the right
            else {
                result = node.getRight();
            }
        }
        return result;
    } 




    /**
     * Internal method to find an item in a subtree.
     *
     * @param x
     *            is item to search for.
     * @param node
     *            the node that roots the tree.
     * @return node containing the matched item.
     */
    private BinaryNode<T, V> find(T x, BinaryNode<T, V> node) { 
        if (node == null) {
            return null; // Not found
        }
        else if (x.compareTo(node.getKey()) < 0) {
            // Search in the left subtree 
            return find(x, node.getLeft());
        }
        else if (x.compareTo(node.getKey()) > 0) {
            // Search in the right subtree
            return find(x, node.getRight());
        }
        else {
            return node; // Match
        }
    }
    
    
    private BinaryNode<T, V> minValue(BinaryNode<T, V> root) 
    { 
        BinaryNode<T, V> minT = root; 
        
        while (root.getLeft() != null) 
        { 
            minT = root.getLeft(); 
            root = root.getLeft(); 
        } 
        
        return minT; 
    } 

    /**
     * Gets an in-order string representation of the tree
     * If the tree holds  5
     *                  /   \
     *                2      6
     *                 \
     *                  3
     * It would print (2, 3, 5, 6)
     * @return an in-order string representation of the tree
     */
    @Override
    public String toString() {
        if (root == null) {
            return "()";
        }
        else {
            return "(" + root.toString() + ")";
        }
    }
     
    /**
     * Gets the size of the tree
     * @return integer representing size
     */
    public int getSize() {
        return this.size;
    }

    /**
     * Makes new iterator
     * @return an iterator object
     */
    @Override
    public java.util.Iterator<T> iterator() {
        return new BSTIterator<T>(root);
    }   
    
  
}