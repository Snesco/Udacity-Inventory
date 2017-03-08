package com.steven.inventory;

/**
 * Created by steven on 3/2/17.
 */

public class Product {

	private final int id;
	private final String name;
	private final float price;
	private final int quantity;
	private final String supplier;

	public Product(int id, String name, float price, int quantity, String supplier) {
		this.id = id;
		this.name = name;
		this.price = price;
		this.quantity = quantity;
		this.supplier = supplier;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public float getPrice() {
		return price;
	}

	public int getQuantity() {
		return quantity;
	}

	public String getSupplier() {
		return supplier;
	}
}
