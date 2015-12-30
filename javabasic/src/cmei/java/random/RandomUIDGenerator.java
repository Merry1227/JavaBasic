package cmei.java.random;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * Created by cmei on 2015/12/1.
 */
public class RandomUIDGenerator {

    /**
     * return distinct d
     */
    public static Set getDistintNumbers(int total,int max,int min){
        Set numbers=new HashSet();
        if(total<0||max<min){
            return numbers;
        }
        int max0=Integer.MAX_VALUE;
        if(max0>max){
            max0=max;
        }
        int min0=0;
        if(min>min0){
            min0=min;
        }

        int n=max-min;
        if(total<n){
            n=total;
        }
        Random r=new Random();
        //System.out.println(max0+","+min0+" n:"+n);
        while(numbers.size()<n){
            int temp=min0+r.nextInt(max0-min0+1);
            numbers.add(temp);
            //		System.out.println(temp);
        }
        return numbers;
    }


    public static Set getUVSIDS(int total){
        Set ids=new HashSet();
        Set numbers= RandomUIDGenerator.getDistintNumbers(total,99999,1);
        Iterator ite=numbers.iterator();
        while(ite.hasNext()){
            Number id=-((Number)ite.next()).intValue();
            ids.add(id);
        }
        return ids;
    }

    public static void save2File(Collection ids,int total,int rightnumber,String fileName){
        Set codes1= RandomUIDGenerator.getDistintNumbers(rightnumber,99999,10000);
        Set codes2= RandomUIDGenerator.getDistintNumbers(rightnumber,999999,100000);

        StringBuffer bf=new StringBuffer();
        bf.append("customer_id,coupon_code\n");

        Iterator id_ite=ids.iterator();
        Iterator code_ite1=codes1.iterator();
        Iterator code_ite2=codes2.iterator();
        //System.out.println("id:"+ids.size()+" code1:"+codes1.size()+" code2:"+codes2.size());
        int i=0;
        while(id_ite.hasNext()&&code_ite1.hasNext()&&code_ite2.hasNext()&&i<total){
            Number id=(Number)id_ite.next();
            Number code1=(Number)code_ite1.next();
            Number code2=(Number)code_ite2.next();
            bf.append(id+","+code1+code2+"\n");
            i++;
        }
        while(id_ite.hasNext()&&i<total){
            Number id=(Number)id_ite.next();
            String code="xxxxx";
            bf.append(id+","+code+"\n");
            i++;
        }
        System.out.println("line:"+total);

        try {
            BufferedWriter writer=new BufferedWriter(new FileWriter("F:\\workspace\\hellowords\\"+fileName+".csv"));
            //System.out.println(bf.toString());
            writer.append(bf.toString());
            writer.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public static void main(String[] args) throws IOException{
        Set ids= RandomUIDGenerator.getUVSIDS(6000);
        System.out.println(ids.size());
        int[] n={1000,2000,3000,4000,5000};
        double[] rate={0,0.2,0.5,0.8,1};
        String baseName="uvsid_couponcodes";
        for(int i=0;i<n.length;i++){
            for(int j=0;j<rate.length;j++){
                String fileName=baseName+"_"+n[i]+"-"+rate[j];
                File f=new File("F:\\workspace\\hellowords\\"+fileName+".csv");
                if(!f.exists()){
                    f.createNewFile();
                }
                RandomUIDGenerator.save2File(ids,n[i],(int)(n[i]*rate[j]),fileName);
            }
        }
    }

}

