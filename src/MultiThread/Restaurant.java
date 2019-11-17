package MultiThread;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.locks.*;

public class Restaurant {
	
	public static Queue<String> orderReceived = new LinkedList<String>();
	public static Queue<String> ordersCooked = new LinkedList<String>();
	
	static ReadWriteLock lock = new ReentrantReadWriteLock();
	static Lock writeLock = lock.writeLock();

	public static void main(String[] args) throws IOException {
		
		Chef.readFromOutput();
		orderReceived.forEach(System.out::println);
		
		// Initialse the Chef Threads
		Chef chef1 = new Chef();
		chef1.setName("Chef John");
		chef1.start();
		
		Chef chef2 = new Chef();
		chef2.setName("Chef Mark");
		chef2.start();
		
		// Initialise the Server Threads
		Server server1 = new Server();
		server1.setName("Server Katie");
		server1.start();
		
		Server server2 = new Server();
		server2.setName("Server Andrew");
		server2.start();
		
		Server server3 = new Server();
		server3.setName("Server Emily");
		server3.start();
	}
	
	public static void processingFood() {
		// Lock,
		// Remove order from received orders queue,
		// Add order to cooked orders queue,
		// then unlock
		 
		 String order = remove(orderReceived);
		 put(orderReceived,order);
	}
	
	public static void servingFood() {
		// Lock,
		// Remove order from cooked orders queue
		// then unlock
	}
	
	public static String remove(Queue<String> orders) {
		try {
			//while
			writeLock.lock();
			return orders.remove();
		} finally {
			writeLock.unlock();
		}
	}
	
	public static void put(Queue<String> orders, String order) {
		try {
			writeLock.lock();
			orders.add(order);
		} finally {
			writeLock.unlock();
		}
	}
}
