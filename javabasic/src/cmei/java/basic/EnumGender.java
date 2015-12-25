package cmei.java.basic;

import java.util.EnumMap;
import java.util.EnumSet;

/**
 * 考虑用Enum的情况：
 * 1.实例数目确认有限
 * 2.属性简单
 * 3.Examples: dbconnectioninfo, errormessage,
 *
 * Enum
 * 1.可实现接口
 * 2. 可直接用switch
 * 3. EnumSet，EnumMap 为enum 做了特殊的优化，比普通的HashMap，HashSet更快 http://blog.sina.com.cn/s/blog_4b00fd1b0100x3yp.html
 * */

public enum EnumGender implements printable{
    MALE('M'),FEMALE('F');

    private char code;

    private EnumGender(char code){
        this.code=code;
    }

    public char getCode(){
        return code;
    }

    @Override
    public void print() {
        System.out.print(code);
    }

    public static EnumGender valueOf(char code){
        if(code=='M'){
            return EnumGender.MALE;
        }else{
            return EnumGender.FEMALE;
        }
    }

    public static void main(String[]args){
        System.out.println(EnumGender.valueOf('M'));
        System.out.println(EnumGender.values());
        System.out.println(EnumGender.valueOf(EnumGender.class,"MALE"));

        //switch
        EnumGender x=EnumGender.valueOf(EnumGender.class,"MALE");
        switch (x){
            case MALE:
                System.out.println(x);
                break;
            case FEMALE:
                System.out.println(x);
                break;
        }

        //EnumSet
        EnumSet<EnumGender> enumSet1= EnumSet.of(MALE);
        EnumSet<EnumGender> enumSet2=EnumSet.complementOf(enumSet1);
        EnumSet<EnumGender> enumSet3=EnumSet.range(MALE,FEMALE);

        EnumMap<EnumGender,String> enumMap=new EnumMap<EnumGender, String>(EnumGender.class);
        enumMap.put(MALE,"male");
        enumMap.put(FEMALE,"female");
        System.out.print(enumMap.get(FEMALE));


//		System.out.println(OfferType.getOfferTypeByID(0));
//		System.out.println(OfferType.getOfferIndex(OfferType.PERCENTAGE));
    }


    public static enum OfferType {

        DEFAULTYPE,FIXED, PERCENTAGE;

        static OfferType getOfferTypeByID(int i){
            return OfferType.values()[i];
        }

        static int getOfferIndex(OfferType typeName){
            return typeName.ordinal();
        }
    }
}

interface printable{
    void print();
}
