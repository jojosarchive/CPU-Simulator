public class Word32 {
    private Bit[] longWord;

    public Word32() {
        this.longWord = new Bit[32];
        for(int i = 0; i < 32; i++){
            this.longWord[i] = new Bit(false);
        }
    }

    public Word32(Bit[] in) {
        this.longWord = new Bit[32];
        for(int i = 0; i < 32; i++){
            if (in[i].getValue() == Bit.boolValues.TRUE){
                this.longWord[i] = new Bit(true);
            }else if (in[i].getValue() == Bit.boolValues.FALSE){
                this.longWord[i] = new Bit(false);
            }
        }
    }


    public void getTopHalf(Word16 result) { // sets result = bits 0-15 of this word. use bit.assign
        for(int i = 0; i < 16; i++){
            result.getBit(i).assign(this.longWord[i].getValue());
        }

    }
    //masking
    public void getBottomHalf(Word16 result) { // sets result = bits 16-31 of this word. use bit.assign
        for(int i = 16; i < 32; i++)
            result.getBit(i - 16).assign(this.longWord[i].getValue());
    }

    public void copy(Word32 result) { // sets result's bit to be the same as this. use bit.assign
        for(int i = 0; i < 32; i++){
            result.longWord[i].assign(this.longWord[i].getValue());
        }
    }

    public boolean equals(Word32 other) {
        return (Word32.equals(this, other));
    }

    public static boolean equals(Word32 a, Word32 b) {
        for(int i = 0; i < 32; i++){
            if(a.longWord[i].getValue() != b.longWord[i].getValue())
                return false;
        }
        return true;
    }

    public void getBitN(int n, Bit result) { // use bit.assign
        result.assign(this.longWord[n].getValue());
    }

    public void setBitN(int n, Bit source) { //  use bit.assign
        this.longWord[n].assign(source.getValue());
    }

    public void and(Word32 other, Word32 result) {
        Word32.and(this, other, result);
    }

    public static void and(Word32 a, Word32 b, Word32 result) {
        for(int i = 0; i < 32; i++) {
            if (a.longWord[i].getValue() == Bit.boolValues.TRUE) {
                if (b.longWord[i].getValue() == Bit.boolValues.TRUE) {
                    result.longWord[i].assign(Bit.boolValues.TRUE);
                } else if (b.longWord[i].getValue() == Bit.boolValues.FALSE) {
                    result.longWord[i].assign(Bit.boolValues.FALSE);
                }
            } else if (a.longWord[i].getValue() == Bit.boolValues.FALSE ) {
                if (b.longWord[i].getValue() == Bit.boolValues.TRUE) {
                    result.longWord[i].assign(Bit.boolValues.FALSE);
                } else if (b.longWord[i].getValue() == Bit.boolValues.FALSE)
                    result.longWord[i].assign(Bit.boolValues.FALSE);
            }
        }
    }

    public void or(Word32 other, Word32 result) {
        Word32.or(this, other, result);
    }

    public static void or(Word32 a, Word32 b, Word32 result) {
        for(int i = 0; i < 32; i++) {
            if (a.longWord[i].getValue() == Bit.boolValues.TRUE) {
                if (b.longWord[i].getValue() == Bit.boolValues.TRUE) {
                    result.longWord[i].assign(Bit.boolValues.TRUE);
                } else if (b.longWord[i].getValue() == Bit.boolValues.FALSE) {
                    result.longWord[i].assign(Bit.boolValues.TRUE);
                }
            } else if (a.longWord[i].getValue() == Bit.boolValues.FALSE) {
                if (b.longWord[i].getValue() == Bit.boolValues.TRUE) {
                    result.longWord[i].assign(Bit.boolValues.TRUE);
                } else if (b.longWord[i].getValue() == Bit.boolValues.FALSE)
                    result.longWord[i].assign(Bit.boolValues.FALSE);
            }
        }
    }

    public void xor(Word32 other, Word32 result) {
        Word32.xor(this, other, result);
    }

    public static void xor(Word32 a, Word32 b, Word32 result) {
        for(int i = 0; i < 32; i++) {
            if (a.longWord[i].getValue() == Bit.boolValues.TRUE) {
                if (b.longWord[i].getValue() == Bit.boolValues.TRUE) {
                    result.longWord[i].assign(Bit.boolValues.FALSE);
                } else if (b.longWord[i].getValue() == Bit.boolValues.FALSE) {
                    result.longWord[i].assign(Bit.boolValues.TRUE);
                }
            } else if (a.longWord[i].getValue() == Bit.boolValues.FALSE ) {
                if (b.longWord[i].getValue() == Bit.boolValues.TRUE) {
                    result.longWord[i].assign(Bit.boolValues.TRUE);
                } else if (b.longWord[i].getValue() == Bit.boolValues.FALSE)
                    result.longWord[i].assign(Bit.boolValues.FALSE);
            }
        }
    }

    public void not( Word32 result) {
        Word32.not(this, result);
    }

    public static void not(Word32 a, Word32 result) {
        for(int i = 0; i < 32; i++){
            if(a.longWord[i].getValue() == Bit.boolValues.FALSE){
                result.longWord[i].assign(Bit.boolValues.TRUE);
            } else if (a.longWord[i].getValue() == Bit.boolValues.TRUE) {
                result.longWord[i].assign(Bit.boolValues.FALSE);
            }
        }
    }

    public Bit getBit(int index){
        return longWord[index];
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (Bit bit : longWord) {
            sb.append(bit.toString());
            sb.append(",");
        }

        return sb.toString();
    }
}
