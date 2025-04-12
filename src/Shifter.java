public class Shifter {
    public static void LeftShift(Word32 source, int amount, Word32 result) {
        for (int i = 0; i < 32 - amount; i++){
            Bit temp = new Bit(false);
            source.getBitN(i + amount, temp);

            if(temp.getValue() == Bit.boolValues.TRUE){
                result.setBitN(i, new Bit(true));
            } else if (temp.getValue() == Bit.boolValues.FALSE){
                result.setBitN(i, new Bit(false));
            }
        }
    }
    public static void LeftShiftWord16(Word16 source, int amount, Word16 result) {
        for (int i = 0; i < 16 - amount; i++){
            Bit temp = new Bit(false);
            source.getBitN(i + amount, temp);

            if(temp.getValue() == Bit.boolValues.TRUE){
                result.setBitN(i, new Bit(true));
            } else if (temp.getValue() == Bit.boolValues.FALSE){
                result.setBitN(i, new Bit(false));
            }
        }
    }


    public static void RightShift(Word32 source, int amount, Word32 result) {
        for (int i = 31; i > amount; i--){
            Bit temp = new Bit(false);
            source.getBitN(i - amount, temp);

            if (temp.getValue() == Bit.boolValues.TRUE){
                result.setBitN(i, new Bit(true));
            } else if (temp.getValue() == Bit.boolValues.FALSE){
                result.setBitN(i, new Bit(false));
            }


        }
    }
    public static void RightShiftWord16(Word16 source, int amount, Word16 result) {
        for (int i = 15; i > amount; i--){
            Bit temp = new Bit(false);
            source.getBitN(i - amount, temp);

            if (temp.getValue() == Bit.boolValues.TRUE){
                result.setBitN(i, new Bit(true));
            } else if (temp.getValue() == Bit.boolValues.FALSE){
                result.setBitN(i, new Bit(false));
            }


        }
    }
}
