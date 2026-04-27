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
public class IUSingleLinkedList<E extends Comparable<E>> implements IndexedUnsortedList<E> {
	private LinearNode<E> front, rear;
	private int count;
	private int modCount;
	
	/** Creates an empty list */
	public IUSingleLinkedList() {
		front = rear = null;
		count = 0;
		modCount = 0;
	}

	@Override
	public void addToFront(E element) {
		// TODO 
		LinearNode<E> node = new LinearNode<E>(element);
		node.setNext(front);
		front = node;

		if (count == 0) {
			rear = front;
		}
		count++;
		modCount++;
		
	}

	@Override
	public void addToRear(E element) {
		// TODO 
		LinearNode<E> node = new LinearNode<E>(element);
		if (isEmpty()) { 
			front = rear = node;
		} else {
			rear.setNext(node);
			rear = node;
		}
		
		count++;
		modCount++;
		
	}

	@Override
	public void add(E element) {
		// TODO 
		
		addToRear(element);
		
	}

	@Override
	public void addAfter(E element, E target) {
		// TODO 
		if (!contains(target)) {
			throw new NoSuchElementException();
		}
		LinearNode<E> current = front;
		while (current != null && !current.getElement().equals(target)) {
			current = current.getNext();
		}

		LinearNode<E> node = new LinearNode<E>(element);
		node.setNext(current.getNext());
		current.setNext(node);

		if (current == rear) {
			rear = node;
		}

		count++;
		modCount++;

	}

	@Override
	public void add(int index, E element) {
		// TODO 
		if (index < 0 || index > count) {
			throw new IndexOutOfBoundsException();
		}
		
		if(index == 0) {
			addToFront(element);
		} else if (index == count) {
			addToRear(element);
		
		} else { 

			LinearNode<E> current = front, previous = null;
			for (int i = 0; i < index; i++) {
				previous = current;
				current = current.getNext();
			}
	
			LinearNode<E> node = new LinearNode<E>(element);
			node.setNext(current);
			previous.setNext(node);

			count++;
			modCount++;
		}

	}

	@Override
	public E removeFirst() {
		// TODO 
		return remove(first());

	}

	@Override
	public E removeLast() {
		// TODO 
		return remove(last());

	}

	@Override
	public E remove(E element) {
		if (isEmpty()) {
			throw new NoSuchElementException();
		}
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

	@Override
	public E remove(int index) {
		// TODO
		if (index < 0 || index >= count) {
			throw new IndexOutOfBoundsException();
		}

		LinearNode<E> current = front, previous = null;

		for (int i = 0; i < index; i++) {
			previous = current;
			current = current.getNext();
		}
		return removeElement(previous, current);
	}

	@Override
	public void set(int index, E element) {
		// TODO 
		if (index < 0 || index >= count) {
			throw new IndexOutOfBoundsException();
		}

		LinearNode<E> current = front;
		for (int i = 0; i < index; i++) {
			current = current.getNext();
		}
		current.setElement(element);
		modCount++;
		
	}

	@Override
	public E get(int index) {
		// TODO 
		if (index < 0 || index >= count) {
			throw new IndexOutOfBoundsException();
		}

		LinearNode<E> current = front;
		for (int i = 0; i < index; i++) {
			current = current.getNext();
		}

		return current.getElement();
	}

	@Override
	public int indexOf(E element) {
		// TODO 
		LinearNode<E> current = front;
		int index = 0;
		
		while (current != null) {
			if(current.getElement().equals(element)) {
				return index;
			}

			current = current.getNext();
			index++;
		}

		return -1;
	}

	@Override
	public E first() {
		// TODO 
		if (isEmpty()) {
			throw new NoSuchElementException();
		}
		return front.getElement();
	}

	@Override
	public E last() {
		// TODO 
		if (isEmpty()) {
			throw new NoSuchElementException();
		}
		return rear.getElement();
	}

	@Override
	public boolean contains(E target) {
		// TODO 
		return indexOf(target) != -1;
	}

	@Override
	public boolean isEmpty() {
		// TODO 
		return size() == 0;
	}

	@Override
	public int size() {
		// TODO 
		return count;
	}

	@Override
	public String toString() {
		// TODO

		String result = "[";
    	LinearNode<E> current = front;
    	while (current != null) {
        	if (current != front) {
            	result += ", ";
        	}

        result += current.getElement();
        current = current.getNext();
    	}

    	result += "]";
    	return result;
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

	@Override
	public Iterator<E> iterator() {
		return new SLLIterator();
	}

	/** Iterator for IUSingleLinkedList */
	private class SLLIterator implements Iterator<E> {
		private LinearNode<E> previous;
		private LinearNode<E> current;
		private LinearNode<E> next;
		private int iterModCount;
		private boolean canRemove;
		
		/** Creates a new iterator for the list */
		public SLLIterator() {
			previous = null;
			current = null;
			next = front;
			iterModCount = modCount;
			canRemove = false;
		}

		@Override
		public boolean hasNext() {
			// TODO 
			if (iterModCount != modCount) {
				throw new ConcurrentModificationException();
			}
            return next != null;
		}

		@Override
		public E next() {
			// TODO 
			if (!hasNext()) {
				throw new NoSuchElementException();
			}
			previous = current;
			current = next;
			next = next.getNext();
			canRemove = true;
			return current.getElement();
			
		}
		
		@Override
		public void remove() {
			// TODO
			if (iterModCount != modCount) {
				throw new ConcurrentModificationException();
			}

			if (!canRemove) {
				throw new IllegalStateException();
			}

			if ( current == front) {
				front = front.getNext();

				if (current == null) {
					rear = null;
				}

				current = null;
			} else {
				LinearNode<E> tempNode = front;
				while(tempNode != null && tempNode.getNext() != current) {
					tempNode = tempNode.getNext();
				}

				tempNode.setNext(current.getNext());
				if (current == rear) {
					rear = tempNode;
				}

				current = previous;
			}

			count--;
			iterModCount++;
			modCount++;


		}
	}

	// IGNORE THE FOLLOWING CODE
	// DON'T DELETE ME, HOWEVER!!!
	@Override
	public ListIterator<E> listIterator() {
		throw new UnsupportedOperationException();
	}

	@Override
	public ListIterator<E> listIterator(int startingIndex) {
		throw new UnsupportedOperationException();
	}
}
