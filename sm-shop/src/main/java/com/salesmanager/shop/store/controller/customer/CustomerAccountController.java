package com.salesmanager.shop.store.controller.customer;

import java.sql.*;
import java.util.*;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.salesmanager.core.business.services.storecredit.AdStcrService;
import com.salesmanager.core.business.services.storecredit.PayPalService;
import com.salesmanager.core.model.storecredit.AdStcr;
import com.salesmanager.core.model.storecredit.PayPalStcr;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import com.salesmanager.core.business.exception.ServiceException;
import com.salesmanager.core.business.services.customer.CustomerService;
import com.salesmanager.core.business.services.customer.attribute.CustomerAttributeService;
import com.salesmanager.core.business.services.customer.attribute.CustomerOptionService;
import com.salesmanager.core.business.services.customer.attribute.CustomerOptionSetService;
import com.salesmanager.core.business.services.customer.attribute.CustomerOptionValueService;
import com.salesmanager.core.business.services.order.OrderService;
import com.salesmanager.core.business.services.reference.country.CountryService;
import com.salesmanager.core.business.services.reference.language.LanguageService;
import com.salesmanager.core.business.services.reference.zone.ZoneService;
import com.salesmanager.core.business.utils.ajax.AjaxResponse;
import com.salesmanager.core.model.customer.Customer;
import com.salesmanager.core.model.customer.attribute.CustomerAttribute;
import com.salesmanager.core.model.customer.attribute.CustomerOptionType;
import com.salesmanager.core.model.merchant.MerchantStore;
import com.salesmanager.core.model.reference.country.Country;
import com.salesmanager.core.model.reference.language.Language;
import com.salesmanager.shop.constants.Constants;
import com.salesmanager.shop.model.customer.CustomerEntity;
import com.salesmanager.shop.model.customer.CustomerPassword;
import com.salesmanager.shop.model.customer.ReadableCustomer;
import com.salesmanager.shop.model.customer.address.Address;
import com.salesmanager.shop.populator.customer.ReadableCustomerPopulator;
import com.salesmanager.shop.store.controller.AbstractController;
import com.salesmanager.shop.store.controller.ControllerConstants;
import com.salesmanager.shop.store.controller.customer.facade.CustomerFacade;
import com.salesmanager.shop.store.controller.order.facade.OrderFacade;
import com.salesmanager.shop.utils.EmailTemplatesUtils;
import com.salesmanager.shop.utils.LabelUtils;
import com.salesmanager.shop.utils.LanguageUtils;
import com.salesmanager.shop.utils.LocaleUtils;
import org.springframework.web.servlet.ModelAndView;

/**
 * Entry point for logged in customers
 * @author Carl Samson
 *
 */
@Controller
@RequestMapping("/shop/customer")
public class CustomerAccountController extends AbstractController {
	
	private static final String CUSTOMER_ID_PARAMETER = "customer";
    private static final String BILLING_SECTION="/shop/customer/billing.html";
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CustomerAccountController.class);
	
	@Inject
	private CustomerService customerService;
	
	@Inject
	private CustomerOptionService customerOptionService;
	
	@Inject
	private CustomerOptionValueService customerOptionValueService;
	
	@Inject
	private CustomerOptionSetService customerOptionSetService;
	
	@Inject
	private CustomerAttributeService customerAttributeService;
	
    @Inject
    private LanguageService languageService;
    
    @Inject
    private LanguageUtils languageUtils;
    
	@Inject
	private PasswordEncoder passwordEncoder;


    @Inject
    private CountryService countryService;
    
	@Inject
	private EmailTemplatesUtils emailTemplatesUtils;

    
    @Inject
    private ZoneService zoneService;
    
    @Inject
    private CustomerFacade customerFacade;
    
    @Inject
    private OrderService orderService;
    
    @Inject
    private OrderFacade orderFacade;
    
	@Inject
	private LabelUtils messages;

	@Inject
	private AdStcrService adStcrService;

	@Inject
	private PayPalService payPalService;
	
	/**
	 * Dedicated customer logon page
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/customLogon.html", method=RequestMethod.GET)
	public String displayLogon(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		

	    MerchantStore store = getSessionAttribute(Constants.MERCHANT_STORE, request);


		//dispatch to dedicated customer logon
		
		/** template **/
		StringBuilder template = new StringBuilder().append(ControllerConstants.Tiles.Customer.customerLogon).append(".").append(store.getStoreTemplate());

		return template.toString();
		
	}
	
	
	@RequestMapping(value="/accountSummary.json", method=RequestMethod.GET)
	public @ResponseBody ReadableCustomer customerInformation(@RequestParam String userName, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
	
	
		MerchantStore store = getSessionAttribute(Constants.MERCHANT_STORE, request);
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Customer customer = null;
    	if(auth != null &&
        		 request.isUserInRole("AUTH_CUSTOMER")) {
    		customer = customerFacade.getCustomerByUserName(auth.getName(), store);

        } else {
        	response.sendError(401, "Customer not authenticated");
			return null;
        }
    	
    	if(StringUtils.isBlank(userName)) {
        	response.sendError(403, "Customer name required");
			return null;
    	}
    	
    	if(customer==null) {
        	response.sendError(401, "Customer not authenticated");
			return null;
    	}
    	
    	if(!customer.getNick().equals(userName)) {
        	response.sendError(401, "Customer not authenticated");
			return null;
    	}
    	
    	
    	ReadableCustomer readableCustomer = new ReadableCustomer();
    	

    	Language lang = languageUtils.getRequestLanguage(request, response);
    	
    	ReadableCustomerPopulator readableCustomerPopulator = new ReadableCustomerPopulator();
    	readableCustomerPopulator.populate(customer, readableCustomer, store, lang);
    	
    	return readableCustomer;
		
	}
		
	@PreAuthorize("hasRole('AUTH_CUSTOMER')")
	@RequestMapping(value="/account.html", method=RequestMethod.GET)
	public String displayCustomerAccount(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		

	    MerchantStore store = getSessionAttribute(Constants.MERCHANT_STORE, request);

		
		
		/** template **/
		StringBuilder template = new StringBuilder().append(ControllerConstants.Tiles.Customer.customer).append(".").append(store.getStoreTemplate());

		return template.toString();
		
	}

	@PreAuthorize("hasRole('AUTH_CUSTOMER')")
	@RequestMapping(value="/storecredit.html", method=RequestMethod.GET)
	public ModelAndView displayStoreCredit(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {


		MerchantStore store = getSessionAttribute(Constants.MERCHANT_STORE, request);
		List<AdStcr> list=adStcrService.getAllAdStcr();
		/** template **/
		StringBuilder template = new StringBuilder().append(ControllerConstants.Tiles.Customer.storecredit).append(".").append(store.getStoreTemplate());
		return new ModelAndView(template.toString(),"list",list);

	}
	@PreAuthorize("hasRole('AUTH_CUSTOMER')")
	@RequestMapping(value="/storecredit.html/{id}",method = RequestMethod.GET)
	public ModelAndView specificStoreCredit(@PathVariable(name="id") int id, ModelMap model, HttpServletRequest request, HttpServletResponse response){
		MerchantStore store = getSessionAttribute(Constants.MERCHANT_STORE, request);
		AdStcr specific = adStcrService.getAdStcrById(id);
		StringBuilder template = new StringBuilder().append(ControllerConstants.Tiles.Customer.storecredit).append(".").append(store.getStoreTemplate());
		return new ModelAndView(template.toString(),"specific", specific);
	}

	@PreAuthorize("hasRole('AUTH_CUSTOMER')")
	@RequestMapping(value="/paypalStcrSuccess.html",method = RequestMethod.GET)
	public ModelAndView paypalStcrSuccess(ModelMap model, HttpServletRequest request, HttpServletResponse response){
		MerchantStore store = getSessionAttribute(Constants.MERCHANT_STORE, request);
		StringBuilder template = new StringBuilder().append(ControllerConstants.Tiles.Customer.paypalstcrsuccess).append(".").append(store.getStoreTemplate());
		return new ModelAndView(template.toString());
	}

	@PreAuthorize("hasRole('AUTH_CUSTOMER')")
	@RequestMapping(value="/savePayPalStcr.html/{credit}", method = RequestMethod.GET)
	public String savePayPalStcr(@ModelAttribute("paypalstcr") PayPalStcr paypalstcr, @PathVariable(name="credit") double credit, BindingResult result, Model model, HttpServletRequest request, Locale locale) throws Exception {
		MerchantStore store = getSessionAttribute(Constants.MERCHANT_STORE, request);
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Customer customer = customerFacade.getCustomerByUserName(auth.getName(), store);
		Connection connection = DriverManager.getConnection("jdbc:h2:file:./SALESMANAGER", "test", "password");
		Statement statement = connection.createStatement();
		String queryString = "SELECT * FROM SALESMANAGER.PAYPALSTCR WHERE CUSTOMERID = ? ORDER BY ID DESC LIMIT 1";
		PreparedStatement pstatement = connection.prepareStatement(queryString);
		int customerId = Math.toIntExact(customer.getId());
		pstatement.setInt(1, customerId);
		ResultSet resultset = pstatement.executeQuery();
		if(!resultset.next()) {
			System.out.println("Sorry, could not find that publisher. ");
		} else {
			String queryString2 = "INSERT INTO SALESMANAGER.PAYPALSTCR (CUSTOMERID, PRICE) VALUES (?,?)";
			PreparedStatement pstatement2 = connection.prepareStatement(queryString2);
			int availablestcr = resultset.getInt("price");
			double total =  availablestcr + credit;
			pstatement2.setInt(1, customerId);
			pstatement2.setDouble(2, total);
			int resultset2 = pstatement2.executeUpdate();
			return "redirect:/shop/customer/paypalStcrSuccess.html";
		}
		return "redirect:/shop";
	}

	@PreAuthorize("hasRole('AUTH_CUSTOMER')")
	@RequestMapping(value="/payStcrSuccess.html",method = RequestMethod.GET)
	public ModelAndView payStcrSuccess(ModelMap model, HttpServletRequest request, HttpServletResponse response){
		MerchantStore store = getSessionAttribute(Constants.MERCHANT_STORE, request);
		StringBuilder template = new StringBuilder().append(ControllerConstants.Tiles.Customer.paystcrsuccess).append(".").append(store.getStoreTemplate());
		return new ModelAndView(template.toString());
	}

	@RequestMapping(value={"/savePayStcr.html/{orderprice}"}, method=RequestMethod.POST)
	public String savePayStcr(@ModelAttribute(value="paypalstcr") PayPalStcr paypalstcr, @PathVariable(name="orderprice") double orderprice, Model model, HttpServletRequest request, HttpServletResponse response, Locale locale) throws Exception {
		MerchantStore store = getSessionAttribute(Constants.MERCHANT_STORE, request);
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Customer customer = customerFacade.getCustomerByUserName(auth.getName(), store);
		Connection connection = DriverManager.getConnection("jdbc:h2:file:./SALESMANAGER", "test", "password");
		Statement statement = connection.createStatement();
		String queryString = "SELECT * FROM SALESMANAGER.PAYPALSTCR WHERE CUSTOMERID = ? ORDER BY ID DESC LIMIT 1";
		PreparedStatement pstatement = connection.prepareStatement(queryString);
		int customerId = Math.toIntExact(customer.getId());
		pstatement.setInt(1, customerId);
		ResultSet resultset = pstatement.executeQuery();
		if(!resultset.next()) {
			System.out.println("Sorry, could not find that publisher. ");
		} else {
			String queryString2 = "INSERT INTO SALESMANAGER.PAYPALSTCR (CUSTOMERID, PRICE) VALUES (?,?)";
			PreparedStatement pstatement2 = connection.prepareStatement(queryString2);
			int availablestcr = resultset.getInt("price");
			double remainder =  availablestcr - orderprice;
			if (orderprice > availablestcr){
				return "redirect:/shop/customer/storecredit.html";
			}
			pstatement2.setInt(1, customerId);
			pstatement2.setDouble(2, remainder);
			int resultset2 = pstatement2.executeUpdate();
			return "redirect:/shop/customer/payStcrSuccess.html";
		}
		return "redirect:/shop";
	}

	@PreAuthorize("hasRole('AUTH_CUSTOMER')")
	@RequestMapping(value="/password.html", method=RequestMethod.GET)
	public String displayCustomerChangePassword(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		

	    MerchantStore store = getSessionAttribute(Constants.MERCHANT_STORE, request);

		CustomerPassword customerPassword = new CustomerPassword();
		model.addAttribute("password", customerPassword);
		
		/** template **/
		StringBuilder template = new StringBuilder().append(ControllerConstants.Tiles.Customer.changePassword).append(".").append(store.getStoreTemplate());

		return template.toString();
		
	}
	
	@PreAuthorize("hasRole('AUTH_CUSTOMER')")
	@RequestMapping(value="/changePassword.html", method=RequestMethod.POST)
	public String changePassword(@Valid @ModelAttribute(value="password") CustomerPassword password, BindingResult bindingResult, Model model, HttpServletRequest request, HttpServletResponse response, Locale locale) throws Exception {
		

	    MerchantStore store = getSessionAttribute(Constants.MERCHANT_STORE, request);
	    
		/** template **/
		StringBuilder template = new StringBuilder().append(ControllerConstants.Tiles.Customer.changePassword).append(".").append(store.getStoreTemplate());

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Customer customer = null;
    	if(auth != null &&
        		 request.isUserInRole("AUTH_CUSTOMER")) {
    		customer = customerFacade.getCustomerByUserName(auth.getName(), store);

        }
    	
    	if(customer==null) {
    		return "redirect:/"+Constants.SHOP_URI;
    	}
    	
    	String currentPassword = password.getCurrentPassword();

    	BCryptPasswordEncoder encoder = (BCryptPasswordEncoder)passwordEncoder;
    	if(!encoder.matches(currentPassword, customer.getPassword())) {
          FieldError error = new FieldError("password","password",messages.getMessage("message.invalidpassword", locale));
          bindingResult.addError(error);
    	}

    	
        if ( bindingResult.hasErrors() )
        {
            LOGGER.info( "found {} validation error while validating customer password",
                         bindingResult.getErrorCount() );
    		return template.toString();

        }
    	
		CustomerPassword customerPassword = new CustomerPassword();
		model.addAttribute("password", customerPassword);
		
		String newPassword = password.getPassword();
		String encodedPassword = passwordEncoder.encode(newPassword);
		
		customer.setPassword(encodedPassword);
		
		customerService.saveOrUpdate(customer);
		
		emailTemplatesUtils.changePasswordNotificationEmail(customer, store, LocaleUtils.getLocale(customer.getDefaultLanguage()), request.getContextPath());
		
		model.addAttribute("success", "success");

		return template.toString();
		
	}
	

	
	/**
	 * Manage the edition of customer attributes
	 * @param request
	 * @param locale
	 * @return
	 * @throws Exception
	 */
	@PreAuthorize("hasRole('AUTH_CUSTOMER')")
	@RequestMapping(value={"/attributes/save.html"}, method=RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> saveCustomerAttributes(HttpServletRequest request, Locale locale) throws Exception {
		

		AjaxResponse resp = new AjaxResponse();
		final HttpHeaders httpHeaders= new HttpHeaders();
	    httpHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
		
		MerchantStore store = (MerchantStore)request.getAttribute(Constants.MERCHANT_STORE);
		
		//1=1&2=on&3=eeee&4=on&customer=1

		@SuppressWarnings("rawtypes")
		Enumeration parameterNames = request.getParameterNames();
		
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Customer customer = null;
    	if(auth != null &&
        		 request.isUserInRole("AUTH_CUSTOMER")) {
    		customer = customerFacade.getCustomerByUserName(auth.getName(), store);

        }
    	
    	if(customer==null) {
    		LOGGER.error("Customer id [customer] is not defined in the parameters");
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			String returnString = resp.toJSONString();
			return new ResponseEntity<String>(returnString,httpHeaders,HttpStatus.OK);
    	}
		
		

		
		if(customer.getMerchantStore().getId().intValue()!=store.getId().intValue()) {
			LOGGER.error("Customer id does not belong to current store");
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			String returnString = resp.toJSONString();
			return new ResponseEntity<String>(returnString,httpHeaders,HttpStatus.OK);
		}
		
		List<CustomerAttribute> customerAttributes = customerAttributeService.getByCustomer(store, customer);
		Map<Long,CustomerAttribute> customerAttributesMap = new HashMap<Long,CustomerAttribute>();
		
		for(CustomerAttribute attr : customerAttributes) {
			customerAttributesMap.put(attr.getCustomerOption().getId(), attr);
		}

		parameterNames = request.getParameterNames();
		
		while(parameterNames.hasMoreElements()) {
			
			String parameterName = (String)parameterNames.nextElement();
			String parameterValue = request.getParameter(parameterName);
			try {
				
				String[] parameterKey = parameterName.split("-");
				com.salesmanager.core.model.customer.attribute.CustomerOption customerOption = null;
				com.salesmanager.core.model.customer.attribute.CustomerOptionValue customerOptionValue = null;

				
				if(CUSTOMER_ID_PARAMETER.equals(parameterName)) {
					continue;
				}
				
					if(parameterKey.length>1) {
						//parse key - value
						String key = parameterKey[0];
						String value = parameterKey[1];
						//should be on
						customerOption = customerOptionService.getById(new Long(key));
						customerOptionValue = customerOptionValueService.getById(new Long(value));
						

						
					} else {
						customerOption = customerOptionService.getById(new Long(parameterName));
						customerOptionValue = customerOptionValueService.getById(new Long(parameterValue));

					}
					
					//get the attribute
					//CustomerAttribute attribute = customerAttributeService.getByCustomerOptionId(store, customer.getId(), customerOption.getId());
					CustomerAttribute attribute = customerAttributesMap.get(customerOption.getId());
					if(attribute==null) {
						attribute = new CustomerAttribute();
						attribute.setCustomer(customer);
						attribute.setCustomerOption(customerOption);
					} else {
						customerAttributes.remove(attribute);
					}
					
					if(customerOption.getCustomerOptionType().equals(CustomerOptionType.Text.name())) {
						if(!StringUtils.isBlank(parameterValue)) {
							attribute.setCustomerOptionValue(customerOptionValue);
							attribute.setTextValue(parameterValue);
						}  else {
							attribute.setTextValue(null);
						}
					} else {
						attribute.setCustomerOptionValue(customerOptionValue);
					}
					
					
					if(attribute.getId()!=null && attribute.getId().longValue()>0) {
						if(attribute.getCustomerOptionValue()==null){
							customerAttributeService.delete(attribute);
						} else {
							customerAttributeService.update(attribute);
						}
					} else {
						customerAttributeService.save(attribute);
					}
					


			} catch (Exception e) {
				LOGGER.error("Cannot get parameter information " + parameterName,e);
			}
			
		}
		
		//and now the remaining to be removed
		for(CustomerAttribute attr : customerAttributes) {
			customerAttributeService.delete(attr);
		}
		
		//refresh customer
		Customer c = customerService.getById(customer.getId());
		super.setSessionAttribute(Constants.CUSTOMER, c, request);
		
		resp.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);
		String returnString = resp.toJSONString();
		return new ResponseEntity<String>(returnString,httpHeaders,HttpStatus.OK);
		

	}

	@PreAuthorize("hasRole('AUTH_CUSTOMER')")
	//@Secured("AUTH_CUSTOMER")
	@RequestMapping(value="/billing.html", method=RequestMethod.GET)
    public String displayCustomerBillingAddress(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        

        MerchantStore store = getSessionAttribute(Constants.MERCHANT_STORE, request);
        Language language = getSessionAttribute(Constants.LANGUAGE, request);
    
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Customer customer = null;
    	if(auth != null &&
        		 request.isUserInRole("AUTH_CUSTOMER")) {
    		customer = customerFacade.getCustomerByUserName(auth.getName(), store);

        }
    	
    	if(customer==null) {
    		return "redirect:/"+Constants.SHOP_URI;
    	}
        
        
        CustomerEntity customerEntity = customerFacade.getCustomerDataByUserName( customer.getNick(), store, language );
        if(customer !=null){
           model.addAttribute( "customer",  customerEntity);
        }
        
        
        /** template **/
        StringBuilder template = new StringBuilder().append(ControllerConstants.Tiles.Customer.Billing).append(".").append(store.getStoreTemplate());

        return template.toString();
        
    }
    
	@PreAuthorize("hasRole('AUTH_CUSTOMER')")
    @RequestMapping(value="/editAddress.html", method={RequestMethod.GET,RequestMethod.POST})
    public String editAddress(final Model model, final HttpServletRequest request,
                              @RequestParam(value = "billingAddress", required = false) Boolean billingAddress) throws Exception {
        MerchantStore store = getSessionAttribute(Constants.MERCHANT_STORE, request);
        
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Customer customer = null;
    	if(auth != null &&
        		 request.isUserInRole("AUTH_CUSTOMER")) {
    		customer = customerFacade.getCustomerByUserName(auth.getName(), store);

        }
    	
    	if(customer==null) {
    		return "redirect:/"+Constants.SHOP_URI;
    	}
        
        
        
        Address address=customerFacade.getAddress( customer.getId(), store, billingAddress );
        model.addAttribute( "address", address);
        model.addAttribute( "customerId", customer.getId() );
        StringBuilder template = new StringBuilder().append(ControllerConstants.Tiles.Customer.EditAddress).append(".").append(store.getStoreTemplate());
        return template.toString();
    }
    
    
	@PreAuthorize("hasRole('AUTH_CUSTOMER')")
    @RequestMapping(value="/updateAddress.html", method={RequestMethod.GET,RequestMethod.POST})
    public String updateCustomerAddress(@Valid
                                        @ModelAttribute("address") Address address,BindingResult bindingResult,final Model model, final HttpServletRequest request,
                              @RequestParam(value = "billingAddress", required = false) Boolean billingAddress) throws Exception {
       
        MerchantStore store = getSessionAttribute(Constants.MERCHANT_STORE, request);
        
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Customer customer = null;
    	if(auth != null &&
        		 request.isUserInRole("AUTH_CUSTOMER")) {
    		customer = customerFacade.getCustomerByUserName(auth.getName(), store);

        }
    	
    	StringBuilder template = new StringBuilder().append(ControllerConstants.Tiles.Customer.EditAddress).append(".").append(store.getStoreTemplate());
    	
    	if(customer==null) {
    		return "redirect:/"+Constants.SHOP_URI;
    	}
    	
    	model.addAttribute( "address", address);
        model.addAttribute( "customerId", customer.getId() );
        
        
        if(bindingResult.hasErrors()){
            LOGGER.info( "found {} error(s) while validating  customer address ",
                         bindingResult.getErrorCount() );
            return template.toString();
        }
        

        Language language = getSessionAttribute(Constants.LANGUAGE, request);
        customerFacade.updateAddress( customer.getId(), store, address, language);
        
        Customer c = customerService.getById(customer.getId());
		super.setSessionAttribute(Constants.CUSTOMER, c, request);
        
        model.addAttribute("success", "success");
        
        return template.toString();

    }
    
    
	@ModelAttribute("countries")
	protected List<Country> getCountries(final HttpServletRequest request){
	    
        Language language = (Language) request.getAttribute( "LANGUAGE" );
        try
        {
            if ( language == null )
            {
                language = (Language) request.getAttribute( "LANGUAGE" );
            }

            if ( language == null )
            {
                language = languageService.getByCode( Constants.DEFAULT_LANGUAGE );
            }
            
            List<Country> countryList=countryService.getCountries( language );
            return countryList;
        }
        catch ( ServiceException e )
        {
            LOGGER.error( "Error while fetching country list ", e );

        }
        return Collections.emptyList();
    }

    //@ModelAttribute("zones")
    //public List<Zone> getZones(final HttpServletRequest request){
    //    return zoneService.list();
    //}
 




}
