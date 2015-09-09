package com.sogeti.webshop.userweb.backingbean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import com.sogeti.webshop.business.ProductService;
import com.sogeti.webshop.business.utitlity.Constants;
import com.sogeti.webshop.model.Category;
import com.sogeti.webshop.model.Orders;
import com.sogeti.webshop.model.Product;
import com.sogeti.webshop.model.ProductOrders;
import com.sogeti.webshop.model.User;

/**
 * @author mpattabh
 *
 *         This class handles all the requests made for product related
 *         operations by the webshop user
 */
@ManagedBean(name = "productBean")
@SessionScoped
public class ProductBean implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * EJB call which handles all the business operations
	 */
	@Inject
	ProductService productService;

	private List<Product> productList;

	private List<Category> categories;

	private String value;

	private List<Product> shoppingCartList;

	@SuppressWarnings("unused")
	private List<Product> shoppingCart;

	private boolean selected;

	private double totalPrice;

	private String userName;

	private String password;

	private Orders orderEntity;

	private String selectedCategory;

	/**
	 * This method populates the categories in left hand Menu
	 */
	public void populateCategories() {
		categories = new ArrayList<Category>();

		List<Category> categoryEntities = productService.getCategories();

		setCategories(categoryEntities);

	}

	/**
	 * This method handles the call when user clicks on a particular category.
	 * Products corresponding to a particular category is displayed
	 * 
	 * @return product details page
	 */
	public String displayProduct() {

		Map<String, String> params = FacesContext.getCurrentInstance()
				.getExternalContext().getRequestParameterMap();
		String selectedCategory = params.get("selectedCategory");
		List<Product> products = productService.getProducts(Integer
				.valueOf(selectedCategory));
		setProductList(products);
		HttpServletRequest request = (HttpServletRequest) FacesContext
				.getCurrentInstance().getExternalContext().getRequest();
		request.getSession().setAttribute("productList", products);
		request.getSession().setAttribute("selectedCategoryId",
				selectedCategory);
		this.productList = products;
		return "productDetails";
	}

	/**
	 * 
	 * This method handles the shopping cart logic. When the user add a product
	 * to the shopping cart it is added after checking for duplicates
	 * 
	 */
	public void addToShoppingCart() {

		// Get selected items.
		StringBuilder messages = new StringBuilder();
		if (getShoppingCartList() == null) {
			shoppingCartList = new ArrayList<Product>();
		}
		for (Product dataItem : productList) {
			if (dataItem.isSelected()) {

				if (!isDuplicate(shoppingCartList, dataItem.getProductId())) {
					messages.append(dataItem.getName() + ",");
					shoppingCartList.add(dataItem);

				}
			}
		}
		if (messages.toString().length() > 0) {
			FacesMessage msg = new FacesMessage(
					"Following products are added to cart ", messages
							.toString().substring(0,
									messages.toString().length() - 1));
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}

	}

	/**
	 * This method creates the order after user enters the order details in the
	 * shopping cart. Then this user is redirected to invoice display page
	 */

	public String doOrder() {
		String uri = "invoiceDisplay";
		Orders order = prepareOrderEntity(this.getShoppingCart());
		Orders orderEntity = productService.createOrders(order);
		this.setOrderEntity(orderEntity);
		this.shoppingCartList.clear();
		return uri;
	}

	/**
	 * This method returns the categories to be selected by the user
	 */
	public List<Category> getCategories() {
		categories = new ArrayList<Category>();
		List<Category> categoryEntities = productService.getCategories();
		categories.addAll(categoryEntities);
		return categories;
	}

	/**
	 * This method redirects the user to login page
	 */
	public String displayOrder() {
		return "login";
	}

	/**
	 * This method is used to check for the duplicates when user enters
	 * duplicate entries in the session
	 * 
	 * @param productList
	 * @param productId
	 * @return
	 */
	public boolean isDuplicate(List<Product> productList, Integer productId) {
		Boolean dupicateValue = false;
		if (productList != null)
			if (productList.size() > 0) {
				for (Product product : productList) {
					if (productId == product.getProductId()) {
						dupicateValue = true;
					}
				}
			}
		return dupicateValue;
	}

	/**
	 * This method is used to remove duplicates from the session
	 */
	public void removeProductsFromSession() {

		StringBuilder messages = new StringBuilder();

		List<Product> newList = new ArrayList<Product>();
		if (productList != null)
			if (productList.size() > 0) {
				for (Product dataItem : shoppingCartList) {
					if (!dataItem.isSelectedForSessionRemoval()) {
						newList.add(dataItem);

					} else {
						messages.append(dataItem.getName() + ",");
					}
				}
			}
		shoppingCartList.clear();
		shoppingCartList.addAll(newList);

		if (messages.toString().length() > 0) {
			FacesMessage msg = new FacesMessage(
					"Following products are removed from the car ", messages
							.toString().substring(0,
									messages.toString().length() - 1));
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}

	}

	/**
	 * 
	 * This method is used to prepare the order entity
	 * 
	 * @param products
	 * @return
	 */
	private Orders prepareOrderEntity(List<Product> products) {
		Orders order = new Orders();
		order.setOrderDate(new java.sql.Timestamp(System.currentTimeMillis()));
		order.setOrderNo(0 + (int) Math.random() * 10000);
		order.setStatus(Constants.ORDER_STATUS_SHIPPED);
		User user = new User();

		HttpServletRequest request = (HttpServletRequest) FacesContext
				.getCurrentInstance().getExternalContext().getRequest();
		if (request.getSession().getAttribute(Constants.LOGGED_IN_USER) != null) {
			userName = (String) request.getSession().getAttribute(
					Constants.LOGGED_IN_USER);
		}

		user.setUserName(userName);
		order.setUser(user);
		List<ProductOrders> productOrders = new ArrayList<ProductOrders>();
		for (Product product : products) {
			ProductOrders productOrder = new ProductOrders();
			productOrder.setOrder(order);
			productOrder.setProduct(product);
			productOrders.add(productOrder);
		}
		order.setProductOrders(productOrders);
		return order;
	}

	/**
	 * This method is used to calculate the total price int the order
	 * 
	 * @return double - Total value
	 */
	public double getTotalPrice() {
		totalPrice = 0;
		List<Product> productList = getShoppingCart();
		if (productList != null)
			if (productList.size() > 0) {
				for (Product product : productList) {
					this.totalPrice = this.totalPrice
							+ Double.parseDouble(product.getPrice());
				}
			}
		return this.totalPrice;
	}

	public void setCategories(List<Category> categories) {
		this.categories = categories;
	}

	public List<Product> getShoppingCartList() {
		return shoppingCartList;
	}

	public void setShoppingCartList(List<Product> shoppingCartList) {
		this.shoppingCartList = shoppingCartList;
	}

	public List<Product> getShoppingCart() {

		return shoppingCartList;

	}

	public void setShoppingCart(List<Product> shoppingCart) {

		this.shoppingCart = shoppingCart;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Orders getOrderEntity() {
		return orderEntity;
	}

	public void setOrderEntity(Orders orderEntity) {
		this.orderEntity = orderEntity;
	}

	public String getSelectedCategory() {
		return selectedCategory;
	}

	public void setSelectedCategory(String selectedCategory) {
		this.selectedCategory = selectedCategory;
	}

	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public List<Product> getProductList() {
		setProductList(productList);
		return productList;
	}

	public void setProductList(List<Product> productList) {
		this.productList = productList;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}