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
 * TreeNode class for the Huffman tree.
 * Represents both leaf nodes (with values) and internal nodes (with children).
 */
public class TreeNode implements Comparable<TreeNode> {
    
    private int value;
    private int frequency;
    private TreeNode left;
    private TreeNode right;
    
    /**
     * Constructor for leaf nodes.
     * @param value the character value
     * @param frequency the frequency of this character
     */
    public TreeNode(int value, int frequency) {
        this.value = value;
        this.frequency = frequency;
        this.left = null;
        this.right = null;
    }
    
    /**
     * Constructor for internal nodes.
     * @param left the left child
     * @param frequency the combined frequency
     * @param right the right child
     */
    public TreeNode(TreeNode left, int frequency, TreeNode right) {
        this.value = -1; // Internal nodes don't have a character value
        this.frequency = frequency;
        this.left = left;
        this.right = right;
    }
    
    /**
     * Get the character value (for leaf nodes).
     * @return the character value
     */
    public int getValue() {
        return value;
    }
    
    /**
     * Get the frequency of this node.
     * @return the frequency
     */
    public int getFrequency() {
        return frequency;
    }
    
    /**
     * Get the left child.
     * @return the left child node
     */
    public TreeNode getLeft() {
        return left;
    }
    
    /**
     * Get the right child.
     * @return the right child node
     */
    public TreeNode getRight() {
        return right;
    }
    
    /**
     * Check if this is a leaf node.
     * @return true if this node has no children
     */
    public boolean isLeaf() {
        return left == null && right == null;
    }
    
    /**
     * Compare nodes based on frequency for priority queue ordering.
     * @param other the other TreeNode to compare to
     * @return negative if this has lower frequency, positive if higher, 0 if equal
     */
    public int compareTo(TreeNode other) {
        return Integer.compare(this.frequency, other.frequency);
    }
    
    /**
     * String representation of the node.
     * @return string representation
     */
    public String toString() {
        if (isLeaf()) {
            return "Leaf: value=" + value + ", freq=" + frequency;
        } else {
            return "Internal: freq=" + frequency;
        }
    }
}
