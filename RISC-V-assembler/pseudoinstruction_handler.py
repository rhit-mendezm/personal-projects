"""

 Pseudoinstruction Handler

 Helper functions for the CSSE232 Assembler. 
 Processes assembly code to replace pseudoinstructions with 
 core instructions.

 Student: Marlon Mendez-Yanez

 Author: Robert J Williamson, 2024

"""

import assembler

def get_pseudoinstruction_defs():
    """Returns a dictionary mapping pseudoinstruction names to methods 
        that will translate a given pseudoinstruction call to a list of 
        core instructions. Each of these function should have the 
        signature: `func(inst_string, line_num)`"""
    pseudo_dict = {"double":double,
                   "diffsums":diffsums,
                   "push":push,
                   "li":li,
                   "beqz":beqz,
                   "jalif":jalif}

    return pseudo_dict

##############
#
# Individual definitions for pseudoinstructions
#
# Each of these functions should take in a string (e.g. "double t0, t1")
# and the line number from the original code that is being translated.
# The line number is mostly for error tracing, but you may find it
# useful for some pseudoinstructions.
##############

def double(instruction, line_number):
    """Takes a string representing a call to the `double` pseudoinstruction. 
        Returns a list of strings containing the calls for the core instructions 
        which implement this pseudoinstruction.

        Behavior:
            `double r1, r2 -> Reg[r1] = 2 * r2`
        """

    split_instructions = instruction.strip().replace(",", " ").split()

    if len(split_instructions) != 3:
        raise assembler.BadOperands("Found invalid number of operands for psuedoinstruction double on line %s with args:\n\t%s\n" % (line_number, instruction))

    registers = split_instructions[1:3]
    for register in registers:
        if not assembler.is_register_name(register):
            raise assembler.BadRegister("Found invalid register name in psuedoinstruction double on line %s with args:\n\tdouble %s\n" % (line_number, registers))
    
    rs1 = split_instructions[1]
    rs2 = split_instructions[2]

    output = "add %s, %s, %s" % (rs1, rs2, rs2)

    return [output]

def diffsums(instruction, line_number):
    """Takes a string representing a call to the `diffsums` pseudoinstruction. 
        Returns a list of strings containing the calls for the core instructions 
        which implement this pseudoinstruction.

        Behavior:
            `diffsums r1, r2, r3, r4, r5 -> Reg[r1] = (r2 + r3) - (r4 + r5)`

        Note: the same register may be used multiple times in this instruction, e.g.:
            `diffsums t0, t0, t1, t0, t2`
        """

    split_instructions = instruction.strip().replace(",", " ").split()

    if len(split_instructions) != 6:
        raise assembler.BadOperands("Found invalid number of operands for psuedoinstruction diffsums on line %s with args:\n\t%s\n" % (line_number, instruction))
    
    registers = split_instructions[1:6]
    for register in registers:
        if not assembler.is_register_name(register):
            raise assembler.BadRegister("Found invalid register name in psuedoinstruction diffsums on line %s with args:\n\tdiffsums %s\n" % (line_number, registers))
        
    rs1 = split_instructions[1]
    rs2 = split_instructions[2]
    rs3 = split_instructions[3]
    rs4 = split_instructions[4]
    rs5 = split_instructions[5]
    
    first_sum = "add %s, %s, %s" % (rs2, rs2, rs3)
    second_sum = "add %s, %s, %s" % (rs4, rs4, rs5)
    difference = "sub %s, %s, %s" % (rs1, rs2, rs4)

    output = [first_sum, second_sum, difference]

    return output

def push(instruction, line_number):
    """Takes a string representing a call to the `push` pseudoinstruction. 
        Returns a list of strings containing the calls for the core instructions 
        which implement this pseudoinstruction.

        Behavior:
            `push r1 -> sp = sp-4 ; Mem[sp] = r1`
        """

    split_instructions = instruction.strip().replace(",", " ").split()

    if len(split_instructions) != 2:
        raise assembler.BadOperands("Found invalid number of operands for psuedoinstruction push on line %s with args:\n\t%s\n" % (line_number, instruction))
    
    rs1 = split_instructions[1]
    if not assembler.is_register_name(rs1):
        raise assembler.BadRegister("Found invalid register name in psuedoinstruction push on line %s with args:\n\tpush %s\n" % (line_number, rs1))

    add = "addi sp, sp, -4"
    store = "sw %s, 0(sp)" % rs1
    
    output = [add, store]
    
    return output

def li(instruction, line_number):
    """Takes a string representing a call to the `li` pseudoinstruction. 
        Returns a list of strings containing the calls for the core instructions 
        which implement this pseudoinstruction.

        Behavior:
            `li rd, imm -> rd = imm`

        Assumes that imm can be up to 32 bits.
        """

    split_instructions = instruction.strip().replace(",", " ").split()

    if len(split_instructions) != 3:
        raise assembler.BadOperands("Found invalid number of operands for psuedoinstruction li on line %s with args:\n\t%s\n" % (line_number, instruction))
    
    rd = split_instructions[1]
    if not assembler.is_register_name(rd):
        raise assembler.BadRegister("Found invalid register name in psuedoinstruction li on line %s with args:\n\tli %s\n" % (line_number, rd))

    immediate = split_instructions[2]
    if assembler.is_register_name(immediate):
        raise assembler.BadImmediate("Found invalid immediate value in psuedoinstruction li on line %s with args:\n\tli %s\n" % (line_number, immediate))

    immediate_decimal = int(immediate)
    if not (-2**32 <= immediate_decimal <= 2**32 - 1):
        raise assembler.BadImmediate("Immediate value out of range for psuedoinstruction li on line %s with args:\n\tli %s\n" % (line_number, immediate))

    immediate_binary = assembler.decimal_to_binary(immediate_decimal, 32)

    load = "lui %s, %s" % (rd, immediate_binary[31:12])
    add = "addi %s, %s, %s" % (rd, rd, immediate_binary[11:0])
    output = [load, add]

    return output

def beqz(instruction, line_number):
    """Takes a string representing a call to the `beqz` pseudoinstruction. 
        Returns a list of strings containing the calls for the core instructions 
        which implement this pseudoinstruction.

        Behavior:
            `beqz r1, LABEL -> if(r1 == 0) PC = LABEL`

        Assumes LABEL should fit into 12 bits.
        """

    split_instructions = instruction.strip().replace(",", " ").split()

    if len(split_instructions) != 3:
        raise assembler.BadOperands("Found invalid number of operands for psuedoinstruction beqz on line %s with args:\n\t%s\n" % (line_number, instruction))
    
    rs1 = split_instructions[1]
    if not assembler.is_register_name(rs1):
        raise assembler.BadRegister("Found invalid register name in psuedoinstruction beqz on line %s with args:\n\tbeqz %s\n" % (line_number, rs1))
    
    label = split_instructions[2]

    output = "beq %s, x0, %s" % (rs1, label)
   
    return [output]

def jalif(instruction, line_number):
    """Takes a string representing a call to the `jalif` pseudoinstruction. 
        Returns a list of strings containing the calls for the core instructions 
        which implement this pseudoinstruction.

        Behavior:
            `jalif r1, r2, LABEL -> if(r1 == r2) {ra = PC+4; PC=LABEL}`

        Assumes LABEL should fit into 20 bits.
        """

    split_instructions = instruction.strip().replace(",", " ").split()

    if len(split_instructions) != 4:
        raise assembler.BadOperands("Found invalid number of operands for psuedoinstruction jalif on line %s with args:\n\t%s\n" % (line_number, instruction))
    
    registers = split_instructions[1:3]
    for register in registers:
        if not assembler.is_register_name(register):
            raise assembler.BadRegister("Found invalid register name in psuedoinstruction jalif on line %s with args:\n\tjalif %s\n" % (line_number, registers))
        
    rs1 = split_instructions[1]
    rs2 = split_instructions[2]
    label = split_instructions[3]

    if assembler.is_int(label):
        label = int(label)
        if not (-2**20 <= label <= 2**20 - 1):
            raise assembler.BadImmediate("Immediate value out of range for psuedoinstruction jalif on line %s with args:\n\tjalif %s\n" % (line_number, label))

    condition1 = "bne %s, %s, 8" % (rs1, rs2)
    jump = "jal ra, %s" % (label)

    output = [condition1, jump]

    return output



##############
#
# Helper methods
#
##############

def replace_all(old, new, list):
    """Replaces all instances of `old` with `new` in each string in the list `list`."""

    new_list = []
    for item in list:
        new_list.append(item.replace(old, str(new)))

    return new_list
