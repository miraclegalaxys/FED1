# FED1

A secure file encryption and decryption utility using AES/CBC/PKCS5Padding algorithm.

## Security Features

1. Advanced Encryption Standard (AES)
   - 128-bit key size
   - CBC mode with PKCS5Padding
   - Secure random IV generation

2. Password Security
   - SHA-256 password hashing
   - Secure memory handling
   - Password prompt masking

## Technical Specifications

- Algorithm: AES/CBC/PKCS5Padding
- Key Generation: SHA-256 hash of password
- IV Size: 16 bytes
- Buffer Size: 8192 bytes
- Secure Random IV generation for each encryption

## Prerequisites

- Java Development Kit (JDK) 8 or higher
- Java Runtime Environment (JRE)

## Installation

1. Clone the repository:
```bash
git clone https://github.com/miraclegalaxys/FED1.git
cd FED1
```

2. Compile the source code:
```bash
javac fed.java
```

## Usage

Run the program:
```bash
java fed
```

### Interactive Prompts

The program will guide you through:
1. Password entry (masked for security)
2. Operation mode selection (encrypt/decrypt)
3. Input file path specification
4. Output file path specification

### Example Session:
```
███████╗███████╗██████╗  ██╗
██╔════╝██╔════╝██╔══██╗███║
█████╗  █████╗  ██║  ██║╚██║
██╔══╝  ██╔══╝  ██║  ██║ ██║
██║     ███████╗██████╔╝ ██║
╚═╝     ╚══════╝╚═════╝  ╚═╝
=== File Encryption Tool ===
Enter password: [hidden]
Enter mode (encrypt/decrypt): encrypt
Enter input file path: /path/to/<file>
Enter output file path: /path/to/<new_file>
```

## Security Considerations

1. Password Protection
   - Passwords are immediately cleared from memory
   - Never stored in plaintext
   - Securely hashed using SHA-256

2. File Operations
   - Original files are preserved
   - Encrypted files include random IV
   - Progress monitoring during operations

3. Error Handling
   - Comprehensive exception handling
   - Clear error messages
   - File existence checks

## Implementation Details

1. Key Generation
   - Derived from password using SHA-256
   - Truncated to 128 bits for AES
   - Secure random IV for each operation

2. File Processing
   - Efficient streaming with 8KB buffer
   - Progress monitoring
   - IV prepended to encrypted files

3. Memory Management
   - Secure password handling
   - Efficient buffer usage
   - Automatic resource cleanup

## Best Practices

1. Password Selection
   - Use strong, unique passwords
   - Avoid common patterns
   - Mix characters, numbers, and symbols

2. File Management
   - Keep secure backups
   - Verify file integrity after operations
   - Maintain secure storage of encrypted files

## Contributing

Contributions are welcome. Please ensure:
- Comprehensive security testing
- Clear documentation
- Proper error handling
- Code style consistency

## License

[MIT License](LICENSE)

## Author

By ggalaxys_

