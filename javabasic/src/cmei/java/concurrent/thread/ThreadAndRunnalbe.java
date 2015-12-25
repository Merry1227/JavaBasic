package cmei.java.concurrent.thread;

public class ThreadAndRunnalbe {
	
	/***
	 * topic 1:thread 和runnable的区别
	 * 1）thread也实现了runnable
	 * 2)runnable更多定义了task是要干什么事 what in run（）
	 * 3）thread更多的控制了如何控制task的执行 （因为start in thread） 和其他与线程相关的上下文等
	 * 4）多个Runnable里面的私有属性可共享（如果被同一个thread wrap）的话，而不同thread的属性则不是共享的。
	 * 
	 * topic 2:how to stop a thread?
	 * 1)Firstly, the thing you shouldn't do is call Thread.stop() abruptly
	 * @param args
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws InterruptedException{
		for (int i=0;i<10;i++){
			Thread t=new MyThread();
			t.start();
		}
		Thread.sleep(1000);
		MyRunnable r=new MyRunnable();
		for(int i=0;i<10;i++){
				Thread t=new Thread(r);
				t.start();
		}
	
	   MyRunnable r2=new MyRunnable();
	   
	   Thread t2=new Thread(r2);
	   t2.start();
	}
}


class MyThread extends Thread{
	
	private static int s=0;
	private int x=0;
	
	public void run(){
		++x;
		++s;
		System.out.println("MyThread-"+this.getId()+"-"+this.getName()+"-"+this.getPriority()+"-"+this.hashCode()+"-"+this.isDaemon()+this.isInterrupted()+":x="+x+":s"+s);
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

class MyRunnable implements Runnable{
	private int x=0;

	public void run() {
		//synchronized(this){
		++x;
		System.out.println("MyRunnableThread-"+Thread.currentThread().getId()+"-"+Thread.currentThread().getName()+"-"+Thread.currentThread().getPriority()+"-"+Thread.currentThread().hashCode()+"-"+Thread.currentThread().isDaemon()+Thread.currentThread().isInterrupted()+":x="+x);
		//}
	}
}
