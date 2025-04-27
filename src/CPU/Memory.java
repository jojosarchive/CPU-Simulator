package CPU;

import CPU.Bit;

public class Memory {
    public Word32 address= new Word32();
    public Word32 value = new Word32();

    private final Word32[] dram= new Word32[1000];

    public int addressAsInt() {
        int sum = 0;
        int index = 0;

        for (int i = 31; i >= 0; i--){
            if (this.address.getBit(i).getValue() == Bit.boolValues.TRUE)
                sum += (int) Math.pow(2, index);
            index++;
        }

        return sum;
    }

    public Memory() {
        for(int i = 0; i < this.dram.length; i ++){
            this.dram[i] = new Word32();
        }
    }

    public void read() {
        dram[addressAsInt()].copy(value);
    }

    public void write() {
        value.copy(dram[addressAsInt()]);
    }

    public void load(String[] data) {
        insertString(data);
    }

    public void insertString(String[] data) {
        for (int i = 0; i < data.length; i++) {
            String oneString = data[i];
            Word32 word = new Word32();
            for (int j = 0; j < 32; j++) {
                if (oneString.charAt(j) == 't') {
                    word.setBitN(j, new Bit(true));
                } else if (oneString.charAt(j) == 'f')
                    word.setBitN(j, new Bit(false));

            }
            word.copy(this.dram[i]);
        }
    }

}
