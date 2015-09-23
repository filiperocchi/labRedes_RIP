/*
 UFSCar Sorocaba - 2015/2
 Laboratório de Redes de Computadores - Prof. Fábio
    
 Filipe Santos Rocchi 552194
 Rafael Brandão Barbosa Fairbanks 552372
 */

package RIP;

class Line {
	public Integer idNode; // id do no cujo custo está ligado
	private Integer cost; //custo do hop
	private Integer idNextHop; // hop atraves de que no chega no idNode
	
	public Line(Integer in, Integer c, Integer irh){
		idNode = in;
		cost = c;
		idNextHop = irh;
		System.out.println("Linha inserida: idNode: "+idNode+", custo: "+cost+", idHop: "+idNextHop);
	}
	
	public void updateCost(Integer c, Integer irh){
		cost = c;
		idNextHop = irh;
	}
	
	public Integer getCost(){
		return cost;
	}
	
	public Integer getNext(){
		return idNextHop;
	}
	
	public void printLine(){
		System.out.println(" "+idNode+"     "+cost+"       "+idNextHop);
	}
}
