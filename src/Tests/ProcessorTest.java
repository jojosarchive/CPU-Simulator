package Tests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import CPU.*;
import static org.junit.jupiter.api.Assertions.*;

public class ProcessorTest {
@Test
    public void testAdd() {
        String[] program = {
                "add 10 r0",
                "syscall 0"
        } ;
    var p = runProgram(program);
    Assertions.assertEquals("r0:f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,t,f,t,f,",p.output.getFirst());
    }
    @Test
    public void testArray() {
        String[] program = {
                "copy 10 r5", // address 0
                "leftshift 1 r5", // R5 holds 20

                "copy 10 r4", // address 1
                "leftshift 5 r4", // R4 holds 320

                "copy 0 r5", // address 2 go to address 4
                "call 2", // waste space

                "halt", // address 3
                "halt",

                "store r5 r4", // address 4
                "add 1 r4",

                "add 1 r5", // address 5
                "copy 0 r9",

                "compare 20 r5", // address 6
                "bne -3",

                "copy 0 r9", // address 7
                "return"


        } ;
        var p = runProgram(program);
        Assertions.assertEquals("r0:f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,t,f,t,f,",p.output.getFirst());
    }

    @Test
    public void testFibonacci() {
        String[] program = {
                "copy 10 r9", // address  -- 0
                "leftshift 1 r9", // r9 = 20
                "copy 15 r0", // limit for fib -- 1
                "store 0 r9",  // first fib
                "add 1 r9", // move to 2nd -- 2
                "store 1 r9",  // second fib
                "add 1 r9", // move to next spot in ram -- 3
                "copy r9 r2", // temporary
                "copy r9 r3", // temporary2 -- 4
                "load -1 r2", // load f(n-1) into r2
                "load -2 r3", // load f(n-2) into r3-- 5
                "add r2 r3", // create next fib
                "store r3 r9", // store fib -- 6
                "subtract 1 r0", // decrement counter
                "compare 0 r0", // compare counter to 0 -- 7
                "bne -4", // loop back to 3
                "syscall 1", // print out memory -- 8
                "halt" // done
        };
        var p = runProgram(program);
        Assertions.assertEquals("35:f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,t,f,f,t,t,f,f,f,t,f,",p.output.get(35));
        Assertions.assertEquals("36:f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,t,t,t,t,f,t,t,f,t,t,",p.output.get(36));
    }
    @Test
    public void testPower() {
        String[] program = {
                "copy 6 r0", // address 0
                "copy 2 r1",
                "copy 0 r2", // address 1
                "call 4", // call 1+4 = 5
                "syscall 0",// address 2
                "copy 7 r0",
                "copy 3 r1",// address 3
                "call 2", // call 3 + 2
                "syscall 0",// address 4
                "halt",
                "copy r0 r2",// address 5
                "copy r0 r2", // waste a spot
                "multiply r0 r2", // address 6
                "subtract 1 r1",
                "compare 1 r1",// address 7
                "bne -1", // -- loop back to address 6
                "return", // address 8
        } ;
        var p = runProgram(program);
        Assertions.assertEquals("r2:f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,t,f,f,t,f,f,",p.output.get(2));
        Assertions.assertEquals("r2:f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,t,f,t,f,t,f,t,t,t,",p.output.get(34));
    }

    private static Processor runProgram(String[] program) {
        var assembled = Assembler.assemble(program);
        var merged = Assembler.finalOutput(assembled);
        var m = new Memory();
        m.load(merged);
        var p = new Processor(m);
        p.run();
        return p;
    }
}
