package com.arlen.common.paramtype;

import java.util.ArrayList;
import java.util.List;

public class Basket<T> {

	private List<T> goodsList = new ArrayList<T>();
	
	public void put(T goods) {
		goodsList.add(goods);
	}
	
	public List<T> getGoodsList() {
		return goodsList;
	}
}
