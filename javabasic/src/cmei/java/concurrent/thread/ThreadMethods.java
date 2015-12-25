package cmei.java.concurrent.thread;

public class ThreadMethods {
	
	public static void main(String[] args){
//		for(int i=1;i<=10;i++){
//			MyThread myThread=new MyThread();
//			myThread.setPriority(Thread.MIN_PRIORITY*i);
//			myThread.setName("thread"+String.valueOf(i));
//			myThread.start();
//		}
		
		InterruptThread it=new InterruptThread();
		it.start();
		try {
			Thread.currentThread().sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		it.stop=true;
		it.interrupt();
	}
}

/***
 *1)calling interrupt doesn't stop the thread. a mechanism for "waking a thread up" early from a blocking call
 *2)Thread.interrupt()方法不会中断一个正在运行的线程。
 *这一方法实际上完成的是，在线程受到阻塞时抛出一个中断信号，这样线程就得以退出阻塞的状态。
 *更确切的说，如果线程被Object.wait, Thread.join和Thread.sleep三种方法之一阻塞，那么，它将接收到一个中断异常（InterruptedException），从而提早地终结被阻塞状态。
 * 因此，如果线程被上述几种方法阻塞，正确的停止线程方式是设置共享变量，并调用interrupt()（注意变量应该先设置）。如果线程没有被阻塞，这时调用interrupt()将不起作用；否则，线程就将得到异常（该线程必须事先预备好处理此状况），接着逃离阻塞状态。在任何一种情况中，最后线程都将检查共享变量然后再停止。
 * 3)如果线程的阻塞来自IO等，则interrupt不能中断阻塞，而是其他的 io close==。
 */
class InterruptThread extends Thread{

	public volatile boolean stop=false; 
	public void run(){
		while(!stop){
			try {
				System.out.println("thread is sleeping...");
				Thread.sleep(1000);
				//System.out.println("inerrupt current thread-"+Thread.currentThread().isInterrupted()+":"+Thread.currentThread().interrupted());
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				//System.out.println("inerrupt current thread-"+Thread.currentThread().isInterrupted()+":"+Thread.currentThread().interrupted());
				
				//Thread.currentThread().interrupt();
				//System.out.println("inerrupt current thread-"+Thread.currentThread().isInterrupted()+":"+Thread.currentThread().interrupted());
				//break;
		} 
		}
		System.out.println("thread is stopped!");
	}
}
