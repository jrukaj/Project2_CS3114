/**
 * Class that represents a pair of values.
 * @author Jonathan Rukaj
 * @author Drew Wissler
 * @version 1.0
 * @date 10.22.2020
 */
public class Pair<T extends Comparable<T>, K extends Comparable<K>>
    implements Comparable<Pair<T, K>>{
	
	private T t;
	private K k;

	/**
	 * Constructor for Pair object
	 * @param t: generic parameter
	 * @param k: generic parameter
	 */
	public Pair(T t, K k) {
		this.t = t;
		this.k = k;
	}
	
	/**
	 * Returns the T field
	 * @return private field t
	 */
	public T getT() {
		return t;
	}
	
	/**
	 * Returns the K field
	 * @return private field k
	 */
	public K getK() {
		return k;
	}
	
	/**
	 * Compares the Pair object to another object
	 * @param o: the object to be compared
	 * @return 0 if they are the same, -1 if Pair is less than the other,
	 * and 1 if our Pair is greater than the other
	 */
	@Override
	public int compareTo(Pair<T, K> o) {
		Pair<T, K> pair = (Pair<T, K>)o;
		T pairT = pair.getT();
		K pairK = pair.getK();
		if (t == null || pairT == null || t.compareTo(pairT) == 0) {
			if (pairK == null || k == null) {
				return 0;
			}
			return k.compareTo(pairK);
		}
		return t.compareTo(pairT);
	}
}
