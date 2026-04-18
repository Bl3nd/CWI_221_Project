import java.util.*;

/**
 * Array-based implementation of IndexedUnsortedList.
 * 
 * @author 
 *
 * @param <E> type to store
 */
public class IUArrayList<E> implements IndexedUnsortedList<E> {
	private static final int DEFAULT_CAPACITY = 10;
	private static final int NOT_FOUND = -1;
	
	private E[] array;
	private int rear;
	private int modCount; // DO NOT REMOVE ME
	
	/** Creates an empty list with default initial capacity */
	public IUArrayList() {
		this(DEFAULT_CAPACITY);
	}
	
	/** 
	 * Creates an empty list with the given initial capacity
	 * @param initialCapacity
	 */
	@SuppressWarnings("unchecked")
	public IUArrayList(int initialCapacity) {
		array = (E[])(new Object[initialCapacity]);
		rear = 0;
		modCount = 0; // DO NOT REMOVE ME
	}
	
	/** Double the capacity of array */
	private void expandCapacity() {
		array = Arrays.copyOf(array, array.length*2);
	}

	@Override
	public void addToFront(E element) {
		if (rear == array.length) {
			expandCapacity();
		}
		for (int i = rear; i > 0; i--) {
			array[i] = array[i - 1];
		}
		array[0] = element;
		rear++;
		modCount++; // DO NOT REMOVE ME
	}

	@Override
	public void addToRear(E element) {
		if (rear == array.length) {
			expandCapacity();
		}
		array[rear] = element;
		rear++;
		modCount++; // DO NOT REMOVE ME
	}

	@Override
	public void add(E element) {
		if (rear == array.length) {
			expandCapacity();
		}
		array[rear] = element;
		rear++;
		modCount++; // DO NOT REMOVE ME
	}

	@Override
	public void addAfter(E element, E target) {
		int index = indexOf(target);
		if (index == NOT_FOUND) {
			throw new NoSuchElementException();
		}

		if (rear == array.length) {
			expandCapacity();
		}

		for (int i = rear; i > index + 1; i--) {
			array[i] = array[i - 1];
		}

		array[index + 1] = element;
		rear++;
		modCount++; // DO NOT REMOVE ME
	}

	@Override
	public void add(int index, E element) {
		if (index < 0 || index > rear) {
			throw new IndexOutOfBoundsException();
		}

		if (rear == array.length) {
			expandCapacity();
		}

		for (int i = rear; i > index; i--) {
			array[i] = array[i - 1];
		}

		array[index] = element;
		rear++;
		modCount++; // DO NOT REMOVE ME
	}

	@Override
	public E removeFirst() {
		if (isEmpty()) {
			throw new NoSuchElementException();
		}

		E removed = array[0];

		for (int i = 0; i < rear - 1; i++) {
			array[i] = array[i + 1];
		}

		rear--;
		array[rear] = null;

		modCount++; // DO NOT REMOVE ME
		return removed;
	}

	@Override
	public E removeLast() {
		if (isEmpty()) {
			throw new NoSuchElementException();
		}

		rear--;
		E removed = array[rear];
		array[rear] = null;

		modCount++; // DO NOT REMOVE ME
		return removed;
	}

	@Override
	public E remove(int index) {
		if (index < 0 || index >= rear) {
			throw new IndexOutOfBoundsException();
		}

		E removed = array[index];

		for (int i = index; i < rear - 1; i++) {
			array[i] = array[i + 1];
		}

		rear--;
		array[rear] = null;

		modCount++; // DO NOT REMOVE ME
		return removed;
	}

	@Override
	public void set(int index, E element) {
		if (index < 0 || index >= rear) {
			throw new IndexOutOfBoundsException();
		}

		array[index] = element;
		modCount++; // DO NOT REMOVE ME
	}

	@Override
	public E get(int index) {
		if (index < 0 || index >= rear) {
			throw new IndexOutOfBoundsException();
		}

		return array[index];
	}

	@Override
	public E first() {
		if (isEmpty()) {
			throw new NoSuchElementException();
		}
		return array[0];
	}

	@Override
	public E last() {
		if (isEmpty()) {
			throw new NoSuchElementException();
		}
		return array[rear - 1];
	}

	@Override
	public boolean isEmpty() {
		return rear == 0;
	}

	@Override
	public int size() {
		return rear;
	}

	@Override
	public int indexOf(E element) {
		for (int i = 0; i < rear; i++) {
			if ((array[i] == null && element == null) ||
				(array[i] != null && array[i].equals(element))) {
				return i;
			}
		}
		return NOT_FOUND;
	}

	@Override
	public boolean contains(E element) {
		return indexOf(element) != NOT_FOUND;
	}

	@Override
	public E remove(E element) {
		int index = indexOf(element);
		if (index == NOT_FOUND) {
			throw new NoSuchElementException();
		}
		return remove(index);
	}

	@Override
	public String toString() {
		String result = "[";
		for (int i = 0; i < rear; i++) {
			result += array[i];
			if (i < rear - 1) {
				result += ", ";
			}
		}
		result += "]";
		return result;
	}

	// IGNORE THE FOLLOWING COMMENTED OUT CODE UNTIL LAB 10

	// DON'T DELETE ME, HOWEVER!!!
	public Iterator<E> iterator() {
		// return new IUArrayListIterator(); // UNCOMMENT ME IN LAB 10
		return null; // REMOVE ME IN LAB 10
	}

	// UNCOMMENT THE CODE BELOW IN LAB 10
	
	// private class IUArrayListIterator implements Iterator<E> {

	// 	private int iterModCount, current;
	// 	private boolean canRemove;

	// 	public IUArrayListIterator() {
	// 		iterModCount = modCount;
	// 		current = 0;
	// 		canRemove = false;
	// 	}

	// 	@Override
	// 	public boolean hasNext() {
    //         if (iterModCount != modCount) {
    //             throw new ConcurrentModificationException();
    //         }
    //         return current < rear;
	// 	}

	// 	@Override
	// 	public E next() {
    //         if (!hasNext()) {
    //             throw new NoSuchElementException();
    //         }
    //         E item = array[current];
	// 		current++;
    //         canRemove = true;
    //         return item;
	// 	}

	// 	@Override
	// 	public void remove() {
    //         if (iterModCount != modCount) {
    //             throw new ConcurrentModificationException();
    //         }
    //         if (!canRemove) {
    //             throw new IllegalStateException();
    //         }
    //         // remove the element in the array at index current-1
    //         // presumably decrement the rear
    //         // presumably the modCount is getting incremented
	// 		// all indices have to back up by one
	// 		current--;
	// 		rear--;
	// 		// shift elements to the left
	// 		for (int i = current; i < rear; i++) {
	// 			array[i] = array[i + 1];
	// 		}
	// 		array[rear] = null;
	// 		modCount++;
	// 		iterModCount++;
	// 		// Can only remove the LAST "seen" element
	// 		// set back to a non-removal state 
    //         canRemove = false;
	// 	}
		
	// }

	// IGNORE THE FOLLOWING CODE
	// DON'T DELETE ME, HOWEVER!!!
	@Override
	public ListIterator<E> listIterator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ListIterator<E> listIterator(int startingIndex) {
		// TODO Auto-generated method stub
		return null;
	}

}
