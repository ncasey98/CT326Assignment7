// 17773075
// Nora Casey

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Restaurant {
	
	private static final int CAPACITY = 10;
	public static final Queue<String> queue = new LinkedList<>();
	public static final Queue<String> queueToWrite = new LinkedList<>();
	// The HashMap 'preparingHistory' contains the following information <Chef, <Order name, Count of Order>>
	public static final HashMap<String, HashMap<String, Integer>> preparingHistory = new HashMap<>();
	// The HashMap 'servingHistory' contains the following information <Server, <Order Name, Count of Order>>
	public static final HashMap<String, HashMap<String, Integer>> servingHistory = new HashMap<>();
	
	// Lock and condition variables
	private final Lock lock = new ReentrantLock();
	private final Condition bufferNotFull = lock.newCondition();
	private final Condition bufferNotEmpty = lock.newCondition();
	
	// private static Integer orderProcessedCount;
	
	public void cook() throws InterruptedException {
		lock.lock();
		try {
			while (queue.size() == CAPACITY) {
				// Line(s) below used for testing
				// System.out.println(Thread.currentThread().getName() + " : Buffer is full, waiting");
				bufferNotEmpty.await();
			}
			String value = null;
			
			if (queueToWrite.size() != 0) {
				value = queueToWrite.remove();
			}
			if (value != null) {
				if (queue.add(value)) {
					System.out.printf("%s is preparing %s from queue %n", Thread.currentThread().getName(), value);
					
					String name = Thread.currentThread().getName();
					// Get rid of the numbers from lines input from 'orderList' so they can be compared
					String order = value.replaceAll("[0123456789]", "");
					HashMap<String, HashMap<String, Integer>> map = preparingHistory;
					HashMap<String, Integer> orderProcessedMap = map.get(name);
					
					Boolean done = false;
					
					if (orderProcessedMap == null) {
						orderProcessedMap = new HashMap<String, Integer>();
						orderProcessedMap.put(order, 1);
						map.put(name, orderProcessedMap);
						done = true;
					}
					
					Integer orderProcessedCount = 1;
					
					if (orderProcessedMap.containsKey(order) && done == false) {
						orderProcessedCount = orderProcessedMap.get(order) + 1;
						orderProcessedMap.put(order, orderProcessedCount);
						map.put(name, orderProcessedMap);
					} else {
						orderProcessedMap.put(order, 1);
						map.put(name, orderProcessedMap);
					}
					// Line(s) below used for testing 
					// System.out.println(Thread.currentThread().getName() + " : Signalling that buffer is not empty anymore");
					bufferNotFull.signalAll();
				}
			}
		} finally {
			lock.unlock();
		}
	}
	
	public void serve() throws InterruptedException {
		lock.lock();
		try {
			while (queue.size() == 0 && queueToWrite.size() != 0) {
				// Line(s) below used for testing
				// System.out.println(Thread.currentThread().getName() + " : Buffer is empty, waiting");
				bufferNotFull.await();
			}
			String value = null;
			
			if (queue.size() != 0) {
				value = queue.remove();
			}
			if (value != null) {
				System.out.printf("%s is serving %s from queue %n", Thread.currentThread().getName(), value);
				
				String name = Thread.currentThread().getName();
				// Get rid of the numbers from lines input from 'orderList' so they can be compared
				String order = value.replaceAll("[0123456789]", "");
				HashMap<String, HashMap<String, Integer>> map = servingHistory;
				HashMap<String, Integer> orderProcessedMap = map.get(name);
				
				Boolean done = false;
				
				if (orderProcessedMap == null) {
					orderProcessedMap = new HashMap<String, Integer>();
					orderProcessedMap.put(order, 1);
					map.put(name, orderProcessedMap);
					done = true;
				}
				
				Integer orderProcessedCount = 1;
				
				if (orderProcessedMap.containsKey(order) && done == false) {
					orderProcessedCount = orderProcessedMap.get(order) + 1;
					orderProcessedMap.put(order, orderProcessedCount);
					map.put(name, orderProcessedMap);
				} else {
					orderProcessedMap.put(order, 1);
					map.put(name, orderProcessedMap);
				}
				// Line(s) below used for testing
				// Signal producer thread that buffer may be empty now
				// System.out.println(Thread.currentThread().getName() + " : Signalling that buffer may be empty now");
				bufferNotEmpty.signalAll();
			}
		} finally {
			lock.unlock();
		}
	}
	
	public static void readFromOutput() {
		File output = new File("orderList.txt");
		FileReader r = null;
		
		try {
			r = new FileReader(output);
		} catch (FileNotFoundException ex) {
			// TODO Auto-generated catch block
			ex.printStackTrace();
		}
		BufferedReader reader = new BufferedReader(r);
		String line = "";
		
		try {
			while ((line = reader.readLine()) != null) {
				queueToWrite.add(line);
			}
		} catch (IOException ex) {
			// TODO Auto-generated catch block
			ex.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		readFromOutput();
		
		// Object on which Chef and Server threads will operate
		Restaurant restaurant = new Restaurant();
		
		while (true) {
			if (!queueToWrite.isEmpty()) {
				Chef chef = new Chef(restaurant, "Chef Mark");
				chef.start();
				Chef chefEile = new Chef(restaurant, "Chef John");
				chefEile.start();
			}
			if (!queue.isEmpty()) {
				Server server = new Server(restaurant, "Server Katie");
				server.start();
				Server serverEile = new Server(restaurant, "Server Andrew");
				serverEile.start();
				Server serverDeireanach = new Server(restaurant, "Server Emily");
				serverDeireanach.start();
			}
			if (queueToWrite.isEmpty() && queue.isEmpty()) {
				break;
			}
		}
		
		// This only iterates through the outside hashmap of 'preparingHistory', I could not figure 
		// out how to iterate through the inside hashmap, which contains the count of number of dishes
		// each Chef has prepared
		for (Object name : preparingHistory.keySet()) {
			System.out.println(name + " finished serving ?? orders, including " + preparingHistory.get(name));
		}
		for (Object name : servingHistory.keySet()) {
			System.out.println(name + " finished serving ?? orders, including " + servingHistory.get(name));
		}
	}
}
