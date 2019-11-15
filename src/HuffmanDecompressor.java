import java.io.*;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;

public class HuffmanDecompressor {

    private class Node implements Comparable<Node> {

        private byte data;
        private int frequency;
        Node root = null;
        Node left = null;
        Node right = null;

        Node(byte data, int frequency){

            this.data = data;
            this.frequency = frequency;

        }

        public byte getData() {
            return data;
        }

        public int getFrequency() {
            return frequency;
        }

        public void setData(byte data) {
            this.data = data;
        }

        public void setFrequency(int frequency) {
            this.frequency = frequency;
        }

        void setRoot(Node root){
            this.root = root;
        }

        void setLeft(Node left){
            this.left = left;
        }

        void setRight(Node right){
            this.right = right;
        }

        Node getRoot(){
            return root;
        }

        Node getLeft(){
            return left;
        }

        Node getRight(){
            return right;
        }

        @Override
        public int compareTo(Node o) {
            return Integer.compare(o.frequency, this.frequency);
        }

        @Override
        public String toString() {
            return (char)data + ": " + frequency;
        }
    }

    private Boolean isBitSet(byte b, int bit){
        return (b & (1 << 7-bit)) != 0;
    }

    public void decompress(String inputFile, String outputFile) throws Exception{

        FileInputStream fileInput;
        DataInputStream dataInput;
        try{
            fileInput = new FileInputStream(inputFile);
            dataInput = new DataInputStream(new BufferedInputStream(fileInput));

        }catch (FileNotFoundException e){
            throw new Exception("File not found!\nError message:\n" + e);
        }

        DataOutputStream dataOutput = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(outputFile)));

        ArrayList<Node> leafNodeList = new ArrayList<>();

        int headerint = dataInput.readInt();

        for (int i = 0; i < headerint; i++) {
            byte data = dataInput.readByte();
            int frequency = dataInput.readInt();

            leafNodeList.add(new Node(data, frequency));
        }

        byte[] byteData = new byte[dataInput.available()];
        dataInput.readFully(byteData);



        dataInput.close();
        BitSet bitSet = BitSet.valueOf(byteData);

        System.out.println(bitSet.toString());

        String compressedString = bitSet.toString();
        String uncompressedString = "";

        Node[] leafNodeArray = leafNodeList.toArray(new Node[leafNodeList.size()]);

        while (leafNodeList.size() > 1){

            Node node1 = leafNodeList.get(leafNodeList.size()-2);
            Node node2 = leafNodeList.get(leafNodeList.size()-1);

            Node localRoot = new Node((byte)0, node1.getFrequency() + node2.getFrequency());

            localRoot.setLeft(node1);
            localRoot.setRight(node2);
            node1.setRoot(localRoot);
            node2.setRoot(localRoot);

            leafNodeList.remove(leafNodeList.size()-2);
            leafNodeList.remove(leafNodeList.size()-1);

            leafNodeList.add(localRoot);

            Collections.sort(leafNodeList);
        }

        Node root = leafNodeList.get(0);

        Node currentNode = root;

        for (int i = 0; i < bitSet.length(); i++) {

            if (currentNode.left == null && currentNode.right == null){
                System.out.println("Found Leaf!" + currentNode.data);
                dataOutput.writeByte(currentNode.data);

                currentNode = root;
            }

            if (!bitSet.get(i)){
                currentNode = currentNode.left;
            }else {
                currentNode = currentNode.right;
            }
        }

        dataOutput.close();
    }

    public static void main(String[] args) throws Exception{

        HuffmanDecompressor huffmanDecompressor = new HuffmanDecompressor();

        huffmanDecompressor.decompress("src/compressed.txt", "src/output.txt");
    }
}
