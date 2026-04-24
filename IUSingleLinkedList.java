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

    private LinearNode<E> head;
    private LinearNode<E> rear;
    private int size;
    private int modCount;

    public IUSingleLinkedList() {
        head = rear = null;
        size = 0;
        modCount = 0;
    }

    // ---------- ADD METHODS ----------

    public void addToFront(E element) {
        LinearNode<E> node = new LinearNode<>(element);
        node.setNext(head);
        head = node;

        if (size == 0) rear = node;

        size++;
        modCount++;
    }

    public void addToRear(E element) {
        LinearNode<E> node = new LinearNode<>(element);

        if (size == 0) {
            head = rear = node;
        } else {
            rear.setNext(node);
            rear = node;
        }

        size++;
        modCount++;
    }

    public void add(E element) {
        addToRear(element);
    }

    public void addAfter(E element, E target) {
        LinearNode<E> current = head;

        while (current != null && !current.getElement().equals(target)) {
            current = current.getNext();
        }

        if (current == null) throw new NoSuchElementException();

        LinearNode<E> node = new LinearNode<>(element);
        node.setNext(current.getNext());
        current.setNext(node);

        if (current == rear) rear = node;

        size++;
        modCount++;
    }

    public void add(int index, E element) {
        if (index < 0 || index > size) throw new IndexOutOfBoundsException();

        if (index == 0) {
            addToFront(element);
            return;
        }

        if (index == size) {
            addToRear(element);
            return;
        }

        LinearNode<E> previous = getNode(index - 1);
        LinearNode<E> node = new LinearNode<>(element);

        node.setNext(previous.getNext());
        previous.setNext(node);

        size++;
        modCount++;
    }

    // ---------- REMOVE METHODS ----------

    public E removeFirst() {
        if (isEmpty()) throw new NoSuchElementException();

        E result = head.getElement();
        head = head.getNext();

        if (head == null) rear = null;

        size--;
        modCount++;
        return result;
    }

    public E removeLast() {
        if (isEmpty()) throw new NoSuchElementException();

        if (size == 1) return removeFirst();

        LinearNode<E> previous = getNode(size - 2);
        E result = rear.getElement();

        previous.setNext(null);
        rear = previous;

        size--;
        modCount++;
        return result;
    }

    public E remove(E element) {
        if (isEmpty()) throw new NoSuchElementException();

        LinearNode<E> current = head;
        LinearNode<E> previous = null;

        while (current != null && !current.getElement().equals(element)) {
            previous = current;
            current = current.getNext();
        }

        if (current == null) throw new NoSuchElementException();

        return unlink(previous, current);
    }

    public E remove(int index) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException();

        if (index == 0) return removeFirst();

        LinearNode<E> previous = getNode(index - 1);
        return unlink(previous, previous.getNext());
    }

    private E unlink(LinearNode<E> previous, LinearNode<E> current) {
        E result = current.getElement();

        if (previous == null) {
            head = current.getNext();
        } else {
            previous.setNext(current.getNext());
        }

        if (current == rear) {
            rear = previous;
        }

        size--;
        modCount++;
        return result;
    }

    // ---------- ACCESS METHODS ----------

    public void set(int index, E element) {
        getNode(index).setElement(element);
        modCount++;
    }

    public E get(int index) {
        return getNode(index).getElement();
    }

    public int indexOf(E element) {
        LinearNode<E> current = head;
        int i = 0;

        while (current != null) {
            if (current.getElement().equals(element)) return i;
            current = current.getNext();
            i++;
        }

        return -1;
    }

    public E first() {
        if (isEmpty()) throw new NoSuchElementException();
        return head.getElement();
    }

    public E last() {
        if (isEmpty()) throw new NoSuchElementException();
        return rear.getElement();
    }

    public boolean contains(E target) {
        return indexOf(target) != -1;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    private LinearNode<E> getNode(int index) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException();

        LinearNode<E> current = head;
        for (int i = 0; i < index; i++) {
            current = current.getNext();
        }
        return current;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        LinearNode<E> current = head;

        while (current != null) {
            sb.append(current.getElement());
            if (current.getNext() != null) sb.append(", ");
            current = current.getNext();
        }

        return sb.append("]").toString();
    }

    // ---------- ITERATOR ----------

    public Iterator<E> iterator() {
        return new SLLIterator();
    }

    private class SLLIterator implements Iterator<E> {

        private LinearNode<E> previous = null;
        private LinearNode<E> current = null;
        private LinearNode<E> next = head;
        private int expectedMod = modCount;
        private boolean canRemove = false;

        public boolean hasNext() {
            checkMod();
            return next != null;
        }

        public E next() {
            checkMod();
            if (next == null) throw new NoSuchElementException();

            previous = current;
            current = next;
            next = next.getNext();

            canRemove = true;
            return current.getElement();
        }

        public void remove() {
            checkMod();
            if (!canRemove) throw new IllegalStateException();

            if (current == head) {
                head = head.getNext();
                if (head == null) rear = null;
            } else {
                previous.setNext(current.getNext());
                if (current == rear) rear = previous;
            }

            current = previous;
            size--;
            modCount++;
            expectedMod++;
        }

        private void checkMod() {
            if (expectedMod != modCount)
                throw new ConcurrentModificationException();
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
