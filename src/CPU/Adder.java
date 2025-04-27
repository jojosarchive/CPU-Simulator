package CPU;

public class Adder {


    public static void subtract(Word32 a, Word32 b, Word32 result) {
        //2's complement to make a - b = a + (-b)
        for (int i = 0; i < 32; i++){
            Bit temp = new Bit(false);
            b.getBitN(i, temp);
            temp.not(temp);
            b.setBitN(i, temp);
        }

        Word32 binaryOne = new Word32();
        binaryOne.setBitN(31, new Bit(true));
        add(b, binaryOne, b);
        add(a, b, result);


    }

    public static void add(Word32 a, Word32 b, Word32 result) {
        /*
        case 1    case 2    case 3     case 4
        1         1         0          0
        1         0         1          0
        */

        Bit carryIn = new Bit(false);
        Bit[] sumAndCarry = new Bit[1];
        //Iterating through but starting from right
        for (int i = 31; i >= 0; i--) {

            sumAndCarry = singularAdder(a.getBit(i), b.getBit(i), carryIn);
            if (sumAndCarry[0].getValue() == Bit.boolValues.TRUE)
                result.setBitN(i, new Bit(true));

            else if (sumAndCarry[0].getValue() == Bit.boolValues.FALSE)
                result.setBitN(i, new Bit(false));

            if (sumAndCarry[1].getValue() == Bit.boolValues.TRUE)
                carryIn.assign(Bit.boolValues.TRUE);

            else if (sumAndCarry[1].getValue() == Bit.boolValues.FALSE)
                carryIn.assign(Bit.boolValues.FALSE);

        }
    }
    public static void addWord16(Word16 a, Word16 b, Word16 result) {
        /*
        case 1    case 2    case 3     case 4
        1         1         0          0
        1         0         1          0
        */

        Bit carryIn = new Bit(false);
        Bit[] sumAndCarry = new Bit[1];
        //Iterating through but starting from right
        for (int i = 15; i >= 0; i--) {

            sumAndCarry = singularAdder(a.getBit(i), b.getBit(i), carryIn);
            if (sumAndCarry[0].getValue() == Bit.boolValues.TRUE)
                result.setBitN(i, new Bit(true));

            else if (sumAndCarry[0].getValue() == Bit.boolValues.FALSE)
                result.setBitN(i, new Bit(false));

            if (sumAndCarry[1].getValue() == Bit.boolValues.TRUE)
                carryIn.assign(Bit.boolValues.TRUE);

            else if (sumAndCarry[1].getValue() == Bit.boolValues.FALSE)
                carryIn.assign(Bit.boolValues.FALSE);

        }
    }

    public static Bit[] singularAdder(Bit a, Bit b, Bit carryIn){
        // ADDER SLIDES S = X XOR Y XOR Cin

        Bit sum = new Bit(false);
        // case 1: 1 1 0
        a.xor(b, sum);
        // case 1: 0 0 0
        sum.xor(carryIn, sum);

        Bit carryOut = new Bit(false);
        Bit temp = new Bit(false);

        //ADDER SLIDES FOR CARRYOUT
        a.and(b, temp);
        a.xor(b, carryOut);
        carryOut.and(carryIn, carryOut);
        temp.or(carryOut, carryOut);

        Bit[] sumAndCarry = {sum, carryOut};

        return sumAndCarry;
    }

}


