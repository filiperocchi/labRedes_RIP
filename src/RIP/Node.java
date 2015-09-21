/*
 UFSCar Sorocaba - 2015/2
 Laboratório de Redes de Computadores - Prof. Fábio
    
 Filipe Santos Rocchi 552194
 Rafael Brandão barbosa Fairbanks 552372
 */
package RIP;

import java.util.ArrayList;
import java.util.HashMap;

public class Node {

	private Integer id;
	private NodeList nodeList;

	public void init() {
		
		// rotina de inicio do no 0
		if (id == 0) {
			// no 0
			nodeList.addReachable(id);
			nodeList.updateCost(id, 0);
			
			// no 1
			nodeList.addReachable(1);
			nodeList.updateCost(1, 1);
			
			// no 2
			nodeList.addReachable(2);
			nodeList.updateCost(2, 3);
			
			// no 3
			nodeList.addReachable(3);
			nodeList.updateCost(3, 7);
			
		}
		
		// rotina de inicio do no 1
		if (id == 1) {
			// no 0
			nodeList.addReachable(0);
			nodeList.updateCost(0, 1);
			
			// no 1
			nodeList.addReachable(id);
			nodeList.updateCost(id, 0);
			
			// no 2
			nodeList.addReachable(2);
			nodeList.updateCost(2, 1);
			
			// no 3
			//nao alcança
			
		}
		
		// rotina de inicio do no 2
		if (id == 2) {
			// no 0
			nodeList.addReachable(0);
			nodeList.updateCost(0, 3);
			
			// no 1
			nodeList.addReachable(1);
			nodeList.updateCost(1, 1);
			
			// no 2
			nodeList.addReachable(id);
			nodeList.updateCost(id, 0);
			
			// no 3
			nodeList.addReachable(3);
			nodeList.updateCost(3, 2);
			
		}
		
		// rotina de inicio do no 3
		if (id == 2) {
			// no 0
			nodeList.addReachable(0);
			nodeList.updateCost(0, 7);
			
			// no 1
			// nao alcança
			
			// no 2
			nodeList.addReachable(2);
			nodeList.updateCost(2, 2);
			
			// no 3
			nodeList.addReachable(id);
			nodeList.updateCost(id, 0);
			
		}
		
	}

	public void update() {
		
	}
}

// Modelo de impressão
/*
System.out.println(
                "round x||------- custos -> nós ---------|\n" +
                "| nós  ||   0   |   1   |   2   |   3   |\n" +
                "|------||-------------------------------|\n" +
                "|   0  ||   x   |   x   |   x   |   x   |\n" +
                "|------||-------------------------------|\n" +
                "|   1  ||   x   |   x   |   x   |   x   |\n" +
                "|------||-------------------------------|\n" +
                "|   2  ||   x   |   x   |   x   |   x   |\n" +
                "|------||-------------------------------|\n" +
                "|   3  ||   x   |   x   |   x   |   x   |\n" +
                "|------||-------------------------------|\n");
*/
