package Task_1;

class Semaphore{
	private int value;
	
	public Semaphore() {
		this(0);
	}
	public Semaphore(int value) {
		if (this.value < 0) {
			this.value = 0;
		} else {
			this.value = value;
		}
	}

	public synchronized void Wait() {
		if (this.value < 0) {
			try {
				wait();
			} catch (InterruptedException e) {
				System.out.println("Semaphore::Wait() - caught InterruptedException: " + e.getMessage());
				e.printStackTrace();
			}
		}
		this.value--;

	}

	public synchronized void Signal() {
		++this.value;
		notify();
	}

	public synchronized void P() {
		this.Wait();
	}

	public synchronized void V() {
		this.Signal();
	}
}