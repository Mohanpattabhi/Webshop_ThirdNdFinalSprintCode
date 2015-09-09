package com.sogeti.webshop.userweb.backingbean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
 *         This class handles all the category related operations in the admin
 *         module. This includes display categories, AJAX calls for edit and
 *         update
 */
@ManagedBean(name = "categoryBean")
@SessionScoped
public class CategoryManagedBean implements Serializable {

	/**
	 * EJB call which handles all the business operations
	 */
	@Inject
	ProductService productService;

	private static final long serialVersionUID = 1L;
	private String name;
	private String code;
	private String description;
	public static final List<Category> categoryList = new ArrayList<Category>();
	private List<Category> categoryEntities = new ArrayList<Category>();
	private Map<String, String> categoryIdMap = new LinkedHashMap<String, String>();;
	private String selectedCategory;
	private List<Product> products = new ArrayList<Product>();

	Category category;

	/**
	 * @return List<Category>
	 */
	public static List<Category> getCategorylist() {
		return categoryList;
	}

	/**
	 * Inititalize category list for display
	 */
	public void doInit() {
		List<Category> categories = productService.getCategories();
		categoryList.clear();
		categoryList.addAll(categories);
	}

	/**
	 * prepare categoryIdMap for dropdown display
	 * 
	 * @return Map<String, String>
	 */
	public Map<String, String> getCategoryIdMap() {
		List<Category> categories = productService.getCategories();
		categoryList.clear();
		categoryIdMap.clear();
		categoryIdMap = new LinkedHashMap<String, String>();

		for (Category category : categories) {
			categoryIdMap.put(category.getName(),
					String.valueOf(category.getCategoryId()));
		}
		return categoryIdMap;

	}

	/**
	 * This method adds the category to the DB
	 * 
	 * @return outCome
	 */
	public String addAction() {
		Category category = new Category();
		category.setName(this.name);
		category.setDescription(this.description);
		category.setCode(this.code);
		productService.addCategory(category);
		List<Category> categories = productService.getCategories();
		categoryList.clear();
		categoryList.addAll(categories);

		name = "";
		code = "";
		description = "";
		return null;
	}

	/**
	 * This method handles the AJAX call to edit a particular category.This
	 * method calls the business layer to update the DB
	 * 
	 * @param event
	 *            - details of the edited row
	 */
	public void onEdit(RowEditEvent event) {
		Category category = (Category) event.getObject();
		productService.addCategory(category);
		List<Category> categories = productService.getCategories();
		categoryList.clear();
		categoryList.addAll(categories);
		FacesMessage msg = new FacesMessage("Category Edited",
				((Category) event.getObject()).getName());
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	/**
	 * This method handles the AJAX call to delete a particular category.This
	 * method calls the business layer to update the DB
	 * 
	 * @param event
	 *            - details of the d row
	 */

	public void onCancel(RowEditEvent event) {
		FacesMessage msg = new FacesMessage("Category Deleted");
		Category category = (Category) event.getObject();
		productService.removeCategory(category);
		List<Category> categories = productService.getCategories();
		categoryList.clear();
		categoryList.addAll(categories);
		FacesContext.getCurrentInstance().addMessage(null, msg);
		categoryList.remove((Category) event.getObject());
	}

	public List<Product> getProducts() {
		return products;
	}

	public void setProducts(List<Product> products) {
		this.products = products;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public void setCategoryIdMap(Map<String, String> categoryIdMap) {
		this.categoryIdMap = categoryIdMap;
	}

	public String getSelectedCategory() {
		return selectedCategory;
	}

	public void setSelectedCategory(String selectedCategory) {
		this.selectedCategory = selectedCategory;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public List<Category> getCategoryEntities() {
		return this.categoryList;
	}

	public void setCategoryEntities(List<Category> categoryEntities) {
		this.categoryEntities = categoryEntities;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}