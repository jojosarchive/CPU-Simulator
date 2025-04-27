package CPU;

public class Word16 {

    public Bit[] word;

    // default constructor that makes an empty instance of "CPU.Word16"
    public Word16() {
        this.word = new Bit[16];
        for(int i = 0; i < 16; i++) {
            this.word[i] = new Bit(false);

        }
    }
    // overloaded constructor that populates and instance of "CPU.Word16" with an array of bits
    public Word16(Bit[] in) {
        this.word = new Bit[16];
        System.arraycopy(in, 0, this.word, 0, 16);
    }




    public void copy(Word16 result) { // sets the values in "result" to be the same as the values in this instance; use "bit.assign"
        for(int i = 0; i < 16; i++){
                result.word[i].assign(this.word[i].getValue());
        }

    }

    public void setBitN(int n, Bit source) { // sets the nth bit of this word to "source"
        this.word[n].assign(source.getValue());

    }

    public void getBitN(int n, Bit result) { // sets result to be the same value as the nth bit of this word
        result.assign(this.word[n].getValue());
    }

    public boolean equals(Word16 other) { // is other equal to this
        return (Word16.equals(this, other));
    }

    public static boolean equals(Word16 a, Word16 b) {
        for(int i = 0; i < 16; i++) {
            if (a.word[i].getValue() != b.word[i].getValue())
                return false;
        }
        return true;
    }

    public void and(Word16 other, Word16 result) {
        Word16.and(this, other, result);
    }

    public static void and(Word16 a, Word16 b, Word16 result) {
        for (int i = 0; i < 16; i++) {
            if (a.word[i].getValue() == Bit.boolValues.TRUE) {
                if (b.word[i].getValue() == Bit.boolValues.TRUE) {
                    result.word[i].assign(Bit.boolValues.TRUE);
                } else if (b.word[i].getValue() == Bit.boolValues.FALSE) {
                    result.word[i].assign(Bit.boolValues.FALSE);
                }

            } else if (a.word[i].getValue() == Bit.boolValues.FALSE) {
                if (b.word[i].getValue() == Bit.boolValues.TRUE) {
                    result.word[i].assign(Bit.boolValues.FALSE);
                } else if (b.word[i].getValue() == Bit.boolValues.FALSE) {
                    result.word[i].assign(Bit.boolValues.FALSE);
                }
            }
        }
    }

    public void or(Word16 other, Word16 result) {
        Word16.or(this, other, result);
    }

    public static void or(Word16 a, Word16 b, Word16 result) {
        for (int i = 0; i < 16; i++) {
            if (a.word[i].getValue() == Bit.boolValues.TRUE) {
                if (b.word[i].getValue() == Bit.boolValues.TRUE) {
                    result.word[i].assign(Bit.boolValues.TRUE);
                } else if (b.word[i].getValue() == Bit.boolValues.FALSE) {
                    result.word[i].assign(Bit.boolValues.TRUE);
                }

            } else if (a.word[i].getValue() == Bit.boolValues.FALSE) {
                if (b.word[i].getValue() == Bit.boolValues.TRUE) {
                    result.word[i].assign(Bit.boolValues.TRUE);
                } else if (b.word[i].getValue() == Bit.boolValues.FALSE) {
                    result.word[i].assign(Bit.boolValues.FALSE);
                }
            }
        }
    }

    public void xor(Word16 other, Word16 result) {
        Word16.xor(this, other, result);
    }

    public static void xor(Word16 a, Word16 b, Word16 result) {
        for (int i = 0; i < 16; i++) {
            if (a.word[i].getValue() == Bit.boolValues.TRUE) {
                if (b.word[i].getValue() == Bit.boolValues.TRUE) {
                    result.word[i].assign(Bit.boolValues.FALSE);
                } else if (b.word[i].getValue() == Bit.boolValues.FALSE) {
                    result.word[i].assign(Bit.boolValues.TRUE);
                }

            } else if (a.word[i].getValue() == Bit.boolValues.FALSE) {
                if (b.word[i].getValue() == Bit.boolValues.TRUE) {
                    result.word[i].assign(Bit.boolValues.TRUE);
                } else if (b.word[i].getValue() == Bit.boolValues.FALSE) {
                    result.word[i].assign(Bit.boolValues.FALSE);
                }
            }
        }
    }

    public void not( Word16 result) {
        Word16.not(this, result);
    }

    public static void not(Word16 a, Word16 result) {
        for(int i = 0; i < 16; i++) {
            if (a.word[i].getValue() == Bit.boolValues.TRUE){
                result.word[i].assign(Bit.boolValues.FALSE);
            }else if(a.word[i].getValue() == Bit.boolValues.FALSE){
                result.word[i].assign(Bit.boolValues.TRUE);
            }
        }
    }

    //Helper for debugging
    public Bit getBit(int index){
        return word[index];
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (Bit bit : word) {
            sb.append(bit.toString());
            sb.append(",");
        }

        return sb.toString();
    }
}