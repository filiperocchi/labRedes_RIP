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

	public static void main(String[] args) throws InterruptedException {
		for(int i = 0; i < Node.NUM_NODES; i++){
			Node.nodeList.add(new Node(i));
		}
		
		for(Node n: Node.nodeList){
			n.init();
		}
		
		System.out.println("\nMAIN: Starting Nodes.");
		for(Node n : Node.nodeList){
			n.start();
		}
		
		for(Node n : Node.nodeList){
			n.join();
		}
		
		Thread.sleep(1000);
	}

}
