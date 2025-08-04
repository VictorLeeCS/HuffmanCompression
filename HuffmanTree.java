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

public class HuffmanTree {

    // ask if we can create an instance variable of bitvalue map
    public TreeNode root;

    //Precondition : freq != null
    //takes in a map of frequency and builds a huffmanTree
    //using the map of frequency
    public HuffmanTree(Map<Integer, Integer> freq) {
        if(freq == null) {
            throw new IllegalArgumentException("Violation of precondition");
        }
        
        //creates TreeNodes and puts it into the queue
        PriorityQueue<TreeNode> queue = new PriorityQueue<TreeNode>();
        for (int current : freq.keySet()) {
            TreeNode toBeAdded = new TreeNode(current, freq.get(current));
            queue.enqueue(toBeAdded);
        }

        // puts in a pseudo_eof into the map
        freq.put(IHuffConstants.PSEUDO_EOF, 1);
        
        //start building the tree
        while (queue.size() != 1) {
            TreeNode leftChild = queue.dequeue();
            TreeNode rightChild = queue.dequeue();
            TreeNode newRoot = new TreeNode(leftChild,
                    leftChild.getFrequency() + rightChild.getFrequency(), rightChild);
            queue.enqueue(newRoot);
        }
        root = queue.dequeue();
    }
    
    //Precondition : freq != null && newBitValues != null
    // Accepts map of frequency and a map of newBitValues uses the freq map to get the 
    // new bit values and set it to newBitValues
    public HuffmanTree(Map<Integer, Integer> freq, Map<Integer, String> newBitValues) {
        this(freq);
        if(newBitValues == null) {
            throw new IllegalArgumentException("Violation of precondition");
        }
        getNewBitValue(root, "", newBitValues);
    }
    
    //Precondition: input != null
    //Takes in a bitInputStream object and reads in input from a file
    //compressed using tree format and builds a huffman Tree
    public HuffmanTree(BitInputStream input) throws IOException {
        if(input == null) {
            throw new IllegalArgumentException("Violation of precondition");
        }
        int sizeOfHeader = input.readBits(IHuffConstants.BITS_PER_INT);
        int [] size = {sizeOfHeader};
        root = treeFormatBuildTree(size,input);
    }
    
    //recursive helper method for the HuffmanTree(BitInputStream input) method to build a huffman
    // tree for decompression of files using the treeHeader format
    private TreeNode treeFormatBuildTree(int[] numMoreBits, BitInputStream input) throws IOException{
        if(numMoreBits[0] > 0) {
            TreeNode current = null;
            int leafOrInternal = input.readBits(1);
            if(leafOrInternal == 0) {
                numMoreBits[0] --;
                TreeNode leftChild = treeFormatBuildTree(numMoreBits,input);
                TreeNode rightChild = treeFormatBuildTree(numMoreBits, input);
                current = new TreeNode(leftChild,0,rightChild);
                return current;
            }else {
                numMoreBits[0] -= (1 + IHuffConstants.BITS_PER_WORD) + 1;
                int value = Integer.parseInt(Integer.toString(input.readBits(IHuffConstants.BITS_PER_WORD + 1)),10);
                current = new TreeNode(value,0);
                return current;
            }
        }
        return null;
    }
    
    
    // recursive method that traverses the tree and set the new bitValues in the map
    // passed in
    private void getNewBitValue(TreeNode node, String current, Map<Integer, String> bitValues) {
        if (node != null) {
            if (node.isLeaf()) {
                bitValues.put(node.getValue(), current);
            }
            getNewBitValue(node.getLeft(), current.concat("0"), bitValues);
            getNewBitValue(node.getRight(), current.concat("1"), bitValues);
        }
    }

    //preconditions: output != null
    // takes in a BitOutputStream and outputs the header information for the
    // tree format
    public void treeHeaderInformation(BitOutputStream output) throws IOException {
        if(output == null) {
            throw new IllegalArgumentException("Violation of precondition");
        }
        
        // calls recursive helper method tree size and writes the size of the tree
        // header
        // information onto the file
        output.writeBits(IHuffConstants.BITS_PER_INT, treeSize());
        preOrderTraversal(output, root);
    }
    
    // recursive helper method for tree header information
    // traverse the tree in preOrder order and writes each leaf nodes
    // value onto the file
    private void preOrderTraversal(BitOutputStream output, TreeNode node) throws IOException {
        if (node != null) {
            if (node.isLeaf()) {
                output.writeBits(1, 1);
                output.writeBits(IHuffConstants.BITS_PER_WORD + 1, node.getValue());
            } else {
                output.writeBits(1, 0);
            }
            preOrderTraversal(output, node.getLeft());
            preOrderTraversal(output, node.getRight());
        }

    }

    // Determine the size of the header information, it is public so
    // that a compress object can call it to determine the size of
    // the header information if the tree format is used
    public int treeSize() {
        return treeSizeHelper(root);
    }

    // Recursive helper method for treeSize method
    private int treeSizeHelper(TreeNode node) {
        int count = 0;
        if (node != null) {
            if (node.isLeaf()) {
                
                //1 representing the bit used to represent this is a tree
                //IHuffContants.BITS_PER_WORD + 1 as 9 bits is used to represent
                //the value of a tree node
                return 1 + (IHuffConstants.BITS_PER_WORD + 1) ;
            } else {
                count++;
                count += treeSizeHelper(node.getLeft());
                count += treeSizeHelper(node.getRight());
            }
        }
        return count;
    }
    
    //method is used to uncompressed tree header format file
    //takes an BitInputStream and BitOutputStream object and traverses the tree based on the 
    //input of the compressed files and writes the orignial code to the uncompressed file
    //each time when a leaf node is encountered
    public int newCode(BitInputStream input, BitOutputStream output) throws IOException {
        if(input == null || output == null) {
            throw new IllegalArgumentException("Violation of precondition");
        }
        TreeNode currentNode = root;
        int currentBit = input.readBits(1);
        int bitsWritten = 0;
        
        while(currentBit != -1) {
            if(currentBit == 1) {
                currentNode = currentNode.getRight();
            }else {
                currentNode = currentNode.getLeft();
            }
            
            //check if this is a child node
            if(currentNode.isLeaf()) {
                
                //stop reading bits when PSEUDO_EOF is encountered
                if(IHuffConstants.PSEUDO_EOF == currentNode.getValue()) {
                    return bitsWritten;
                }
                bitsWritten += IHuffConstants.BITS_PER_WORD;
                output.writeBits(IHuffConstants.BITS_PER_WORD, currentNode.getValue());
                currentNode = root;
            }
            currentBit = input.readBits(1);
        }
        
        //when there is not enough bits and last node is on an internal node
        if(!currentNode.isLeaf()) {
            throw new IOException("File is not compressed properly last node should"
                    + " be in a leaf");
        }
        
        //when an PSUEDO_EOF is not met exception is thrown
        if(currentBit == -1) {
            throw new IOException("Error reading compressed file. \n" +
                    "unexpected end of input. No PSEUDO_EOF value.");
        }
        return bitsWritten;
    }

}