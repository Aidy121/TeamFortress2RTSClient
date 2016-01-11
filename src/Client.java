// Author: Aidan Fisher

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.awt.image.BufferedImage;
import java.io.*;

public class Client implements Runnable {

	public static ArrayList<List<? extends Object>> taskQueue = new ArrayList<List<? extends Object>>();
	public static ArrayList<Object> taskQueueObject = new ArrayList<Object>();
	public static ArrayList<Method> taskQueueMethod = new ArrayList<Method>();

	public static int userID;
	public static Socket socket;
	public static ObjectInputStream in;
	public static ObjectOutputStream out;
	public static Client client;

	public Client() {

	}

	public static void connect(String ip) throws Exception {
		System.out.println("Connecting...");
		socket = new Socket(ip, 7788);
		System.out.println("Connection Made.");
		socket.setTcpNoDelay(true);
		in = new ObjectInputStream(socket.getInputStream());
		out = new ObjectOutputStream(socket.getOutputStream());
		userID = in.readInt();
		client = new Client();
		Thread thread = new Thread(client);
		thread.start();
	}

	public void run() {
		while (!input()) {
		}
	}

	public boolean input() {
		try {
			int x;
			x = in.readInt();
			if (x == -1) { // Map update.
				Object object = in.readObject();
				if (object instanceof Game) {
					// This shouldn't be like this. (needs to be flushed properly)
					Game.game = (Game) object;
					Component.lastTick = System.nanoTime();
				}
			} else if (x == -12) {
				// Flushed.
				disperseQueue();
			}
		} catch (IOException e) {
			System.out.println("Input error: ");
			e.printStackTrace();
			in = null;
			// Disconnected from server, essentially.
			return true;
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return true;
		} catch (Exception e) {
			System.out.println("Other exception.");
			e.printStackTrace();
			return true;
		}
		return false;
	}

	public void disperseQueue() {
		// Queue is to be dispersed;
		Component.disperseQueue = true;
		//System.out.println(System.currentTimeMillis() + ": " + Client.taskQueue.size());
		for (int i = 0; i < Client.taskQueue.size(); i++) {
			//System.out.println("Finished: " + System.currentTimeMillis());
			//System.out.println(i + ": " + Client.taskQueueMethod.get(i));
			List<? extends Object> taskToRun = Client.taskQueue.get(i);
			Object taskObject = Client.taskQueueObject.get(i);
			Method taskMethod = Client.taskQueueMethod.get(i);
			try {
				//System.out.println("Invoking: " + taskMethod.getName() + " at: " + System.currentTimeMillis() % 10000);
				taskMethod.invoke(taskObject, taskToRun.toArray());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		Client.taskQueue.clear();
		Client.taskQueueObject.clear();
		Client.taskQueueMethod.clear();
		Component.disperseQueue = false;
	}

	public static void queueMethod(Object object, String methodName, List<? extends Object> objects, Class<?>... args) throws Exception {
		Method method = object.getClass().getMethod(methodName, args);
		taskQueue.add(objects);
		taskQueueObject.add(object);
		taskQueueMethod.add(method);
	}

	// For static methods
	public static void queueMethod(Class<?> object, String methodName, List<? extends Object> objects, Class<?>... args) throws Exception {
		Method method = object.getMethod(methodName, args);
		taskQueue.add(objects);
		taskQueueObject.add(object);
		taskQueueMethod.add(method);
	}
}
