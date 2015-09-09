package com.sogeti.webshop.userweb.backingbean;

import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.sogeti.webshop.business.RegistrationService;
import com.sogeti.webshop.business.utitlity.Constants;
import com.sogeti.webshop.model.Product;
import com.sogeti.webshop.model.User;

/**
 * @author mpattabh
 *
 *  This class handles all the requests made for login/registration
 *  related operations by the webshop user
 */
/**
 * @author mpattabh
 *
 */
/**
 * @author mpattabh
 *
 */
@ManagedBean(name = "registrationBean")
@RequestScoped
public class RegistrationBean implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * EJB call which handles all the registration related business operations
	 */
	@Inject
	RegistrationService registrationService;

	private String firstName;

	private String lastName;

	private String userName;

	private String password;

	private String Address;

	private String city;

	private String email;

	private String state;

	private List<Product> shoppingCart;

	private String country;

	private String errorMessage;

	/**
	 * This method is used to handle check the login related operations
	 * 
	 * @return outCome - Depending on success/failure user will be redirected to
	 *         login page
	 */
	public String doLogin() {
		String outCome = "login";
		User user = prepareUser(this);
		HttpServletRequest request = (HttpServletRequest) FacesContext
				.getCurrentInstance().getExternalContext().getRequest();
		Boolean userPresent = registrationService.validateLogin(user);
		if (userPresent) {
			request.getSession().setAttribute(Constants.LOGIN_ERROR_MSG,
					Constants.EMPTY_STRING);
			ProductBean productBean = (ProductBean) FacesContext
					.getCurrentInstance().getExternalContext().getSessionMap()
					.get("productBean");
			List<Product> products = productBean.getShoppingCart();
			this.setShoppingCart(products);
			request.getSession().setAttribute(Constants.LOGGED_IN_USER,
					user.getUserName());
			outCome = "order";
		} else {

			request.getSession()
					.setAttribute(Constants.LOGIN_ERROR_MSG,
							"User credentials not matching.Pls sign or give proper credentials");
		}

		return outCome;
	}

	/**
	 * This method is used to register the user with the system This method
	 * creates user entity in the system
	 * 
	 * @return order - order page display
	 */
	public String doRegister() {
		User user = prepareUser(this);
		registrationService.addUser(user);
		HttpServletRequest request = (HttpServletRequest) FacesContext
				.getCurrentInstance().getExternalContext().getRequest();
		request.getSession().setAttribute(Constants.LOGGED_IN_USER,
				user.getUserName());
		return "order";

	}

	/**
	 * This method is used to prepare the user entity
	 * 
	 * @return order - order page display
	 */
	private User prepareUser(RegistrationBean registrationBean) {
		User user = new User();
		user.setFirstName(registrationBean.getFirstName());
		user.setLastName(registrationBean.getLastName());
		user.setUserName(registrationBean.getUserName());
		user.setPassWord(registrationBean.getPassword());
		user.setEmail(registrationBean.getEmail());
		user.setAddress(registrationBean.getAddress());
		user.setCity(registrationBean.getCity());
		user.setState(registrationBean.getState());
		user.setCountry(registrationBean.getCountry());
		return user;
	}

	/**
	 * This method is used to logout
	 */
	public String logOut() {
		((HttpSession) FacesContext.getCurrentInstance().getExternalContext()
				.getSession(true)).invalidate();
		return "productDetails";
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getAddress() {
		return Address;
	}

	public void setAddress(String address) {
		Address = address;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public RegistrationService getRegistrationService() {
		return registrationService;
	}

	public void setRegistrationService(RegistrationService registrationService) {
		this.registrationService = registrationService;
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

	public List<Product> getShoppingCart() {
		return shoppingCart;
	}

	public void setShoppingCart(List<Product> shoppingCart) {
		this.shoppingCart = shoppingCart;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
}