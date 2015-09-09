package com.sogeti.webshop.dao;

import java.util.List;

import com.sogeti.webshop.model.Category;
import com.sogeti.webshop.model.Orders;
import com.sogeti.webshop.model.Product;

public interface ProductDAO {

	public List<Category> getCategories();

	public List<Product> getProducts(Integer categoryId);

	public Orders addOrder(Orders order);
	
	public void addCategory(Category category);
	
	public void removeCategory(Category category);
	
	public Category getCategory(Integer categoryId);
	
	public void addProduct(Product product);
		public void removeProduct(Product product);
}
