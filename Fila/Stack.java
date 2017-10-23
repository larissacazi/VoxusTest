public class Stack {

	private static final int EMPTY_STACK = -1;

	private Node head;
	private int size;
	private int min;

	private class Node {
		private int number;
		private Node next;
	}

	public Stack() {
		this.head = null;
		this.size = 0;
		this.min = Integer.MAX_VALUE;
	}

	public boolean isEmpty() {
		return this.head == null;
	}

	public int getSize() {
		return this.size;
	}

	private void findMinValue() {
		Node top = this.head;
		while(top != null) {
			if(top.number < this.min) this.min = top.number;
			top = top.next;
		}
	}

	public int pop() {
		if(isEmpty()) return EMPTY_STACK;

		int element = this.head.number;
		head = head.next;
		this.size--;

		if(element == this.min) {
			this.min = Integer.MAX_VALUE;
			findMinValue();
		}

		return element;
	}

	public void push(int element) {
		Node top = this.head;
		this.head = new Node();
		this.head.number = element;
		this.head.next = top;
		this.size++;

		if(element < this.min) this.min = element;
	}

	public int min() {
		return min;
	}
}