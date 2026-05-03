import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.ConcurrentModificationException;

public class IUDoubleLinkedList<E> implements IndexedUnsortedList<E>{
    private BidirectionalNode<E> head, tail;
    private int size;
    private int modCount;

    public IUDoubleLinkedList() {
        head = tail = null;
        size = 0;
        modCount = 0;
    }

    @Override
    public void addToFront(E element) {
        BidirectionalNode<E> newNode = new BidirectionalNode<>(element);
        if (isEmpty()) {
            head = tail = newNode;
        } else {
            newNode.setNext(head);
            head.setPrevious(newNode);
            head = newNode;
        }
        size++;
        modCount++;
    }

    @Override
    public void addToRear(E element) {
        BidirectionalNode<E> newNode = new BidirectionalNode<>(element);
        if (isEmpty()) {
            head = tail = newNode;
        } else {
            newNode.setPrevious(tail);
            tail.setNext(newNode);
            tail = newNode;
        }
        size++;
        modCount++;
    }

    @Override
    public void add(E element) {
        addToRear(element);
    }

    @Override
    public void addAfter(E element, E target) {
        int index = indexOf(target);
        if (index == -1) throw new java.util.NoSuchElementException();
        add(index + 1, element);
    }

    @Override
    public void add(int index, E element) {
        ListIterator<E> lit = listIterator(index);
        lit.add(element);
    }

    @Override
    public E removeFirst() {
        if (isEmpty()) throw new java.util.NoSuchElementException();
        
        E element = head.getElement();
        head = head.getNext();
        
        if (head == null) {
            tail = null;
        } else {
            head.setPrevious(null);
        }
        
        size--;
        modCount++;
        return element;
    }

    @Override
    public E removeLast() {
        if (isEmpty()) throw new java.util.NoSuchElementException();
        
        E element = tail.getElement();
        tail = tail.getPrevious();
        
        if (tail == null) {
            head = null;
        } else {
            tail.setNext(null);
        }
        
        size--;
        modCount++;
        return element;
    }

    @Override
    public E remove(E element) {
        int index = indexOf(element);
        if (index == -1) throw new java.util.NoSuchElementException();
        return remove(index);
    }

    @Override
    public E remove(int index) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException();
        ListIterator<E> lit = listIterator(index);
        E element = lit.next();
        lit.remove();
        return element;
    }

    @Override
    public void set(int index, E element) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException();
        BidirectionalNode<E> current = head;
        for (int i = 0; i < index; i++) {
            current = current.getNext();
        }
        current.setElement(element);

        modCount++;
    }

    @Override
    public E get(int index) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException();
        return listIterator(index).next();
    }

    @Override
    public int indexOf(E element) {
        int index = 0;
        BidirectionalNode<E> current = head;
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
        if (isEmpty()) throw new java.util.NoSuchElementException();
        return head.getElement();
    }

    @Override
    public E last() {
        if (isEmpty()) throw new java.util.NoSuchElementException();
        return tail.getElement();
    }

    @Override
    public boolean contains(E target) {
        return indexOf(target) != -1;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public Iterator<E> iterator() {
        return new DLLIterator(0);
    }

    @Override
    public ListIterator<E> listIterator() {
        return new DLLIterator(0);
    }

    @Override
    public ListIterator<E> listIterator(int startingIndex) {
        return new DLLIterator(startingIndex);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        BidirectionalNode<E> current = head;
        while (current != null) {
            sb.append(current.getElement());
            current = current.getNext();
            if (current != null) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    private class DLLIterator implements ListIterator<E>{
        private BidirectionalNode<E> nextNode;
        private BidirectionalNode<E> lastReturned;
        private int nextIndex;
        private int iterModCount;

        public DLLIterator(int startingIndex) {
            if (startingIndex < 0 || startingIndex > size) {
                throw new IndexOutOfBoundsException();
            }
            nextNode = head;
            for (int i = 0; i < startingIndex; i++) {
                nextNode = nextNode.getNext();
            }
            nextIndex = startingIndex;
            iterModCount = modCount;
            lastReturned = null;
        }

        @Override
        public boolean hasNext() {
            checkMod();
            return nextIndex < size;
        }

        @Override
        public E next() {
            checkMod();
            if (!hasNext()) throw new java.util.NoSuchElementException();
            lastReturned = nextNode;
            nextNode = nextNode.getNext();
            nextIndex++;
            return lastReturned.getElement();
        }

        @Override
        public boolean hasPrevious() {
            checkMod();
            return nextIndex > 0;
        }

        @Override
        public E previous() {
            if (!hasPrevious()) {
                throw new NoSuchElementException();
            }
            
            if (nextNode == null) { 
                nextNode = tail; 
            } else {
                nextNode = nextNode.getPrevious();
            }

            lastReturned = nextNode;
            nextIndex--;
            return lastReturned.getElement();
        }


        @Override
        public int nextIndex() {
            checkMod();
            return nextIndex;
        }

        @Override
        public int previousIndex() {
            checkMod();
            return nextIndex - 1;
        }

        @Override
        public void set(E e) {
            checkMod();
            if (lastReturned == null) throw new IllegalStateException();
            lastReturned.setElement(e);

            modCount++;
            iterModCount++;
        }

        @Override
        public void add(E e) {
            checkMod();
            BidirectionalNode<E> newNode = new BidirectionalNode<>(e);
            
            if (isEmpty()) {
                head = tail = newNode;
            } else if (nextNode == head) {
                newNode.setNext(head);
                head.setPrevious(newNode);
                head = newNode;
            } else if (nextNode == null) {
                newNode.setPrevious(tail);
                tail.setNext(newNode);
                tail = newNode;
            } else {
                newNode.setNext(nextNode);
                newNode.setPrevious(nextNode.getPrevious());
                nextNode.getPrevious().setNext(newNode);
                nextNode.setPrevious(newNode);
            }

            size++;
            modCount++;
            iterModCount++;
            nextIndex++;
            lastReturned = null;
        }

        @Override
        public void remove() {
            checkMod();
            if (lastReturned == null) {
                throw new IllegalStateException();
            }

            BidirectionalNode<E> p = lastReturned.getPrevious();
            BidirectionalNode<E> n = lastReturned.getNext();

            if (p != null) {
                p.setNext(n);
            } else {
                head = n;
            }

            if (n != null) {
                n.setPrevious(p);
            } else {
                tail = p;
            }

            if (lastReturned == nextNode) {
                nextNode = n;
            } else {
                nextIndex--;
            }

            lastReturned = null;
            size--;
            modCount++;
            iterModCount++;
                        
            if (size == 0) {
                head = tail = nextNode = null;
                nextIndex = 0;
            }
        }

        private void checkMod() {
            if (modCount != iterModCount) throw new ConcurrentModificationException();
        }
    }
    
}

