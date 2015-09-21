/*
    UFSCar Sorocaba - 2015/2
    Laboratório de Redes de Computadores - Prof. Fábio
    
    Filipe Santos Rocchi 552194
    Rafael Brandão Barbosa Fairbanks 552372
 */

package RIP;

import java.io.IOException;
import java.util.ArrayList;

public class RIP {
	
	static int NUM_NODES=4;

    public static void main(String[] args) throws IOException {
        // cria os nós, threads e inicia
		ArrayList<Node> nodes = new ArrayList<>();
		Node node = null;
		
        int i;
		for(i=0; i<NUM_NODES; i++){
			node = new Node(i);
			node.init();
			nodes.add(node);
		}
		
		for(Node n : nodes){
			n.update();
		}
		
    }
    
}