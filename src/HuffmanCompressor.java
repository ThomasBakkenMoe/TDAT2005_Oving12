import java.io.*;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;

public class HuffmanCompressor {

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
            return data + ": " + frequency;
        }
    }

    private ArrayList<Node> findFrequency(byte[] byteData){
        ArrayList<Node> returnList = new ArrayList<>();

        boolean foundInReturn;

        for (int i = 0; i < byteData.length; i++) {
            foundInReturn = false;

            for (Node aReturnList : returnList) {
                if (aReturnList.data == byteData[i]) {
                    aReturnList.frequency++;
                    foundInReturn = true;
                    break;
                }
            }

            if (!foundInReturn){
                returnList.add(new Node(byteData[i], 1));
            }

        }

        Collections.sort(returnList);
        return returnList;
    }

    public void compress(String inputFile, String outputFile) throws Exception{
        FileInputStream fileInput;
        DataInputStream dataInput;
        byte[] byteData;
        try{
            fileInput = new FileInputStream(inputFile);
            dataInput = new DataInputStream(new BufferedInputStream(fileInput));

        }catch (FileNotFoundException e){
            throw new Exception("File not found!\nError message:\n" + e);
        }

        DataOutputStream dataOutput = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(outputFile)));

        byteData = new byte[fileInput.available()];

        dataInput.read(byteData);

        dataInput.close();

        ArrayList<Node> treeList = findFrequency(byteData);

        Node[] leafNodeArray = treeList.toArray(new Node[treeList.size()]);

        Node root;

        while (treeList.size() > 1){

            Node node1 = treeList.get(treeList.size()-2);
            Node node2 = treeList.get(treeList.size()-1);

            Node localRoot = new Node((byte)0, node1.getFrequency() + node2.getFrequency());

            localRoot.setLeft(node1);
            localRoot.setRight(node2);
            node1.setRoot(localRoot);
            node2.setRoot(localRoot);

            treeList.remove(treeList.size()-2);
            treeList.remove(treeList.size()-1);

            treeList.add(localRoot);

            Collections.sort(treeList);
        }

        root = treeList.get(0);

        //Generer bitsekvenser

        String[] bitSequenceArray = new String[leafNodeArray.length];

        Node currentNode;
        Node localRoot;

        for (int i = 0; i < bitSequenceArray.length; i++) {
            currentNode = leafNodeArray[i];
            bitSequenceArray[i] = "";

            while (currentNode.getRoot() != null){
                localRoot = currentNode.getRoot();

                if (localRoot.getLeft() == currentNode){
                    bitSequenceArray[i] = "0" + bitSequenceArray[i];
                    System.out.println("Generated 0");
                }else{
                    bitSequenceArray[i] = "1" + bitSequenceArray[i];
                    System.out.println("Generated 1");
                }
                currentNode = localRoot;
            }
        }

        for (int i = 0; i < bitSequenceArray.length; i++) {
            System.out.print((char) leafNodeArray[i].data);
            System.out.println(bitSequenceArray[i]);
        }


        //Generer bitString

        String bitString = "";
        StringBuilder sb = new StringBuilder();

        byte currentByte;

        for (int i = 0; i < byteData.length; i++) {
            currentByte = byteData[i];

            //find compressed value for current byte
            for (int j = 0; j < leafNodeArray.length; j++) {
                if (currentByte == leafNodeArray[j].getData()){
                    sb.append(bitSequenceArray[j]);
                    break;
                }
            }
        }

        bitString = sb.toString();

        //Konverter tekst

        BitSet bitSet = new BitSet();

        System.out.println("bitstring length: " + bitString.length());

        for (int i = 0; i < bitString.length(); i++) {
            if (bitString.charAt(i) == '0'){
                bitSet.set(i, false);
            }else {
                bitSet.set(i, true);
            }
        }

        System.out.println(bitSet.toString());

        // Export

        // Writes headerint to file
        dataOutput.writeInt(leafNodeArray.length);

        // Writes the frequency table to file
        for (Node node: leafNodeArray) {
            dataOutput.writeByte(node.data);
            dataOutput.writeInt(node.frequency);
        }

        byte[] exportByteArray = bitSet.toByteArray();
        for (byte b: exportByteArray) {
            dataOutput.writeByte(b);
            System.out.println("Byte written: " + b);
        }
        dataOutput.close();

        System.out.println("Done!");
    }

    public static void main(String[] args) throws Exception{
        HuffmanCompressor huffmanCompressor = new HuffmanCompressor();
        huffmanCompressor.compress("src/opg12.txt", "src/compressed.txt");
    }
}