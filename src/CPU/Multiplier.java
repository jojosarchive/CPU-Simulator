package CPU;

import CPU.Adder;
import CPU.Bit;

public class Multiplier {
    public static void multiply(Word32 a, Word32 b, Word32 result) {

        Word32 wordTemp = new Word32();
        Word32 shifterTemp = new Word32();

        a.copy(wordTemp);
        //bottom
        for (int i = 31; i >= 0; i--){

            Bit temp = new Bit(false);
            b.getBitN(i, temp);

            if(temp.getValue() == Bit.boolValues.TRUE){
                Adder.add(result, wordTemp, result);
            }

            Shifter.LeftShift(wordTemp, 1, shifterTemp);
            shifterTemp.copy(wordTemp);
        }
    }
}
