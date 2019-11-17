package MultiThread;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Chef extends Thread {
	
	public static long randomNumber; 

	@Override
	public void run() {
		super.run();
		System.out.println (Thread.currentThread().getName() + " is working"); 
		Restaurant.processingFood();
		try {
			Thread.sleep(randomNumber);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static void readFromOutput() throws IOException {
		File output = new File("orderList.txt");
		
		FileReader r = new FileReader(output);
		BufferedReader reader = new BufferedReader(r);
		
		String line = "";
		while ((line = reader.readLine()) != null) {
			Restaurant.orderReceived.add(line);
		}
	}
	
	private static long getRandomNumber() {
		randomNumber = (long) ((Math.random()*((800-200)+1))+200);
		return randomNumber;
	}
}
