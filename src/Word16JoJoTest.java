import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Word16JoJoTest {

    Bit t = new Bit(true);
    Bit f = new Bit(false);

    private Word16 getStripe() {
        return new Word16(new Bit[]{t, f, t, f, t, f, t, f, t, f, t, f, t, f, t, f});
    }

    private Word16 getStripe2() {
        return new Word16(new Bit[]{f, t, f, t, f, t, f, t, f, t, f, t, f, t, f, t});
    }

    private Word16 getAllTrue() {
        return new Word16(new Bit[]{t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t});
    }

    private Word16 getAllFalse() {
        return new Word16(new Bit[]{f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f});
    }

    @Test
    void Copy() {
        Word16 stripe = getStripe();
        Word16 result = new Word16() ;
        for (int i = 0; i < 16; i++) {
            stripe.copy(result);

            assertTrue(stripe.equals(result));
        }


    }

    @Test
    void toStringTest() {
        Word16 word = new Word16();
        Word32 longWord = new Word32();
        Word16 stripe = getStripe();
        word.getBit(0).assign(Bit.boolValues.TRUE);
        longWord.getBit(23).assign(Bit.boolValues.TRUE);

        Assertions.assertEquals("t",word.getBit(0).toString());
        Assertions.assertEquals("f",word.getBit(1).toString());
        Assertions.assertEquals("t",longWord.getBit(23).toString());
        Assertions.assertEquals("f",longWord.getBit(1).toString());
        Assertions.assertEquals("f",stripe.getBit(0).toString());
    }

    @Test
    void setN(){
        Word16 word = new Word16();
        Word32 longWord = new Word32();

        word.setBitN(0, new Bit(true));
        longWord.setBitN(1, new Bit(true));
        Assertions.assertEquals("t",word.getBit(0).toString());
        Assertions.assertEquals("f",word.getBit(1).toString());
        Assertions.assertEquals("t",longWord.getBit(1).toString());
    }

}