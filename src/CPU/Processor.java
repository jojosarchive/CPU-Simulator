package CPU;

import java.util.*;

public class Processor {
    // processor components
    private int currentClockCycle = 0;
    private Memory mem;
    private ALU alu = new ALU();
    private InstructionCache cache;
    private Word32 programCounter = new Word32();
    public  Word32[] registers = new Word32[32];
    private final Stack<Word32> stack = new Stack<>();
    public List<String> output = new LinkedList<>();
    // Decoding a 16 bit instruction opcode +
    public Word16 instruction = new Word16();
    private Word16 opCode = new Word16();
    private Word16 op1 = new Word16();
    private Word16 op2 = new Word16();
    // Flags
    private Bit isCallReturnFormatFlag = new Bit(false);
    private Bit isImmediateFlag = new Bit(false);
    private Bit isALUCertifiedFlag = new Bit(false);
    private Bit isSecondFetchInstruction = new Bit(false);
    private Bit isBranchFlag = new Bit(false);
    private Bit isMemoryFlag = new Bit(false);
    private Bit isEqualSet = new Bit(false);
    // helpers
    private final Word32 oneAsWord32 = new Word32(new Bit(true));
    private final Word32 negativeOne = new Word32();

    public Processor(Memory m) {
        mem = m;
        cache = new InstructionCache(mem);
        startUpRegisters();
        setNegativeOne();
    }

    public void run() {
        // call fetch(), decode(), execute() and store()
        // if halt is encountered, stop loop

        var i = 0;
        while (true) {
            fetch();

            decode();

            if (smallerRangeToInt(opCode) == 0)
                break;

            execute();

            store();

            cleanUpProcessor();
        }
    }
    private void fetch() {
        mem.address = programCounter;
        // cache isn't finished, continue
        // first case, fill cache with addresses 0-3

            if (isSecondFetchInstruction.getValue().equals(Bit.boolValues.FALSE)) {
                mem.read();
                mem.value.getTopHalf(instruction);
            } else {
                mem.read();
                mem.value.getBottomHalf(instruction);
            }

    }
    private void decode() {
        isolateOpCode().copy(opCode);

        switch (smallerRangeToInt(opCode)) {
            // CPU.ALU handles these: Add, And, Multiply, LeftShift, Subtract, Or, RightShift, Compare
            case 1, 2, 3, 4, 5, 6, 7, 11:
                isALUCertifiedFlag.assign(Bit.boolValues.TRUE);
                immediateOr2R();
                break;
            //Syscall, Call, Return
            case 8, 9, 10:
                isCallReturnFormatFlag.assign(Bit.boolValues.TRUE);
                isolateImmediateValue11bits();
                break;
            // BLE, BLT, BGE, BGT, BEQ, BNE
            case 12, 13, 14, 15, 16, 17:
                isCallReturnFormatFlag.assign(Bit.boolValues.TRUE);
                isBranchFlag.assign(Bit.boolValues.TRUE);
                isolateImmediateValue11bits();
                break;
            // Load, Store, Copy
            case 18, 19, 20:
                isMemoryFlag.assign(Bit.boolValues.TRUE);
                immediateOr2R();
                break;
        }
    }
    private void execute() {
        // opcodes 1-7, 11
        if (isALUCertifiedFlag.getValue().equals(Bit.boolValues.TRUE)) {
            // set alu with current operands
            setAlu();
            // execute alu
            alu.doInstruction();
            // compare, set flags, mostly for BNE( opcode 17 )
            if (smallerRangeToInt(opCode) == 11)
                isEqualSet.assign(alu.equal.getValue());
            if (smallerRangeToInt(opCode) == 3)
                currentClockCycle += 10;
            else
                currentClockCycle += 2;
        }

        // opcodes 8-17
        else if (isCallReturnFormatFlag.getValue().equals(Bit.boolValues.TRUE)) {
            // opcodes 8-10 12-17
                handleReturnCallFormat();
        }
        // opcodes 18, 19, 20
        else if (isMemoryFlag.getValue().equals(Bit.boolValues.TRUE)) {
            handleMemory();
        }
    }
    private void store() {
        // let alu.result handle it

        // store alu opcodes
        if (isALUCertifiedFlag.getValue() == Bit.boolValues.TRUE) {
            if(smallerRangeToInt(opCode) != 11)
                alu.result.copy(registers[smallerRangeToInt(op2)]);

            incrementProgramCounter();
        }


        else if (isCallReturnFormatFlag.getValue() == Bit.boolValues.TRUE) {
            // if it's a call don't increment program counter and don't set flag isNotFirstFetch
                // Call

            // BNE
            if(isBranchFlag.getValue() == Bit.boolValues.TRUE) {
                if (isEqualSet.getValue() == Bit.boolValues.FALSE)
                    isSecondFetchInstruction.assign(Bit.boolValues.FALSE);
                else
                    incrementProgramCounter();
            }

            else {
                if (smallerRangeToInt(opCode) == 8) {
                    incrementProgramCounter();
                }
                if (smallerRangeToInt(opCode) == 9) {
                    isSecondFetchInstruction.assign(Bit.boolValues.FALSE);
                }
                // Return reset flag as its going to be first fetch
                else if (smallerRangeToInt(opCode) == 10) {
                    isSecondFetchInstruction.assign(Bit.boolValues.FALSE);
                }
            }
        }


        else if (isMemoryFlag.getValue() == Bit.boolValues.TRUE) {
            // Load
            if (smallerRangeToInt(opCode) == 18) {
                Word32 i = new Word32();

                if (isImmediateFlag.getValue() == Bit.boolValues.TRUE) {
                    Word16 temp = new Word16();
                    Adder.addWord16(op1, word32asWord16(registers[smallerRangeToInt(op2)]), temp);


                    mem.address = word16asWord32(temp);
                    mem.read();
                    mem.value.copy(registers[smallerRangeToInt(op2)]);
                } else {
                    mem.address = registers[smallerRangeToInt(op1)];
                    mem.read();
                    mem.value.copy(registers[smallerRangeToInt(op2)]);
                }

            }
            // Store
            if (smallerRangeToInt(opCode) == 19) {
                mem.address = registers[smallerRangeToInt(op2)];
                if (isImmediateFlag.getValue() == Bit.boolValues.TRUE)
                    word16asWord32(op1).copy(mem.value);
                else
                    registers[smallerRangeToInt(op1)].copy(mem.value);
                mem.write();


            }
            if (smallerRangeToInt(opCode) == 20){
                if (isImmediateFlag.getValue().equals(Bit.boolValues.TRUE))
                    word16asWord32(op1).copy(registers[smallerRangeToInt(op2)]);
                else
                    registers[smallerRangeToInt(op1)].copy(registers[smallerRangeToInt(op2)]);
            }


                incrementProgramCounter();
        }






    }

    public void incrementProgramCounter() {
        if (isSecondFetchInstruction.getValue() == Bit.boolValues.TRUE) {
            Adder.add(programCounter, oneAsWord32, programCounter);
            isSecondFetchInstruction.assign(Bit.boolValues.FALSE);
        } else isSecondFetchInstruction.assign(Bit.boolValues.TRUE);
    }

    // Execute based on flags
    public void handleBranch() {
        //BNE
        if (smallerRangeToInt(opCode) == 17) {
            // EQUAL is NOT set
            if (isEqualSet.getValue() == Bit.boolValues.FALSE) {
                Word32 temp = word16asWord32(op1);
                if (op1.getBit(5).getValue() == Bit.boolValues.TRUE) {
                    for (int i = 0; i < 22; i++) {
                        temp.setBitN(i, new Bit(true));
                    }
                }
                Adder.add(programCounter, temp, programCounter);

            }
        }
    }
    public void handleReturnCallFormat() {
        // BNE
        if (isBranchFlag.getValue() == Bit.boolValues.TRUE) {
            if (isEqualSet.getValue() == Bit.boolValues.FALSE) {
                Word32 temp = word16asWord32(op1);
                if (op1.getBit(5).getValue() == Bit.boolValues.TRUE) {
                    for (int i = 0; i < 22; i++) {
                        temp.setBitN(i, new Bit(true));
                    }
                }
                Adder.add(programCounter, temp, programCounter);

            }
        }
        // opCode: Sysout
        else {
            if (smallerRangeToInt(opCode) == 8) {
                if (op1.getBit(15).getValue().equals(Bit.boolValues.FALSE)) {
                    printReg();
                } else
                    printMem();
            }
            // Call
            else if (smallerRangeToInt(opCode) == 9) {

                Word32 temp = new Word32();
                programCounter.copy(temp);
                Adder.add(temp, oneAsWord32, temp);
                stack.add(temp);
                //set pc to pc + immediate
                Adder.add(programCounter, word16asWord32(op1), programCounter);
                //account for the increment of program counter at endAdder.add(programCounter, negativeOne, programCounter);

            }
            //return
            else if (smallerRangeToInt(opCode) == 10) {
                // pop the address from the stack and set as the new program counter
                stack.pop().copy(programCounter);
            }
        }
    }
    public void handleMemory() {

        // BNE

    }

    public void setAlu() {
        instruction.copy(alu.instruction);

        if (isImmediateFlag.getValue().equals(Bit.boolValues.TRUE)) {
            // set CPU.ALU op1
            word16asWord32(op1).copy(alu.op2);
        } else
            registers[smallerRangeToInt(op1)].copy(alu.op2);

        // set CPU.ALU op2
        registers[smallerRangeToInt(op2)].copy(alu.op1);
    }

    private void printReg() {
        for (int i = 0; i < 32; i++) {
            var line = "r"+ i + ":" + registers[i].toString(); // TODO: add the register value here...
            output.add(line);
            System.out.println(line);
        }
    }
    private void printMem() {
        for (int i = 0; i < 1000; i++) {
            Word32 addr = new Word32();
            Word32 value = new Word32();
            // Convert i to CPU.Word32 here...

            addr.copy(mem.address);
            mem.read();
            mem.value.copy(value);
            var line = i + ":" + value + "(" + TestConverter.toInt(value) + ")";
            output.add(line);
            System.out.println(line);
        }
    }



    public int smallerRangeToInt(Word16 op){
        boolean isNegative = false;
        Word16 temp = new Word16();
        op.copy(temp);

        if (isImmediateFlag.getValue() == Bit.boolValues.TRUE && op.equals(op1)) {
            if (temp.getBit(11).getValue() == Bit.boolValues.TRUE) {
                isNegative = true;
                twosCompliment(temp);
            }
        }

        int sum = 0;
        int index = 0;
        //5 indices
        for (int i = 15; i > 10; i--){
            if (temp.getBit(i).getValue() == Bit.boolValues.TRUE)
                sum += (int) Math.pow(2, index);
            index++;
        }

        if(isNegative)
            sum = sum * -1;

        return sum;
    }

    public void twosCompliment(Word16 word){
        for(int i = 11; i < 16; i++){
            Bit temp = new Bit(false);
            word.getBit(i).not(temp);
            word.setBitN(i, temp);
        }

        Word32 binaryNumber1 = new Word32();
        binaryNumber1.setBitN(31, new Bit(true));

        Word32 temp = new Word32();
        word16asWord32(word).copy(temp);

        Adder.add(temp, binaryNumber1, temp);

        word32asWord16(temp).copy(word);


    }
    public int immediateValueToInt(Word16 op){
        int sum = 0;
        int index = 0;
        //5 indices
        for (int i = 15; i > 10; i--){
            if (op.getBit(i).getValue() == Bit.boolValues.TRUE)
                sum += (int) Math.pow(2, index);
            index++;
        }
        return sum;
    }


    // Helpers for decode
    // Isolates op1 and op2 in either immediate or 2R
    private void immediateOr2R() {
        if (instruction.getBit(5).getValue() == Bit.boolValues.FALSE) {
            isolateRegisterOne().copy(op1);
            isolateRegisterTwo().copy(op2);
        } else {
            isolateImmediateValue().copy(op1);
            isolateRegisterTwo().copy(op2);
            isImmediateFlag.assign(Bit.boolValues.TRUE);
        }
    }
    public Word16 isolateOpCode() {
        Word16 opTemp = new Word16();
        Word16 shifterTemp = new Word16();
        instruction.copy(opTemp);
        Shifter.RightShiftWord16(opTemp, 11, shifterTemp);

        return shifterTemp;
    }
    public Word16 isolateRegisterOne() {
        Word16 opTemp = new Word16();
        Word16 shifterTemp = new Word16();
        Word16 shifterTemp2 = new Word16();
        instruction.copy(opTemp);
        Shifter.LeftShiftWord16(instruction, 6, shifterTemp);
        Shifter.RightShiftWord16(shifterTemp, 11, shifterTemp2);

        return shifterTemp2;
    }

    public Word16 isolateRegisterTwo() {
        Word16 opTemp = new Word16();
        Word16 shifterTemp = new Word16();
        Word16 shifterTemp2 = new Word16();
        instruction.copy(opTemp);
        Shifter.LeftShiftWord16(instruction, 11, shifterTemp);
        Shifter.RightShiftWord16(shifterTemp, 11, shifterTemp2);

        return shifterTemp2;
    }
    public void isolateImmediateValue11bits() {
        Word16 opTemp = new Word16();
        Word16 shifterTemp = new Word16();
        Word16 shifterTemp2 = new Word16();
        instruction.copy(opTemp);

        Shifter.LeftShiftWord16(instruction, 5, shifterTemp);
        Shifter.RightShiftWord16(shifterTemp, 5, shifterTemp2);

        shifterTemp2.copy(op1);
    }
    public Word16 isolateImmediateValue() {
        Word16 opTemp = new Word16();
        Word16 shifterTemp = new Word16();
        Word16 shifterTemp2 = new Word16();

        instruction.copy(opTemp);
        Shifter.LeftShiftWord16(instruction, 6, shifterTemp);
        Shifter.RightShiftWord16(shifterTemp, 11, shifterTemp2);
        // negative
        if (shifterTemp2.getBit(11).getValue() == Bit.boolValues.TRUE)
            signExtend(shifterTemp2);

        return shifterTemp2;
    }
    public void signExtend(Word16 word) {
        for (int i = 0; i < 11; i ++)
            word.setBitN(i, new Bit(true));

    }

    // helpers for initialization

    public void cleanUpProcessor() {
        // CPU.ALU reset
        alu = new ALU();
        // CPU.Processor reset
        opCode = new Word16();
        op1 = new Word16();
        op2 = new Word16();
        // flag reset
        isCallReturnFormatFlag = new Bit(false);
        isImmediateFlag = new Bit(false);
        isALUCertifiedFlag = new Bit(false);

        isBranchFlag = new Bit(false);
        isMemoryFlag = new Bit(false);
        // reset instruction
        instruction = new Word16();

    }
    public void startUpRegisters() {
        for (int i = 0; i < this.registers.length; i ++) {
            this.registers[i] = new Word32();
        }
    }

    // Helpers for Words
    public Word32 word16asWord32(Word16 word) {
        Word32 temp = new Word32();
        for(int i = 0; i < 16; i ++){
            if(word.getBit(i).getValue() == Bit.boolValues.TRUE)
                temp.setBitN(i, new Bit(true));
            else
                temp.setBitN(i, new Bit(false));
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
                temp16.setBitN(i, new Bit(true));
            else
                temp16.setBitN(i, new Bit(false));
        }
        return temp16;
    }
    public void setNegativeOne() {
        for (int i = 0; i < 32; i++) {
            negativeOne.setBitN(i, new Bit(true));
        }
    }
}