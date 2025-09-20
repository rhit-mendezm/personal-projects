# Risc V Assembler

## Overview
`assembler.py` is a Python-based assembler for a subset of the RISC-V instruction set. It supports core instructions, label handling, 
and pseudoinstruction expansion. This project was implemented for educational purposes in CSSE232 (RISC-V Assembly Programming).

## Features
- **Core Instruction Assembly**
  - Converts RISC-V instructions (`R-type`, `I-type`, `S-type`, `B-type`, `U-type`, `J-type`) into machine code.
  - Supports immediate, register, and label operands.
  
- **Label Handling**
  - Parses labels in assembly code.
  - Detects duplicate or invalid labels (`BadLabel`).
  
- **Pseudoinstructions**
  - Supports common pseudoinstructions like:
    - `double`
    - `diffsums`
    - `push`
    - `li`
    - `beqz`
    - `jalif`
  - Automatically expands pseudoinstructions into core instructions.
  
- **Error Checking**
  - Detects:
    - Out-of-range immediates (`BadImmediate`)
    - Invalid registers (`BadRegister`)
    - Wrong number/type of operands (`BadOperands`)

- **Integration**
  - Combines core instructions and pseudoinstructions in a single pass.
  - Handles labels across multiple instruction types.

## Getting Started

### Requirements
- Python 3.10+

### Installation
Clone this repository:
```bash
git clone https://github.com/<your-username>/risc-v-assembler.git
cd risc-v-assembler

