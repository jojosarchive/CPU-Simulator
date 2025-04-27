package Tests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import CPU.*;
import static org.junit.jupiter.api.Assertions.*;

class MultiplierTest {

    int[] numbers = new int[] {
            0,10,20,300,
            3856
    };
    @Test
    void multiply() {
        for (var i : numbers)
            for (var j : numbers) {
                Word32 iw = new Word32();
                Word32 jw = new Word32();
                Word32 result = new Word32();
                TestConverter.fromInt(i,iw);
                TestConverter.fromInt(j,jw);
                Multiplier.multiply(iw,jw,result);
                Assertions.assertEquals(i*j, TestConverter.toInt(result));
            }
    }
}