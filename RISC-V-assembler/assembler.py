"""

 Assembler for 32-bit RISC-V
 made for CSSE232
 
 Student: Marlon Mendez-Yanez

 Author: Robert J Williamson, 2024

"""

#To generate docs:
# python3 -m pdoc assembler.py -o=docs  

#select search bar, "Show and Run Commands", search "interpreter" to select the correct python interpreter
import sys, argparse
from enum import Enum
import pseudoinstruction_handler as ph

PC = int(0x00400000)

def main(args):
    assemble_asm(args.asm.readlines(), args)


def assemble_asm(asm_lines, args = None):
    """Takes a list of strings of assembly code. The strings can contain instructions, 
    labels, blank lines, and comments indicated with `;` (on their own line or following instructions).
    Removes comments and blanks and assembles the entire code, returning a list 
    containing binary strings of machine code."""

    #clean up the code removing comments and blanks
    print("Cleaning comments...")
    asm_list = comments_pass(asm_lines)

    #process the pseudoinstruction defninition
    print("Processing pseudoinstructions...")
    pseudos = ph.get_pseudoinstruction_defs()
    core_asm = pseudoinstruction_pass(asm_list, pseudos)

    #extract the labels
    print("Creating labels...")
    clean_code, labels = parse_labels(core_asm)

    #assemble each line
    print("Translating to machine code...")
    machine_code = machine_pass(clean_code, labels)
    
    #output the code
    #set out=None to print to console
    print("Outputting...")
    mode = None
    out = None
    if(args):
        out = args.out
        if(args.verbose):
            mode = None
        else:
            mode = args.mode
    output(machine_code, clean_code, labels, mode = mode, out = out)

    print("Done.")
    return machine_code

##############
#
# Helpers which define the major passes of the assembler 
#
##############
def comments_pass(asm_lines):
    """Takes in a list representing the contents of an asm file.
        Returns a new list with comments and blank lines removed from the list."""
    asm_list = []
    for line in asm_lines:
        line = remove_comments(line.lstrip())
        if(line == None):
            #this was a comment or blank line
            continue
        asm_list.append(line)
    return asm_list

def pseudoinstruction_pass(assembly_lines, pseudos_dictionary):
    """Takes in a list of assembly instructions (with no comments, labels are okay) 
        and returns a new list where any pseudoinstructions are replaced with their 
        equivalent core instructions."""

    modified_instructions = []
    index = 0

    for line in assembly_lines:
        instruction = remove_comments(line)
        if instruction == None:
            instruction = line
        
        split_instruction = instruction.strip().replace(",", " ").split()

        if has_label(instruction):
            label, instruction = split_out_label(line)
            label += ":"
            split_instruction = instruction.strip().replace(",", " ").split()
            command = split_instruction[0]
            if command in pseudos_dictionary:
                psuedo_command = pseudos_dictionary[command]
                expanded_instructions = psuedo_command(instruction, index)
                modified_instructions.append(label)
                
                for expanded_instruction in expanded_instructions:
                    modified_instructions.append(expanded_instruction)
                    index += 1
            
            else:
                modified_instructions.append(label)
                modified_instructions.append(instruction)
                index += 1
            
        elif split_instruction[0] in pseudos_dictionary:
            command = split_instruction[0]
            psuedo_command = pseudos_dictionary[command]
            expanded_instructions = psuedo_command(line, index)

            for expanded_instruction in expanded_instructions:
                modified_instructions.append(expanded_instruction)
                index += 1

        else:
            modified_instructions.append(line)
            index += 1
            

    return modified_instructions

def machine_pass(asm_lines, labels_dictionary):
    """Taken in a list of assembly lines with no comments or pseudoinstructions. 
        Returns a list containing the binary machine translation of each line."""
    machine_code = []
    for i, line in enumerate(asm_lines):
        result = Assemble(line, i, labels_dictionary)
        machine_code.append(result)

    return machine_code

##############
#
# Actual Assembler Methods
#
##############

def Assemble(instruction, line_number=0, labels=None):
    """Takes an instruction as a string, splits it into parts, and then calls the correct helper
        to assemble it, returning the result.
        The optional parameter `labels` should be a Dictionary mapping Label strings 
        to addresses.
        
        This function and the helpers should raise exceptions when invalid instructions are
        encountered. See the exceptions types defined below in this file, they are all named 
        `BadX` where X is a particular kind of error (e.g. `BadImmediate`). 

        Some test cases rely on these errors being raised at appropriate times. 

        This function (and each of the helpers) should return a binary string with bits in groups of 4
        separated by a space character:

        `0000 1111 0000 1111 0000 1111 0000 1111`

        The spacing is intended to make debugging easier.
        """
    
    if has_label(instruction):
        instruction = instruction[1:]

    split_instructions = instruction.strip().replace(",", " ").split()
    command = split_instructions[0]
    arguments = split_instructions[1:]

    if command in instructions_to_types:
        instruction_type = instructions_to_types[command]
    else:
        raise BadInstruction("Invalid instruction found on line %s:\n\t%s %s\n" % (line_number, command, arguments))

    if instruction_type == Types.R:
        result = Assemble_R_Type(command, arguments, line_number)

    elif instruction_type == Types.I:
        result = Assemble_I_Type(command, arguments, line_number)

    elif instruction_type == Types.S:
        result = Assemble_S_Type(command, arguments, line_number)

    elif instruction_type == Types.SB:
        result = Assemble_SB_Type(command, arguments, line_number, labels)

    elif instruction_type == Types.U:
        result = Assemble_U_Type(command, arguments, line_number)

    elif instruction_type == Types.UJ:
        result = Assemble_UJ_Type(command, arguments, line_number, labels)

    else:
        raise BadInstruction("Invalid instruction found on line %s:\n\t%s %s\n" % (line_number, command, arguments))
    
    return result

def Assemble_R_Type(command, operands, line_number):
    """Takes an R Type instruction name and its operands (as a list) and 
        returns the appropriate binary string. A basic call would look like:
        
        `Assemble_R_Type("add", ["t0", "t1", "x2"], 0)`

        Raises BadInstruction exception when the command is not a valid R-type.

        Raises BadOperands exception when the wrong number of operands is
        provided or if base-offset notation is used.

        Raises BadRegister exception if one of the operands provided is not a
        valid register name (e.g., it is an immediate or label)
    """

    if len(operands) != 3:
        raise BadOperands("Incorrect number of operands found in R Type on line %s with args:\n\t%s %s\n" % (line_number, command, operands))

    if instructions_to_types[command] != Types.R:
        raise BadInstruction("Invalid R Type instruction found on line %s:\n\t%s %s\n" % (line_number, command, operands))

    for register in operands:
        if not is_register_name(register):
            raise BadRegister("Found invalid register name in R Type on line %s with args:\n\t%s %s\n" % (line_number, command, operands))

    field_data = instructions_to_fields[command]
    rd  = get_binary_string_of_register(operands[0])
    rs1 = get_binary_string_of_register(operands[1])
    rs2 = get_binary_string_of_register(operands[2])

    instruction_field_list = [field_data.func7,
                            rs2,
                            rs1,
                            field_data.func3,
                            rd, 
                            field_data.opcode]

    return join_binary_string_into_groups_of_four(instruction_field_list)

def Assemble_I_Type(command, operands, line_number):
    """Takes an I Type instruction name and its operands (as a list) and 
        returns the appropriate binary string.

        Raises BadInstruction exception when the cmd is not a valid I-type.

        Raises BadOperands exception when the wrong number of operands is
        provided

        Raises BadRegister exception if one of the operands in a register
        position (rd or rs1) is not a valid register name

        Raises BadImmediate exception when the value provided does not fit in
        the instruction's immediate space.
    """

    if command in ["lw", "jalr"]:
        return Assemble_I_Type_base_offset(command, operands, line_number)

    if len(operands) != 3:
        raise BadOperands("Incorrect number of operands found in I Type on line %s with args:\n\t%s %s\n" % (line_number, command, operands))

    if command not in instructions_to_types:
        raise BadInstruction("Invalid instruction found on line %s:\n\t%s %s\n" % (line_number, command, operands))

    if instructions_to_types[command] != Types.I:
        raise BadInstruction("Invalid I Type instruction found on line %s:\n\t%s %s\n" % (line_number, command, operands))

    registers = operands[:2]
    for register in registers:
        if(not is_register_name(register)):
            raise BadRegister("Found invalid register name in I Type on line %s with args:\n\t%s %s\n" % (line_number, command, registers))
    
    if is_shift_immediate_instance(command):
        return Assemble_I_Type_shift(command, operands, line_number)

    field_data = instructions_to_fields[command]
    rd = get_binary_string_of_register(operands[0])
    rs1 = get_binary_string_of_register(operands[1])
    immediate = decimal_to_binary(operands[2], 12)

    instruction_field_list = [immediate,
                            rs1,
                            field_data.func3,
                            rd,
                            field_data.opcode]

    return join_binary_string_into_groups_of_four(instruction_field_list)

def Assemble_I_Type_shift(command, operands, line_number):
    """Takes an I Type instruction name and its operands and returns 
        the appropriate binary string.

        Raises BadInstruction exception when the cmd is not a valid I-type
        shift.

        Raises BadOperands exception when the wrong number of operands is
        provided

        Raises BadRegister exception if one of the operands in a register
        position (rd or rs1) is not a valid register name

        Raises BadImmediate exception when the value provided is negative or
        greater than 31.
        """

    field_data = instructions_to_fields[command]
    rd = get_binary_string_of_register(operands[0])
    rs1 = get_binary_string_of_register(operands[1])
    immediate_value = int(operands[2])

    if immediate_value < 0 or immediate_value > 31:
        raise BadImmediate("Invalid immediate value for shift instruction on line %s:\n\t%s %s\n" % (line_number, command, operands))
    
    valid_immediate = decimal_to_binary(operands[2], 12)

    if command == "slli" or command == "srli":
        valid_immediate = "0000000" + valid_immediate[7:]

    elif command == "srai":
        valid_immediate = "0100000" + valid_immediate[7:]

    else:
        raise BadInstruction("Invalid I Type shift instruction found on line %s:\n\t%s %s\n" % (line_number, command, operands))
    
    instruction_field_list = [valid_immediate,
                            rs1,
                            field_data.func3,
                            rd,
                            field_data.opcode]

    return join_binary_string_into_groups_of_four(instruction_field_list)
    
def Assemble_I_Type_base_offset(command, operands, line_number):
    """Takes the operands for a lw or jalr instruction and returns the 
        appropriate binary string.

        Note that the following are valid syntax for base-offset instructions (like jalr):
            jalr x0, 4 (ra)  // <- space betwen 4 and (ra)
            jalr x0, ra, 4   // <- standard I-type format
            jalr x0, 4(ra)   // <- no space between offset and base
        
        Raises BadInstruction exception when the cmd is not a valid I-type
        base-offset instruction.

        Raises BadOperands exception when the wrong number of operands is
        provided.

        Raises BadRegister exception if one of the operands in a register
        position is not a valid register name.

        Raises BadImmediate exception when the value provided will not fit in
        the immediate space for the instruction or if there is a register
        specifier in the immediate operand location.
 """

    field_data = instructions_to_fields[command]
    rd = get_binary_string_of_register(operands[0])
    
    if len(operands) == 2:
        immediate, rs1 = parse_base_offset(operands[1])

    elif len(operands) == 3:
        if is_int(operands[1]):
            immediate, rs1 = parse_base_offset(operands[1] + operands[2])
        
        else:
            rs1 = get_binary_string_of_register(operands[1])
            immediate = decimal_to_binary(operands[2])

    else:
        raise BadOperands("Incorrect number of operands found in I Type on line %s with args:\n\t%s %s\n" % (line_number, command, operands))

    instruction_field_list = [immediate,
                            rs1,
                            field_data.func3,
                            rd,
                            field_data.opcode]

    return join_binary_string_into_groups_of_four(instruction_field_list)


def Assemble_S_Type(command, operands, line_number):
    """Takes the operands for an S Type instruction and returns the 
        appropriate binary string.

        Raises BadInstruction exception when the cmd is not a valid S-type
        instruction.

        Raises BadOperands exception when the wrong number of operands is
        provided.

        Raises BadRegister exception if one of the operands in a register
        position is not a valid register name.

        Raises BadImmediate exception when the value provided will not fit in
        the immediate space for the instruction or if there is a register
        specifier in the immediate operand location.
    """

    field_data = instructions_to_fields[command]

    if instructions_to_types[command] != Types.S:
        raise BadInstruction("Invalid S Type instruction found on line %s:\n\t%s %s\n" % (line_number, command, operands))

    if len(operands) != 2:
        raise BadOperands("Incorrect number of operands found in SB Type on line %s with args:\n\t%s %s\n" % (line_number, command, operands))
    
    immediate, rs1 = parse_base_offset(operands[1])
    rs2 = get_binary_string_of_register(operands[0])

    instruction_field_list = [immediate[0:6],
                            rs2,
                            rs1,
                            field_data.func3,
                            immediate[7:],
                            field_data.opcode]

    output = join_binary_string_into_groups_of_four(instruction_field_list)

    if immediate[0] == "1":
        output = "1" + output[1:]

    else:
        output = "0" + output[1:]

    return output


def Assemble_SB_Type(command, operands, line_number, labels=None):
    """Takes an SB Type instruction name and its operands (as a list) 
        and returns the appropriate binary string. 
        
        This method assumes that if a number is passed in as the 
        third operand (`operands[2]`) it is the PC offset, not the immediate. 
        Therefore the offset will be right-shifted before the immediate is generated.
        
        If a non-integer is passed in instead then this method
        expects that to be a label who's address is specified in the `labels`
        dictionary. 
        
        In both cases the `line_number` is used to calculate the immediate from
        the offset. You should assume that a `line_number` equal to 0 indicates
        an instruction at the beginning of the text segment of memory. 

        Raises BadInstruction exception when the cmd is not a valid SB-type.

        Raises BadOperands exception when the wrong number of operands is
        provided.

        Raises BadRegister exception if one of the operands in a register
        position is not a valid register name.

        Raises BadImmediate exception when the value provided will not fit in
        the immediate space for the instruction or if there is a register
        specifier in the immediate operand location.
        """

    field_data = instructions_to_fields[command]

    if instructions_to_types[command] != Types.SB:
        raise BadInstruction("Invalid SB Type instruction found on line %s:\n\t%s %s\n" % (line_number, command, operands))
    
    if len(operands) != 3:
        raise BadOperands("Incorrect number of operands found in SB Type on line %s with args:\n\t%s %s\n" % (line_number, command, operands))
    
    registers = operands[:2]
    for register in registers:
        if not is_register_name(register):
            raise BadRegister("Found invalid register name in SB Type on line %s with args:\n\t%s %s\n" % (line_number, command, registers))
    
    offset_intermediate = operands[2]
    if is_register_name(offset_intermediate):
        raise BadImmediate("Found register name in SB Type on line %s with args:\n\t%s %s\n" % (line_number, command, operands))

    if is_int(offset_intermediate):
        offset = int(offset_intermediate)

    else:
        offset = label_to_offset(labels, offset_intermediate, line_number)

    immediate = decimal_to_binary(offset >> 1, 12)

    rs1 = get_binary_string_of_register(operands[0])
    rs2 = get_binary_string_of_register(operands[1])

    instruction_field_list = [immediate[0],
                            immediate[2:8],
                            rs2,
                            rs1,
                            field_data.func3,
                            immediate[8:],
                            immediate[1],
                            field_data.opcode]
    
    return join_binary_string_into_groups_of_four(instruction_field_list)

def Assemble_U_Type(command, operands, line_number):
    """Takes an U Type instruction name and its operands 
        (as a list) and returns the appropriate binary string.

        Raises BadInstruction exception when the cmd is not a valid U-type.

        Raises BadOperands exception when the wrong number of operands is
        provided.

        Raises BadRegister exception if the operand in a register
        position is not a valid register name.

        Raises BadImmediate exception when the value provided will not fit in
        the immediate space for the instruction or if there is a register
        specifier in the immediate operand location.
    """

    field_data = instructions_to_fields[command]

    if instructions_to_types[command] != Types.U:
        raise BadInstruction("Invalid U Type instruction found on line %s:\n\t%s %s\n" % (line_number, command, operands))
    
    if len(operands) != 2:
        raise BadOperands("Incorrect number of operands found in U Type on line %s with args:\n\t%s %s\n" % (line_number, command, operands))
    
    if not is_register_name(operands[0]):
        raise BadRegister("Found invalid register name in U Type on line %s with args:\n\t%s %s\n" % (line_number, command, operands))
    
    rd = get_binary_string_of_register(operands[0])
    immediate = decimal_to_binary(operands[1], 20)

    instruction_field_list = [immediate,
                            rd,
                            field_data.opcode]
    
    return join_binary_string_into_groups_of_four(instruction_field_list)

def Assemble_UJ_Type(command, operands, line_number, labels):
    """Takes an UJ Type instruction name and its operands 
        (as a list) and returns the appropriate binary string.

        This method assumes that if a number is passed in as 
        the third operand (`operands[2]`) it is the PC offset, 
        not the immediate. Therefore the offset will be 
        right-shifted before the immediate is generated. 
        Otherwise it is assumed to be a label defined in `labels`.

        Raises BadInstruction exception when the cmd is not a valid UJ-type.

        Raises BadOperands exception when the wrong number of operands is
        provided.

        Raises BadRegister exception if one of the operands in a register
        position is not a valid register name.

        Raises BadImmediate exception when the value provided will not fit in
        the immediate space for the instruction or if there is a register
        specifier in the immediate operand location.

        Raises BadLabel exception if the immediate operand provided is a label
        but it is not defined in the `labels` dictionary.
    """
        
    field_data = instructions_to_fields[command]

    if instructions_to_types[command] != Types.UJ:
        raise BadInstruction("Invalid UJ Type instruction found on line %s:\n\t%s %s\n" % (line_number, command, operands))
    
    if len(operands) != 2:
        raise BadOperands("Incorrect number of operands found in UJ Type on line %s with args:\n\t%s %s\n" % (line_number, command, operands))
    
    rd = operands[0]
    rd_in_binary = get_binary_string_of_register(rd)
    if not is_register_name(rd):
        raise BadRegister("Found invalid register name in UJ Type on line %s with args:\n\t%s %s\n" % (line_number, command, operands))
    
    offset_intermediate = operands[1]
    if is_register_name(offset_intermediate):
        raise BadImmediate("Found register name in UJ Type on line %s with args:\n\t%s %s\n" % (line_number, command, operands))
    
    if is_int(offset_intermediate):
        offset = int(offset_intermediate)
    
    else:
        offset = label_to_offset(labels, offset_intermediate, line_number)
    
    immediate = decimal_to_binary(offset >> 1, 20)
    
    instruction_field_list = [immediate[0],
                              immediate[10:20],
                              immediate[9],
                              immediate[1:9],
                              rd_in_binary,
                              field_data.opcode]
    
    return join_binary_string_into_groups_of_four(instruction_field_list)

##############
#
# Comments, Labels, and other sugar
#
##############

def remove_comments(line):
    """Takes a line of assembly and removes any text after a comment character (`;`).\
        Returns `None` if line is entirely a comment."""
    #removes comment lines or blank lines
    line = line.lstrip()
    if(line.startswith(";") or not line.rstrip()):
        return None
    sline = line.split(";")
    return sline[0]

def parse_labels(assembly_list):
    """Takes in a list where each entry is either a label, an instruction, 
        or a label and an instruction. Assumes there are no comments in this code.
        Returns a tuple containing a new list of only instructions (the labels 
        having been removed), and a dictionary mapping labels to addresses
        in the instruction list."""
    
    index = 0
    instructions = []
    labels = {}

    for entry in assembly_list:
        (label, instruction) = split_out_label(entry)
        
        if instruction:
            instructions.append(instruction)

        if label:
            if label in labels:
                raise BadLabel("Duplicate label found: %s at line address"  %label, labels[label])
            
            labels[label] = index_to_address(index)

            if not instruction:
                index -= 1
            
        index += 1

    return (instructions, labels)

#note: technically we dont need this for RISC-V since all the addresses are PC-relative
def index_to_address(index):
    """Given a line number or index in a program returns the RISC-V address
      of the instruction, assuming the program starts at the beginning 
      of the text segment of memory."""

    return PC + (index * 4)

def label_to_offset(labels, label, instruction_index):
    """Takes in the dictionary of labels, a label of interest and a 
        current instruction-index (not an address). Returns the byte offset between 
        the label and PC calculated from the instruction index."""

    if len(labels) == 0:
        label_address = PC

    elif label not in labels:
        raise BadLabel("Label not found: %s" % label)

    else:    
        label_address = labels[label]
    
    current_address = index_to_address(instruction_index)
    
    offset = label_address - current_address
        
    return offset

def split_out_label(line):
    """Takes a line of raw assembly code and splits any label from the beginning
    of the line.  Returns a tuple of label and instruction, either of which
    could be None (if they don't exist).

    Will return (str, None) if there's a label alone on the line.
    Will return (None, str) if there's no label on the line.
    Will return (str, str) if there's both a label and code on the line.
    Or it might return (None, None) if there's no label or instruction.
    """

    clean = line.strip()
    
    # case 1: line is exclusively a label (and whitespace)
    # case 2: line has a label and maybe an instruction
    if ":" in clean:
        # this is a sneaky one-liner to do the same thing
        #[label, inst] = [x.strip() if len(x.strip()) > 0 else None for x in line.split(":",1)]
        [label, inst] = line.split(":", 1)
        label = label.strip()
        inst = inst.strip() if len(inst.strip()) > 0 else None
        if(len(label.split()) > 1): raise BadLabel("Whitespace chars disallowed in labels")
        return (label, inst)
    
    # case 3: no label, return only the code
    return (None, clean)


def has_label(line):
    """Takes a line of raw assembly code, and returns True if the line either
    *is* or contains a label."""
    (label, inst) = split_out_label(line)
    return label is not None


##############
#
# Output 
#
##############

def output(machine_code, clean_code, labels, mode = None, out = None):
    """Takes in two lists, the first a list of binary machine translations,
    the second a list containing the raw assembly associated with each 
    instruction (no comments or blank lines).

    These two lists must be the same size.

    Also takes in the labels dictionary to add labels to the output.

    If `out` is None then the output is printed to the console, otherwise
    the output is written to the file specified by the `out` parameter.

    If mode is `None` then outputs binary with hex and raw assembly in comments.

    If mode is `bin` then outputs binary with raw assembly in comments (no hex).

    If mode is `hex` outputs hex with raw assembly in comments (no binary).

    Addresses of each instruction are always printed in the comments.
    """
    i = int("00400000", 16)
    address_to_label = {v:k for (k,v) in labels.items()}
    for m, c in zip(machine_code, clean_code):
        label = "\t"
        if(i in address_to_label):
            label = address_to_label[i] + ":\t"
        if(not mode):
            s = ("%s // 0x%s ;;; %s - %s%s " % (m, bin_to_hex(m), hex(i), label, c.rstrip()))
        elif (mode == "bin"):
            s = ("%s // %s - %s%s " % (m, hex(i), label, c.rstrip()))
        else:
            s = ("%s // %s - %s%s " % (bin_to_hex(m), hex(i), label, c.rstrip()))

        if(out):
            out.write(s+"\n")
        else:
            print(s)
        i += 4

##############
#
# Utilities 
#
##############

#Enum of Types
Types = Enum("Types", ["R", "I", "S", "SB", "U", "UJ", "PSEUDO"])
"""Enum of instruction Types"""

#dictionary mapping instruction name to types
instructions_to_types = {#R types
                "add":Types.R, "sub":Types.R, "xor":Types.R, "or":Types.R, "and":Types.R, "sll":Types.R,
                "srl":Types.R, "sra":Types.R, "slt":Types.R, 
                #I Types and S Types
                "addi":Types.I, "xori":Types.I, "ori":Types.I, "andi":Types.I, "slli":Types.I, "srli":Types.I,
                "srai":Types.I, "lw":Types.I, "sw":Types.S, "jalr":Types.I,
                #SB Types
                "beq":Types.SB, "bne":Types.SB, "blt":Types.SB, "bge":Types.SB, 
                #U and UJ Types
                "jal":Types.UJ, "lui":Types.U
                }
"""Dictionary mapping instruction name to types"""

class FieldData():
    """
    Struct to hold data for different fields of instructions.
    """
    def __init__(self, opcode, func3=None, func7=None):
        self.opcode = opcode
        self.func7 = func7
        self.func3 = func3

#dictionay mapping instruction name to the different fields as a FieldData object
instructions_to_fields = {#R types
                "add":FieldData("0110011", "000", "0000000"), 
                "sub":FieldData("0110011", "000", "0100000"), 
                "xor":FieldData("0110011", "100", "0000000"), 
                "or": FieldData("0110011", "110", "0000000"), 
                "and":FieldData("0110011", "111", "0000000"), 
                "sll":FieldData("0110011", "001", "0000000"),
                "srl":FieldData("0110011", "101", "0000000"), 
                "sra":FieldData("0110011", "101", "0100000"), 
                "slt":FieldData("0110011", "010", "0000000"), 
                #I Types and S Types
                "addi":FieldData("0010011", "000"), 
                "xori":FieldData("0010011", "100"), 
                "ori": FieldData("0010011", "110"), 
                "andi":FieldData("0010011", "111"), 
                "slli":FieldData("0010011", "001"), 
                "srli":FieldData("0010011", "101"),
                "srai":FieldData("0010011", "101"), 
                "lw":  FieldData("0000011", "010"), 
                "sw":  FieldData("0100011", "010"), 
                "jalr":FieldData("1100111", "000"),
                #SB Types
                "beq":FieldData("1100011", "000"), 
                "bne":FieldData("1100011", "001"), 
                "blt":FieldData("1100011", "100"), 
                "bge":FieldData("1100011", "101"), 
                #U and UJ Types
                "jal":FieldData("1101111"), 
                "lui":FieldData("0110111")
                }
"""Dictionay mapping instruction name to the different fields as a FieldData object"""

#dictionary that maps register names to their ID numbers (in decimal)
register_name_to_num = {"x0":0, "zero":0, "x1":1, "ra":1,
                        "x2":2, "sp":2, "x3":3, "gp":3, 
                        "x4":4, "tp":4, "x5":5, "t0":5,
                        "x6":6, "t1":6, "x7":7, "t2":7,
                        "x8":8, "s0":8, "fp":8, 
                        "x9":9, "s1":9, "x10":10, "a0":10, 
                        "x11":11, "a1":11, "x12":12, "a2":12,
                        "x13":13, "a3":13, "x14":14, "a4":14,
                        "x15":15, "a5":15, "x16":16, "a6":16,
                        "x17":17, "a7":17, "x18":18, "s2":18,
                        "x19":19, "s3":19, "x20":20, "s4":20,
                        "x21":21, "s5":21, "x22":22, "s6":22,
                        "x23":23, "s7":23, "x24":24, "s8":24,
                        "x25":25, "s9":25, "x26":26, "s10":26,
                        "x27":26, "s11":27, "x28":28, "t3":28,
                        "x29":29, "t4":29, "x30":30, "t5":30,
                        "x31":31, "at":31
                        }
"""Dictionary that maps register names to their ID numbers (in decimal)"""

def is_register_name(name):
    """Returns True if the provided name is a valid register name or x value."""
    return name in register_name_to_num.keys()

def get_binary_string_of_register(name):
    """Returns the binary string version of a register ID given its name."""
    if(name not in register_name_to_num.keys()):
        raise BadRegister("Found unknown register name: \n\t%s\n" % name)
        
    #the [2:] here strips of the leading '0b' in the binary string
    binary_string = format(register_name_to_num[name], "#05b")[2:]
    return "0"*(5-len(binary_string)) + binary_string
    
def is_shift_immediate_instance(inst):
    """Returns true if this is a shift immediate instruction."""
    return inst in ["slli", "srli", "srai"]

def is_core_instruction(inst):
    """Returns true if this instruction is in our list of core instructions."""
    return inst in instructions_to_types.keys()

def parse_base_offset(operand_string):
    """Takes in the base-offset address field from memory instructions
        returns a tuple including the binary immediate and binary register.

        Assumes the immediate is in decimal.

        e.g. `lw t0, 4(t1)` will lead to this behavior:

            `parse_base_offset("4(t1)") -> ("000000000100", "00110")` """
    #remove the close paren
    operand_string = operand_string.replace(")", "")
    #split on the open to separate the parts
    pieces = operand_string.split("(")
    if(len(pieces) != 2):
        raise BadImmediate("Parsing base-offset address, inappropriate number of elements: \n\t%s\n" % operand_string)

    imm = decimal_to_binary(pieces[0])
    rs1 = get_binary_string_of_register(pieces[1])
    return (imm, rs1)

def reverse_string(s):
    """A helper function to reverse strings using list slicing. 
        Just syntactic sugar to help with readability."""
    #this is not super memory efficient, but its short
    return s[::-1] 

def is_int(s):
    """Checks if a given string can be an integer or not."""
    try:
        int(s)
    except ValueError:
        return False
    return True

###### Functions to convert between different bases #####

def decimal_to_binary(decimal, size=12):
    """Takes a decimal number (as int or string) and returns the 
        binary representation with number of bits equal to `size`. 
        Uses the two's compliment representation for negative numbers."""
    
    if(type(decimal) == str):
        try:
            decimal = int(decimal)
        except ValueError:
            raise BadImmediate("Failed to parse value as an integer: %s" % (decimal))
    
    if(decimal >= 2**size):
        raise BadImmediate("Not enough bits (%s) to represent the decimal number: %s" % (size, decimal))

    #this does some clever math to deal with negative and positive numbers
    #finds the biggest number in this bit size
    #then 'and's with the sought number
    # the 'and' on a positive number will return itself
    # the 'and' on a negative number will return the positive interpretation of 
    # the two's compliement number of the negative value
    binary_string = bin(((1 << size) - 1) & decimal)
    #the [2:] removes the leading '0b' of the string
    binary_string = binary_string[2:]
    #add in any missing leading zeros (this should only affect on positive numbers)
    #negative numbers will already be the right size
    return "0"*(size-len(binary_string)) + binary_string

def join_binary_string_into_groups_of_four(inst_list):
    """Takes a list of binary strings and joins them together 
        and grouping into 4 character slices."""
    binary_string = "".join(inst_list)
    binary_string = binary_string.replace(" ", "")
    #add back in missing leading zeros
    binary_string = "0"*(32-len(binary_string)) + binary_string
    #add spaces every 4 bits for readability
    binary_string = " ".join(binary_string[i:i+4] for i in range(0, 32, 4))
    return binary_string

def bin_to_hex(bin_string):
    """Takes a binary string and converts it into a hex string."""
    #the [2:] here string off the leading '0x' of the hex string
    if(bin_string == None):
        return
    #remove any whitespace in the string
    bin_string = bin_string.replace(" ", "")
    result = hex(int(bin_string, 2))[2:]
    #add in any missing leading zeros
    return "0"*(8-len(result)) + result


##############
#
# Custom Exceptions for Debugging and Niceness 
#
##############

class BadImmediate(Exception):
    """Indicates an immediate is not the right size (in bits) for a given instruction, or some other formatting issue."""
    pass

class BadOperands(Exception):
    """Indicates that the number or type of operands passed to an instruction are incorrect."""
    pass

class BadInstruction(Exception):
    """Indicates that an unknown instruction has been found."""
    pass

class BadRegister(Exception):
    """Indicates that an unknown register has been found."""
    pass

class BadField(Exception):
    """Indicates that the number of bits for an instruction field is incorrect."""
    pass

class BadFormat(Exception):
    """Indicates that the number of fields for a given instruction does not match the format."""
    pass

class BadLabel(Exception):
    """Indicates that a problematic label has been found."""
    pass

##############
#
# Arguments etc.
#
##############
def parse_args():
    """Parses the arguments to the assembler and returns them as a 
        argparse.Namespace object.
        See the python docs for usage.
        """
    parser = argparse.ArgumentParser(description="A parser for 32-bit RISC-V assembly files.")
    parser.add_argument("asm", type=argparse.FileType('r'), help="An asm file containing RISC-V code. \
                        Whitespace will be ignored. Text after a ; is treated as comments. \
                        Labels are identified by a trailing :, they can be on their own line or share a \
                        line with an instruction.")
    parser.add_argument("--out", "-o", type=argparse.FileType('w'), help="The name of the output file that\
                         the assembled machine code will be written to.")
    parser.add_argument("--mode", "-m", choices=["bin","hex"], default="bin", help="The output mode for the\
                         machine file: binary or hexadecimal.")
    parser.add_argument("--verbose", "-v", action="store_true", help="If true then the final output will\
                         include comments listing the RISC-V command that was disassembled on each line, \
                        along with both binary and hex translations.")
    parser.add_argument("--pseudos", "-p", type=argparse.FileType('r'), help="An optional file that defines\
                         pseudoinstructions that this assembler should support.\
                        You should ignore and not use this option unless you talk to an instructor about it.\
                        Pseudoinstruction names and arguments should be listed with a trailing = on one line\
                        then the instructions that define this pseudoinstruction should follow using the argument\
                         names defined on the first line in place of register operands.\
                        EOF or a new pseudoinstruction definition will dileneate the new definitions. \
                        This assumes any register names or numbers (e.g. at, x31, or 31) are constant and should not be\
                         replaced, so dont use register names in the definition. An example:\n\
                        \t double r1, r2 =\
                        \t add r1, r2, r2")
    
    return parser.parse_args()


if __name__== "__main__":
    main(parse_args())
