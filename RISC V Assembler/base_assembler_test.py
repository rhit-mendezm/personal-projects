"""
    Tests for the CSSE232 Risc-V Assembler
    Written by: R Williamson, 2024
"""
import assembler
import unittest
#This is only used by the autograder
#from gradescope_utils.autograder_utils.decorators import weight

def split_inst(inst):
    #helper to make writing tests cleaner
    inst = inst.replace(",","")
    sinst = inst.split()
    return(sinst[0], sinst[1:])

def Assembler_method_testing_helper(myassert, func, inst, machine):
    #helper to make writing individual assembler test cases easier
    #checks the output matches expectation
    cmd, args = split_inst(inst)
    result = func(cmd, args, 0)
    myassert(machine, result)

def Assembler_method_error_testing_helper(myassert, error, func, inst):
    #helper to make writing individual assembler test cases easier
    #checks that an appropriate exception is raised
    cmd, args = split_inst(inst)
    with myassert(error):
        func(cmd, args, 0)

class TestRType(unittest.TestCase):
    #@weight(1)
    def test_R_types_add(self):
        inst = "add x1, x1, x1"
        machine = "0000 0000 0001 0000 1000 0000 1011 0011"
        Assembler_method_testing_helper(self.assertEqual, assembler.Assemble_R_Type, inst, machine)

        inst = "add t0, s0, sp"
        machine = "0000 0000 0010 0100 0000 0010 1011 0011"
        Assembler_method_testing_helper(self.assertEqual, assembler.Assemble_R_Type, inst, machine)

    #@weight(1)
    def test_R_types_sub(self):
        inst = "sub x5, x16, x31"
        machine = "0100 0001 1111 1000 0000 0010 1011 0011"
        Assembler_method_testing_helper(self.assertEqual, assembler.Assemble_R_Type, inst, machine)

        inst = "sub s0, zero, a0"
        machine = "0100 0000 1010 0000 0000 0100 0011 0011"
        Assembler_method_testing_helper(self.assertEqual, assembler.Assemble_R_Type, inst, machine)

    #@weight(1)
    def test_R_types_xor(self): 
        inst = "xor x3, x0, at"
        machine = "0000 0001 1111 0000 0100 0001 1011 0011"
        Assembler_method_testing_helper(self.assertEqual, assembler.Assemble_R_Type, inst, machine)

    #@weight(1)
    def test_R_types_or(self):
        inst = "or x4, s1, s2"
        machine = "0000 0001 0010 0100 1110 0010 0011 0011"
        Assembler_method_testing_helper(self.assertEqual, assembler.Assemble_R_Type, inst, machine)

    #@weight(1)
    def test_R_types_and(self):
        inst = "and t5, t5, s11"
        machine = "0000 0001 1011 1111 0111 1111 0011 0011"
        Assembler_method_testing_helper(self.assertEqual, assembler.Assemble_R_Type, inst, machine)

    #@weight(1)
    def test_R_types_sll(self):
        inst = "sll x7, a1, a2"
        machine = "0000 0000 1100 0101 1001 0011 1011 0011"
        Assembler_method_testing_helper(self.assertEqual, assembler.Assemble_R_Type, inst, machine)

    #@weight(1)
    def test_R_types_sra(self):
        inst = "sra a7, s9, x31 "
        machine = "0100 0001 1111 1100 1101 1000 1011 0011"
        Assembler_method_testing_helper(self.assertEqual, assembler.Assemble_R_Type, inst, machine)

    #@weight(1)
    def test_R_types_slt(self):
        inst = "slt a4, a5, a6 "
        machine = "0000 0001 0000 0111 1010 0111 0011 0011"
        Assembler_method_testing_helper(self.assertEqual, assembler.Assemble_R_Type, inst, machine)

    #@weight(1)
    def test_R_types_arguments(self):
        #too many arguments
        inst = "add x1, x1, x1, x2"
        Assembler_method_error_testing_helper(self.assertRaises, assembler.BadOperands, assembler.Assemble_R_Type, inst)
        #too few arguments
        inst = "add x1, x1"
        Assembler_method_error_testing_helper(self.assertRaises, assembler.BadOperands, assembler.Assemble_R_Type, inst)
        #wrong argument format
        inst = "add x1, x1(x1)"
        Assembler_method_error_testing_helper(self.assertRaises, assembler.BadOperands, assembler.Assemble_R_Type, inst)

    #@weight(1)
    def test_R_types_registers(self):
        #testing bad register names
        inst = "add z1, x1, x1"
        Assembler_method_error_testing_helper(self.assertRaises, assembler.BadRegister, assembler.Assemble_R_Type, inst)
        inst = "or x1, sid, x1"
        Assembler_method_error_testing_helper(self.assertRaises, assembler.BadRegister, assembler.Assemble_R_Type, inst)
        inst = "sll x1, x1, t22"
        Assembler_method_error_testing_helper(self.assertRaises, assembler.BadRegister, assembler.Assemble_R_Type, inst)

        #testing accidental immediate
        inst = "add x1, x1, 4"
        Assembler_method_error_testing_helper(self.assertRaises, assembler.BadRegister, assembler.Assemble_R_Type, inst)
        inst = "add x1, x1, LABEL"
        Assembler_method_error_testing_helper(self.assertRaises, assembler.BadRegister, assembler.Assemble_R_Type, inst)
    
class TestIType(unittest.TestCase):
    #@weight(1)
    def test_I_types_addi(self):
        inst = "addi t5, s0, 2"
        machine = "0000 0000 0010 0100 0000 1111 0001 0011"
        Assembler_method_testing_helper(self.assertEqual, assembler.Assemble_I_Type, inst, machine)

        inst = "addi t5, s0, -2"
        machine = "1111 1111 1110 0100 0000 1111 0001 0011"
        Assembler_method_testing_helper(self.assertEqual, assembler.Assemble_I_Type, inst, machine)
        
        inst = "addi t5, s0, 256"
        machine = "0001 0000 0000 0100 0000 1111 0001 0011"
        Assembler_method_testing_helper(self.assertEqual, assembler.Assemble_I_Type, inst, machine)

        inst = "addi t5, s0, -256"
        machine = "1111 0000 0000 0100 0000 1111 0001 0011"
        Assembler_method_testing_helper(self.assertEqual, assembler.Assemble_I_Type, inst, machine) 

    #@weight(1)
    def test_I_types_xori(self):
        inst = "xori t5, s0, 2"
        machine = "0000 0000 0010 0100 0100 1111 0001 0011"
        Assembler_method_testing_helper(self.assertEqual, assembler.Assemble_I_Type, inst, machine)

        inst = "xori t5, s0, -2"
        machine = "1111 1111 1110 0100 0100 1111 0001 0011"
        Assembler_method_testing_helper(self.assertEqual, assembler.Assemble_I_Type, inst, machine)

    #@weight(1)
    def test_I_types_ori(self):	 
        inst = "ori t5, s0, 2"
        machine = "0000 0000 0010 0100 0110 1111 0001 0011"
        Assembler_method_testing_helper(self.assertEqual, assembler.Assemble_I_Type, inst, machine)

        inst = "ori t5, s0, -2"
        machine = "1111 1111 1110 0100 0110 1111 0001 0011"
        Assembler_method_testing_helper(self.assertEqual, assembler.Assemble_I_Type, inst, machine)

    #@weight(1)
    def test_I_types_andi(self):	 
        inst = "andi t5, s0, 2"
        machine = "0000 0000 0010 0100 0111 1111 0001 0011"
        Assembler_method_testing_helper(self.assertEqual, assembler.Assemble_I_Type, inst, machine)

        inst = "andi t5, s0, -2"
        machine = "1111 1111 1110 0100 0111 1111 0001 0011"
        Assembler_method_testing_helper(self.assertEqual, assembler.Assemble_I_Type, inst, machine)

    #@weight(1)
    def test_I_types_slli(self):
        inst = "slli t5, s0, 2"
        machine = "0000 0000 0010 0100 0001 1111 0001 0011"
        Assembler_method_testing_helper(self.assertEqual, assembler.Assemble_I_Type_shift, inst, machine)

        inst = "slli t5, s0, -2"
        Assembler_method_error_testing_helper(self.assertRaises, assembler.BadImmediate, assembler.Assemble_I_Type_shift, inst)

    #@weight(1)
    def test_I_types_srli(self): 
        inst = "srli t5, s0, 2"
        machine = "0000 0000 0010 0100 0101 1111 0001 0011"
        Assembler_method_testing_helper(self.assertEqual, assembler.Assemble_I_Type_shift, inst, machine)

        inst = "srli t5, s0, -2"
        Assembler_method_error_testing_helper(self.assertRaises, assembler.BadImmediate, assembler.Assemble_I_Type_shift, inst)

    #@weight(1)
    def test_I_types_srai(self):
        inst = "srai t5, s0, 2"
        machine = "0100 0000 0010 0100 0101 1111 0001 0011"
        Assembler_method_testing_helper(self.assertEqual, assembler.Assemble_I_Type_shift, inst, machine)

        inst = "srai t5, s0, -2"
        Assembler_method_error_testing_helper(self.assertRaises, assembler.BadImmediate, assembler.Assemble_I_Type_shift, inst)

    #@weight(1)
    def test_I_types_arguments(self):
        #testing bad number of args
        inst = "addi t5, s0"
        Assembler_method_error_testing_helper(self.assertRaises, assembler.BadOperands, assembler.Assemble_I_Type, inst)
        inst = "addi t5, 4"
        Assembler_method_error_testing_helper(self.assertRaises, assembler.BadOperands, assembler.Assemble_I_Type, inst)
        inst = "addi t5, s0, 4, t0"
        Assembler_method_error_testing_helper(self.assertRaises, assembler.BadOperands, assembler.Assemble_I_Type, inst)
        inst = "addi t5, s0, 4, 4"
        Assembler_method_error_testing_helper(self.assertRaises, assembler.BadOperands, assembler.Assemble_I_Type, inst)
        inst = "addi t5, s0, t4, t0"
        Assembler_method_error_testing_helper(self.assertRaises, assembler.BadOperands, assembler.Assemble_I_Type, inst)

        #testing bad register names
        inst = "addi z1, x1, 2"
        Assembler_method_error_testing_helper(self.assertRaises, assembler.BadRegister, assembler.Assemble_I_Type, inst)
        inst = "ori x1, sid, 2"
        Assembler_method_error_testing_helper(self.assertRaises, assembler.BadRegister, assembler.Assemble_I_Type, inst)
        inst = "slli x1, a10, 2"
        Assembler_method_error_testing_helper(self.assertRaises, assembler.BadRegister, assembler.Assemble_I_Type_shift, inst)

    #@weight(1)
    def test_I_types_immediates(self):
        #testing bad immediates
        inst = "addi t5, s0, 4097"
        Assembler_method_error_testing_helper(self.assertRaises, assembler.BadImmediate, assembler.Assemble_I_Type, inst)
        inst = "slli t5, s0, 33"
        Assembler_method_error_testing_helper(self.assertRaises, assembler.BadImmediate, assembler.Assemble_I_Type_shift, inst)

    #@weight(1)
    def test_I_types_bad_instructions(self):
        #testing for instructions that shouldnt exist
        inst = "subi t5, s0, 2"
        Assembler_method_error_testing_helper(self.assertRaises, assembler.BadInstruction, assembler.Assemble_I_Type, inst)

class TestBaseOffsetInstructions(unittest.TestCase):
    #@weight(1)
    def test_lw(self): 
        inst = "lw a0, 0(sp)"
        machine = "0000 0000 0000 0001 0010 0101 0000 0011"
        Assembler_method_testing_helper(self.assertEqual, assembler.Assemble_I_Type_base_offset, inst, machine)

        inst = "lw a0, -4(sp)"
        machine = "1111 1111 1100 0001 0010 0101 0000 0011"
        Assembler_method_testing_helper(self.assertEqual, assembler.Assemble_I_Type_base_offset, inst, machine)

    #@weight(1)
    def test_sw(self):
        inst = "sw a0, 0(sp)"
        machine = "0000 0000 1010 0001 0010 0000 0010 0011"
        Assembler_method_testing_helper(self.assertEqual, assembler.Assemble_S_Type, inst, machine)

        inst = "sw a0, -4(sp)"
        machine = "1111 1110 1010 0001 0010 1110 0010 0011"
        Assembler_method_testing_helper(self.assertEqual, assembler.Assemble_S_Type, inst, machine)

    #@weight(1)
    def test_jalr(self):	
        inst = "jalr ra, 4(a0)"
        machine = "0000 0000 0100 0101 0000 0000 1110 0111"
        Assembler_method_testing_helper(self.assertEqual, assembler.Assemble_I_Type_base_offset, inst, machine)

        inst = "jalr ra, 4 (a0)"
        machine = "0000 0000 0100 0101 0000 0000 1110 0111"
        Assembler_method_testing_helper(self.assertEqual, assembler.Assemble_I_Type_base_offset, inst, machine)

        inst = "jalr ra, a0, 4"
        machine = "0000 0000 0100 0101 0000 0000 1110 0111"
        Assembler_method_testing_helper(self.assertEqual, assembler.Assemble_I_Type, inst, machine)

    #@weight(1)
    def test_I_types_bad_instructions(self):
        #testing lw for errors 
        inst = "lw a0, a0(sp)"
        Assembler_method_error_testing_helper(self.assertRaises, assembler.BadImmediate, assembler.Assemble_I_Type_base_offset, inst)
        inst = "lw a0, 4(4)"
        Assembler_method_error_testing_helper(self.assertRaises, assembler.BadRegister, assembler.Assemble_I_Type_base_offset, inst)
        inst = "lw at, 4097(t5)"
        Assembler_method_error_testing_helper(self.assertRaises, assembler.BadImmediate, assembler.Assemble_I_Type_base_offset, inst)

        #testing jalr for errors 
        inst = "jalr a0, a0(sp)"
        Assembler_method_error_testing_helper(self.assertRaises, assembler.BadImmediate, assembler.Assemble_I_Type_base_offset, inst)
        inst = "jalr a0, 4(4)"
        Assembler_method_error_testing_helper(self.assertRaises, assembler.BadRegister, assembler.Assemble_I_Type_base_offset, inst)
        inst = "jalr at, 4097(t5)"
        Assembler_method_error_testing_helper(self.assertRaises, assembler.BadImmediate, assembler.Assemble_I_Type_base_offset, inst)

        #testing sw for errors
        inst = "sw a0, a0(sp)"
        Assembler_method_error_testing_helper(self.assertRaises, assembler.BadImmediate, assembler.Assemble_S_Type, inst)
        inst = "sw a0, 4(4)"
        Assembler_method_error_testing_helper(self.assertRaises, assembler.BadRegister, assembler.Assemble_S_Type, inst)
        inst = "sw at, 4097(t5)"
        Assembler_method_error_testing_helper(self.assertRaises, assembler.BadImmediate, assembler.Assemble_S_Type, inst)

class TestFileAssembly(unittest.TestCase):
    #@weight(1)
    def test_remove_comments(self):
        instructions = """add x1, x1, x1 ; end of line comment
                        ;comment line
                        add t0, s0, sp

                        sub x5, x16, x31"""
        instructions = instructions.split("\n")
        cleaned = """add x1, x1, x1
                        add t0, s0, sp                        
                        sub x5, x16, x31"""
        #clean up the whitespace and make it a list
        cleaned = [x.lstrip().rstrip() for x in cleaned.split("\n")]

        result = assembler.comments_pass(instructions)
        #check number of lines is right
        self.assertEqual(len(cleaned), len(result))
        #check each line is right
        for m,a in zip(cleaned, result):
            self.assertEqual(m.rstrip(), a.rstrip())

    #@weight(2)
    def test1(self):
        #Mix of R types
        instructions = """add x1, x1, x1
                        add t0, s0, sp
                        sub x5, x16, x31 
                        sub s0, zero, a0
                        xor x3, x0, at
                        or x4, s1, s2"""
        instructions = instructions.split("\n")
        machine = """0000 0000 0001 0000 1000 0000 1011 0011
                    0000 0000 0010 0100 0000 0010 1011 0011
                    0100 0001 1111 1000 0000 0010 1011 0011
                    0100 0000 1010 0000 0000 0100 0011 0011 
                    0000 0001 1111 0000 0100 0001 1011 0011
                    0000 0001 0010 0100 1110 0010 0011 0011"""
        #clean up the whitespace and make it a list
        machine = [x.lstrip().rstrip() for x in machine.split("\n")]

        assembled = assembler.assemble_asm(instructions)
        #check number of instructions is right
        self.assertEqual(len(machine), len(assembled))
        #check each instruction is right
        for m,a in zip(machine, assembled):
            self.assertEqual(m, a)

    #@weight(2)
    def test2(self):
        #Mix of I types
        instructions = """addi t5, s0, 279
                        andi t5, s0, 2
                        addi t5, s0, -7
                        xori t5, s0, 200
                        ori t5, s0, -289"""
        instructions = instructions.split("\n")
        machine = """0001 0001 0111 0100 0000 1111 0001 0011 
                    0000 0000 0010 0100 0111 1111 0001 0011
                    1111 1111 1001 0100 0000 1111 0001 0011
                    0000 1100 1000 0100 0100 1111 0001 0011
                    1110 1101 1111 0100 0110 1111 0001 0011"""
        #clean up the whitespace and make it a list
        machine = [x.lstrip().rstrip() for x in machine.split("\n")]

        assembled = assembler.assemble_asm(instructions)
        #check number of instructions is right
        self.assertEqual(len(machine), len(assembled))
        #check each instruction is right
        for m,a in zip(machine, assembled):
            self.assertEqual(m, a)

    #@weight(2)
    def test3(self):
        #Mix of R and I types
        instructions = """xor x3, x0, at
                        addi t5, s0, 27
                        sub x5, x16, x31
                        andi t5, s0, 2
                        addi t5, s0, -7
                        add t0, s0, sp
                        xori t5, s0, 2004"""
        instructions = instructions.split("\n")
        machine = """0000 0001 1111 0000 0100 0001 1011 0011 
                    0000 0001 1011 0100 0000 1111 0001 0011
                    0100 0001 1111 1000 0000 0010 1011 0011 
                    0000 0000 0010 0100 0111 1111 0001 0011 
                    1111 1111 1001 0100 0000 1111 0001 0011 
                    0000 0000 0010 0100 0000 0010 1011 0011 
                    0111 1101 0100 0100 0100 1111 0001 0011"""
        #clean up the whitespace and make it a list
        machine = [x.lstrip().rstrip() for x in machine.split("\n")]

        assembled = assembler.assemble_asm(instructions)
        #check number of instructions is right
        self.assertEqual(len(machine), len(assembled))
        #check each instruction is right
        for m,a in zip(machine, assembled):
            self.assertEqual(m, a)

    #@weight(2)
    def test4(self):
        #Mix of base off-set
        instructions = """lw a0, 0(sp)
                        sw a0, 0(sp)
                        lw a0, -4(sp)
                        jalr ra, 4(a0)
                        sw a0, -4(sp)
                        jalr ra, a0, 4"""
        instructions = instructions.split("\n")
        machine = """0000 0000 0000 0001 0010 0101 0000 0011  
                    0000 0000 1010 0001 0010 0000 0010 0011
                    1111 1111 1100 0001 0010 0101 0000 0011
                    0000 0000 0100 0101 0000 0000 1110 0111
                    1111 1110 1010 0001 0010 1110 0010 0011
                    0000 0000 0100 0101 0000 0000 1110 0111"""
        #clean up the whitespace and make it a list
        machine = [x.lstrip().rstrip() for x in machine.split("\n")]

        assembled = assembler.assemble_asm(instructions)
        #check number of instructions is right
        self.assertEqual(len(machine), len(assembled))
        #check each instruction is right
        for m,a in zip(machine, assembled):
            self.assertEqual(m, a)

    #@weight(2)
    def test5(self):
        #Mix of R, I, and base-offset
        instructions = """xor x3, x0, at
                        addi t5, s0, 27
                        sub x5, x16, x3
                        lw a0, -4(sp)
                        andi t5, s0, 2
                        addi t5, s0, -7
                        add t0, s0, sp
                        xori t5, s0, 2004
                        jalr ra, a0, 4
                        sw a0, 0(sp)"""
        instructions = instructions.split("\n")
        machine = """0000 0001 1111 0000 0100 0001 1011 0011
                    0000 0001 1011 0100 0000 1111 0001 0011 
                    0100 0000 0011 1000 0000 0010 1011 0011 
                    1111 1111 1100 0001 0010 0101 0000 0011 
                    0000 0000 0010 0100 0111 1111 0001 0011
                    1111 1111 1001 0100 0000 1111 0001 0011 
                    0000 0000 0010 0100 0000 0010 1011 0011 
                    0111 1101 0100 0100 0100 1111 0001 0011
                    0000 0000 0100 0101 0000 0000 1110 0111 
                    0000 0000 1010 0001 0010 0000 0010 0011"""
        #clean up the whitespace and make it a list
        machine = [x.lstrip().rstrip() for x in machine.split("\n")]

        assembled = assembler.assemble_asm(instructions)
        #check number of instructions is right
        self.assertEqual(len(machine), len(assembled))
        #check each instruction is right
        for m,a in zip(machine, assembled):
            self.assertEqual(m, a)