package Task_2;

//Our own exceptions
import CharStackExceptions.*;

public class StackManager {
	// The Stack
	private static CharStack stack = new CharStack();
	private static final int NUM_ACQREL = 4; // Number of Producer/Consumer threads
	private static final int NUM_PROBERS = 1; // Number of threads dumping stack
	private static int iThreadSteps = 3; // Number of steps they take
	// Semaphore declarations. Insert your code in the following:
	private static Semaphore LOCK = new Semaphore(1);
	
	///private static Semaphore OccupiedSlots = new Semaphore(4);
	//private static Semaphore emptySlots = new Semaphore(6);
	// The main()

	public static void main(String[] argv) {
		// Some initial stats...
		try {
			System.out.println("Main thread starts executing. hello");
			System.out.println("Initial value of top = " + stack.getTop() + ".");
			System.out.println("Initial value of stack top = " + CharStack.pick() + ".");
			System.out.println("Main thread will now fork several threads.");
		} catch (CharStackEmptyException e) {
			System.out.println("Caught exception: StackCharEmptyException");
			System.out.println("Message : " + e.getMessage());
			System.out.println("Stack Trace : ");
			e.printStackTrace();
		}
		/*
		 * The birth of threads
		 */
		Consumer ab1 = new Consumer();
		Consumer ab2 = new Consumer();
		System.out.println("Two Consumer threads have been created.");
		Producer rb1 = new Producer();
		Producer rb2 = new Producer();
		System.out.println("Two Producer threads have been created.");
		CharStackProber csp = new CharStackProber();
		System.out.println("One CharStackProber thread has been created.");
		/*
		 * start executing
		 */
		ab1.start();
		rb1.start();
		csp.start();
		ab2.start();
		rb2.start();
		
		/*
		 * Wait by here for all forked threads to die
		 */
		try {
			ab1.join();
			ab2.join();
			rb1.join();
			rb2.join();
			csp.join();
			
			// Some final stats after all the child threads terminated...
			System.out.println("System terminates normally.");
			System.out.println("Final value of top = " + stack.getTop() + ".");
			System.out.println("Final value of stack top = " + CharStack.pick() + ".");
			System.out.println("Final value of stack top-1 = " + stack.getAt(stack.getTop() - 1) + ".");
			System.out.println("Stack access count = " + stack.getAccessCounter());
		} catch (InterruptedException e) {
			System.out.println("Caught InterruptedException: " + e.getMessage());
			System.exit(1);
		} catch (Exception e) {
			System.out.println("Caught exception: " + e.getClass().getName());
			System.out.println("Message : " + e.getMessage());
			System.out.println("Stack Trace : ");
			e.printStackTrace();
		}
	} // main()
	/*
	 * Inner Consumer thread class
	 */

	static class Consumer extends BaseThread {
		private char copy; // A copy of a block returned by pop()
		
		public void run() {
			System.out.println("Consumer thread [TID=" + this.iTID + "] starts executing.");
			for (int i = 0; i <StackManager.iThreadSteps; i++) {
				// Insert your code in the following:
				try {
					LOCK.Wait();
					copy = stack.pop();
					System.out.println("Consumer thread [TID=" + this.iTID + "] pops character =" + this.copy);
				} catch (CharStackEmptyException e) {
					System.out.println(e.getMessage());
				}
				finally {
				//System.out.println("Consumer thread [TID=" + this.iTID + "] aquired the lock.");
				LOCK.Signal();
				}
		}
			System.out.println("Consumer thread [TID=" + this.iTID + "] terminates.");
	} // class Consumer
	/*
	 * Inner class Producer
	 */
	}
	static class Producer extends BaseThread {
		private char block; // block to be returned
		
		public void Produce() {}
		public void run() {
			System.out.println("Producer thread [TID=" + this.iTID + "] starts executing.");
			for (int i = 0; i < StackManager.iThreadSteps; i++) {
				// Insert your code in the following:
				try {
					LOCK.Wait();
					block = (char) (stack.pick()+1);
					//System.out.println(block);;
					stack.push(block);
					System.out.println("Producer thread [TID=" + this.iTID + "] pushes character =" + this.block);
				} catch (CharStackEmptyException e) {
					System.out.println(e.getMessage());
					try {
						stack.push('a');
						System.out.println("Producer thread [TID=" + this.iTID + "] pushes character =a");
					} catch (CharStackFullException e1) {
						System.out.println(e.getMessage());
					}
				} catch (CharStackFullException e) {
					System.out.println(e.getMessage());
				}
				finally {
					LOCK.Signal();
				}
			}
			System.out.println("Producer thread [TID=" + this.iTID + "] terminates.");
		}
	} // class Producer
	/*
	 * Inner class CharStackProber to dump stack contents
	 */

	static class CharStackProber extends BaseThread {
		
		public void run() {
			System.out.println("CharStackProber thread [TID=" + this.iTID + "] starts executing.");
			for (int i = 0; i < 2 * 3; i++) {
				// Insert your code in the following. Note that the stack state must be
				// printed in the required format.
				LOCK.Wait();
				String stackStr = "Stack = (";
				try {
					System.out.println("Stack S = ([" + stack.getAt(0) + "],["
							+ stack.getAt(1) + "],[" + stack.getAt(2) + "],["
							+ stack.getAt(3) + "],[" + stack.getAt(4) + "],["
							+ stack.getAt(5) + "],[" + stack.getAt(6) + "],["
							+ stack.getAt(7) + "],[" + stack.getAt(8) + "],["
							+ stack.getAt(9) + "])");
				} catch (CharStackInvalidAceessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                LOCK.Signal();
			}
		}
	} // class CharStackProber
} // class StackManager