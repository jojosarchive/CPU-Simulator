package CPU;

import CPU.Adder;
import CPU.Bit;

public class ALU {
    public Word16 instruction = new Word16();
    public Word32 op1 = new Word32();
    public Word32 op2 = new Word32();
    public Word32 result = new Word32();
    public Bit less = new Bit(false);
    public Bit equal = new Bit(false);
    public final Word32 oneInBinary = new Word32();
    public Word32 instructionAsWord32 = new Word32();
    public Word32 instructionCounter = new Word32();

    public void doInstruction(){
        oneInBinary.getBit(31).assign(Bit.boolValues.TRUE);
        controlUnit();
        cleanUp();
     }

    public void setInstruction() {
        Bit temp = new Bit(false);
        for (int i = 15; i > -1; i--){
            instruction.getBitN(i, temp);
            instructionAsWord32.setBitN(i, temp);
        }
        Word32 shifterTemp = new Word32();
        Shifter.RightShift(instructionAsWord32, 27, shifterTemp);
        shifterTemp.copy(instructionAsWord32);
    }
    public boolean isOpCode() {
        boolean isEqual = instructionAsWord32.equals(instructionCounter);
        //increment
        Adder.add(instructionCounter, oneInBinary, instructionCounter);
        return isEqual;
    }

    public void cleanUp(){
        Word32 blank = new Word32();
        blank.copy(instructionAsWord32);
        blank.copy(instructionCounter);
    }
    public static int smallerRangeToInt(Word32 op){

        int sum = 0;
        int index = 0;
        //5 indices
        for (int i = 31; i >= 27; i--){
            if (op.getBit(i).getValue() == Bit.boolValues.TRUE)
                sum += (int) Math.pow(2, index);
            index++;
        }
        return sum;
    }
    public boolean checkIfZero(){
        for(int i = 0; i < 32; i++){
            if(result.getBit(i).getValue() == Bit.boolValues.TRUE)
                return false;
        }
        return true;
    }
    public void controlUnit(){
        //fetching an instruction
        setInstruction();

        //interpreting an instruction then executing

        //current instruction == 0 Halt
        if(isOpCode())
            System.out.println("");
        //current instruction == 1 ADD
        else if(isOpCode())
            Adder.add(op1, op2, result);
        //current instruction == 2 AND
        else if(isOpCode())
            Word32.and(op1, op2, result);
        //current instruction == 3 MULTIPLY
        else if(isOpCode())
            Multiplier.multiply(op1, op2, result);
        //current instruction == 4 LEFT SHIFT
        else if(isOpCode())
            Shifter.LeftShift(op1, smallerRangeToInt(op2), result);
        //current instruction == 5 AND
        else if(isOpCode())
            Adder.subtract(op1, op2, result);
        //current instruction == 6 OR
        else if(isOpCode())
            Word32.or(op1, op2, result);
        //current instruction == 7 RIGHT SHIFT
        else if(isOpCode())
            Shifter.RightShift(op1, smallerRangeToInt(op2), result);
            //go to op code 11
            Adder.add(instructionCounter, oneInBinary, instructionCounter);
            Adder.add(instructionCounter, oneInBinary, instructionCounter);
            Adder.add(instructionCounter, oneInBinary, instructionCounter);
            //current instruction == 11 COMPARE
        if(isOpCode()) {
            Adder.subtract(op1, op2, result);
            //check if the result is zero
            if(checkIfZero())
                equal.assign(Bit.boolValues.TRUE);
            else
                equal = new Bit(false);


            //check if the number is negative
            Shifter.RightShift(result, 30, result);
            //if its negative
            if(result.getBit(31).getValue() == Bit.boolValues.TRUE)
                less.assign(Bit.boolValues.TRUE);
            else
                less.assign(Bit.boolValues.FALSE);
        }

    }
}
