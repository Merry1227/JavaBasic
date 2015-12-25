package cmei.java.concurrent.thread;

import java.util.Map.Entry;

public class ObjectMethods extends Object{
	
	public static boolean signal=true;
	
	
	/** topic 1:wait and notify 用户线程协调工作，根据条件来进行task的处理，
	 * 首先，wait()和notify(),notifyAll()是Object类的方法，sleep()和yield()是Thread类的方法。
	(1).常用的wait方法有wait()和wait(long timeout):
	void wait() 在其他线程调用此对象的 notify() 方法或 notifyAll() 方法前，导致当前线程等待。 
	void wait(long timeout) 在其他线程调用此对象的 notify() 方法或 notifyAll() 方法，或者超过指定的时间量前，导致当前线程等待。
	wait()后，线程会释放掉它所占有的“锁标志”，从而使线程所在对象中的其它synchronized数据可被别的线程使用。
	 
	wait()和notify()因为会对对象的“锁标志”进行操作，所以它们必须在synchronized函数或synchronized代码块中进行调用。
	如果在non-synchronized函数或non-synchronized代码块中进行调用，虽然能编译通过，但在运 行时会发生IllegalMonitorStateException的异常。
	(2) API doc: wait 应该放在while循环里面放置假唤醒。
      (3)实际开发中一般不太需要用上这俩，因为相应的生产者开发者信号量等已经有相应的lib after jdk 1.5
      (4)
	 * @author cmei
	 *
	 *
	 *topic 2: hash and clone,equals
	 */
	public static void main(String[] args){
		
		final Object lock=new Object();
		
	
		
		Thread t1=new Thread(new Runnable(){
			public void run() {
			synchronized(lock){
				int i=0;
				
				while(i<10){
					System.out.println("t1:"+i);
					while(signal){
						try {
							lock.wait();
							System.out.println("t1 wait:"+i);
							
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					signal=true;
					lock.notify();
					i++;
				 }
				
				
				
			    }
			  
			}
		});
		
		Thread t2=new Thread(new Runnable(){
			public void run() {
			synchronized(lock){
				int i=0;	
				while(i<10){
					System.out.println("t2:"+i);
					while(!signal){
						try {
							lock.wait();
							System.out.println("t2 wait:"+i);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					signal=false;
					lock.notify();	
					i++;
				}
					
			}
			}
		});
		
		
			t1.start();
			t2.start();
			try {
				t2.join();
				t1.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		 for(Entry<Thread, StackTraceElement[]> entry:Thread.getAllStackTraces().entrySet()){
			 Thread t=entry.getKey();
			 System.out.println(t.getId()+":"+t.getState());
		 }
		
		
	 
		   
	}
	

}
