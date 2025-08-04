# Huffman Encoding Project

## Overview
This project implements Huffman encoding for file compression and decompression. Huffman coding is a lossless data compression algorithm that assigns variable-length codes to characters based on their frequency of occurrence.

## Files Description

### Core Classes
- **SimpleHuffProcessor.java**: Main processor implementing the IHuffProcessor interface
- **HuffmanTree.java**: Implementation of the Huffman tree data structure
- **TreeNode.java**: Node class for the Huffman tree
- **Compress.java**: Handles the compression logic and file writing
- **Uncompress.java**: Handles the decompression logic and file reading
- **PriorityQueue.java**: Custom priority queue implementation for building the Huffman tree

### I/O Classes
- **BitInputStream.java**: Allows reading individual bits from an input stream
- **BitOutputStream.java**: Allows writing individual bits to an output stream

### Interfaces
- **IHuffProcessor.java**: Interface defining compression/decompression methods
- **IHuffViewer.java**: Interface for displaying progress messages
- **IHuffConstants.java**: Constants used throughout the project

### Test Files
- **HuffmanTest.java**: Simple test program to verify functionality

## Features

### Compression Formats
The project supports two header formats:
1. **STORE_COUNTS**: Stores character frequencies (256 integers)
2. **STORE_TREE**: Stores the tree structure directly

### Key Functionality
- Preprocessing to analyze file and build Huffman tree
- Compression with optional force mode
- Decompression with error checking
- Support for both count-based and tree-based headers
- Bit-level I/O operations

## Usage

### Basic Compression Example
```java
SimpleHuffProcessor processor = new SimpleHuffProcessor();
processor.setViewer(new MyViewer()); // Optional

// Preprocess the file
FileInputStream input = new FileInputStream("input.txt");
int bitsSaved = processor.preprocessCompress(input, IHuffConstants.STORE_COUNTS);

// Compress the file
input = new FileInputStream("input.txt");
FileOutputStream output = new FileOutputStream("compressed.hf");
int bitsWritten = processor.compress(input, output, false);
```

### Basic Decompression Example
```java
SimpleHuffProcessor processor = new SimpleHuffProcessor();

// Decompress the file
FileInputStream compressed = new FileInputStream("compressed.hf");
FileOutputStream decompressed = new FileOutputStream("output.txt");
int bitsWritten = processor.uncompress(compressed, decompressed);
```

### Running the Test
```bash
javac *.java
java HuffmanTest
```

## Technical Details

### Algorithm Steps
1. **Analysis**: Count character frequencies in the input file
2. **Tree Building**: Create Huffman tree using priority queue
3. **Code Generation**: Generate binary codes for each character
4. **Compression**: Write header + encoded data to output file
5. **Decompression**: Read header, rebuild tree, decode data

### File Format
Compressed files (.hf) contain:
1. Magic number (32 bits)
2. Header format indicator (32 bits)
3. Header data (frequency counts or tree structure)
4. Compressed data
5. Pseudo-EOF marker

## Error Handling
- Validates input parameters
- Checks for valid compressed file format (magic number)
- Handles end-of-file conditions properly
- Provides meaningful error messages through IHuffViewer

## Performance
The implementation is designed for educational purposes and demonstrates:
- Optimal compression ratios for text with repeated characters
- Efficient tree traversal for encoding/decoding
- Memory-efficient bit-level I/O operations

## Students
- Victor Lee (VCL363) - victorleecs@utexas.edu
- Peter Hwang (sh49687) - petersh0317@utexas.edu
- TA: Nina

## Notes
- This implementation uses a custom priority queue instead of Java's built-in version
- The project includes comprehensive error checking and validation
- Files are processed at the bit level for maximum compression efficiency
