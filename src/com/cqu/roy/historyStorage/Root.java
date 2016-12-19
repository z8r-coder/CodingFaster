package com.cqu.roy.historyStorage;

import java.util.ArrayList;

//版本数的根节点
public class Root {
	private ArrayList<Node> subnodeSet;//每一行就是一个子节点
	public Root() {
		// TODO Auto-generated constructor stub
		subnodeSet = new ArrayList<>();
	}
	public ArrayList<Node> getSubNodeSet() {
		return subnodeSet;
	}
}
