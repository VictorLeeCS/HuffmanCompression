/*  Student information for assignment:
 *
 *  On OUR honor, Victor Lee and Peter Hwang, 
 *  this programming assignment is OUR own work
 *  and WE have not provided this code to any other student.
 *
 *  Number of slip days used:0
 *
 *  Student 1 (Student whose Canvas account is being used)
 *  UTEID:VCL363   
 *  email address: victorleecs@utexas.edu
 *  TA name:Nina
 *  
 *  Student 2 
 *  UTEID: sh49687  
 *  email address: petersh0317@utexas.edu
 */

/**
 * Constants used in the Huffman compression and decompression process.
 * These constants define magic numbers, bit sizes, and format options.
 */
public interface IHuffConstants {
    
    /** Magic number used to identify compressed files */
    public static final int MAGIC_NUMBER = 1234567873;
    
    /** Number of bits per byte */
    public static final int BITS_PER_WORD = 8;
    
    /** Number of bits per integer */
    public static final int BITS_PER_INT = 32;
    
    /** Size of alphabet (number of possible byte values) */
    public static final int ALPH_SIZE = 256;
    
    /** Pseudo end-of-file character */
    public static final int PSEUDO_EOF = ALPH_SIZE;
    
    /** Header format option: store character frequencies */
    public static final int STORE_COUNTS = 0;
    
    /** Header format option: store tree structure */
    public static final int STORE_TREE = 1;
}
