package com.sogeti.webshop.userweb.backingbean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import org.primefaces.event.RowEditEvent;

import com.sogeti.webshop.business.ProductService;
import com.sogeti.webshop.model.Category;
import com.sogeti.webshop.model.Product;

/**
 * @author mpattabh
 *
 *         This class handles all the product related operations in the admin
 *         module. This includes display and add products, AJAX calls for edit
 *         and update
 */
@ManagedBean(name = "productBean")
@SessionScoped
public class ProductManagedBean implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * EJB call which handles all the business operations
	 */
	@Inject
	ProductService productService;

	private Integer productId;

	private String selectedCategoryName;

	private String selectedCategory;

	private String name;

	private String description;

	private String price;

	private List<Product> productEnitites = new ArrayList<Product>();

	private List<Product> products = new ArrayList<Product>();

	/**
	 * This method displays the product page where the user can add,edit and
	 * remove the products
	 * 
	 * @return productDisplay
	 */
	public String doDisplay() {
		String selectedCategory = this.selectedCategory;
		Category category = productService.getCategory(Integer
				.valueOf(this.selectedCategory));
		this.selectedCategoryName = category.getName();
		List<Product> productEntityList = productService.getProducts(Integer
				.valueOf(selectedCategory));
		this.products.clear();
		this.products.addAll(productEntityList);
		return "addProduct.xhtml";

	}

	/**
	 * This method adds the product details entered by the user to the DB
	 * 
	 * @return productDisplay
	 */
	public String addAction() {
		Product product = new Product();
		product.setName(this.name);
		product.setDescription(this.description);
		product.setPrice(this.price);
		Category category = productService.getCategory(Integer
				.valueOf(this.selectedCategory));
		product.setCategory(category);
		productService.addProduct(product);
		List<Product> productEntityList = productService.getProducts(Integer
				.valueOf(selectedCategory));
		this.products.clear();
		this.products.addAll(productEntityList);

		this.name = "";
		this.price = "";
		this.description = "";
		this.selectedCategoryName = category.getName();
		return null;
	}

	/**
	 * This method handles the AJAX call to edit a particular product.This
	 * method calls the business layer to update the DB
	 * 
	 * @param event
	 *            - details of the edited row
	 */
	public void onEdit(RowEditEvent event) {
		Product product = (Product) event.getObject();
		productService.addProduct(product);
		List<Product> productEntityList = productService.getProducts(Integer
				.valueOf(selectedCategory));
		this.products.clear();
		this.products.addAll(productEntityList);
		FacesMessage msg = new FacesMessage("Product Edited",
				((Product) event.getObject()).getName());
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	/**
	 * This method handles the AJAX call to delete a particular product.This
	 * method calls the business layer to update the DB
	 * 
	 * @param event
	 *            - details of the deleted row
	 */

	public void onCancel(RowEditEvent event) {
		FacesMessage msg = new FacesMessage("Product Deleted");
		Product product = (Product) event.getObject();
		productService.removeProduct(product);
		List<Product> productEntityList = productService.getProducts(Integer
				.valueOf(selectedCategory));
		this.products.clear();
		this.products.addAll(productEntityList);
		FacesContext.getCurrentInstance().addMessage(null, msg);
		productEnitites.remove((Product) event.getObject());
	}

	public List<Product> getProducts() {
		return products;
	}

	public void setProducts(List<Product> products) {
		this.products = products;
	}

	public String getSelectedCategoryName() {
		return selectedCategoryName;
	}

	public void setSelectedCategoryName(String selectedCategoryName) {
		this.selectedCategoryName = selectedCategoryName;
	}

	public List<Product> getProductEnitites() {
		return this.products;
	}

	public void setProductEnitites(List<Product> productEnitites) {
		this.productEnitites = productEnitites;
	}

	public String getSelectedCategory() {
		return selectedCategory;
	}

	public void setSelectedCategory(String selectedCategory) {
		this.selectedCategory = selectedCategory;
	}

	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}