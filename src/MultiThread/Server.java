package MultiThread;

public class Server extends Thread {

	public static long randomNumber; 
	
	@Override
	public void run() {
		super.run();
		System.out.println ( Thread.currentThread().getName() + " is working"); 
		Restaurant.servingFood();
		try {
			Thread.sleep(randomNumber);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static long getRandomNumber() {
		randomNumber = (long) ((Math.random()*((800-200)+1))+200);
		return randomNumber;
	}
}
