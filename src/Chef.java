// 17773075
// Nora Casey

//import java.util.Random;

public class Chef extends Thread {
	
	Restaurant restaurant;
	
	public Chef(Restaurant restaurant, String name) {
		super(name);
		this.restaurant = restaurant;
	}
	
	public void run() {
		try {
			restaurant.cook();
			Thread.sleep(3);
		} catch (InterruptedException ex) {
			// TODO Auto-generated catch block
			ex.printStackTrace();
		}
	}
	
//	public static int generateRandomInt() {
//		Random r = new Random();
//		return r.nextInt();
//	}
}
