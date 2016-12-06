package cmei.java.basic;

import java.math.BigDecimal;

/**
 * Created by canhuamei on 11/25/16.
 */
public class Partitioner {

    /**
     *  Converts a string to a BigDecimal representation in Base 2^21 format.
     *  The maximum Unicode code point value defined is 10FFFF.  Although
     *  not all database system support UTF16 and mostly we expect UCS2
     *  characters only, for completeness, we assume that all the unicode
     *  characters are supported.
     *  Given a string 's' containing characters s_0, s_1,..s_n,
     *  the string is interpreted as the number: 0.s_0 s_1 s_2 s_3 s_48)
     *  This can be split and each split point can be converted back to
     *  a string value for comparison purposes.   The number of characters
     *  is restricted to prevent repeating fractions and rounding errors
     *  towards the higher fraction positions.
     */
    private static final BigDecimal UNITS_BASE = new BigDecimal(0x200000);
    private static final int MAX_CHARS_TO_CONVERT = 4;

    public static void main(String args[]){
        String abc="中国人";
        Partitioner partitioner=new Partitioner();
        BigDecimal res=partitioner.textToBigDecimal(abc);
        System.out.println(res);
    }

    private BigDecimal textToBigDecimal(String str) {
        BigDecimal result = BigDecimal.ZERO;
        BigDecimal divisor = UNITS_BASE;

        int len = Math.min(str.length(), MAX_CHARS_TO_CONVERT);

        for (int n = 0; n < len; ) {
            int codePoint = str.codePointAt(n);
            n += Character.charCount(codePoint);
            BigDecimal val = divide(new BigDecimal(codePoint), divisor);
            result = result.add(val);
            divisor = divisor.multiply(UNITS_BASE);
        }

        return result;
    }

    private String bigDecimalToText(BigDecimal bd) {
        BigDecimal curVal = bd.stripTrailingZeros();
        StringBuilder sb = new StringBuilder();

        for (int n = 0; n < MAX_CHARS_TO_CONVERT; ++n) {
            curVal = curVal.multiply(UNITS_BASE);
            int cp = curVal.intValue();
            if (0 >= cp) {
                break;
            }

            if (!Character.isDefined(cp)) {
                int t_cp = Character.MAX_CODE_POINT < cp ? 1 : cp;
                // We are guaranteed to find at least one character
                while(!Character.isDefined(t_cp)) {
                    ++t_cp;
                    if (t_cp == cp) {
                        break;
                    }
                    if (t_cp >= Character.MAX_CODE_POINT || t_cp <= 0)  {
                        t_cp = 1;
                    }
                }
                cp = t_cp;
            }
            curVal = curVal.subtract(new BigDecimal(cp));
            sb.append(Character.toChars(cp));
        }

        return sb.toString();
    }


    protected BigDecimal divide(BigDecimal numerator, BigDecimal denominator) {
        try {
            return numerator.divide(denominator);
        } catch (ArithmeticException ae) {
            return numerator.divide(denominator, BigDecimal.ROUND_HALF_UP);
        }
    }
}
