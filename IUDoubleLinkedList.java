import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

public class IUDoubleLinkedList<E> implements IndexedUnsortedList<E> {

    private BidirectionalNode<E> front, rear;
    private int count, modCount;

    public IUDoubleLinkedList() {
        front = rear = null;
        count = 0;
        modCount = 0;
    }

    @Override
    public void addToFront(E element) {
        BidirectionalNode<E> newNode = new BidirectionalNode<>(element);
        if (isEmpty()) {
            front = rear = newNode;
        } else {
            newNode.setNext(front);
            front.setPrevious(newNode);
            front = newNode;
        }

        count++;
        modCount++;
    }

    @Override
    public void addToRear(E element) {
        BidirectionalNode<E> newNode = new BidirectionalNode<>(element);
        if (isEmpty()) {
            front = rear = newNode;
        } else {
            newNode.setPrevious(rear);
            rear.setNext(newNode);
            rear = newNode;
        }

        count++;
        modCount++;
    }

    @Override
    public void add(E element) {
        addToRear(element);
    }

    @Override
    public void addAfter(E element, E target) {
        if (!contains(target)) {
            throw new NoSuchElementException();
        }

        BidirectionalNode<E> current = front;
        while (current != null && !current.getElement().equals(target)) {
            current = current.getNext();
        }

        BidirectionalNode<E> newNode = new BidirectionalNode<>(element);
        newNode.setNext(current.getNext());
        newNode.setPrevious(current);
        if (current.getNext() != null) {
            current.getNext().setPrevious(newNode);
        }
        current.setNext(newNode);
        if (current == rear) {
            rear = newNode;
        }

        count++;
        modCount++;
    }

    @Override
    public void add(int index, E element) {
        if (index < 0 || index > count) {
            throw new IndexOutOfBoundsException();
        }

        if (index == 0) {
            addToFront(element);
        } else if (index == count) {
            addToRear(element);
        } else {
            BidirectionalNode<E> current = front, previous = null;
            for (int i = 0; i < index; i++) {
                previous = current;
                current = current.getNext();
            }

            BidirectionalNode<E> newNode = new BidirectionalNode<>(element);
            newNode.setNext(current);
            newNode.setPrevious(previous);
            current.setPrevious(newNode);
            if (previous != null) {
                previous.setNext(newNode);
            } else {
                front = newNode;
            }

            count++;
            modCount++;
        }
    }

    @Override
    public E removeFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }

        return removeElement(null, front);
    }

    @Override
    public E removeLast() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }

        return removeElement(rear.getPrevious(), rear);
    }

    @Override
    public E remove(E element) {
        if (!contains(element)) {
            throw new NoSuchElementException();
        }

        BidirectionalNode<E> current = front, previous = null;
        while (current != null && !current.getElement().equals(element)) {
            previous = current;
            current = current.getNext();
        }

        if (current == null) {
            throw new NoSuchElementException();
        }

        return removeElement(previous, current);
    }

    @Override
    public E remove(int index) {
        if (index < 0 || index >= count) {
            throw new IndexOutOfBoundsException();
        }

        BidirectionalNode<E> current = front, previous = null;
        for (int i = 0; i < index; i++) {
            previous = current;
            current = current.getNext();
        }

        return removeElement(previous, current);
    }

    @Override
    public void set(int index, E element) {
        if (index < 0 || index >= count) {
            throw new IndexOutOfBoundsException();
        }

        BidirectionalNode<E> current = front;
        for (int i = 0; i < index; i++) {
            current = current.getNext();
        }

        current.setElement(element);
        modCount++;
    }

    @Override
    public E get(int index) {
        if (index < 0 || index >= count) {
            throw new IndexOutOfBoundsException();
        }

        BidirectionalNode<E> current = front;
        for (int i = 0; i < index; i++) {
            current = current.getNext();
        }

        return current.getElement();
    }

    @Override
    public int indexOf(E element) {
        BidirectionalNode<E> current = front;
        int index = 0;
        while (current != null) {
            if (current.getElement().equals(element)) {
                return index;
            }
            current = current.getNext();
            index++;
        }

        // Element not found
        return -1;
    }

    @Override
    public E first() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }

        return front.getElement();
    }

    @Override
    public E last() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }

        return rear.getElement();
    }

    @Override
    public boolean contains(E target) {
        return indexOf(target) != -1;
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public int size() {
        return count;
    }

    public String toString() {
        StringBuilder result = new StringBuilder("[");
        BidirectionalNode<E> current = front;
        while (current != null) {
            result.append(current.getElement());
            if (current.getNext() != null) {
                result.append(", ");
            }
            current = current.getNext();
        }
        return result.append("]").toString();
    }

    private E removeElement(BidirectionalNode<E> previous, BidirectionalNode<E> current) {
        E result = current.getElement();
        BidirectionalNode<E> succ = current.getNext();

        if (previous != null) {
            previous.setNext(succ);
        } else {
            front = succ;
            if (front != null) {
                front.setPrevious(null);
            }
        }

        if (succ != null) {
            succ.setPrevious(previous);
        } else {
            rear = previous;
        }

        count--;
        modCount++;
        return result;
    }

    @Override
    public Iterator<E> iterator() {
        return new DLLIterator();
    }

    @Override
    public ListIterator<E> listIterator() {
        return new DLLIterator();
    }

    @Override
    public ListIterator<E> listIterator(int startingIndex) {
        return new DLLIterator(startingIndex);
    }

    private class DLLIterator implements ListIterator<E> {

        private BidirectionalNode<E> current;
        private BidirectionalNode<E> next;
        private int nextIndex;
        private int iterModCount;
        private boolean canRemove;
        private boolean canSet;

        public DLLIterator() {
            this(0);
        }

        public DLLIterator(int startingIndex) {
            if (startingIndex < 0 || startingIndex > count) {
                throw new IndexOutOfBoundsException();
            }

            iterModCount = modCount;
            current = null;
            next = front;
            for (int i = 0; i < startingIndex; i++) {
                next = next.getNext();
            }

            nextIndex = startingIndex;
            canRemove = false;
            canSet = false;
        }

        @Override
        public boolean hasNext() {
            if (iterModCount != modCount) {
                throw new ConcurrentModificationException();
            }

            return nextIndex < count;
        }

        @Override
        public E next() {
            if (iterModCount != modCount) {
                throw new ConcurrentModificationException();
            }

            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            current = next;
            next = next.getNext();
            nextIndex++;
            canRemove = true;
            canSet = true;
            return current.getElement();
        }

        @Override
        public boolean hasPrevious() {
            if (iterModCount != modCount) {
                throw new ConcurrentModificationException();
            }

            return nextIndex > 0;
        }

        @Override
        public E previous() {
            if (iterModCount != modCount) {
                throw new ConcurrentModificationException();
            }

            if (!hasPrevious()) {
                throw new NoSuchElementException();
            }

            if (next == null) {
                current = rear;
            } else {
                current = next.getPrevious();
            }

            next = current;
            nextIndex--;
            canRemove = true;
            canSet = true;
            return current.getElement();
        }

        @Override
        public int nextIndex() {
            if (iterModCount != modCount) {
                throw new ConcurrentModificationException();
            }

            return nextIndex;
        }

        @Override
        public int previousIndex() {
            if (iterModCount != modCount) {
                throw new ConcurrentModificationException();
            }

            return nextIndex - 1;
        }

        @Override
        public void remove() {
            if (iterModCount != modCount) {
                throw new ConcurrentModificationException();
            }

            if (!canRemove) {
                throw new IllegalStateException();
            }

            BidirectionalNode<E> currentNode = current;
            BidirectionalNode<E> previousNode = currentNode.getPrevious();
            BidirectionalNode<E> nextNode = currentNode.getNext();

            if (currentNode == front) {
                front = nextNode;
                if (front != null) {
                    front.setPrevious(null);
                }
                if (nextNode == null) {
                    rear = null;
                }
                current = null;
            } else {
                previousNode.setNext(nextNode);
                if (nextNode != null) {
                    nextNode.setPrevious(previousNode);
                } else {
                    rear = previousNode;
                }
                current = previousNode;
            }

            if (next == currentNode) {
                next = nextNode;
            } else {
                nextIndex--;
            }

            canRemove = true;
            canSet = false;
            count--;
            modCount++;
            iterModCount++;
        }

        @Override
        public void set(E e) {
            if (iterModCount != modCount) {
                throw new ConcurrentModificationException();
            }

            if (!canSet || current == null) {
                throw new IllegalStateException();
            }

            current.setElement(e);
            modCount++;
            iterModCount++;
        }

        @Override
        public void add(E e) {
            if (iterModCount != modCount) {
                throw new ConcurrentModificationException();
            }

            BidirectionalNode<E> newNode = new BidirectionalNode<>(e);
            if (next == null) {
                if (rear == null) {
                    front = rear = newNode;
                } else {
                    newNode.setPrevious(rear);
                    rear.setNext(newNode);
                    rear = newNode;
                }
            } else {
                BidirectionalNode<E> nextPreviousNode = next.getPrevious();
                newNode.setNext(next);
                newNode.setPrevious(nextPreviousNode);
                next.setPrevious(newNode);
                if (nextPreviousNode != null) {
                    nextPreviousNode.setNext(newNode);
                } else {
                    front = newNode;
                }
            }

            nextIndex++;
            next = newNode.getNext();
            canRemove = false;
            canSet = false;
            count++;
            modCount++;
            iterModCount++;
        }
    }

}