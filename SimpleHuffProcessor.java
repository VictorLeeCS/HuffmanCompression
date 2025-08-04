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
import java.util.Map;
import java.util.TreeMap;

public class SimpleHuffProcessor implements IHuffProcessor {

    private IHuffViewer myViewer;
    private Compress compressor;
    private int chooseHeaderFormat;
    private int amountCompressed;
    private int newFileBits;
   

    /**
     * Preprocess data so that compression is possible --- count characters/create
     * tree/store state so that a subsequent call to compress will work. The
     * InputStream is <em>not</em> a BitInputStream, so wrap it int one as needed.
     * @param in is the stream which could be subsequently compressed
     * @param headerFormat a constant from IHuffProcessor that determines what kind
     *        of header to use, standard count format, standard tree
     *        format, or possibly some format added in the future.
     * @return number of bits saved by compression or some other measure Note, to
     *         determine the number of bits saved, the number of bits written
     *         includes ALL bits that will be written including the magic number,
     *         the header format number, the header to reproduce the tree, AND the
     *         actual data.
     * @throws IOException if an error occurs while reading from the input file.
     */
    public int preprocessCompress(InputStream in, int headerFormat) throws IOException {
        chooseHeaderFormat = headerFormat;
        BitInputStream input = new BitInputStream(in);
        int originalBits = readInBits(input);
        in.close();
        // compressor is initialized inside readInBits, so it's safe to use here
        newFileBits = compressor.newNumBits(headerFormat);
        amountCompressed = originalBits - newFileBits;
        
        //printing out message to GUI
        showString("Original number of bits: " + originalBits);
        showString("Bits saved through compression: " + amountCompressed);
        return amountCompressed;
    }

    
    // helper method for the constructor, it uses the bit input stream
    // to set a map of frequency of words and then creates a new compress
    // object and passes the map to it
    private int readInBits(BitInputStream input) throws IOException {
        int originalBits = 0;
        
        //creating a map of frequency to be passed to compressed 
        Map<Integer, Integer> freq = new TreeMap<Integer, Integer>();
        int inbits = input.readBits(IHuffConstants.BITS_PER_WORD);
        while (inbits != -1) {
            if (freq.containsKey(inbits)) {
                freq.put(inbits, freq.get(inbits) + 1);
            } else {
                freq.put(inbits, 1);
            }
            inbits = input.readBits(IHuffConstants.BITS_PER_WORD);
            originalBits += IHuffConstants.BITS_PER_WORD;
        }
        
        //adding a PSEUDO_EOF
        freq.put(IHuffConstants.PSEUDO_EOF, 1);
        input.close();
        
        //creation of a compress class, and calling the 
        //its constructor that takes in a frequency
        compressor = new Compress(freq);
        return originalBits;
    }

    /**
     * Compresses input to output, where the same InputStream has previously been
     * pre-processed via <code>preprocessCompress</code> storing state used by this
     * call. <br>
     * pre: <code>preprocessCompress</code> must be called before this method
     * @param in is the stream being compressed (NOT a BitInputStream)
     * @param out is bound to a file/stream to which bits are written for the
     * compressed file (not a BitOutputStream)
     * @param force if this is true create the output file even if it is larger than
     * the input file. If this is false do not create the output file
     * if it is larger than the input file.
     * @return the number of bits written.
     * @throws IOException if an error occurs while reading from the input file or
     * writing to the output file.
     */
    public int compress(InputStream in, OutputStream out, boolean force) throws IOException {
        if(amountCompressed < 0 && !force) {
            myViewer.showError("Compressed file has " + (-amountCompressed) + " more bits "+
                        "than uncompressed file. \n" + "Select force compression option to compress.");
            
            //returns 0 if file is not compressed
            return 0;
        }
        BitInputStream input = new BitInputStream(in);
        BitOutputStream output = new BitOutputStream(out);
        
        //using the compressor's writeNewFile method to write to the compressed file
        compressor.writeNewFile(input, output, chooseHeaderFormat);
        input.close();
        output.close();
        showString("New file number of bits: " + newFileBits);
        return newFileBits;
    }

    /**
     * Uncompress a previously compressed stream in, writing the uncompressed
     * bits/data to out.
     * @param in  is the previously compressed data (not a BitInputStream)
     * @param out is the uncompressed file/stream
     * @return the number of bits written to the uncompressed file/stream
     * @throws IOException if an error occurs while reading from the input file or
     *                     writing to the output file.
     */
    public int uncompress(InputStream in, OutputStream out) throws IOException {
        BitInputStream input = new BitInputStream(in);
        if(input.readBits(IHuffConstants.BITS_PER_INT) != IHuffConstants.MAGIC_NUMBER) {
            myViewer.showError("This is not a .hf file.");
            input.close();
            
            //return -1 if file cannot be uncompressed
            return -1;
        }
        Uncompress decompressor = new Uncompress(input);
        BitOutputStream output = new BitOutputStream(out);
        
        //using decompressor's writeToFile method to write to uncompressed file
        int bitsWritten = decompressor.writeToFile(input, output);
        input.close();
        output.close();
        showString("Number of bits for uncompressed file: " + bitsWritten);
        return bitsWritten;
    }

    //sets the viewer instance varaible in this class
    public void setViewer(IHuffViewer viewer) {
        myViewer = viewer;
    }

    //displays the string passed into the method
    private void showString(String s) {
        if (myViewer != null)
            myViewer.update(s);
    }
}