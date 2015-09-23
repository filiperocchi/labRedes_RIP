/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RIP;

import java.util.HashMap;

/**
 *
 * @author Administrator
 */
public class Message
{
	public Integer idSender;
	public HashMap<Integer, Line> msgTable;
	
	Message(Integer i, HashMap<Integer, Line> t)
	{
		idSender = new Integer(i);
		msgTable = new HashMap<>();
		msgTable.putAll(t);
	}
}
