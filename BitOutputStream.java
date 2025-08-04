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

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * BitOutputStream allows writing of individual bits to an OutputStream.
 * Maintains a buffer to efficiently write bits to the underlying stream.
 */
public class BitOutputStream {
    
    private BufferedOutputStream output;
    private int bitBuffer;
    private int numBitsInBuffer;
    
    /**
     * Create a BitOutputStream from an OutputStream.
     * @param out the OutputStream to wrap
     */
    public BitOutputStream(OutputStream out) {
        output = new BufferedOutputStream(out);
        bitBuffer = 0;
        numBitsInBuffer = 0;
    }
    
    /**
     * Write the specified number of bits to the stream.
     * @param numBits the number of bits to write (1-32)
     * @param value the value containing the bits to write
     * @throws IOException if an I/O error occurs
     */
    public void writeBits(int numBits, int value) throws IOException {
        if (numBits < 0 || numBits > 32) {
            throw new IllegalArgumentException("numBits must be between 1 and 32");
        }
        
        // Write bits from left to right
        for (int i = numBits - 1; i >= 0; i--) {
            int bit = (value >> i) & 1;
            
            // Add bit to buffer
            bitBuffer = (bitBuffer << 1) | bit;
            numBitsInBuffer++;
            
            // If buffer is full, write it out
            if (numBitsInBuffer == 8) {
                output.write(bitBuffer);
                bitBuffer = 0;
                numBitsInBuffer = 0;
            }
        }
    }
    
    /**
     * Flush any remaining bits in the buffer and close the stream.
     * If there are bits in the buffer, they are padded with zeros on the right.
     * @throws IOException if an I/O error occurs
     */
    public void close() throws IOException {
        // If there are remaining bits, pad with zeros and write
        if (numBitsInBuffer > 0) {
            bitBuffer <<= (8 - numBitsInBuffer);
            output.write(bitBuffer);
        }
        output.close();
    }
    
    /**
     * Flush the underlying output stream.
     * @throws IOException if an I/O error occurs
     */
    public void flush() throws IOException {
        output.flush();
    }
}
