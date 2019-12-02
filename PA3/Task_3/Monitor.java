package Task_3;
public class Monitor  {
	/*
	 * ------------
	 * Data members
	 * ------------
	 */

	  private enum status{eating, hungry, thinking};
	  private boolean isTalking;
	  private status []state;
	  private int nbChopSticks;
	/**
	 * Constructor
	 */
	public Monitor(int NumberOfPhilosophers){
		this.nbChopSticks = NumberOfPhilosophers;
		state = new status[NumberOfPhilosophers];
		for(int i=0; i<NumberOfPhilosophers;i++){
				state[i]=status.thinking;
			}
		isTalking = false;
		// TODO: set appropriate number of chopsticks based on the # of philosophers
	}

	/*
	 * -------------------------------
	 * User-defined monitor procedures
	 * -------------------------------
	 */
	/**Here we check to see if, both neighbors are not eating, so the number of chopsticks available for us to eat is sufficient, 
		*then only in that case, and if we are hungry then, can we catagorize ourselves as eating.
		*/
	public synchronized void test(final int piTID){
		
		if (state[(piTID + 1)%nbChopSticks] != status.eating && state[(piTID-1+nbChopSticks)%nbChopSticks] != status.eating && state[piTID] == status.hungry){
			state[piTID] = status.eating;
			notifyAll();
		}
	}
	/**
	 * Grants request (returns) to eat when both chopsticks are available.
	 * Else forces the philosopher to wait()
	 */
	public synchronized void pickUp(final int piTID){
		
		int phil = piTID -1;
		state[phil] = status.hungry;
		test(phil);
		
		if(state[phil] != status.eating){
			try {
				wait();
				pickUp(piTID);                     
			}
		    catch(InterruptedException e){
				System.out.println(e);
			}
		}
	}

	/**
	 * When a given philosopher's done eating, they put the chopstiks down
	 * and let others know they are available.
	 */
	public synchronized void putDown(final int piTID){
		int phil = piTID-1;
        state[phil] = status.thinking;
        notifyAll();
	}

	/**
	 * Only one philosopher at a time is allowed to BullShit
	 * (while he is not eating).
	 */
	public synchronized void requestTalk(){
		if(isTalking) {
			try {
				wait(); //If someone else is currently talking then this philosopher has to wait
				requestTalk(); //When he gets notified that someone stopped eating or talking, he needs to check if the boolean has been changed to false
			}
			catch(InterruptedException e){
				System.out.println(e);
			}
		}
		isTalking = true; //The philosopher starts talking, we change the boolean to true to stop other philosophers from talking
	}

	/**
	 * When one philosopher is done talking, another gets the chance to BullShit.
	 */
	public synchronized void endTalk()
	{
		isTalking = false; //We set the boolean to false
		notifyAll();//so that any other philosopher waiting to talk, wakes up. 
	}
}

// EOF
