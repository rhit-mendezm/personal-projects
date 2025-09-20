# Kebab to Snake Case CLI Tool

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
git clone git@github.com:rhit-mendezm/personal-projects.git

cd kebab_to_snake_case_cli_tool

chmod +x rename_to_snake_case.sh

./rename_to_snake_case.sh [--silent] [--max-depth N] <file_or_directory>
```

## Testing

Make the test script executable:

```bash
chmod +x test.sh

./test.sh
```

**Note**: Tests will output text to the console, and `rename_to_snake_case.sh` does not need to be executable for tests to run.
