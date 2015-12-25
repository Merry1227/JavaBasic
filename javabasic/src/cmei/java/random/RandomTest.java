package cmei.java.random;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * 1.seed作为初始输入，seed一定，产生的随机数序列一定，seed一定，序列可预见
 * 2.尽量使seed随机，可为多维度因素的hash值，当前时间，内存状态等。
 * @author cmei
 *
 */
public class RandomTest {
    public static void main(String args[]){
        testRandom(0);
        testRandom(0);
        testRandom(2);
        testRandom(2);
    }

    public static void testRandom(int seed){
        Map m = new HashMap();
        Random rand = new Random();//然后用Random rand = new Random(5);测试比较
        rand.setSeed(seed);
        for (int i = 0; i < 10; i++){
            int a = rand.nextInt(10);
            Integer j = new Integer(a);
            int counter=0;
            if (m.containsKey(j)){
                counter=(Integer)m.get(j);
            }
            m.put(j,++counter);
            System.out.print(a+",");
        }
        System.out.println(m);
    }

}

