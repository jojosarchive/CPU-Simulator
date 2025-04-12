import java.util.*;

public class Processor {
    private Memory mem;
    public List<String> output = new LinkedList<>();

    private Word32[] registers = new Word32[32];
    private ALU alu = new ALU();
    private Word16 opCode = new Word16();
    private Word16 op1 = new Word16();
    private Word16 op2 = new Word16();
    private Word16 isCallReturn = new Word16();
    private Word16 isImmediate = new Word16();
    private Word32 programCounter = new Word32();
    private Word32 twoInstructions = new Word32();
    private Word16 instructionOne = new Word16();
    private Word16 instructionTwo = new Word16();

    public Processor(Memory m) {
        mem = m;
    }

    public void run() {
        // call fetch(), decode(), execute() and store()
        // if halt is encountered, stop loop
        boolean done = false;
        while (!done) {
            //make stack in memory


            fetch();
            //TODO check this
            twoInstructions.getTopHalf(instructionOne);
            twoInstructions.getBottomHalf(instructionTwo);
            // Decode twice
            /*
            Decode makes use of shifting to isolate opCode, operation 1 and 2,
            also a flag to determine what SIA format is present
             */

            decode(instructionOne);
            execute();
            store();

            decode(instructionTwo);
            execute();
            store();

        }
    }

    private void fetch() {
        mem.address = programCounter;
        mem.read();
        mem.value.copy(twoInstructions);
    }

    private void decode(Word16 instruction) {
        //TODO 20 switch statements
        Word32 counter = new Word32();
        isolateOpCode(instruction).copy(opCode);
        int numericalOpCode = smallerRangeToInt(opCode);

        switch (numericalOpCode) {
            // Halt
            case 0:
                System.exit(0);
                break;
            // Add, And, Multiply, LeftShift, Subtract, Or, RightShift, Compare, Load, Store, Copy
            case 1, 2, 3, 4, 5, 6, 7, 11, 18, 19, 20:
                immediateOr2R(instruction);
                break;

            case 8, 9, 10, 12, 13, 14, 15, 16, 17:
                isolateImmediateValue11bits(instruction);
                break;
        }
    }

    private void immediateOr2R(Word16 instruction) {
        if (instruction.getBit(5).getValue() == Bit.boolValues.FALSE) {
            isolateRegisterOne(instruction).copy(op1);
            isolateRegisterTwo(instruction).copy(op2);
        } else {
            isolateImmediateValue(instruction).copy(op1);
            isolateRegisterTwo(instruction).copy(op2);
            isImmediate.getBitN(15, new Bit(true));
        }
    }

    private void execute() {
       int numericalOpCode = smallerRangeToInt(opCode);

       if (isCallReturn.getBit(15).getValue().equals(Bit.boolValues.TRUE)){
           // opCode: Sysout
           if (numericalOpCode == 8) {
               if(op1.getBit(15).getValue().equals(Bit.boolValues.TRUE)) {
                   printReg();
               } else
                   printMem();
           }
       }
        // Format: Immediate
        else if (isImmediate.getBit(15).getValue().equals(Bit.boolValues.TRUE)) {
            //TODO format bit 1

            // set ALU instruction
            opCode.copy(alu.instruction);
            // set ALU op1
            word16asWord32(op1).copy(alu.op1);
            // set ALU op2
            word16asWord32(op2).copy(alu.op2);
            // run ALU
            alu.doInstruction();
        }
        // Format: 2R
        else {
            // set ALU instruction
            opCode.copy(alu.instruction);
            // set ALU op1
            word16asWord32(op1).copy(alu.op1);
            // set ALU op2
            word16asWord32(op2).copy(alu.op2);
            // run ALU
            alu.doInstruction();
        }

    }

    private void printReg() {
        for (int i = 0; i < 32; i++) {
            var line = "r"+ i + ":" + ""; // TODO: add the register value here...
            output.add(line);
            System.out.println(line);
        }
    }

    private void printMem() {
        for (int i = 0; i < 1000; i++) {
            Word32 addr = new Word32();
            Word32 value = new Word32();
            // Convert i to Word32 here...

            addr.copy(mem.address);
            mem.read();
            mem.value.copy(value);
            var line = i + ":" + value + "(" + TestConverter.toInt(value) + ")";
            output.add(line);
            System.out.println(line);
        }
    }

    private void store() {
    }


    public int smallerRangeToInt(Word16 op){
        int sum = 0;
        int index = 0;
        //5 indices
        for (int i = 15; i >= 11; i--){
            if (op.getBit(i).getValue() == Bit.boolValues.TRUE)
                sum += (int) Math.pow(2, index);
            index++;
        }
        return sum;
    }
    public int immediateValueToInt(Word16 op){
        int sum = 0;
        int index = 0;
        //5 indices
        for (int i = 15; i >= 5; i--){
            if (op.getBit(i).getValue() == Bit.boolValues.TRUE)
                sum += (int) Math.pow(2, index);
            index++;
        }
        return sum;
    }
    public Word16 isolateOpCode(Word16 instruction) {
        Word16 opTemp = new Word16();
        Word16 shifterTemp = new Word16();
        instruction.copy(opTemp);

        Shifter.RightShiftWord16(opTemp, 11, shifterTemp);

        return shifterTemp;
    }
    public Word16 isolateRegisterOne(Word16 instruction) {
        Word16 opTemp = new Word16();
        Word16 shifterTemp = new Word16();
        Word16 shifterTemp2 = new Word16();
        instruction.copy(opTemp);

        Shifter.LeftShiftWord16(instruction, 6, shifterTemp);
        Shifter.RightShiftWord16(shifterTemp, 11, shifterTemp2);

        return shifterTemp2;
    }
    public Word16 isolateRegisterTwo(Word16 instruction) {
        Word16 opTemp = new Word16();
        Word16 shifterTemp = new Word16();
        Word16 shifterTemp2 = new Word16();
        instruction.copy(opTemp);

        Shifter.LeftShiftWord16(instruction, 11, shifterTemp);
        Shifter.RightShiftWord16(shifterTemp, 11, shifterTemp2);

        return shifterTemp2;
    }
    public Word16 isolateImmediateValue11bits(Word16 instruction) {
        Word16 opTemp = new Word16();
        Word16 shifterTemp = new Word16();
        Word16 shifterTemp2 = new Word16();
        instruction.copy(opTemp);

        Shifter.LeftShiftWord16(instruction, 5, shifterTemp);
        Shifter.RightShiftWord16(shifterTemp, 5, shifterTemp2);

        return shifterTemp2;
    }
    public Word16 isolateImmediateValue(Word16 instruction) {
        Word16 opTemp = new Word16();
        Word16 shifterTemp = new Word16();
        Word16 shifterTemp2 = new Word16();
        instruction.copy(opTemp);

        Shifter.LeftShiftWord16(instruction, 6, shifterTemp);
        Shifter.RightShiftWord16(shifterTemp, 11, shifterTemp2);

        return shifterTemp2;
    }


    public Word32 word16asWord32(Word16 word) {
        Word32 temp = new Word32();
        for(int i = 0; i < 16; i ++){
            if(word.getBit(i).getValue() == Bit.boolValues.TRUE)
                temp.getBitN(i, new Bit(true));
            else
                temp.getBitN(i, new Bit(false));
        }
        Word32 shifterTemp = new Word32();
        Shifter.RightShift(temp, 16, shifterTemp);
        shifterTemp.copy(temp);

        return temp;
    }
    public Word16 word32asWord16(Word32 word) {
        Word32 temp = new Word32();
        word.copy(temp);
        Word32 shifterTemp = new Word32();
        Shifter.LeftShift(temp, 16, shifterTemp);
        shifterTemp.copy(temp);
        Word16 temp16 = new Word16();

        for(int i = 0; i < 16; i ++){
            if(temp.getBit(i).getValue() == Bit.boolValues.TRUE)
                temp16.getBitN(i, new Bit(true));
            else
                temp16.getBitN(i, new Bit(false));
        }
        return temp16;
    }
}