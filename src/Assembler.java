import java.util.HashMap;
import java.util.Map;

public class Assembler {
    public  Assembler(){

    }
    public static String[] assemble(String[] input) {
        // TODO: finish putting all cases for opCodeMap and registersMap
        HashMap<String, String> opCodeMap = new HashMap<>();
        HashMap<String,String> registersMap = new HashMap<>();

        opCodeMap.put("halt","fffff");
        opCodeMap.put("add","fffft");
        opCodeMap.put("and","ffftf");
        opCodeMap.put("multiply","ffftt");
        opCodeMap.put("leftshift","fftff");
        opCodeMap.put("subtract","fftft");
        opCodeMap.put("or","ffttf");
        opCodeMap.put("rightshift","ffttt");
        opCodeMap.put("syscall","ftfff");
        opCodeMap.put("call","ftfft");
        opCodeMap.put("return","ftftf");
        opCodeMap.put("compare","ftftt");
        opCodeMap.put("ble","fttff");
        opCodeMap.put("blt","fttft");
        opCodeMap.put("bge","ftttf");
        opCodeMap.put("bgt","ftttt");
        opCodeMap.put("beq","tffff");
        opCodeMap.put("bne","tffft");
        opCodeMap.put("load","tfftf");
        opCodeMap.put("store","tfftt");
        opCodeMap.put("copy","tftff");

        registersMap.put("r0","fffff");
        registersMap.put("r1","fffft");
        registersMap.put("r2","ffftf");
        registersMap.put("r3","ffftt");
        registersMap.put("r4","fftff");
        registersMap.put("r5","fftft");
        registersMap.put("r6","ffttf");
        registersMap.put("r7","ffttt");
        registersMap.put("r8","ftfff");
        registersMap.put("r9","ftfft");
        registersMap.put("r10","ftftf");
        registersMap.put("r11","ftftt");
        registersMap.put("r12","fttff");
        registersMap.put("r13","fttft");
        registersMap.put("r14","ftttf");
        registersMap.put("r15","ftttt");
        registersMap.put("r16","tffff");
        registersMap.put("r17","tffft");
        registersMap.put("r18","tfftf");
        registersMap.put("r19","tfftt");
        registersMap.put("r20","tftff");
        registersMap.put("r21","tftft");
        registersMap.put("r22","tfttf");
        registersMap.put("r23","tfttt");
        registersMap.put("r24","ttfff");
        registersMap.put("r25","ttfft");
        registersMap.put("r26","ttftf");
        registersMap.put("r27","ttftt");
        registersMap.put("r28","tttff");
        registersMap.put("r29","tttft");
        registersMap.put("r30","ttttf");
        registersMap.put("r31","ttttt");

        String[] finalOutput = new String[input.length];
        int index = 0;

        for(String assemblyLine : input) {
            // split line using spaces as the delimiter
            String[] assemblyWords = assemblyLine.split("\\s");
            finalOutput[index] = "";
            // the first 5 bits are always an opCode
            if(opCodeMap.containsKey(assemblyWords[0]))
                finalOutput[index] += opCodeMap.get(assemblyWords[0]);
            else
                System.out.println("opCode wrong");
            /*
                Case 1)

                instruction format: return/call
                5 bits opCode, 11 bits signed immediate value (0)
                return

                ftftf fffffffffff
            */
            if (assemblyWords.length == 1) {
                finalOutput[index] += "fffffffffff";
            }
            /*
                Case 2)

                instruction format: return/call
                5 bits opCode, 11 bits signed immediate value

                return 100

             */
            else if (assemblyWords.length == 2)  {
                finalOutput[index] += immediateValue(assemblyWords);
            }
             /*
                Case 3)

                instruction format: 2R
                5 bits opCode, 1 bit format, 5 bits immediate register, 5 bits destination register
                really just checking for 2 registers
                format is false as per 2R format

                add r1 r2
                fffft f fffft ffftf

                Case 4)

                instruction format: Intermediate
                5 bits opCode, 1 bit format, 5 bits signed intermediate value, 5 bits destination register
                format bit = t

                add 10 r2

                fffft t ftftf ffftf
            */
            else {

                // case 3
                if (registersMap.containsKey(assemblyWords[1])) {
                    // format bit
                    finalOutput[index] += 'f';
                    // register
                    finalOutput[index] += registersMap.get(assemblyWords[1]);

                    if (registersMap.containsKey(assemblyWords[2]))
                        // destination register
                        finalOutput[index] += registersMap.get(assemblyWords[2]);
                    else
                        System.out.println("case 3 mess up");

                }
                // case 4
                else {
                    // format bit
                    finalOutput[index] += 't';
                    // intermediateValue
                    finalOutput[index] += immediateValue(assemblyWords);

                    if (registersMap.containsKey(assemblyWords[2]))
                        // destination register
                        finalOutput[index] += registersMap.get(assemblyWords[2]);
                    else
                        System.out.println("case 4 mess up");
                }
            }
            // increment to parse another line of assembly into binary
            index++;
        }
        return finalOutput;
    }
    public static String immediateValue(String[] assemblyWord){
        Word32 binary = new Word32();
        String finalOutput = "";
        int intermediateValueSize = 0;

        TestConverter.fromInt(Integer.parseInt(assemblyWord[1]), binary);

        Word32 shifterTemp = new Word32();
        if (assemblyWord.length == 2) {
            intermediateValueSize = 11;
            Shifter.LeftShift(binary, 21, shifterTemp);
        }
        else {
            intermediateValueSize = 5;
            Shifter.LeftShift(binary, 27, shifterTemp);
        }
            shifterTemp.copy(binary);

        for (int i = 0; i < intermediateValueSize; i++){
            if(binary.getBit(i).getValue() == Bit.boolValues.TRUE)
                finalOutput += 't';
            else
                finalOutput += 'f';
        }
        return finalOutput;
    }

    public static String[] finalOutput(String[] input) {
      int outputSize = (input.length + 1) / 2;
       String[] f = new String[outputSize];

        // keeps track of what instruction we are writing to

        for (int i = 0; i < outputSize; i++) {
            String upperHalf = input[i * 2];
            String lowerHalf = "";

            if (input[(i * 2) + 1] == null) {
                lowerHalf = "ffffffffffffffff";
            } else
                lowerHalf = input[(i * 2) + 1];

            f[i] = upperHalf + lowerHalf;
        }

        return f;
    }
}