/*
 UFSCar Sorocaba - 2015/2
 Laboratório de Redes de Computadores - Prof. Fábio
    
 Filipe Santos Rocchi 552194
 Rafael Brandão barbosa Fairbanks 552372
 */
package RIP;

import java.util.ArrayList;
import java.util.HashMap;

public class NodeList {
    private ArrayList<Integer> idList; 
    private HashMap<Integer, Boolean> isDirReachable; // se é diretamente alcançável por id
    private HashMap<Integer, Integer> cost; // qual o custo por id
    
    public NodeList(ArrayList<Integer> i){
	idList = i;
    }
    
    public void addReachable(Integer id){
	isDirReachable.put(id, true);
    }
    
    public Boolean getReachable(Integer id){
	return isDirReachable.get(id);
    }
    
    public void updateCost(Integer id, Integer newCost){
	cost.put(id, newCost);
    }
    
    public Integer getCost(Integer id){
	return cost.get(id);
    }
    
}
