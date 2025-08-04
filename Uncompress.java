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

public class Uncompress {
    
    
    private HuffmanTree tree;
    
    //Precondition: input != null
    //creates a HuffmanTree object using the appropriate constructors
    public Uncompress(BitInputStream input) throws IOException {
        if(input == null) {
            throw new IllegalArgumentException("Violation of precondition");
        }
        
        //checks the format of this file
        int chooseHeaderFormat = input.readBits(IHuffConstants.BITS_PER_INT);
        if(chooseHeaderFormat == IHuffConstants.STORE_COUNTS) {
            
            //creating a map of frequency if using the count format
            Map<Integer, Integer> freq = new TreeMap<Integer, Integer>();
            getFreqMap(input,freq);
            tree = new HuffmanTree(freq);
        }else if(chooseHeaderFormat == IHuffConstants.STORE_TREE) {
            
            //creating a huffmanTree object by using the Bitinputstream constructor
            tree = new HuffmanTree(input);
            
        }    
    }
    
    //Precondition: freq != null
    //takes in a map and sets the frequency of each characters in the file
    private void getFreqMap(BitInputStream input, Map<Integer, Integer> freq) throws IOException{
        if(freq == null || input == null) {
            throw new IllegalArgumentException("Violation of precondition");
        }
        for(int i = 0; i < IHuffConstants.ALPH_SIZE; i++) {
            int readInFreq = input.readBits(IHuffConstants.BITS_PER_INT);
            if(readInFreq != 0) {
                freq.put(i, readInFreq);
            }
        }
        freq.put(IHuffConstants.PSEUDO_EOF,1);
    }
    
    //Takes in a bitInputStream and bitOutputStream object and
    //calls the newCode method in the tree class to write to a uncompressed file
    //to be used for uncompressing tree header format files
    public int writeToFile(BitInputStream input, BitOutputStream output) throws IOException{
        if(input == null || output == null) {
            throw new IllegalArgumentException("Violation of precondition");
        }
        return tree.newCode(input,output);
    }
    
    

    
    
}
