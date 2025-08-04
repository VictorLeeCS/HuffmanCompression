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

import java.io.*;

/**
 * Simple test class to verify the Huffman compression implementation.
 * Tests basic compression and decompression functionality.
 */
public class HuffmanTest {
    
    public static void main(String[] args) {
        try {
            // Create a test file with lots of repetition to show real compression
            StringBuilder testDataBuilder = new StringBuilder();
            
            // Add highly repetitive content that will compress well
            for (int i = 0; i < 100; i++) {
                testDataBuilder.append("AAAAAAAAAA");  // 10 A's
                testDataBuilder.append("BBBBBBBBBB");  // 10 B's  
                testDataBuilder.append("CCCCCCCCCC");  // 10 C's
                testDataBuilder.append("          ");  // 10 spaces
            }
            
            // Add some variety but still repetitive
            for (int i = 0; i < 50; i++) {
                testDataBuilder.append("The quick brown fox jumps over the lazy dog. ");
            }
            
            String testData = testDataBuilder.toString();
            
            // Write test data to a file
            FileOutputStream fos = new FileOutputStream("test_input.txt");
            fos.write(testData.getBytes());
            fos.close();
            
            // Test compression
            System.out.println("Testing Huffman Compression...");
            
            SimpleHuffProcessor processor = new SimpleHuffProcessor();
            processor.setViewer(new SimpleViewer());
            
            // Test compression with STORE_TREE format (smaller header)
            System.out.println("\n=== Testing with STORE_TREE format ===");
            
            SimpleHuffProcessor processor2 = new SimpleHuffProcessor();
            processor2.setViewer(new SimpleViewer());
            
            // Preprocess for compression
            FileInputStream fis2 = new FileInputStream("test_input.txt");
            int bitsSaved2 = processor2.preprocessCompress(fis2, IHuffConstants.STORE_TREE);
            System.out.println("Bits saved with tree format: " + bitsSaved2);
            
            // Compress the file
            fis2 = new FileInputStream("test_input.txt");
            FileOutputStream compressedOut2 = new FileOutputStream("test_compressed_tree.hf");
            int bitsWritten2 = processor2.compress(fis2, compressedOut2, true);
            System.out.println("Compressed file size with tree format (bits): " + bitsWritten2);
            
            // Test both formats
            System.out.println("\n=== Testing with STORE_COUNTS format ===");
            
            // Preprocess for compression
            FileInputStream fis = new FileInputStream("test_input.txt");
            int bitsSaved = processor.preprocessCompress(fis, IHuffConstants.STORE_COUNTS);
            System.out.println("Bits saved: " + bitsSaved);
            
            // Compress the file
            fis = new FileInputStream("test_input.txt");
            FileOutputStream compressedOut = new FileOutputStream("test_compressed.hf");
            int bitsWritten = processor.compress(fis, compressedOut, true);
            System.out.println("Compressed file size (bits): " + bitsWritten);
            
            // Test decompression
            FileInputStream compressedIn = new FileInputStream("test_compressed.hf");
            FileOutputStream decompressedOut = new FileOutputStream("test_decompressed.txt");
            int decompressedBits = processor.uncompress(compressedIn, decompressedOut);
            System.out.println("Decompressed file size (bits): " + decompressedBits);
            
            // Verify the decompressed file matches original
            String originalContent = readFile("test_input.txt");
            String decompressedContent = readFile("test_decompressed.txt");
            
            if (originalContent.equals(decompressedContent)) {
                System.out.println("SUCCESS: Decompressed file matches original!");
            } else {
                System.out.println("ERROR: Decompressed file does not match original!");
            }
            
            // Clean up test files
            new File("test_input.txt").delete();
            new File("test_compressed.hf").delete();
            new File("test_compressed_tree.hf").delete();
            new File("test_decompressed.txt").delete();
            
        } catch (Exception e) {
            System.err.println("Error during testing: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static String readFile(String filename) throws IOException {
        StringBuilder content = new StringBuilder();
        FileInputStream fis = new FileInputStream(filename);
        int b;
        while ((b = fis.read()) != -1) {
            content.append((char) b);
        }
        fis.close();
        return content.toString();
    }
    
    /**
     * Simple implementation of IHuffViewer for testing.
     */
    static class SimpleViewer implements IHuffViewer {
        public void update(String s) {
            System.out.println("Update: " + s);
        }
        
        public void showError(String s) {
            System.err.println("Error: " + s);
        }
    }
}
