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
import java.util.Map;
import java.util.TreeMap;

public class Compress {

    private Map<Integer, Integer> freq;
    private Map<Integer, String> newBitValues;
    private HuffmanTree tree;

    // Precondition: freq != null
    // Takes in a map of frequency and creates a tree map
    // object. The treeMap object will then set the priority queue
    public Compress(Map<Integer, Integer> freq) throws IOException {
        if (freq == null) {
            throw new IllegalArgumentException("Violation of precondition");
        }

        // setting instance variables
        this.freq = freq;
        newBitValues = new TreeMap<Integer, String>();
        tree = new HuffmanTree(freq, newBitValues);
    }

    // precondtion: headerFormat == IHuffConstants.STORE_TREE || headerFormat ==
    // IHuffConstants.STORE_COUNTS
    // return the number of bits that is saved if the compression is
    // applied, takes in an int representing the choosen header information
    public int newNumBits(int headerFormat) {
        if (headerFormat != IHuffConstants.STORE_TREE
                && headerFormat != IHuffConstants.STORE_COUNTS) {
            throw new IllegalArgumentException("Violation of precondition");
        }

        // magic value plus the choice of header Information
        int newSum = IHuffConstants.BITS_PER_INT + IHuffConstants.BITS_PER_INT;

        // determining if file is using tree or count format
        // and calculates the size of the header infomation
        if (headerFormat == IHuffConstants.STORE_TREE) {

            // adding the size representation of the tree format
            // before calculating the tree size
            newSum += (IHuffConstants.BITS_PER_INT + tree.treeSize());
        } else if (headerFormat == IHuffConstants.STORE_COUNTS) {
            newSum += (IHuffConstants.ALPH_SIZE * IHuffConstants.BITS_PER_INT);
        }

        // determing the size of the words after the new bit
        // values are applied
        for (int current : freq.keySet()) {
            int freqOfCurrent = freq.get(current);
            newSum += (newBitValues.get(current).length() * freqOfCurrent);
        }
        return newSum;
    }

    // precondtion: input != null && output != null
    // This method is called after the new bit value have been generated, it reads
    // in the
    // information from the original file and writes the new bit out to the new file
    public void writeNewFile(BitInputStream input, BitOutputStream output, int headerFormat)
            throws IOException {
        if (input == null || output == null || (headerFormat != IHuffConstants.STORE_COUNTS
                && headerFormat != IHuffConstants.STORE_TREE)) {
            throw new IllegalArgumentException("Violation of precondition");
        }
        final int BINARY_BASE = 2;

        // adding magic number and the choosen header format
        output.writeBits(IHuffConstants.BITS_PER_INT, IHuffConstants.MAGIC_NUMBER);
        output.writeBits(IHuffConstants.BITS_PER_INT, headerFormat);
        writeHeaderFormat(output, headerFormat);

        // reading the original code in the old file
        // and writing the new code to the file
        int currentWord = input.readBits(IHuffConstants.BITS_PER_WORD);
        String newCode = newBitValues.get(currentWord);
        while (currentWord != -1) {
            output.writeBits(newCode.length(), Integer.parseInt(newCode, BINARY_BASE));
            currentWord = input.readBits(IHuffConstants.BITS_PER_WORD);
            newCode = newBitValues.get(currentWord);
        }

        // adding psuedo_eof to the end
        newCode = newBitValues.get(IHuffConstants.PSEUDO_EOF);
        output.writeBits(newCode.length(), Integer.parseInt(newCode, BINARY_BASE));
    }

    // private helper method that takes in a Bitoutputstream object and a int
    // representing headerFormat and writes the header information based on the
    // choosen header format
    private void writeHeaderFormat(BitOutputStream output, int headerFormat) throws IOException {
        if (headerFormat != IHuffConstants.STORE_COUNTS
                && headerFormat != IHuffConstants.STORE_TREE) {
            throw new IllegalArgumentException("Header format is not tree or count format");
        }

        // checks if the headerFormat is count or tree formate
        if (headerFormat == IHuffConstants.STORE_COUNTS) {

            // loops through 256 times and sets the int at each positon to
            // the appropriate frequency
            for (int index = 0; index < IHuffConstants.ALPH_SIZE; index++) {
                int doesItExist = index;
                if (freq.containsKey(doesItExist)) {
                    output.writeBits(IHuffConstants.BITS_PER_INT, freq.get(doesItExist));
                } else {
                    output.writeBits(IHuffConstants.BITS_PER_INT, 0);
                }
            }

            // if the file is using tree format the treeHeaderInformation in the
            // tree class is called
        } else if (headerFormat == IHuffConstants.STORE_TREE) {
            tree.treeHeaderInformation(output);
        }
    }

}
