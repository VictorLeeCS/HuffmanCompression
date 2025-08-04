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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Interface for Huffman encoding processor.
 * Defines the contract for compression and decompression operations.
 */
public interface IHuffProcessor {
    
    /**
     * Preprocess data for compression.
     * @param in the input stream to analyze
     * @param headerFormat the format for the header (STORE_COUNTS or STORE_TREE)
     * @return the number of bits saved by compression
     * @throws IOException if an error occurs while reading
     */
    public int preprocessCompress(InputStream in, int headerFormat) throws IOException;
    
    /**
     * Compress the input stream to the output stream.
     * @param in the input stream to compress
     * @param out the output stream for compressed data
     * @param force whether to force compression even if file gets larger
     * @return the number of bits written
     * @throws IOException if an error occurs during compression
     */
    public int compress(InputStream in, OutputStream out, boolean force) throws IOException;
    
    /**
     * Decompress a previously compressed stream.
     * @param in the compressed input stream
     * @param out the output stream for decompressed data
     * @return the number of bits written to the uncompressed file
     * @throws IOException if an error occurs during decompression
     */
    public int uncompress(InputStream in, OutputStream out) throws IOException;
    
    /**
     * Set the viewer for displaying progress messages.
     * @param viewer the viewer to use for displaying messages
     */
    public void setViewer(IHuffViewer viewer);
}
