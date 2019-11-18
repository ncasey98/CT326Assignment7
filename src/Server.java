// 17773075
// Nora Casey

// import java.util.Random;

public class Server extends Thread {
	
	Restaurant restaurant;
	
	public Server(Restaurant restaurant, String name) {
		super(name);
		this.restaurant = restaurant;
	}
	
	public void run() {
		try {
			restaurant.serve();
			Thread.sleep(2);
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
