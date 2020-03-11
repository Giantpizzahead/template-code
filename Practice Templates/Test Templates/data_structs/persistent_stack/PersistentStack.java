package data_structs.persistent_stack;

import java.util.*;

public class PersistentStack {
	int[] parents, values;
	int maxOperations, currOp;
	
	/**
	 * Tests the persistent stack.
	 */
	public static void main(String[] args) {
		Scanner s = new Scanner(System.in);
		System.out.println("Size of the persistent stack?");
		PersistentStack stack = new PersistentStack(s.nextInt());
		String op;
		int arg;
		while (true) {
			System.out.println("Operation #" + stack.currOp + " (push <num>, pop, jump <num>, peek)");
			op = s.next();
			switch (op) {
			case "push":
				arg = s.nextInt();
				stack.push(arg);
				System.out.println("Pushed " + arg + " to the stack");
				break;
			case "pop":
				System.out.println("Popped " + stack.pop() + " from the stack");
				break;
			case "jump":
				arg = s.nextInt();
				stack.jump(arg);
				System.out.println("Jumped to the time before " + arg);
				break;
			case "peek":
				System.out.println("Top value of stack is " + stack.peek());
				break;
			default:
				System.out.println("Unrecognized command: " + op);
			}
		}
	}
	
	/**
	 * Creates a persistent stack with the given max # of operations.
	 * @param maxOperations - The maximum # of operations that can be performed
	 * on this persistent stack.
	 */
	public PersistentStack(int maxOperations) {
		this.maxOperations = maxOperations;
		parents = new int[maxOperations + 1];
		parents[0] = -1;
		values = new int[maxOperations + 1];
		values[0] = -1;
		currOp = 0;
	}
	
	/**
	 * Pushes a value to the stack.
	 * @param value - The value to push.
	 */
	public void push(int value) {
		currOp++;
		parents[currOp] = currOp - 1;
		values[currOp] = value;
	}
	
	/**
	 * Pops a value from the stack.
	 * @return The popped value.
	 */
	public int pop() {
		int ret = values[currOp];
		currOp++;
		parents[currOp] = parents[currOp - 1];
		values[currOp] = values[parents[currOp]];
		parents[currOp] = parents[parents[currOp]];
		return ret;
	}
	
	/**
	 * Jumps to before the given operation # was performed.
	 * @param operationNum - The operation to jump BEFORE.
	 */
	public void jump(int operationNum) {
		currOp++;
		parents[currOp] = operationNum - 1;
		values[currOp] = values[parents[currOp]];
		parents[currOp] = parents[parents[currOp]];
	}
	
	/**
	 * Peeks at the top value of the stack. Returns -1 if the stack is empty.
	 * @return The top value of the stack. -1 if the stack is empty.
	 */
	public int peek() {
		return values[currOp];
	}
	
}
