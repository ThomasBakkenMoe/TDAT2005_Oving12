import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
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
                if (aReturnList.data == (char) byteData[i]) {
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

    public void compress(String file) throws Exception{
        FileInputStream fileInput;
        DataInputStream dataInput;
        byte[] byteData;
        ArrayList<Character> charList= new ArrayList<>();
        try{
            fileInput = new FileInputStream(file);
            dataInput = new DataInputStream(new BufferedInputStream(fileInput));

        }catch (FileNotFoundException e){
            throw new Exception("File not found!\nError message:\n" + e);
        }

        byteData = new byte[fileInput.available()];

        dataInput.read(byteData);

        ArrayList<Node> treeList = findFrequency(byteData);

        Node[] leafNodeArray = treeList.toArray(new Node[treeList.size()]);

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
            System.out.println(Arrays.toString(treeList.toArray()));
        }
    }

    public static void main(String[] args) throws Exception{
        HuffmanCompressor huffmanCompressor = new HuffmanCompressor();
        huffmanCompressor.compress("src/opg12.txt");
    }
}

