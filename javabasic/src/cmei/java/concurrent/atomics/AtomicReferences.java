package cmei.java.concurrent.atomics;

import java.util.concurrent.atomic.AtomicMarkableReference;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicStampedReference;

/**
 * ������AtomicLong/Boolean/Integer ��AtomicReference<Long>,AtomicReference<Boolean>,AtomicRefence<Integer>
 * AtomicReference�ṩ���������͵������ṩatomic��CompareAndSwap.
 * AtomicStampedReference���ṩһ��int stamp���ABA���⡣
 * AtomicMarkableReference:��ʵ��AtomicStampReference�ļ򻯣�stamp�����״̬�Ƚ϶࣬��һ��״̬�б���markable ��ֻ������״̬�������ڱ�ʾ�߼��ϸ������Ƿ�ɾ����
 * 
 * AtomicReferenceFieldUpdater:���Զ����е����volatile�����ԭ���޸ġ�����ʵ����AtomicRefence(Integer/Long)���ơ���ͬ��Ӧ������������Ҫrefactor��ʱ��
 * @author cmei
 *
 */
public class AtomicReferences {
	
	public static final String abc0="abc0";

	public final static AtomicReference<String> aref=new AtomicReference<String>();
	
	public final static AtomicStampedReference<String> asref=new AtomicStampedReference<String>("abc0", 0);

	public final static AtomicMarkableReference<String> amref=new AtomicMarkableReference<String>("abc0",false);
	//ABA ����
	
	//public AtomicIntegerFieldUpdater
	
	
	public static void main(String[] args){
		int n=100;
		Thread[] ths=new Thread[n];
		final String abc1="abc1";
		
		final String abc2=new String(abc1);
		aref.set("abc0");
		for(int i=0;i<n;i++){
			final int num=i;
			ths[i]=new Thread(){
				public void run(){
//					try{
//						Thread.sleep((num%2)*100);
//					}catch(Exception e){
//						e.printStackTrace();
//					}
//					if(aref.compareAndSet("abc0",abc1)){
//						System.out.println("num="+num+"abc0-1");	
//					}else if(aref.compareAndSet(abc1,abc2)){
//						System.out.println("num="+num+"abc1-2");
//					}else if(aref.compareAndSet(abc2,"abc0")){
//						System.out.println("num="+num+"abc2-0");
//					}
					
//					if(asref.compareAndSet("abc0",abc1,num,num+1)){
//						System.out.println("asref:num="+num+"abc0-1");	
//					}else if(asref.compareAndSet(abc1,abc2,num,num+1)){
//						System.out.println("asref:num="+num+"abc1-2");
//					}else if(asref.compareAndSet(abc2,"abc0",num,num+1)){
//						System.out.println("asref:num="+num+"abc2-0");
//					}
					
					if(amref.compareAndSet("abc0",abc1,false,false)){
						System.out.println("asref:num="+num+"abc0-1");	
					}else if(amref.compareAndSet(abc1,abc2,false,false)){
						System.out.println("asref:num="+num+"abc1-2");
					}else if(amref.compareAndSet(abc2,"abc0",false,true)){
						System.out.println("asref:num="+num+"abc2-0");
					}
					
				}	
			};
			ths[i].start();
			
		}
		
		 for(int i=0;i<n;i++){
			 try {
				ths[i].join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		 }
	}
}
