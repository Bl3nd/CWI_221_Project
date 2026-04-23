import java.util.*;

/**
 * Single-linked node implementation of IndexedUnsortedList.
 * An Iterator with working remove() method is implemented, but
 * ListIterator is unsupported.
 * 
 * @author 
 * 
 * @param <E> type to store
 */
public class IUSingleLinkedList<E> implements IndexedUnsortedList<E> {
	private LinearNode<E> front, rear;
	private int count;
	private int modCount;
	
	/** Creates an empty list */
	public IUSingleLinkedList() {
		front = rear = null;
		count = 0;
		modCount = 0;
	}

	public void addToFront(E element) {
		LinearNode<E> newNode = new LinearNode<>(element);
        newNode.setNext(front);
        front = newNode;
        if (rear == null) { rear = front; }
        count++;
        modCount++; 
		
	}

	public void addToRear(E element) {
		LinearNode<E> newNode = new LinearNode<>(element);
        if (isEmpty()) {
            front = newNode;
        } else {
            rear.setNext(newNode);
        }
        rear = newNode;
        count++;
        modCount++;
	}

	public void add(E element) {
		addToRear(element);
	}

	public void addAfter(E element, E target) {
		LinearNode<E> current = front;
        while (current != null && !current.getElement().equals(target)) {
            current = current.getNext();
        }
        if (current == null) { throw new NoSuchElementException(); }
        
        LinearNode<E> newNode = new LinearNode<>(element);
        newNode.setNext(current.getNext());
        current.setNext(newNode);
        if (current == rear) { rear = newNode; }
        count++;
        modCount++;
	}

	public void add(int index, E element) {
		if (index < 0 || index > count) { throw new IndexOutOfBoundsException(); }
        if (index == 0) {
            addToFront(element);
        } else if (index == count) {
            addToRear(element);
        } else {
            LinearNode<E> prev = front;
            for (int i = 0; i < index - 1; i++) { prev = prev.getNext(); }
            LinearNode<E> newNode = new LinearNode<>(element);
            newNode.setNext(prev.getNext());
            prev.setNext(newNode);
            count++;
            modCount++;
		}
	}

	public E removeFirst() {
		if (isEmpty()) { throw new NoSuchElementException(); }
        return removeElement(null, front);
	}

	public E removeLast() {
		if (isEmpty()) { throw new NoSuchElementException(); }
        LinearNode<E> prev = null;
        LinearNode<E> current = front;
        while (current.getNext() != null) {
            prev = current;
            current = current.getNext();
        }
        return removeElement(prev, current);
	}

	public E remove(E element) {
		if (isEmpty()) { throw new NoSuchElementException(); }
		LinearNode<E> current = front, previous = null;
		while (current != null && !current.getElement().equals(element)) {
			previous = current;
			current = current.getNext();
		}
		// Matching element not found
		if (current == null) {
			throw new NoSuchElementException();
		}
		return removeElement(previous, current);		
	}

	public E remove(int index) {
		if (index < 0 || index >= count) { throw new IndexOutOfBoundsException(); }
        LinearNode<E> prev = null;
        LinearNode<E> current = front;
        for (int i = 0; i < index; i++) {
            prev = current;
            current = current.getNext();
        }
        return removeElement(prev, current);
	}

	public void set(int index, E element) {
		if (index < 0 || index >= count) { throw new IndexOutOfBoundsException(); }
        LinearNode<E> current = front;
        for (int i = 0; i < index; i++) { current = current.getNext(); }
        current.setElement(element);
        modCount++;
	}

	public E get(int index) {
        if (index < 0 || index >= count) { throw new IndexOutOfBoundsException(); }
        LinearNode<E> current = front;
        for (int i = 0; i < index; i++) { current = current.getNext(); }
        return current.getElement();
    }

    public int indexOf(E element) {
        int index = 0;
        LinearNode<E> current = front;
        while (current != null) {
            if (current.getElement().equals(element)) { return index; }
            current = current.getNext();
            index++;
        }
        return -1;
    }

    public E first() {
        if (isEmpty()) { throw new NoSuchElementException(); }
        return front.getElement();
    }

    public E last() {
        if (isEmpty()) { throw new NoSuchElementException(); }
        return rear.getElement();
    }

    public boolean contains(E target) {
        return indexOf(target) != -1;
    }

    public boolean isEmpty() { return count == 0; }

    public int size() { return count; }

    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        LinearNode<E> current = front;
        while (current != null) {
            sb.append(current.getElement());
            if (current.getNext() != null) { sb.append(", "); }
            current = current.getNext();
        }
        sb.append("]");
        return sb.toString();
    }

	private E removeElement(LinearNode<E> previous, LinearNode<E> current) {
		// Grab element
		E result = current.getElement();
		// If not the first element in the list
		if (previous != null) {
			previous.setNext(current.getNext());
		} else { // If the first element in the list
			front = current.getNext();
		}
		// If the last element in the list
		if (current.getNext() == null) {
			rear = previous;
		}
		count--;
		modCount++;

		return result;
	}

	public Iterator<E> iterator() { return new SLLIterator(); }

	/** Iterator for IUSingleLinkedList */
	private class SLLIterator implements Iterator<E> {
		private LinearNode<E> previous, current, next;
		private int iterModCount;
		
		/** Creates a new iterator for the list */
		public SLLIterator() {
			previous = null;
			current = null;
			next = front;
			iterModCount = modCount;
		}

		@Override
		public boolean hasNext() {
			if (iterModCount != modCount) { throw new ConcurrentModificationException(); }
            return next != null;
		}

		@Override
		public E next() {
			if (!hasNext()) { throw new NoSuchElementException(); }
            previous = current;
            current = next;
            next = next.getNext();
            return current.getElement();
		}
		
		@Override
		public void remove() {
			if (iterModCount != modCount) { throw new ConcurrentModificationException(); }
            if (current == null) { throw new IllegalStateException(); }
            
            if (current == front) {
                front = next;
            } else {
                previous.setNext(next);
            }
            
            if (current == rear) {
                rear = previous;
            }
            
            current = null;
            count--;
            modCount++;
            iterModCount++;
		}
	}

	// IGNORE THE FOLLOWING CODE
	// DON'T DELETE ME, HOWEVER!!!
	public ListIterator<E> listIterator() {
		throw new UnsupportedOperationException();
	}

	public ListIterator<E> listIterator(int startingIndex) {
		throw new UnsupportedOperationException();
	}
}
