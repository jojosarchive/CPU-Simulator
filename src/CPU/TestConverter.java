package CPU;

import CPU.Adder;
import CPU.Bit;

public class TestConverter {

    public static void fromInt(int value, Word32 result) {
        boolean negativeNumber = false;
        int index = 31;

        resetResult(result);

        if (value < 0) {
            negativeNumber = true;
            value = value * -1;
        }

        while (value != 0){
            if(value % 2 == 1)
                result.setBitN(index, new Bit(true));
            else
                result.setBitN(index, new Bit(false));

            value /= 2;
            index--;
        }

        // make all 0's to 1's and all 1's to 0's
        if(negativeNumber)
            twosCompliment(result);

    }

    public static int toInt(Word32 value) {
        Word32 tempVal = new Word32();
        value.copy(tempVal);
        boolean isNegative = false;
        int sum = 0;
        int index = 0;

        Bit temp = new Bit(false);
        tempVal.getBitN(0, temp);

        //2's complement
        if (temp.getValue() == Bit.boolValues.TRUE) {
            isNegative = true;
            twosCompliment(tempVal);
        }

        for (int i = 31; i > 0; i--){
            if (tempVal.getBit(i).getValue() == Bit.boolValues.TRUE)
                sum += (int) Math.pow(2, index);
            index++;
        }

        if(isNegative)
            sum = sum * -1;

        resetResult(tempVal);

        return sum;
    }

    public static void resetResult(Word32 result){
        for(int i = 0; i < 32; i++){
            result.setBitN(i, new Bit(false));
        }
    }

    public static void twosCompliment(Word32 word){
        for(int i = 0; i < 32; i++){
            Bit temp = new Bit(false);
            word.getBit(i).not(temp);
            word.setBitN(i, temp);
        }

        Word32 binaryNumber1 = new Word32();
        binaryNumber1.setBitN(31, new Bit(true));
        Adder.add(word, binaryNumber1, word);
    }


}

//        fffftfttff