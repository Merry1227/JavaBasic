package cmei.java.concurrent.locks;

import sun.tools.jconsole.Worker;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * CyclicBarrier:用于多个线程等待某个线程完成某件事后再执行的场景。
 * 比如说：跑步时， 所有人准备好，然后等待裁判发出ready-go信号（或者其他事），然后在开始跑（各干各的事）
 * new CyclicBarrier(parties,runnable)：
 * { parties:等待同步信号的线程数，
 * runnable:等待裁判做某件事，可选，或等着所有线程调用到 await 开始执行（也就是说它也好等着所有人准备好
 *         runnable是可选的，如果它没有，则是其他线程互相等着，直到所有人都准备好）
 *
 * Created by canhuamei on 12/6/16.
 */
public class CyclicBarrierExample {

    public static void main(String[] args) throws InterruptedException {
        Runnable runnable=()-> System.out.println(" runnable in cyclicBarrier!");
        
        int parties=3;

        CyclicBarrier cyclicBarrier=new CyclicBarrier(parties,runnable);

        Thread[] threads=new Thread[parties];
        for (int i=0;i<parties;i++){
            threads[i]=new Worker(String.valueOf(i),cyclicBarrier);
            threads[i].start();
        }

//        for (int i=0;i<parties;i++){
//            threads[i].join();
//        }


        System.out.println("all finished!");

    }

    static class Worker extends Thread{
        private CyclicBarrier cyclicBarrier;

        private String id;

        public Worker(String name,CyclicBarrier cyclicBarrier){
            this.id=name;
            this.cyclicBarrier=cyclicBarrier;
        }

        public void run(){
            System.out.println(this.id+" start@"+Thread.currentThread());

            try {
                cyclicBarrier.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }

            for(int i=0;i<5;i++){
                System.out.println(this.id+" "+i);
            }

            System.out.println(this.id+" finished@"+Thread.currentThread());

        }
    }



}

