package babycino;

public enum TACOpType {
    MOV,    // r1 = r2              - reg-reg move
    IMMED,  // r1 = n               - reg-const move
    LOAD,   // r1 = [r2]            - reg-reg move (indirect)
    STORE,  // [r1] = r2            - reg-reg move (indirect)
    BINOP,  // r1 = f_n r2 r3       - reg-reg binary operation
    PARAM,  // param r1             - reg parameter
    CALL,   // call r1              - reg call
    RET,    // return               - return
    LABEL,  // lab:                 - jump label
    JMP,    // jmp lab              - label jump
    JZ,     // if (r1=0) jmp lab    - label jump, conditional on reg
    MALLOC, // r1 = malloc r2       - allocate memory: r2 units, pointer stored in r1
    READ,   // read r1              - read an integer from input into reg
    WRITE,  // write r1             - write an integer to output from reg
    ADDROF, // r1 = lab             - reg-const move of label value
    NOP,    //                      - do nothing
    ;
}

