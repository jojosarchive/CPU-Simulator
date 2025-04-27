package Tests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import CPU.*;
import static org.junit.jupiter.api.Assertions.*;

class JoJoMathTests {
    Bit t= new Bit(true);
    Bit f= new Bit(false);


    @Test
    void testTestConverter() {
        var value = new Word32();
        // assertEquals(0,CPU.TestConverter.toInt(value));
        value = new Word32(new Bit[]{
                f,f,f,f, f,f,f,f,      f,f,f,f, f,f,f,f,
                f,f,f,f, f,f,f,f,      f,f,f,f, t,f,f,t
        });
        Assertions.assertEquals(9, TestConverter.toInt(value));

        value = new Word32(new Bit[]{
                t,t,t,t, t,t,t,t,      t,t,t,t, t,t,t,t,
                t,t,t,t, t,t,t,t,     t,t,t,t, f,f,f,f
        });
        Assertions.assertEquals(-16, TestConverter.toInt(value));
    }

    @Test
    void testFromInt() {
        var value = new Word32();
        // assertEquals(0,CPU.TestConverter.toInt(value));
        value = new Word32();
        TestConverter.fromInt(5, value);
        Assertions.assertEquals(5,value);


        //assertEquals(,CPU.TestConverter.toInt(value));
    }

    @Test
    void add() {
        var x = new Word32();
        var y = new Word32();
        var z = new Word32();

                TestConverter.fromInt(3, x);
                TestConverter.fromInt(3, y);
                Adder.add(x, y, z);
                Assertions.assertEquals(6, TestConverter.toInt(z));

    }



}
