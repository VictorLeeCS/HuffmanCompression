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

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * BitInputStream allows reading of individual bits from an InputStream.
 * Maintains a buffer to efficiently read bits from the underlying stream.
 */
public class BitInputStream {
    
    private BufferedInputStream input;
    private int bitBuffer;
    private int numBitsInBuffer;
    
    /**
     * Create a BitInputStream from an InputStream.
     * @param in the InputStream to wrap
     */
    public BitInputStream(InputStream in) {
        input = new BufferedInputStream(in);
        bitBuffer = 0;
        numBitsInBuffer = 0;
    }
    
    /**
     * Read the specified number of bits from the stream.
     * @param numBits the number of bits to read (1-32)
     * @return the bits read as an integer, or -1 if end of stream
     * @throws IOException if an I/O error occurs
     */
    public int readBits(int numBits) throws IOException {
        if (numBits < 0 || numBits > 32) {
            throw new IllegalArgumentException("numBits must be between 1 and 32");
        }
        
        int result = 0;
        
        // Read bits needed
        for (int i = 0; i < numBits; i++) {
            // If buffer is empty, fill it
            if (numBitsInBuffer == 0) {
                int nextByte = input.read();
                if (nextByte == -1) {
                    return -1; // End of stream
                }
                bitBuffer = nextByte;
                numBitsInBuffer = 8;
            }
            
            // Extract the leftmost bit from buffer
            int bit = (bitBuffer & 0x80) >> 7;
            result = (result << 1) | bit;
            
            // Shift buffer left
            bitBuffer <<= 1;
            numBitsInBuffer--;
        }
        
        return result;
    }
    
    /**
     * Close the underlying input stream.
     * @throws IOException if an I/O error occurs
     */
    public void close() throws IOException {
        input.close();
    }
}
