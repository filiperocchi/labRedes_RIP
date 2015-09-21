/*
 UFSCar Sorocaba - 2015/2
 Laboratório de Redes de Computadores - Prof. Fábio
    
 Filipe Santos Rocchi 552194
 Rafael Brandão Barbosa Fairbanks 552372
 */
package RIP;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Node {

	private Integer id;
	private static int MAX_NODES = 4; // numero de nodes corrente
	private NodeList nodeList = new NodeList(MAX_NODES);
	
	private int round;
	
	public Node(int i){
		id = new Integer(i);
		
		round=0;
		
		System.out.println("Node "+id+" created.");
	}

	public void init() {
		System.out.println("Node "+id+" init...");
		
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
		if (id == 3) {
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
		
		imprime(round); round++;
	}

	public void update() throws IOException {
		// seriam os deuses astronautas?
		System.out.println("Update do "+id);
		// seriam os sockets colocados aqui?
		
		ServerSocket Server = new ServerSocket(9001);
		Socket serviceSocket = Server.accept();
		DataInputStream inputServer = new DataInputStream(serviceSocket.getInputStream());
		DataOutputStream outputServer = new DataOutputStream(serviceSocket.getOutputStream());
		
		Socket Client = new Socket("client"+id, 9001);
		DataInputStream inputClient = new DataInputStream(Client.getInputStream());
		DataOutputStream outputClient = new DataOutputStream(Client.getOutputStream());
		
		
		// é aqui que cria threads para o cliente e servidor
		// ambos devem operar independentes
		
		// Thread para cliente: enviar mensagens
		new Thread() {
			@Override public void run() {
				while(true){
					try {
						System.out.println("Tentando enviar mensagem");
						sendMessage(outputClient);  // ENVIA MENSAGEM
					} catch (IOException ex) {
						Logger.getLogger(Node.class.getName()).log(Level.SEVERE, null, ex);
					}
				}
			}
		}.start();
		
		// Thread para servidor: receber mensagens
		new Thread() {
			@Override public void run() {
				while(true){
					try {
						System.out.println("Tentando receber mensagem");
						receiveMessage(inputServer); // RECEBE MENSAGEM
					} catch (IOException ex) {
						Logger.getLogger(Node.class.getName()).log(Level.SEVERE, null, ex);
					}
				}
			} 
		}.start();
		
	}
	
	
	public void sendMessage(DataOutputStream os) throws IOException{
		int i;
		
		synchronized(this){
			for(i=0; i<MAX_NODES; i++){
				if(nodeList.getReachable(i)){
					Integer cost = nodeList.getCost(i);
					
					System.out.println("Mandando: "+i+","+cost+","+id);
					
					os.writeBytes(i+","+cost+","+id);
				}
			}
		}
	
	}
	
	public void receiveMessage(DataInputStream is) throws IOException{
		String line = is.readLine();
		
		System.out.println("Chegou: "+line);
		
		Integer idNodeMsg = (int )line.charAt(0);
		Integer costNodeMsg = (int )line.charAt(2);
		Integer idRemetente = (int )line.charAt(4);
		
		synchronized(this){
			if(nodeList.getReachable(idRemetente)){ // limita aos remetentes alcancaveis
				if(nodeList.getReachable(idNodeMsg)){ // se quem chegou na msg for alcancavel
					// compara os valores do custo existente com o novo
					if(nodeList.getCost(idNodeMsg) > costNodeMsg+nodeList.getCost(idRemetente)){
						nodeList.updateCost(idNodeMsg, costNodeMsg+nodeList.getCost(idRemetente));
					}
				}
				else{ // se nao for alcancavel, adiciona como alcancavel e com o novo custo
					nodeList.addReachable(idNodeMsg);
					nodeList.updateCost(idNodeMsg, costNodeMsg+nodeList.getCost(idRemetente));
				}
			}
		}
		
		imprime(round); round++;
		
	}
	
	public void imprime(int r) {
		System.out.print(
			"round "+r+"||------- custos -> nós ---------|\n"
			  + "| nó   ||   0   |   1   |   2   |   3   |\n"
			  + "|------||-------------------------------|\n"
			  + "|   "+id+"  ||");
		
		for(int i=0; i<MAX_NODES; i++){
			if(nodeList.getReachable(i)){
				System.out.print("   "+nodeList.getCost(i)+"   |");
			}
			else{
				System.out.print("  inf  |");
			}
		}
		System.out.println("");
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
