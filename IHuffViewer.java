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
 * Interface for viewing progress and error messages during Huffman operations.
 * This allows the processor to communicate with a GUI or other display mechanism.
 */
public interface IHuffViewer {
    
    /**
     * Update the viewer with a progress message.
     * @param s the message to display
     */
    public void update(String s);
    
    /**
     * Display an error message to the user.
     * @param s the error message to display
     */
    public void showError(String s);
}
