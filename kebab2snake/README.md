# kebab2snake

A command-line tool for converting file and directory names from **kebab-case** (`this-is-kebab`) to **snake_case** (`this_is_snake`).  
It supports recursive renaming with a configurable depth limit and optional silent mode.

## Features
- Convert filenames and directory names from kebab-case to snake_case.
- Recursive renaming with `--max-depth` to control traversal depth.
- `--silent` mode to suppress output.
- Handles files and nested directories.
- Extensive automated test suite.

## Installation

Clone the repository and make the script executable:

```bash
git clone https://github.com/<your_username>/kebab2snake.git
cd kebab2snake
chmod +x rename_to_snake_case.sh

./rename_to_snake_case.sh [--silent] [--max-depth N] <file_or_directory>

