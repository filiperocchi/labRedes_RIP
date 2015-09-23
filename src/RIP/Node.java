/*
 UFSCar Sorocaba - 2015/2
 Laboratório de Redes de Computadores - Prof. Fábio
    
 Filipe Santos Rocchi 552194
 Rafael Brandão Barbosa Fairbanks 552372
 */

package RIP;

import java.util.ArrayList;
import java.util.HashMap;

public class Node extends Thread{
	
	private static Boolean debug = false;
	private static Boolean clean = false;
	
	public static ArrayList<Node> nodeList = new ArrayList<Node>();
	public static Integer NUM_NODES = 4;
	
	private Integer id;
	private HashMap<Integer, Line> table; // id do no ligado na tabela
	
	private Boolean update; // para saber quando é necessário mandar a tabela
	
	private Integer clock;
	
	public Node(Integer i){	
		
		id = i;
		
		table = new HashMap<Integer, Line>();
		
		update = true;
		
		clock = new Integer(0);
		
		if(!clean) System.out.println("Node "+id+" criado.");
	}
	
	public void init(){
		if(!clean) System.out.println("\nnode "+id+" inserindo linhas");
		
		switch(id){
			case 0:
				table.put(0, new Line(0, 0, 0));
				table.put(1, new Line(1, 1, 1));
				table.put(2, new Line(2, 3, 2));
				table.put(3, new Line(3, 7, 3));
				break;
				
			case 1:
				table.put(0, new Line(0, 1, 0));
				table.put(1, new Line(1, 0, 1));
				table.put(2, new Line(2, 1, 2));
				break;
				
			case 2:
				table.put(0, new Line(0, 3, 0));
				table.put(1, new Line(1, 1, 1));
				table.put(2, new Line(2, 0, 2));
				table.put(3, new Line(3, 2, 3));
				break;
				
			case 3:
				table.put(0, new Line(0, 7, 0));
				table.put(2, new Line(2, 2, 2));
				table.put(3, new Line(3, 0, 3));
				break;
				
			default:
				System.out.println(id+" Node não cadastrado");
			
		}
		
		printTable();
	}
	
	public void sendTable(){
		for(Node n : nodeList){ // para os nos existentes
			if(table.get(n.id)!=null){ // dos que forem diretamente conectados
				n.receiveTable(this.id, this.table); // envia a própria tabela
			}
		}
	}
	
	public synchronized void receiveTable(Integer idSender, HashMap<Integer, Line> t){
		
		Boolean change = false;
		
		for(int i=0; i<NUM_NODES; i++){ // para todos os nós
			if(t.get(i)!=null){ // se ele existir na tabela que chegou
				if(debug) System.out.println(id+" - recebendo nó do "+idSender);
				if(table.get(i)!=null){ // se ele existir na minha tabela já
					if(debug) System.out.println(id+" - nó "+i+" já existia na minha tabela");
					Line original, nova;
					
					original = table.get(i);
					nova = t.get(i);
					
					// compara o custo já existente com o custo que veio na msg+custo até quem enviou
					if(original.getCost() > nova.getCost()+table.get(idSender).getCost()){
						if(debug) System.out.println(id+" - atualizando custo do no "+i);
						// se for melhor o novo, atualiza
						
						System.out.println(id+" - nova getCost: "+nova.getCost());
						original.updateCost(nova.getCost()+table.get(idSender).getCost(), idSender);
						change = true;
					}
					
				}
				else{
					if(debug) System.out.println(id+" - adicionando o nó "+i+" que não conhecia");
					table.put(i, new Line(i, t.get(i).getCost()+table.get(idSender).getCost(), idSender));
				}
			}
		}
		
		if(change){
			update = true;
			clock++;
		}
	}
	
	public synchronized void printTable(){
		
		System.out.println("\nImprimindo tabela do nó: "+id+", clock:"+clock);
		System.out.println("no | custo | proximo");
		
		for(int i=0; i<NUM_NODES; i++){
			if(table.get(i)!=null){
				table.get(i).printLine();
			}
		}
		System.out.println();
	}
	
	@Override public void run(){
		if(!clean) System.out.println("Node "+id+" começou");
		// Thread para enviar mensagens
		new Thread() {
			@Override public void run() {
				while(true){
					if(update==true){
						update = false;
						sendTable();
						printTable();
					}
				}
			}
		}.start();
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
