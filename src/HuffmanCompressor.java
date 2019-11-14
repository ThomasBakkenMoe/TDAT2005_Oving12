import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class HuffmanCompressor {


    private class Node{

        char character;
        int frequency;
        Node left = null;
        Node right = null;

        Node(char character, int frequency){

            this.character = character;
            this.frequency = frequency;

        }

        void setLeft(Node left){
            this.left = left;
        }

        void setRight(Node right){
            this.right = right;
        }

        Node getLeft(){
            return left;
        }

        Node getRight(){
            return right;
        }
    }

    private void findFrequency(String file){

    }

    public void compress(String file) throws Exception{

        try{

            DataInputStream fileInput = new DataInputStream(new BufferedInputStream(new FileInputStream(file)));

        }catch (FileNotFoundException e){
            throw new Exception("File not found!\nError message:\n" + e);
        }





    }

    public static void main(String[] args) throws Exception{
        HuffmanCompressor huffmanCompressor = new HuffmanCompressor();
        huffmanCompressor.compress("");
    }
}

