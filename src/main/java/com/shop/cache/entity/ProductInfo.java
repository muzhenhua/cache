package com.shop.cache.entity;

public class ProductInfo {

	private Long id;
	private String name;
	private Double price;
	private String color;
	private String size;

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}


	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}


	@Override
	public String toString() {
		return "ProductInfo [id=" + id + ", name=" + name + ", price=" + price
				+ ", color=" + color
				+ ", size=" + size + "]";
	}

}
