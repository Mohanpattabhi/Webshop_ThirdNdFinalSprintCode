package com.sogeti.webshop.business.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;

import com.sogeti.webshop.business.ProductService;
import com.sogeti.webshop.dao.ProductDAO;
import com.sogeti.webshop.dao.RegistrationDAO;
import com.sogeti.webshop.model.Category;
import com.sogeti.webshop.model.Orders;
import com.sogeti.webshop.model.Product;
import com.sogeti.webshop.model.User;

@Stateless
public class ProductServiceImpl implements ProductService {

	@Inject
	ProductDAO productDAO;;

	@Inject
	RegistrationDAO registrationDAO;

	@Transactional
	public List<Category> getCategories() {
		List<Category> categories = productDAO.getCategories();
		for (Category category : categories) {

			if (category.getProducts() != null) {
				List<Product> products = category.getProducts();
				System.out.println("Category is null  >>>>" + products.size());
			} else {
				System.out.println("Category is null");
			}

		}
		return categories;
	}

	@Transactional
	public List<Product> getProducts(Integer categoryId) {
		List<Product> products = productDAO.getProducts(categoryId);
		return products;
	}

	@Transactional
	public Orders createOrders(Orders order) {
		User user = order.getUser();
		User userEntity = registrationDAO.getUserByUserName(user.getUserName());
		order.setShippingAddress(userEntity.getAddress());
		order.setShippingCity(userEntity.getCity());
		order.setUser(userEntity);
		Orders orders = productDAO.addOrder(order);
		return orders;
	}

	@Transactional
	public void addCategory(Category category) {
		productDAO.addCategory(category);
		
	}

	@Transactional
	public void removeCategory(Category category) {
		productDAO.removeCategory(category);
		
	}

	@Transactional
	public Category getCategory(Integer categoryId) {
		Category category = productDAO.getCategory(categoryId);
		return category;
	}

	@Transactional
	public void addProduct(Product product) {
		productDAO.addProduct(product);
		
	}

	@Transactional
	public void removeProduct(Product product) {
		productDAO.removeProduct(product);
		
	}

}
