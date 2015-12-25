package cmei.java.concurrent.thread;

import java.util.LinkedList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ProducterAndConsumer {
	
	public static void main(String[] args){
		LoggingThread3 thread1=new LoggingThread3();
		thread1.start();
		try {
			Thread.currentThread().sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for(int i=0;i<10;i++){
			thread1.log("haha"+i);
		}
		thread1.interrupt(); 
		try {
			Thread.currentThread().sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("5:"+thread1.getTerminate()+thread1.isInterrupted()+thread1.getState()+thread1.interrupted());
		thread1.log("haha"+"terminate notify!");//or wait limited
		thread1.terminate();
		try {
			Thread.currentThread().sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("6:"+thread1.getTerminate()+thread1.isInterrupted()+thread1.getState()+thread1.interrupted());
		
	}

}


/**
 * pre java 5, the common way to implement a producer-consumer pattern was a plain old linkedList with synchronizaion, wait, notify.
 * refences:http://www.javamex.com/tutorials/synchronization_producer_consumer.shtml
 * 1)用timeout避免无限等待，无法检测到teminate命令
 * 2）
 * @author cmei
 *
 */
class LoggingThread1 extends Thread{
	private LinkedList<String> linesToLog=new LinkedList<String>();
	private volatile boolean terminateRequested=false;
	
	public void terminate(){
		this.terminateRequested=true;
	}
	
	public boolean getTerminate(){
		return this.terminateRequested;
	}
	
	public void run(){
		try{
		synchronized(linesToLog){
			String consumeLine;
			while(!terminateRequested){
				System.out.println("1:"+terminateRequested);
				while(!terminateRequested&&linesToLog.isEmpty()){
					System.out.println("thread is waiting...");
					linesToLog.wait(0);//wait limit will be better.
				}
				if(!terminateRequested){//如果在wait循环中由于中途terminate了，则此处需要再次check，termicate
					consumeLine=linesToLog.removeFirst();
					doLogLine(consumeLine);	
				}
			}
			System.out.println("3:"+terminateRequested);
		   }
		}catch(InterruptedException ex){
			ex.printStackTrace();
			Thread.currentThread().interrupt();
		}
	}
	
	public void doLogLine(String line){
		//do log line
		System.out.println("consumed:"+line);
	}
	
	public void log(String productLine){
		synchronized(linesToLog){
			linesToLog.add(productLine);
			System.out.println("produced:"+productLine);
			linesToLog.notify();
		}
	}
}

/***
 * using blocking queues which holds pending tasks from the "real" functionality of the producer/consumer.
**/
class LoggingThread2 extends Thread{
	private BlockingQueue<String> linesToLog=new LinkedBlockingQueue<String>();
	private volatile boolean terminateRequested=false;
	
	public void terminate(){
		this.terminateRequested=true;
	}
	
	public boolean getTerminate(){
		return this.terminateRequested;
	}
	
	public void run(){
		try{
			while(!terminateRequested){
				String line=linesToLog.take();
				doLogLine(line);
			}
		}catch(InterruptedException ex){
			ex.printStackTrace();
			Thread.currentThread().interrupt();
		}
	}
	
	public void doLogLine(String line){
		//do log line
		System.out.println("consumed:"+line);
	}
	
	public void log(String line){
		try {
			linesToLog.put(line);
			System.out.println("produced:"+line);
		} catch (InterruptedException e) {
			//e.printStackTrace();
			//Thread.currentThread().interrupt();
		}
	}
}

class LoggingThread3 extends Thread{
	private LinkedList<String> linesToLog=new LinkedList<String>();
	private ReentrantLock  lock=new ReentrantLock();
	private Condition notEmpty=lock.newCondition();
	
	private volatile boolean terminateRequested=false;
	
	public void terminate(){
		this.terminateRequested=true;
	}
	
	public boolean getTerminate(){
		return this.terminateRequested;
	}
	
	public void run(){
			String consumeLine;
			while(!terminateRequested){
				System.out.println("1:"+terminateRequested);
				lock.lock();
			try {
				while(!terminateRequested&&linesToLog.isEmpty()){
					System.out.println("thread is waiting...");
					boolean elapsed=notEmpty.await(100000,TimeUnit.MICROSECONDS);
					System.out.print("elapsed:"+elapsed);
				}
				if(!terminateRequested){//如果在wait循环中由于中途terminate了，则此处需要再次check，termicate
					consumeLine=linesToLog.removeFirst();
					doLogLine(consumeLine);	
				}else{
					System.out.println("3:"+terminateRequested);
				}
				} catch (InterruptedException e) {
					System.out.println("isInterrupted:"+Thread.currentThread().isInterrupted()+",interrupted:"+Thread.currentThread().interrupted());
					e.printStackTrace();
					Thread.currentThread().interrupt();
					System.out.println("isInterrupted:"+Thread.currentThread().isInterrupted()+",interrupted:"+Thread.currentThread().interrupted());
					
				}finally{
					lock.unlock();
				}
			}
			System.out.println("4:"+terminateRequested);
	}
	
	public void doLogLine(String line){
		//do log line
		System.out.println("consumed:"+line);
	}
	
	public void log(String productLine){
			lock.lock();
			try{
				linesToLog.add(productLine);
				System.out.println("produced:"+productLine);
				notEmpty.signalAll();
			}finally{
				lock.unlock();	
			}	
	}
}