public class Bit {
    public enum boolValues { FALSE, TRUE }
    private boolValues booleanValue;

    public Bit(boolean value) {

        if(value){
            assign(boolValues.TRUE);
        } else if (!value) {
            assign(boolValues.FALSE);
        }
    }

    public boolValues getValue() {
        return this.booleanValue;
    }

    public void assign(boolValues value) {
        this.booleanValue = value;
    }

    public void and(Bit b2, Bit result) {
        // "Bit" references the static method
        Bit.and(this, b2, result);
    }


    public static void and(Bit b1, Bit b2, Bit result) {
        //1 1 1
        //1 0 0
        if(b1.booleanValue == boolValues.TRUE){
            if(b2.booleanValue == boolValues.TRUE) {
                result.assign(boolValues.TRUE);
            } else if (b2.booleanValue == boolValues.FALSE) {
                result.assign((boolValues.FALSE));
            }
        //0 1 0
        //0 0 0
        } else if (b1.booleanValue == boolValues.FALSE) {
            if(b2.booleanValue == boolValues.TRUE) {
                result.assign(boolValues.FALSE);
            } else if (b2.booleanValue == boolValues.FALSE) {
                result.assign(boolValues.FALSE);
            }
        }
    }


    public void or(Bit b2, Bit result) {
        Bit.or(this, b2, result);
    }

    public static void or(Bit b1, Bit b2, Bit result) {
        //1 1 1
        //1 0 1
        if(b1.booleanValue == boolValues.TRUE  ){
            if(b2.booleanValue == boolValues.TRUE) {
                result.assign(boolValues.TRUE);
            } else if (b2.booleanValue == boolValues.FALSE) {
                result.assign((boolValues.TRUE));
            }
            //0 1 1
            //0 0 0
        } else if (b1.booleanValue == boolValues.FALSE) {
            if(b2.booleanValue == boolValues.TRUE) {
                result.assign(boolValues.TRUE);
            } else if (b2.booleanValue == boolValues.FALSE) {
                result.assign(boolValues.FALSE);
            }
        }
    }

    public void xor(Bit b2, Bit result) {
        Bit.xor(this, b2, result);
    }

    public static void xor(Bit b1, Bit b2, Bit result) {
        //1 1 0
        //1 0 1
        if(b1.booleanValue == boolValues.TRUE  ){
            if(b2.booleanValue == boolValues.TRUE) {
                result.assign(boolValues.FALSE);
            } else if (b2.booleanValue == boolValues.FALSE) {
                result.assign((boolValues.TRUE));
            }
            //0 1 1
            //0 0 0
        } else if (b1.booleanValue == boolValues.FALSE) {
            if(b2.booleanValue == boolValues.TRUE) {
                result.assign(boolValues.TRUE);
            } else if (b2.booleanValue == boolValues.FALSE) {
                result.assign(boolValues.FALSE);
            }
        }
    }

    public static void not(Bit b2, Bit result) {
        if(b2.booleanValue == boolValues.TRUE){
            result.booleanValue = boolValues.FALSE;
        }else if(b2.booleanValue == boolValues.FALSE){
            result.booleanValue = boolValues.TRUE;
        }
    }

    public void not(Bit result) {
        Bit.not(this, result);
    }

    public String toString() {
        if(booleanValue == boolValues.TRUE){
            return "t";
        }else{
            return "f";
        }

    }
}
