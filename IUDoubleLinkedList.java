import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

public class IUDoubleLinkedList<E> implements IndexedUnsortedList<E> {
    private BidirectionalNode<E> front, rear;
    private int count;
    private int modCount;

	public IUDoubleLinkedList() {
        front = rear = null;
        count = 0;
        modCount = 0;
    }

    @Override
    public void addToFront(E element) {
        BidirectionalNode<E> node = new BidirectionalNode<E>(element);

        if(isEmpty()) {
            front = rear = node;
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
        BidirectionalNode<E> node = new BidirectionalNode<E>(element);

        if(isEmpty()) {
            front = rear = node;
        } else {
            node.setPrevious(rear);
            rear.setNext(node);
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
		
		while(current != null && !current.getElement().equals(target)){
			current = current.getNext();
		}

		if(current == null){
			throw new NoSuchElementException();
		}

		if(current == rear){
            addToRear(element);
		}
		
        BidirectionalNode<E> node = new BidirectionalNode<E>(element);

		node.setNext(current.getNext());
        node.setPrevious(current); 
        current.getNext().setPrevious(node); 
        current.setNext(node); 
		
		count++;
		modCount++;
    }

    @Override
    public void add(int index, E element) {
        if(index < 0 || index > count) {
			throw new IndexOutOfBoundsException();
		}

        if(index == 0) {
            addToFront(element);
        } else if (index == count) {
            addToRear(element);
        } else {
            BidirectionalNode<E> current = front;
		    for(int i = 0; i < index -1; i++){
			    current = current.getNext();
		}
            // BidirectionalNode<E> current = front, previous = null;
            // for (int i = 0; i < index; i++) {
            //     previous = current;
            //     current = current.getNext();
            // }

            BidirectionalNode<E> node = new BidirectionalNode<E>(element);
            node.setNext(current.getNext());
            node.setPrevious(current);
            current.getNext().setPrevious(node);
            current.setNext(node);
            // node.setNext(current);
            // node.setPrevious(previous);
            // current.setPrevious(node);

            // if(previous != null) {
            //     previous.setNext(node);
            // } else {
            //     front = node;
            // }

            count++;
            modCount++;
        }
    }

    @Override
    public E removeFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }

        return remove(first());
        // return removeElement(null, front);
    }

    @Override
    public E removeLast() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        return remove(last());
        // return removeElement(rear.getPrevious(), rear);
    }

    @Override
    public E remove(E element) {
        if (isEmpty()) {
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
        return remove(get(index));
        // if(index < 0 || index >= count) {
        //     throw new IndexOutOfBoundsException();
        // }
        // BidirectionalNode<E> current = front, previous = null;

        // for(int i = 0; i < index; i++) {
        //     previous = current;
        //     current = current.getNext();
        // }

        // return removeElement(previous, current);
    }

		
    @Override
    public void set(int index, E element) {
        if(index < 0 || index >= size()) {
            throw new IndexOutOfBoundsException();
        }

        BidirectionalNode<E> current = front;
        for(int i = 0; i < index; i++) {
             current = current.getNext();
        }

        current.setElement(element);
        modCount++;
   
    }

    @Override
    public E get(int index) {
        if(index < 0 || index >= size()) {
            throw new IndexOutOfBoundsException();
        }

        BidirectionalNode<E> current = front;
        for(int i = 0; i < index; i++) {
             current = current.getNext();
        }

        return current.getElement();

    }

    @Override
    public int indexOf(E element) {
        BidirectionalNode<E> current = front;
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

    private E removeElement(BidirectionalNode<E>  previous, BidirectionalNode<E>  current) {
		E result = current.getElement();
        BidirectionalNode<E> next = current.getNext();
		
		if (previous != null) {
			previous.setNext(next);
		} else { 
			front = next;
            if(front != null) {
                front.setPrevious(null);
            }
		}
		
		if (next != null) {
            next.setPrevious(previous);
        } else {
            rear = previous;
        }

		count--;
		modCount++;

		return result;
	}

    @Override
	public String toString() {
		
		String result = "[";
    	BidirectionalNode<E> current = front;
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

    private enum ListIteratorState {NEXT, PREVIOUS, NEITHER}

     private class DLLIterator implements ListIterator<E> {

        private BidirectionalNode<E> previous;
        private BidirectionalNode<E> current;
        private BidirectionalNode<E> next;
        private int nextIndex;
        private int iterModCount;
        private ListIteratorState state;

        public DLLIterator() {
            previous = null;
			current = null;
			next = front;
			iterModCount = modCount;
            nextIndex = 0;
            state = ListIteratorState.NEITHER;
        }

        public DLLIterator(int startingIndex) {
            if (startingIndex < 0 || startingIndex > count) {
                throw new IndexOutOfBoundsException();
            }

            previous = null;
            current = null;
            next = front;
            iterModCount = modCount;
            state = ListIteratorState.NEITHER;
            nextIndex = 0;

            for (int i = 0; i < startingIndex; i++) {
                previous = next;
                next = next.getNext();
                nextIndex++;
            }

            // nextIndex = startingIndex;
            // canRemove = false;
            // canSet = false;
        }

        @Override
        public boolean hasNext() {
            if (iterModCount != modCount) {
                throw new ConcurrentModificationException();
            }

            return next != null;
        }

        @Override
        public E next() {
            if (iterModCount != modCount) {
                throw new ConcurrentModificationException();
            }

            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            previous = next;
            current = next;
            next = next.getNext();
            nextIndex++;

            state = ListIteratorState.NEXT; 
            return current.getElement();
        }

        @Override
        public boolean hasPrevious() {
            if (iterModCount != modCount) {
                throw new ConcurrentModificationException();
            }

            return previous != null;
        }

        @Override
        public E previous() {
            if (iterModCount != modCount) {
                throw new ConcurrentModificationException();
            }

            if (!hasPrevious()) {
                throw new NoSuchElementException();
            }

            next = previous;
            current = previous;
            previous = previous.getPrevious();
            nextIndex--;

            state = ListIteratorState.PREVIOUS;
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

            if (state == ListIteratorState.NEITHER) {
                throw new IllegalStateException();
            }

            if (current.getPrevious() != null) {
                current.getPrevious().setNext(current.getNext());
            } else {
                front = current.getNext();
            }
            if (current.getNext() != null) {
                current.getNext().setPrevious(current.getPrevious());
            } else {
                rear = current.getPrevious();
            }

            if (state == ListIteratorState.PREVIOUS) {
                // If last action was previous(), move next past the removed node
                next = current.getNext();
            } else if (state == ListIteratorState.NEXT) {
                // If last action was next(), move previous past the removed node
                previous = current.getPrevious();
                nextIndex--;
            }

			current = null;
			count --; 
			modCount++;
			iterModCount++;
            state = ListIteratorState.NEITHER;

            // BidirectionalNode<E> currentNode = current;
            // BidirectionalNode<E> previousNode = currentNode.getPrevious();
            // BidirectionalNode<E> nextNode = currentNode.getNext();

            // if (currentNode == front) {
            //     front = nextNode;
            //     if (front != null) {
            //         front.setPrevious(null);
            //     }
            //     if (nextNode == null) {
            //         rear = null;
            //     }
            //     current = null;
            // } else {
            //     previousNode.setNext(nextNode);
            //     if (nextNode != null) {
            //         nextNode.setPrevious(previousNode);
            //     } else {
            //         rear = previousNode;
            //     }
            //     current = previousNode;
            // }

            // if (next == currentNode) {
            //     next = nextNode;
            // } else {
            //     nextIndex--;
            // }

            // canRemove = true;
            // canSet = false;
            // count--;
            // modCount++;
            // iterModCount++;
        }

        @Override
        public void set(E e) {
            if (iterModCount != modCount) {
                throw new ConcurrentModificationException();
            }

            if (state == ListIteratorState.NEITHER) {
                throw new IllegalStateException();
            }

            current.setElement(e);
            modCount++;
            iterModCount++;
        }

        @Override
        public void add(E e) {
            if(iterModCount != modCount) {
                throw new ConcurrentModificationException();
            }

            BidirectionalNode<E> newNode = new BidirectionalNode<E>(e);
            if(previous == null) { 
                newNode.setNext(front);
                if (front != null) {
                    front.setPrevious(newNode);
                }
                front = newNode;
                if (rear == null) { 
                    rear = newNode;
                }
            } else if (next == null) { 
                newNode.setPrevious(rear);
                rear.setNext(newNode);
                rear = newNode;
            } else { 
                newNode.setNext(next);
                newNode.setPrevious(previous);
                previous.setNext(newNode);
                next.setPrevious(newNode);
            }

            previous = newNode; 
            nextIndex++;    
            current = null;
            state = ListIteratorState.NEITHER; 
            
            count++;
            modCount++;
            iterModCount++;        

        }
    }
    
}


