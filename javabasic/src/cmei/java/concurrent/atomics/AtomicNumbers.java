package cmei.java.concurrent.atomics;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 
 * A small toolkit of classes that support lock-free thread-safe programming on single variables.
 * AtomicXXX:��Ҫ�ṩ��XXX������ԭ�Ӳ�����
 * AtomicInteger,AtomicLong�����������ṩatomic counter
 * AtomicBoolean������atomically����ĳ��flag
 * 
 * 
 * @since 2014.12.30
 * @author cmei
 *
 *http://blog.163.com/javaee_chen/blog/static/17919507720113138059122/
 */
public class AtomicNumbers {
	
 public static void main(String args[]){
	 int n=1000;
	 ThreadTest[] threads=new ThreadTest[n];
	
	 for(int i=0;i<n;i++){
		 threads[i]=new ThreadTest();
		 threads[i].start();
	 }
	 for(int i=0;i<n;i++){
		 try {
			threads[i].join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	 }
	 ThreadTest.printStatistics();
	 
 }

	
}

/**
 * 
 *ͳ��ĳ��http call�������Ϣ
 *
 */
class HttpStatistics{
	private final AtomicLong requestNum=new AtomicLong(0);
	
	private final AtomicLong errorNum=new AtomicLong(0);
	
	private Long reqNum=0l;
	
	public volatile boolean enable=true;//volatile �������Ϊһ��stop���ƣ������á���һ��д����������
	
	
	private final AtomicBoolean errorControll=new AtomicBoolean(true); 
	
	public void addRequestNum(){
		reqNum++;
		System.out.println("requestNum.incrementAndGet="+requestNum.incrementAndGet());
		if(errorControll.compareAndSet(true,false)){
			System.out.println("#errorNum.incrementAndGet="+errorNum.incrementAndGet());
		}else{
			errorControll.compareAndSet(false, true);
			System.out.println("#errorControll.compareAndSet(false, true);");
		}			
	}
	
	/**
	 * �˴�errorcontroll����volatile��ﲻ�����error��ģ��Ч��,valatile�ܱ�֤�������£�
	 * ���ڶ��̲߳�����enable=false�ĸ�ֵ���ܷ�����enable������ֵΪfalse��������֮ǰ����true��
	 * ������valatile+synchronized���ͬAtomicBoolean
	 */
	public void addRequestNum2(){
		reqNum++;
		System.out.println("requestNum.incrementAndGet="+requestNum.incrementAndGet());
		if(enable){
			
			//if(errorControll.compareAndSet(true,false)){
				System.out.println("#errorNum.incrementAndGet="+errorNum.incrementAndGet());
			enable=false;
		}else{
			System.out.println("#errorControll.compareAndSet(false, true);");
			enable=true;
		}	
	}
	
	public String getStatistics(){
		return "requestNum="+requestNum.get()+",errorNum="+errorNum.get()+"reqNum="+reqNum;
	}
}

class ThreadTest extends Thread{
	
	public final static HttpStatistics statistics=new HttpStatistics();

	public void run(){
		//statistics.addRequestNum();
		statistics.addRequestNum2();
	}
	
	public static void printStatistics(){
		System.out.println(statistics.getStatistics());
	}

}







