import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

public class IUDoubleLinkedList<E> implements IndexedUnsortedList<E> {

    private BidirectionalNode<E> front;
    private BidirectionalNode<E> rear;
    private int count;
    private int modCount;

    public IUDoubleLinkedList() {
        front = null;
        rear = null;
        count = 0;
        modCount = 0;
    }

    @Override
    public void addToFront(E element) {
        BidirectionalNode<E> node = new BidirectionalNode<>(element);

        if (isEmpty()) {
            front = node;
            rear = node;
        } else {
            node.setNext(front);
            front.setPrevious(node);
            front = node;
        }

        count++;
        modCount++;
    }

    @Override
    public void addToRear(E element) {
        BidirectionalNode<E> node = new BidirectionalNode<>(element);

        if (isEmpty()) {
            front = node;
            rear = node;
        } else {
            rear.setNext(node);
            node.setPrevious(rear);
            rear = node;
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
        BidirectionalNode<E> current = front;

        while (current != null && !current.getElement().equals(target)) {
            current = current.getNext();
        }

        if (current == null) {
            throw new NoSuchElementException();
        }

        if (current == rear) {
            addToRear(element);
            return;
        }

        BidirectionalNode<E> node = new BidirectionalNode<>(element);
        BidirectionalNode<E> nextNode = current.getNext();

        node.setPrevious(current);
        node.setNext(nextNode);

        current.setNext(node);
        nextNode.setPrevious(node);

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
            return;
        }

        if (index == count) {
            addToRear(element);
            return;
        }

        BidirectionalNode<E> nextNode = getNode(index);
        BidirectionalNode<E> prevNode = nextNode.getPrevious();

        BidirectionalNode<E> node = new BidirectionalNode<>(element);

        node.setPrevious(prevNode);
        node.setNext(nextNode);

        prevNode.setNext(node);
        nextNode.setPrevious(node);

        count++;
        modCount++;
    }

    @Override
    public E removeFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }

        E result = front.getElement();

        if (count == 1) {
            front = null;
            rear = null;
        } else {
            front = front.getNext();
            front.setPrevious(null);
        }

        count--;
        modCount++;

        return result;
    }

    @Override
    public E removeLast() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }

        E result = rear.getElement();

        if (count == 1) {
            front = null;
            rear = null;
        } else {
            rear = rear.getPrevious();
            rear.setNext(null);
        }

        count--;
        modCount++;

        return result;
    }

    @Override
    public E remove(E element) {
        BidirectionalNode<E> current = front;

        while (current != null && !current.getElement().equals(element)) {
            current = current.getNext();
        }

        if (current == null) {
            throw new NoSuchElementException();
        }

        return unlink(current);
    }

    @Override
    public E remove(int index) {
        if (index < 0 || index >= count) {
            throw new IndexOutOfBoundsException();
        }

        return unlink(getNode(index));
    }

    @Override
    public void set(int index, E element) {
        if (index < 0 || index >= count) {
            throw new IndexOutOfBoundsException();
        }

        getNode(index).setElement(element);
        modCount++;
    }

    @Override
    public E get(int index) {
        if (index < 0 || index >= count) {
            throw new IndexOutOfBoundsException();
        }

        return getNode(index).getElement();
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
        return indexOf(target) >= 0;
    }

    @Override
    public boolean isEmpty() {
        return count == 0;
    }

    @Override
    public int size() {
        return count;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("[");

        BidirectionalNode<E> current = front;

        while (current != null) {
            builder.append(current.getElement());

            if (current.getNext() != null) {
                builder.append(", ");
            }

            current = current.getNext();
        }

        builder.append("]");
        return builder.toString();
    }

    private BidirectionalNode<E> getNode(int index) {
        BidirectionalNode<E> current;

        if (index < count / 2) {
            current = front;

            for (int i = 0; i < index; i++) {
                current = current.getNext();
            }
        } else {
            current = rear;

            for (int i = count - 1; i > index; i--) {
                current = current.getPrevious();
            }
        }

        return current;
    }

    private E unlink(BidirectionalNode<E> node) {
        if (node == front) {
            return removeFirst();
        }

        if (node == rear) {
            return removeLast();
        }

        BidirectionalNode<E> prevNode = node.getPrevious();
        BidirectionalNode<E> nextNode = node.getNext();

        prevNode.setNext(nextNode);
        nextNode.setPrevious(prevNode);

        count--;
        modCount++;

        return node.getElement();
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

        private BidirectionalNode<E> nextNode;
        private BidirectionalNode<E> lastReturned;
        private int nextIndex;
        private int iterModCount;

        public DLLIterator() {
            this(0);
        }

        public DLLIterator(int startingIndex) {
            if (startingIndex < 0 || startingIndex > count) {
                throw new IndexOutOfBoundsException();
            }

            iterModCount = modCount;
            nextIndex = startingIndex;
            lastReturned = null;

            if (startingIndex == count) {
                nextNode = null;
            } else {
                nextNode = getNode(startingIndex);
            }
        }

        private void checkModification() {
            if (iterModCount != modCount) {
                throw new ConcurrentModificationException();
            }
        }

        @Override
        public boolean hasNext() {
            checkModification();
            return nextIndex < count;
        }

        @Override
        public E next() {
            checkModification();

            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            lastReturned = nextNode;
            nextNode = nextNode.getNext();
            nextIndex++;

            return lastReturned.getElement();
        }

        @Override
        public boolean hasPrevious() {
            checkModification();
            return nextIndex > 0;
        }

        @Override
        public E previous() {
            checkModification();

            if (!hasPrevious()) {
                throw new NoSuchElementException();
            }

            if (nextNode == null) {
                nextNode = rear;
            } else {
                nextNode = nextNode.getPrevious();
            }

            lastReturned = nextNode;
            nextIndex--;

            return lastReturned.getElement();
        }

        @Override
        public int nextIndex() {
            checkModification();
            return nextIndex;
        }

        @Override
        public int previousIndex() {
            checkModification();
            return nextIndex - 1;
        }

        @Override
        public void remove() {
            checkModification();

            if (lastReturned == null) {
                throw new IllegalStateException();
            }

            if (lastReturned == nextNode) {
                if (nextNode != null) {
                    nextNode = nextNode.getNext();
                }
            } else {
                nextIndex--;
            }

            unlink(lastReturned);

            lastReturned = null;
            nextIndex++;
            iterModCount++;
        }

        @Override
        public void set(E element) {
            checkModification();

            if (lastReturned == null) {
                throw new IllegalStateException();
            }

            lastReturned.setElement(element);
            modCount++;
            iterModCount++;
        }

        @Override
        public void add(E element) {
            checkModification();

            if (nextNode == front) {
                addToFront(element);
            } else if (nextNode == null) {
                addToRear(element);
            } else {
                BidirectionalNode<E> node = new BidirectionalNode<>(element);
                BidirectionalNode<E> prevNode = nextNode.getPrevious();

                node.setPrevious(prevNode);
                node.setNext(nextNode);

                prevNode.setNext(node);
                nextNode.setPrevious(node);

                count++;
                modCount++;
            }
            
            iterModCount++;
        }
    }
}