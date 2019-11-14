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

    public void compress(String file){

    }

    public static void main(String[] args) {

    }
}

