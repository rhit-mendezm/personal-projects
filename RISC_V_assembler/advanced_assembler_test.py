"""
    Tests for the CSSE232 Risc-V Assembler
    Written by: R Williamson, 2024
"""
import assembler, pseudoinstruction_handler as ph
import unittest
#from gradescope_utils.autograder_utils.decorators import weight

def split_inst(inst):
    #helper to make writing tests cleaner
    inst = inst.replace(",","")
    sinst = inst.split()
    return(sinst[0], sinst[1:])

#@unittest.skip("This is a helper, not a real test")
def Assembler_method_testing_helper(myassert, func, inst, machine, index=0, labels={}):
    #helper to make writing individual assembler test cases easier
    #checks the output matches expectation
    cmd, operands = split_inst(inst)
    result = func(cmd, operands, index, labels)
    myassert(machine, result)
     
#@unittest.skip("This is a helper, not a real test")
def Assembler_method_error_testing_helper(myassert, error, func, inst, index=0, labels={}):
    #helper to make writing individual assembler test cases easier
    #checks that an appropriate exception is raised
    cmd, operands = split_inst(inst)
    with myassert(error):
        func(cmd, operands, index, labels)

class TestLabels(unittest.TestCase):
    #@weight(1)
    def test_basic_labels(self):
        instructions = """LABEL1: add x1, x1, x1 
                        add t0, s0, sp
                        LABEL2: sub x5, x16, x31"""
        instructions = instructions.split("\n")
        start_address = int("00400000", 16)
        labels = {"LABEL1":start_address, "LABEL2":start_address+8}

        mod_instructions, result = assembler.parse_labels(instructions)
        #check that the right number of instructions is in the modified code
        self.assertEqual(len(mod_instructions), len(instructions))
        #check that there are the same number of labels in each
        self.assertEqual(len(labels), len(result))
        #check that the same labels are in both
        self.assertEqual(labels.keys(), result.keys())
        #check each label individually
        for k in labels.keys():
            self.assertEqual(labels[k], result[k])
                             
    #@weight(1)
    def test_labels_only_lines(self):
        instructions = """LABEL1: 
                        add x1, x1, x1 
                        add t0, s0, sp
                        LABEL2: sub x5, x16, x31"""
        instructions = instructions.split("\n")
        start_address = int("00400000", 16)
        labels = {"LABEL1":start_address, "LABEL2":start_address+8}

        mod_instructions, result = assembler.parse_labels(instructions)
        #check that the right number of instructions is in the modified code
        self.assertEqual(len(mod_instructions), len(instructions)-1)
        #check that there are the same number of labels in each
        self.assertEqual(len(labels), len(result))
        #check that the same labels are in both
        self.assertEqual(labels.keys(), result.keys())
        #check each label individually
        for k in labels.keys():
            self.assertEqual(labels[k], result[k])

    #@weight(1)
    def test_duplicate_labels(self):
        instructions = """LABEL1: add x1, x1, x1 
                        add t0, s0, sp
                        LABEL1: sub x5, x16, x31"""
        instructions = instructions.split("\n")
        start_address = int("00400000", 16)
        labels = {"LABEL1":start_address, "LABEL2":start_address+8}
        with self.assertRaises(assembler.BadLabel):
            assembler.parse_labels(instructions) 

class TestSBType(unittest.TestCase):
    #@weight(1)
    def test_SB_types_beq(self):
        #this is just for reference
        full_code = """beq zero, t0, 4
                        L2: beq zero, s0, -4
                        beq zero, a0, L1
                        L1:beq a0, x0, L2"""

        inst = "beq zero, t0, 4 "
        machine = "0000 0000 0101 0000 0000 0010 0110 0011"
        Assembler_method_testing_helper(self.assertEqual, assembler.Assemble_SB_Type, inst, machine)

        inst = "beq zero, s0, -4"
        machine = "1111 1110 1000 0000 0000 1110 1110 0011"
        Assembler_method_testing_helper(self.assertEqual, assembler.Assemble_SB_Type, inst, machine)

        #test that labels work
        #set up the labels for the tests below
        labels = {"L2":int("00400004", 16), "L1":int("0040000c", 16), "L3":int("10000000", 16)}

        address = int("00400008",16) #just for reference
        index = 2
        inst = "beq zero, a0, L1"
        machine = "0000 0000 1010 0000 0000 0010 0110 0011"
        Assembler_method_testing_helper(self.assertEqual, assembler.Assemble_SB_Type, inst, machine, index, labels)

        inst = "beq a0, x0, L2"
        machine = "1111 1110 0000 0101 0000 1100 1110 0011"
        address = int("0040000c",16) #just for reference
        index = 3
        Assembler_method_testing_helper(self.assertEqual, assembler.Assemble_SB_Type, inst, machine, index, labels)

    #@weight(1)
    def test_SB_types_bne(self):
        #this is just for reference
        full_code = """bne zero, t0, 4
                        L2: bne zero, s0, -4
                        bne zero, a0, L1
                        L1:bne a0, x0, L2"""

        inst = "bne zero, t0, 4 "
        machine = "0000 0000 0101 0000 0001 0010 0110 0011"
        Assembler_method_testing_helper(self.assertEqual, assembler.Assemble_SB_Type, inst, machine)

        inst = "bne zero, s0, -4"
        machine = "1111 1110 1000 0000 0001 1110 1110 0011"
        Assembler_method_testing_helper(self.assertEqual, assembler.Assemble_SB_Type, inst, machine)

        #set up the labels for the tests below
        labels = {"L2":int("00400004", 16), "L1":int("0040000c", 16)}

        address = int("00400008",16) #just for reference
        index = 2
        inst = "bne zero, a0, L1"
        machine = "0000 0000 1010 0000 0001 0010 0110 0011"
        Assembler_method_testing_helper(self.assertEqual, assembler.Assemble_SB_Type, inst, machine, index, labels)

        inst = "bne a0, x0, L2"
        machine = "1111 1110 0000 0101 0001 1100 1110 0011"
        address = int("0040000c",16) #just for reference
        index = 3
        Assembler_method_testing_helper(self.assertEqual, assembler.Assemble_SB_Type, inst, machine, index, labels)

    #@weight(1)
    def test_SB_types_blt(self):
        #this is just for reference
        full_code = """blt zero, t0, 4
                        L2: blt zero, s0, -4
                        blt zero, a0, L1
                        L1:blt a0, x0, L2"""

        inst = "blt zero, t0, 4 "
        machine = "0000 0000 0101 0000 0100 0010 0110 0011"
        Assembler_method_testing_helper(self.assertEqual, assembler.Assemble_SB_Type, inst, machine)

        inst = "blt zero, s0, -4"
        machine = "1111 1110 1000 0000 0100 1110 1110 0011"
        Assembler_method_testing_helper(self.assertEqual, assembler.Assemble_SB_Type, inst, machine)

        #set up the labels for the tests below
        labels = {"L2":int("00400004", 16), "L1":int("0040000c", 16)}

        address = int("00400008",16) #just for reference
        index = 2
        inst = "blt zero, a0, L1"
        machine = "0000 0000 1010 0000 0100 0010 0110 0011"
        Assembler_method_testing_helper(self.assertEqual, assembler.Assemble_SB_Type, inst, machine, index, labels)

        inst = "blt a0, x0, L2"
        machine = "1111 1110 0000 0101 0100 1100 1110 0011"
        address = int("0040000c",16) #just for reference
        index = 3
        Assembler_method_testing_helper(self.assertEqual, assembler.Assemble_SB_Type, inst, machine, index, labels)

    #@weight(1)
    def test_SB_types_bge(self):
        #this is just for reference
        full_code = """bge zero, t0, 4
                        L2: bge zero, s0, -4
                        bge zero, a0, L1
                        L1:bbgegt a0, x0, L2"""

        inst = "bge zero, t0, 4 "
        machine = "0000 0000 0101 0000 0101 0010 0110 0011"
        Assembler_method_testing_helper(self.assertEqual, assembler.Assemble_SB_Type, inst, machine)

        inst = "bge zero, s0, -4"
        machine = "1111 1110 1000 0000 0101 1110 1110 0011"
        Assembler_method_testing_helper(self.assertEqual, assembler.Assemble_SB_Type, inst, machine)

        #set up the labels for the tests below
        labels = {"L2":int("00400004", 16), "L1":int("0040000c", 16)}

        address = int("00400008",16) #just for reference
        index = 2
        inst = "bge zero, a0, L1"
        machine = "0000 0000 1010 0000 0101 0010 0110 0011"
        Assembler_method_testing_helper(self.assertEqual, assembler.Assemble_SB_Type, inst, machine, index, labels)

        inst = "bge a0, x0, L2"
        machine = "1111 1110 0000 0101 0101 1100 1110 0011"
        address = int("0040000c",16) #just for reference
        index = 3
        Assembler_method_testing_helper(self.assertEqual, assembler.Assemble_SB_Type, inst, machine, index, labels)

    #@weight(1)
    def test_SB_types_range(self):
        labels = {"L2":int("00400004", 16), "L1":int("0040000c", 16), "L3":int("10000000", 16)}
        #check for immediate issues
        inst = "beq t0, t0, 16383"
        index = 0
        Assembler_method_error_testing_helper(self.assertRaises, assembler.BadImmediate, assembler.Assemble_SB_Type, inst, index, labels)

        inst = "beq t0, t0, L3"
        index = 0
        address = int("00400000",16) #just for reference0
        Assembler_method_error_testing_helper(self.assertRaises, assembler.BadImmediate, assembler.Assemble_SB_Type, inst, index, labels)

    #@weight(1)
    def test_SB_types_operands(self):
        inst = "bne zero, t0, t0"
        Assembler_method_error_testing_helper(self.assertRaises, assembler.BadImmediate, assembler.Assemble_SB_Type, inst)

        inst = "bne zero, 4, 4"
        Assembler_method_error_testing_helper(self.assertRaises, assembler.BadRegister, assembler.Assemble_SB_Type, inst)

        inst = "bne zero, t0, t0, 4"
        Assembler_method_error_testing_helper(self.assertRaises, assembler.BadOperands, assembler.Assemble_SB_Type, inst)

        inst = "bne zero, t0, 8193"
        Assembler_method_error_testing_helper(self.assertRaises, assembler.BadImmediate, assembler.Assemble_SB_Type, inst)

class TestUJType(unittest.TestCase):
    #@weight(1)
    def test_UJ_types_jal(self):
        #this is just for reference
        full_code = """L2: jal x0, 0
                    jal ra, -4
                    jal ra, L1
                    L1: jal ra, L2"""
            
        inst = "jal x0, 0 "
        machine = "0000 0000 0000 0000 0000 0000 0110 1111"
        Assembler_method_testing_helper(self.assertEqual, assembler.Assemble_UJ_Type, inst, machine)

        inst = "jal ra, -4 "
        machine = "1111 1111 1101 1111 1111 0000 1110 1111"
        Assembler_method_testing_helper(self.assertEqual, assembler.Assemble_UJ_Type, inst, machine)

        #set up the labels for the tests below
        labels = {"L2":int("00400000", 16), "L1":int("0040000c", 16)}

        address = int("00400008",16) #just for reference
        index = 2
        inst = "jal ra, L1"
        machine = "0000 0000 0100 0000 0000 0000 1110 1111"
        Assembler_method_testing_helper(self.assertEqual, assembler.Assemble_UJ_Type, inst, machine, index, labels)

        inst = "jal ra, L2"
        machine = "1111 1111 0101 1111 1111 0000 1110 1111"
        address = int("0040000c",16) #just for reference
        index = 3
        Assembler_method_testing_helper(self.assertEqual, assembler.Assemble_UJ_Type, inst, machine, index, labels)

    #@weight(1)
    def test_UJ_types_range(self):
        #set up the labels for the tests below
        labels = {"L2":int("00400000", 16), "L1":int("0040000c", 16), "L3":int("10000000", 16)}

        address = int("00400000",16) #just for reference
        index = 0
        inst = "jal ra, 4194303"
        Assembler_method_error_testing_helper(self.assertRaises, assembler.BadImmediate, assembler.Assemble_UJ_Type, inst, index, labels)

        inst = "jal ra, L3"
        address = int("00400000",16) #just for reference
        index = 0
        Assembler_method_error_testing_helper(self.assertRaises, assembler.BadImmediate, assembler.Assemble_UJ_Type, inst, index, labels)


    #@weight(1)
    def test_UJ_types_operands(self):
        inst = "jal ra, t0"
        Assembler_method_error_testing_helper(self.assertRaises, assembler.BadImmediate, assembler.Assemble_UJ_Type, inst)

        inst = "jal 0, t0"
        Assembler_method_error_testing_helper(self.assertRaises, assembler.BadRegister, assembler.Assemble_UJ_Type, inst)

        inst = "jal ra, t0, L"
        Assembler_method_error_testing_helper(self.assertRaises, assembler.BadOperands, assembler.Assemble_UJ_Type, inst)

        inst = "jal ra, 2097153"
        Assembler_method_error_testing_helper(self.assertRaises, assembler.BadImmediate, assembler.Assemble_UJ_Type, inst)


class TestUType(unittest.TestCase):
    #@weight(1)
    def test_U_type_lui(self):
        inst = "lui t0, 1"
        machine = "0000 0000 0000 0000 0001 0010 1011 0111"
        cmd, operands = split_inst(inst)
        self.assertEqual(machine, assembler.Assemble_U_Type(cmd, operands, 0))

        inst = "lui a0, 123"
        machine = "0000 0000 0000 0111 1011 0101 0011 0111"
        cmd, operands = split_inst(inst)
        self.assertEqual(machine, assembler.Assemble_U_Type(cmd, operands, 0))

        inst = "lui t0, -1"
        machine = "1111 1111 1111 1111 1111 0010 1011 0111"
        cmd, operands = split_inst(inst)
        self.assertEqual(machine, assembler.Assemble_U_Type(cmd, operands, 0))

    #@weight(1)
    def test_U_type_operands(self):
        inst = "lui t0, t0"
        cmd, operands = split_inst(inst)
        with self.assertRaises(assembler.BadImmediate):
            assembler.Assemble_U_Type(cmd, operands, 0)

        inst = "lui t0, t0, 5"
        cmd, operands = split_inst(inst)
        with self.assertRaises(assembler.BadOperands):
            assembler.Assemble_U_Type(cmd, operands, 0)

        inst = "lui t0, 2097153"
        cmd, operands = split_inst(inst)
        with self.assertRaises(assembler.BadImmediate):
            assembler.Assemble_U_Type(cmd, operands, 0)

class TestPseudos(unittest.TestCase):
    #@weight(1)
    def test_double(self):
        #build the pseudos
        pseudos = ph.get_pseudoinstruction_defs()
        
        #check some instructions
        instructions = ["double t0, t0"]
        result = ph.double(instructions[0], 0)
        self.check_single_pseudo(result, 1, ["t0"], allow_immediates=[1])

        instructions = ["double t1, s0"]
        result = ph.double(instructions[0], 0)
        self.check_single_pseudo(result, 1, ["t1", "s0"], allow_immediates=[1])

    #@weight(1)
    def test_double_operands(self):
        #build the pseudos
        pseudos = ph.get_pseudoinstruction_defs()
        
        #check the number of operands
        instructions = ["double t0, t0, t1"]
        with self.assertRaises(assembler.BadOperands):
            ph.double(instructions[0], 0)

        instructions = ["double t0"]
        with self.assertRaises(assembler.BadOperands):
            ph.double(instructions[0], 0)

    #@weight(1)
    def test_double_operand_types(self):
        #build the pseudos
        pseudos = ph.get_pseudoinstruction_defs()
        
        #check the type of operands
        instructions = ["double t0, 4"]
        with self.assertRaises(assembler.BadRegister):
            result = ph.double(instructions[0], 0)
            #we'll notice the wrong operand types when we try and translate
            #the core instructions, at the latest
            assembler.machine_pass(result, {})

        instructions = ["double t0, LABEL"]
        with self.assertRaises(assembler.BadRegister):
            result = ph.double(instructions[0], 0)
            assembler.machine_pass(result, {})

    #@weight(1)
    def test_diffsums(self):
        #build the pseudos
        pseudos = ph.get_pseudoinstruction_defs()
        
        #check some instructions
        instructions = ["diffsums a0, t1, t2, t3, t4"]
        result = ph.diffsums(instructions[0], 0)
        self.check_single_pseudo(result, 3, ["a0", "t1", "t2", "t3","t4"])

        instructions = ["diffsums a0, t1, a0, a0, t4"]
        result = ph.diffsums(instructions[0], 0)
        self.check_single_pseudo(result, 3, ["a0", "t1", "t4"])

    #@weight(1)
    def test_diffsums_operands(self):
        #build the pseudos
        pseudos = ph.get_pseudoinstruction_defs()
        
        #check the number of operands
        instructions = ["diffsums a0, t1, t2, t3"]
        with self.assertRaises(assembler.BadOperands):
            ph.diffsums(instructions[0], 0)

        instructions = ["diffsums a0, t1, t2, t3, t4, t5"]
        with self.assertRaises(assembler.BadOperands):
            ph.diffsums(instructions[0], 0)

    #@weight(1)
    def test_diffsums_operand_types(self):
        #build the pseudos
        pseudos = ph.get_pseudoinstruction_defs()
        
        #check the type of operands
        instructions = ["diffsums a0, t1, t2, t3, 4"]
        with self.assertRaises(assembler.BadRegister):
            result = ph.diffsums(instructions[0], 0)
            #we'll notice the wrong operand types when we try and translate
            #the core instructions, at the latest
            assembler.machine_pass(result, {})

        instructions = ["diffsums a0, LABEL, t2, t3, t4"]
        with self.assertRaises(assembler.BadRegister):
            result = ph.diffsums(instructions[0], 0)
            assembler.machine_pass(result, {})

    #@weight(1)
    def test_push(self):
        #build the pseudos
        pseudos = ph.get_pseudoinstruction_defs()
        
        #check some instructions
        instructions = ["push t0"]
        result = ph.push(instructions[0], 0)
        self.check_single_pseudo(result, 2, ["t0", "sp"], allow_immediates=True)

        instructions = ["push a0"]
        result = ph.push(instructions[0], 0)
        self.check_single_pseudo(result, 2, ["a0", "sp"], allow_immediates=True)

    #@weight(1)
    def test_push_operands(self):
        #build the pseudos
        pseudos = ph.get_pseudoinstruction_defs()
        
        #check the number of operands
        instructions = ["push"]
        with self.assertRaises(assembler.BadOperands):
            ph.push(instructions[0], 0)

        instructions = ["push t0 t1"]
        with self.assertRaises(assembler.BadOperands):
            ph.push(instructions[0], 0)

        #check the type of operands
        instructions = ["push 4"]
        with self.assertRaises(assembler.BadRegister):
            result = ph.push(instructions[0], 0)
            #we'll notice the wrong operand types when we try and translate
            #the core instructions, at the latest
            assembler.machine_pass(result, {})

        instructions = ["push LABEL"]
        with self.assertRaises(assembler.BadRegister):
            result = ph.push(instructions[0], 0)
            assembler.machine_pass(result, {})

    #@weight(1)
    def test_li(self):
        #build the pseudos
        pseudos = ph.get_pseudoinstruction_defs()
        
        #check some instructions
        instructions = ["li t0, 1"]
        result = ph.li(instructions[0], 0)
        self.check_single_pseudo(result, [1, 2], ["t0"], allow_immediates=True)

        instructions = ["li t0, -1"]
        result = ph.li(instructions[0], 0)
        self.check_single_pseudo(result, [1, 2], ["t0"], allow_immediates=True)

        #li t0, 0x888
        instructions = ["li t0, 2184"] 
        result = ph.li(instructions[0], 0)
        self.check_single_pseudo(result, 2, ["t0"], allow_immediates=True)

    #@weight(1)
    def test_li_big_immediates(self):
        #build the pseudos
        pseudos = ph.get_pseudoinstruction_defs()
        
        #li t3, 0x8888
        instructions = ["li t3, 34952"]
        result = ph.li(instructions[0], 0)
        self.check_single_pseudo(result, 2, ["t3"], allow_immediates=True)

        #li t0, 0x0FFF FFFF
        instructions = ["li t0, 268435455"]
        result = ph.li(instructions[0], 0)
        self.check_single_pseudo(result, 2, ["t0"], allow_immediates=True)

    #@weight(1)
    def test_li_operands(self):
        #build the pseudos
        pseudos = ph.get_pseudoinstruction_defs()
        
        #check the number of operands
        instructions = ["li t0, t0, 123"]
        with self.assertRaises(assembler.BadOperands):
            ph.li(instructions[0], 0)

        instructions = ["li 123"]
        with self.assertRaises(assembler.BadOperands):
            ph.li(instructions[0], 0)

        #check the type of operands
        instructions = ["li 123 123"]
        with self.assertRaises(assembler.BadRegister):
            result = ph.li(instructions[0], 0)
            #we'll notice the wrong operand types when we try and translate
            #the core instructions, at the latest
            assembler.machine_pass(result, {})

        #this one gets noticed when we build the core instructions
        #before run time, different from the others
        instructions = ["li t0 t0"]
        with self.assertRaises(assembler.BadImmediate):
            result = ph.li(instructions[0], 0)
            assembler.machine_pass(result, {})

    #@weight(1)
    def test_beqz_with_labels(self):
        #build the pseudos
        pseudos = ph.get_pseudoinstruction_defs()
        
        #check some instructions
        instructions = ["beqz t0, LABEL"]
        result = ph.beqz(instructions[0], 0)
        self.check_single_pseudo(result, 1, allowed_symbols=["t0", "LABEL"], possible_symbols=["zero","x0"])

        instructions = ["beqz a0, LABEL"]
        result = ph.beqz(instructions[0], 0)
        self.check_single_pseudo(result, 1, ["a0", "LABEL"], possible_symbols=["zero","x0"])

    #@weight(1)
    def test_beqz_with_numbers(self):
        #build the pseudos
        pseudos = ph.get_pseudoinstruction_defs()
        
        instructions = ["beqz a0, 400"]
        result = ph.beqz(instructions[0], 0)
        self.check_single_pseudo(result, 1, ["a0", "400"], possible_symbols=["zero","x0"])

        instructions = ["beqz a0, -400"]
        result = ph.beqz(instructions[0], 0)
        self.check_single_pseudo(result, 1, ["a0", "-400"], possible_symbols=["zero","x0"])
        
    #@weight(1)
    def test_beqz_operands(self):
        #build the pseudos
        pseudos = ph.get_pseudoinstruction_defs()
        
        #check the number of operands
        instructions = ["beqz a0"]
        with self.assertRaises(assembler.BadOperands):
            ph.beqz(instructions[0], 0)

        instructions = ["beqz t0, a0, LABEL"]
        with self.assertRaises(assembler.BadOperands):
            ph.beqz(instructions[0], 0)

    #@weight(1)
    def test_beqz_operand_types(self):
        #build the pseudos
        pseudos = ph.get_pseudoinstruction_defs()
        
        #check the type of operands
        instructions = ["beqz 0, LABEL"]
        with self.assertRaises(assembler.BadRegister):
            result = ph.beqz(instructions[0], 0)
            #we'll notice the wrong operand types when we try and translate
            #the core instructions, at the latest
            assembler.machine_pass(result, {"LABEL":int("00400004", 16)})

        instructions = ["beqz a0, t0"]
        with self.assertRaises(assembler.BadImmediate):
            result = ph.beqz(instructions[0], 0)
            assembler.machine_pass(result, {"LABEL":int("00400004", 16)})
        
        instructions = ["beqz a0, 8193"]
        with self.assertRaises(assembler.BadImmediate):
            result = ph.beqz(instructions[0], 0)
            assembler.machine_pass(result, {"LABEL":int("00400004", 16)})

    #@weight(1)
    def test_jalif_labels(self):
        #build the pseudos
        pseudos = ph.get_pseudoinstruction_defs()
        
        #check some instructions
        instructions = ["jalif t0, t0, LABEL"]
        result = ph.jalif(instructions[0], 0)
        self.check_single_pseudo(result, [2, 3], ["t0", "LABEL", "ra"], allow_immediates=True, allow_labels=True)

        instructions = ["jalif a0, s0, PLACE"]
        result = ph.jalif(instructions[0], 0)
        self.check_single_pseudo(result, [2, 3], ["a0", "s0", "PLACE", "ra"], allow_immediates=True, allow_labels=True)

    #@weight(1)
    def test_jalif_numbers(self):
        #build the pseudos
        pseudos = ph.get_pseudoinstruction_defs()
        
        instructions = ["jalif a0, s0, 400"]
        result = ph.jalif(instructions[0], 0)
        self.check_single_pseudo(result, [2, 3], ["a0", "s0", "400", "ra"], allow_immediates=True, allow_labels=True)

        instructions = ["jalif a0, s0, -400"]
        result = ph.jalif(instructions[0], 0)
        self.check_single_pseudo(result, [2, 3], ["a0", "s0", "-400", "ra"], allow_immediates=True, allow_labels=True)

    #@weight(1)
    def test_jalif_operands(self):
        #build the pseudos
        pseudos = ph.get_pseudoinstruction_defs()
        
        #check the number of operands
        instructions = ["jalif t0, t0"]
        with self.assertRaises(assembler.BadOperands):
            ph.jalif(instructions[0], 0)

        instructions = ["jalif t0, t0, t0, LABEL"]
        with self.assertRaises(assembler.BadOperands):
            ph.jalif(instructions[0], 0)

        #check the type of operands
        instructions = ["jalif t0, 0, LABEL"]
        with self.assertRaises(assembler.BadRegister):
            result = ph.jalif(instructions[0], 0)
            #we'll notice the wrong operand types when we try and translate
            #the core instructions, at the latest
            assembler.machine_pass(result, {})

    #@weight(1)
    def test_jalif_with_bad_immediates(self):
        #build the pseudos
        pseudos = ph.get_pseudoinstruction_defs()
        
        instructions = ["jalif t0, t0, t0"]
        with self.assertRaises(assembler.BadImmediate):
            result = ph.jalif(instructions[0], 0)
            assembler.machine_pass(result, {})

        instructions = ["jalif t0, t0, 2097153"]
        with self.assertRaises(assembler.BadImmediate):
            result = ph.jalif(instructions[0], 0)
            assembler.machine_pass(result, {"LABEL":int("00400004", 16)})

    #@weight(1)
    def test_pseudoinstructions_pass(self):
        #build the pseudos
        pseudos = ph.get_pseudoinstruction_defs()
        
        #check some instructions -- these checks are identical to the ones tested
        # before but they rely on the pseudoinstruction_pass method to do the expansion.
        instructions = ["double t1, s0"]
        result = assembler.pseudoinstruction_pass(instructions, pseudos)
        result = ph.double(instructions[0], 0)
        self.check_single_pseudo(result, 1, ["t1", "s0"])

        instructions = ["diffsums a0, t1, t2, t3, t4"]
        result = assembler.pseudoinstruction_pass(instructions, pseudos)
        self.check_single_pseudo(result, 3, ["a0", "t1", "t2", "t3","t4"])

        instructions = ["push t0"]
        result = assembler.pseudoinstruction_pass(instructions, pseudos)
        self.check_single_pseudo(result, 2, ["t0", "sp"], allow_immediates=True)

        instructions = ["li t0, 1"]
        result = assembler.pseudoinstruction_pass(instructions, pseudos)
        self.check_single_pseudo(result, [1, 2], ["t0"], allow_immediates=True)


    def check_single_pseudo(self, code, expected_length, allowed_symbols, possible_symbols=[], allow_immediates = False, allow_labels = False):
        """Helper function to test parts of a pseudoinstruction 
            implementations meet these requirements:
            1) has the correct number of instructions
            2) uses all operands somewhere in the code
            3) uses only allowed registers
            4) only uses immediates if allowed
            5) only used labels if allowed
            Passing these checks do not guarantee that the pseudoinstruction has 
            the correct behavior, just that it meets generic criteria.
            """ 
        #check the numer of instructions in the pseudo implementation
        #make a list for when more than one option is available
        if (type(expected_length) is not list):
            expected_length = [expected_length]
        self.assertTrue(len(code) in expected_length)
        #check that each operand is used at least once
        self.assertTrue(self.check_all_ops_used(code, allowed_symbols))
        #check that only allowed operands are used
        # 'at' is always allowed in pseudos.
        self.assertEqual(None, self.check_all_inst_for_bad_symbols(code, possible_symbols+allowed_symbols+["at","x31"], allow_immediates, allow_labels))
 

    def check_all_inst_for_bad_symbols(self, code, allowed_symbols, allow_immediates = True, allow_labels = False):
        """Returns None if this code uses only allowed operands.
            Otherwise, returns a tupe of the first offending line and a list of 
            non-allowed operands."""
        for line in code:
            other_args = self.remove_allowed(line, allowed_symbols, allow_labels)
            if(allow_immediates):
                other_args = self.remove_immediates(other_args)
            if (other_args):
                return (line, other_args)
        return None

    def check_all_ops_used(self, code, allowed_symbols):
        """Checks that each operand is used at least once in the code.
            Returns True if all are used at least once."""
        unused_syms = allowed_symbols[:]#make a copy of the symbols array
        for line in code:
            #only keep a list of the symbols not present on this line
            unused_syms = [sym for sym in unused_syms if sym not in line]
        return unused_syms == []

    def remove_allowed(self, inst, remove, allow_labels = False):
        #removes values present in 'remove' from the inst operands
        #returns a list of remaining operands
        cmd, operands = split_inst(inst)
        #clean up the base-offset operands
        new_operands = []
        for a in operands:
            new_operands += self.clean_base_offset(a)
        if (allow_labels and cmd in ["beq", "bne", "blt", "bge", "jal"]):
            #just peel off the last operand
            new_operands = new_operands[0:-1]
        return [a for a in new_operands if a not in remove]
    
    def clean_base_offset(self, operand):
        if("(" in operand):
            #base offset
            soperand = operand.replace(")","").split("(")
            return soperand
        return [operand]

    def remove_immediates(self, ls):
        #remove all string integers from the list and returns the rest
        remain = []
        for v in ls:
            try:
                int(v)
            except ValueError:
                remain.append(v)
        return remain

class TestPseudosFileAssembly(unittest.TestCase):
    #@weight(2)
    def test_basic_pass(self):
        #just one double in some R-types
        instructions = """add t0, t0, a0
                            double a1, t0
                            sub t0, a1, a2"""
        instructions = instructions.split("\n")

        assembled = assembler.assemble_asm(instructions)
        #check number of instructions is right
        self.assertEqual(3, len(assembled))

    
    #@weight(2)
    def test_several_pseudos(self):
        #two pseudos mixed with core
        instructions = """add t0, t0, a0
                            double a1, t0
                            sub t0, a1, a2
                            push t0"""
        instructions = instructions.split("\n")

        assembled = assembler.assemble_asm(instructions)
        #check number of instructions is right
        self.assertEqual(5, len(assembled))


        #two pseudos following each other
        instructions = """double a1, t0
                            push t0"""
        instructions = instructions.split("\n")

        assembled = assembler.assemble_asm(instructions)
        #check number of instructions is right
        self.assertEqual(3, len(assembled))

        #all the pseudos at once
        instructions = """L: double a1, t0
                            push t0
                            diffsums a0, s0, s1, s2, a1
                            beqz a0, L
                            jalif a0, a1, 8"""
        instructions = instructions.split("\n")

        assembled = assembler.assemble_asm(instructions)
        #check number of instructions is right
        self.assertEqual(9, len(assembled))

    #@weight(2)
    def test_multiple_use(self):

        instructions = """PLACE: add a0, a0, a1
                            jalif a0, a2, PLACE
                            addi a0, a0, -4
                            jalif a0, a2, PLACE"""
        instructions = [i.lstrip() for i in instructions.split("\n")]

        assembled = assembler.assemble_asm(instructions)
        #check number of instructions is right
        self.assertEqual(6, len(assembled))
        #check that the correct number of labels is present
        pseudos = ph.get_pseudoinstruction_defs()
        expanded = assembler.pseudoinstruction_pass(instructions, pseudos)
        labels = assembler.parse_labels(expanded)
        #implementation will affect the number of labels here
        #but 2 indicates an error
        self.assertTrue(len(labels[1]) == 1 or len(labels[1]) == 3)

    
    def check_all_core(self, code):
        """Returns None if only core instructions are found, otherwise
            returns the offending line."""
        for line in code:
            cmd, operands = split_inst(line)
            if(cmd not in assembler.inst_to_fields):
                return line
        return None
