package Tests;

import CPU.Bit;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import CPU.*;
import static org.junit.jupiter.api.Assertions.*;

class Word16Test {

    Bit t = new Bit(true);
    Bit f = new Bit(false);

    private Word16 getStripe() {
        return new Word16(new Bit[]{ t, f, t, f, t, f, t, f, t, f, t, f, t, f, t, f });
    }

    private Word16 getStripe2() {
        return new Word16(new Bit[]{ f, t, f, t, f, t, f, t, f, t, f, t, f, t, f, t });
    }

    private Word16 getAllTrue() {
        return new Word16(new Bit[]{ t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t });
    }

    private Word16 getAllFalse() {
        return new Word16(new Bit[]{ f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f });
    }

    @Test
    void getBitN() {
        var w = getStripe();
        for (int i=0;i<16;i++) {
            Bit result = new Bit(true);
            w.getBitN(i,result);
            if (i%2==1)
                assertEquals(result.getValue(),f.getValue());
            else
                assertEquals(result.getValue(),t.getValue());
        }
    }

    @Test
    void and() {
        Word16 fw = getAllFalse();
        Word16 tw = getAllTrue();
        Word16 sw = getStripe();
        Word16 result = new Word16();
        fw.and(fw,result);
        Assertions.assertTrue(fw.equals(result));
        tw.and(tw,result);
        Assertions.assertTrue(tw.equals(result));
        sw.and(tw,result);
        Assertions.assertTrue(sw.equals(result));
        sw.and(sw,result);
        Assertions.assertTrue(sw.equals(result));
    }

    @Test
    void or() {
        Word16 fw = getAllFalse();
        Word16 tw = getAllTrue();
        Word16 sw = getStripe();
        Word16 result = new Word16();
        fw.or(fw,result);
        Assertions.assertTrue(fw.equals(result));
        tw.or(tw,result);
        Assertions.assertTrue(tw.equals(result));
        sw.or(tw,result);
        Assertions.assertTrue(tw.equals(result));
        sw.or(sw,result);
        Assertions.assertTrue(sw.equals(result));
        sw.or(fw,result);
        Assertions.assertTrue(sw.equals(result));
    }

    @Test
    void xor() {
        Word16 fw = getAllFalse();
        Word16 tw = getAllTrue();
        Word16 sw = getStripe();
        Word16 sw2 = getStripe2();
        Word16 result = new Word16();
        fw.xor(fw,result);
        Assertions.assertTrue(fw.equals(result));

        tw.xor(tw,result);
        Assertions.assertTrue(fw.equals(result));

        sw.xor(tw,result);
        Assertions.assertTrue(sw2.equals(result));

        sw.xor(sw,result);
        Assertions.assertTrue(fw.equals(result));

        sw.xor(fw,result);
        Assertions.assertTrue(sw.equals(result));
    }

    @Test
    void not() {
        Word16 fw = getAllFalse();
        Word16 tw = getAllTrue();
        Word16 sw = getStripe();
        Word16 sw2 = getStripe2();
        Word16 result = new Word16();
        fw.not(result);
        Assertions.assertTrue(tw.equals(result));
        tw.not(result);
        Assertions.assertTrue(fw.equals(result));
        sw.not(result);
        Assertions.assertTrue(sw2.equals(result));
        sw2.not(result);
        Assertions.assertTrue(sw.equals(result));
    }
}