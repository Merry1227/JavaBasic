package cmei.java.concurrent.atomics;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.concurrent.atomic.AtomicReferenceArray;

/**
 * AtomicIntegerArray/AtomicReferenceArray ��֤��������ÿ��Ԫ�صĸ��²�����thread-safe�ġ�ֻ�����ڹ̶���С�����顣
 * ConcurrentHashMap:��lockmap��ĳ��segement��(��hashtable���lock����map,���ܽϵ�,Hashtable�Ǽ̳�deprecated dictionary ��ʵ��),hashtable������Collections.synchronizedMap+HashMap���档
 * concurrentHashMap��size()�ȷ����ȽϺ�ʱ����Ϊ��Ҫ����segement lock.��synchronizedMap�������Ҫ��������mapʱ��ܵ�Ч����Ϊ�����߳���Ҫ�ϳ��ȴ���ĳ���̱߳���mapʱ��
	 * This is not much of an issue when you have simple inserts and lookups (unless you do it extremely intensively), 
	 * but becomes a big problem when you need to iterate over the entire Map, 
	 * which can take a long time for a large Map - while one thread does that, 
	 * all others have to wait if they want to insert or lookup anything.
	 * 	The ConcurrentHashMap uses very sophisticated techniques to reduce 
	 * the need for synchronization and allow parallel read access by multiple threads without synchronization and, 
	 * more importantly, provides an Iterator that requires no synchronization and even allows the Map to be modified during interation
	 * (though it makes no guarantees whether or not elements that were inserted during iteration will be returned).
 * 


 * @author cmei
 *
 */

public class AtomicArrays {
	
	private final static AtomicIntegerArray intArray=new AtomicIntegerArray(new int []{1,2,3,4,5,6,7,8,9,10});
	//private final static AtomicReferenceArray<String> refArray=new AtomicReferenceArray<String> (10);
	
	private final static int[] iArray=new int []{1,2,3,4,5,6,7,8,9,10};
	
	private final static List<Integer> listArray=Collections.synchronizedList(Arrays.asList(1,2,3,4,5,6,7,8,9,10));
	
	private final static CopyOnWriteArrayList<Integer> copyList=new CopyOnWriteArrayList<Integer>(Arrays.asList(1,2,3,4,5,6,7,8,9,10));
	
	
	public static void main(String[] args){
		int n=10000;
		final Object object=new Object();
		Thread[] ths=new Thread[n];
		for(int i=0;i<n;i++){
			final int index=i%10;
			final int thnum=i;
			ths[i]=new Thread(){
				public void run(){
					int result=intArray.addAndGet(index, 1);
					//System.out.println("thread_"+thnum+",result:"+result);
					try {
						Thread.sleep(index);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					iArray[index]+=1;					
					synchronized(object){
						listArray.set(index, listArray.get(index)+1);		//�޷���֤get Ȼ��set֮��thread-safe������Ҫ��synchronized.
					};
					synchronized(object){
						copyList.set(index,copyList.get(index)+1);
					};
				}
			};
			ths[i].start();
		}
		
		for(Thread th:ths){
			try {
				th.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		System.out.println("\nintArray:");
		for(int i=0;i<intArray.length();i++){
			System.out.print(intArray.get(i)+" ");
		}
		
		System.out.println("\niArray:");
		for(int i=0;i<iArray.length;i++){
			System.out.print(iArray[i]+" ");
		}
		
		System.out.println("\nlistArray:");
		for(int i=0;i<listArray.size();i++){
			System.out.print(listArray.get(i)+" ");
		}
		
		System.out.println("\ncopyList:");
		for(int i=0;i<copyList.size();i++){
			System.out.print(copyList.get(i)+" ");
		}
		
		
	}

}
