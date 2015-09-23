/*
 UFSCar Sorocaba - 2015/2
 Laboratório de Redes de Computadores - Prof. Fábio
    
 Filipe Santos Rocchi 552194
 Rafael Brandão Barbosa Fairbanks 552372
 */

package RIP;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Node extends Thread{
	
	private static final Boolean debug = false;
	private static final Boolean clean = true;
	
	public static final ArrayList<Node> nodeList = new ArrayList<Node>();
	public static final Integer NUM_NODES = 4;
	
	private static final Object printLock = new Object();
	
	
	public final Integer id;
	private final HashMap<Integer, Line> table; // id do nó ligado na tabela
	
	private final Queue<Message> msgsToDo;
	
	private Integer clock;
	
	public Node(Integer i){	
		
		id = i;
		
		table = new HashMap<Integer, Line>();
		
		msgsToDo = new ConcurrentLinkedQueue<Message>();
		
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
			if(n.id != this.id && table.get(n.id)!=null){ // dos que forem diretamente conectados
				//n.receiveTable(this.id, this.table); // envia a própria tabela
				n.receiveMsg(new Message(this.id, this.table));
			}
		}
	}
	
	public void receiveMsg(Message m)
	{
		msgsToDo.add(m);
	}
	
	public void processMsg(Message m){ // receber custo do caminho mais curto de idSender para todos os outros nós
		
		if(m == null) throw new RuntimeException();
		
		Integer idSender = m.idSender;
		HashMap<Integer, Line> t = m.msgTable;
		
		Boolean changed = false;
		
		for (Map.Entry<Integer, Line> entry : t.entrySet())
		{
			if(debug) System.out.println(id+" - recebendo nó do "+idSender);

			if(table.get(entry.getKey()) != null) // se ele existir na minha tabela já
			{
				if(debug) System.out.println(id+" - nó "+entry.getKey()+" já existia na minha tabela");

				Line original = table.get(entry.getKey());
				
				Line nova = entry.getValue();

				// compara o custo já existente com o custo que veio na msg+custo até quem enviou
				if(original.getCost() > 
				   nova.getCost() + 
				   table.get(idSender) //////////////
						   .getCost())
				{
					if(debug) System.out.println(id+" - atualizando custo do no "+entry.getKey());
					// se for melhor o novo, atualiza

					//System.out.println(id+" - nova getCost: "+nova.getCost());
					original.updateCost(nova.getCost() + table.get(idSender).getCost(), idSender);
					
					changed = true;
				}

			}
			
			else
			{
				if(debug) System.out.println(id+" - adicionando o nó "+entry.getKey()+" que não conhecia");
				
				table.put(entry.getKey(), new Line(entry.getKey(), entry.getValue().getCost() + table.get(idSender).getCost(), idSender));
				
				changed = true;
			}
		}
		
		if(changed)
		{
			sendTable();
			printTable();
			clock++;
		}
	}
	
	public void printTable(){
		
		synchronized(printLock)
		{
			System.out.println("\nImprimindo tabela do nó: "+id+", clock:"+clock);
			System.out.println("no | custo | proximo");

			for(int i=0; i<NUM_NODES; i++){
				if(table.get(i)!=null){
					table.get(i).printLine();
				}
			}
			System.out.println();
		}
	}
	
	@Override public void run()
	{
		if(!clean) System.out.println("Node "+id+" começou");
		
		// envia custos uma vez após inicialização
		sendTable();
		printTable();
		
		while(true)
		{
			Message m = msgsToDo.poll();
			
			while(m != null)
			{
				processMsg(m);
				m = msgsToDo.poll();
			}
		}
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
