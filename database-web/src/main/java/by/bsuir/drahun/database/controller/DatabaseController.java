package by.bsuir.drahun.database.controller;

import java.io.Serializable;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import by.bsuir.drahun.database.business.OfferService;
import by.bsuir.drahun.database.domain.Product;
import by.bsuir.drahun.database.domain.ProductOffer;
import by.bsuir.drahun.database.json.AddConditionResult;
import by.bsuir.drahun.database.model.ProductBean;
import by.bsuir.drahun.database.query.Condition;
import by.bsuir.drahun.database.query.Operator;
import by.bsuir.drahun.database.query.ProductOfferQuery;
import by.bsuir.drahun.database.query.SingleCondition;

@Controller
@Scope("session")
@SessionAttributes("query")
public class DatabaseController implements Serializable {
	
	private static final long serialVersionUID = 7372736988583245713L;
	
	private ProductOfferQuery offerQuery = new ProductOfferQuery();
	
	private Condition[] conditions = Condition.values();
	
	private Operator[] operators = Operator.values();
	
	private String[] fields = ProductOfferQuery.FIELDS;
	
	@Autowired
	private OfferService offerService;
	
	@RequestMapping("/")
	public String getDatabasePage() {
		
		return "index";
	}
	
	@RequestMapping(value = "/create-offer", method = RequestMethod.GET)
	public ModelAndView getCreateOfferPage() {
		return new ModelAndView("create-offer", "command", new ProductBean());
	}
	
	@RequestMapping(value = "/create-offer", method = RequestMethod.POST)
	public ModelAndView createOffer(@ModelAttribute("productBean")ProductBean productBean, ModelAndView model) {
		Product product = new Product();
		product.setProductCode(productBean.getProductCode());
		product.setProductTitle(productBean.getProductTitle());
		ProductOffer offer = new ProductOffer();
		offer.setProduct(product);
		offer.setCost(productBean.getCost());
		offer.setQuantity(productBean.getQuantity());
		offerService.saveOffer(offer);
		return getOfferList(model);
	}
	
	
	@RequestMapping(value = "/search-offers", method = RequestMethod.GET, params = {"query"})
	public ModelAndView getOfferList(@RequestParam("query") String query, ModelAndView model, HttpServletRequest request) {
		List<ProductBean> offerList = offerService.fetchOffers(query);
	    model.addObject("offerList", offerList);
	    model.setViewName("offer-list");
	    model.addObject("operatorList", operators);
	    model.addObject("conditionList", conditions);
	    model.addObject("fieldList", fields);
	    model.addObject("resultQuery", updateQueryString());
	    model.addObject("isResultQueryEmpty", StringUtils.isBlank(updateQueryString()));
	    request.getSession().setAttribute("query", query);
		return model;
	}
	
	@RequestMapping(value = "/search-offers", method = RequestMethod.GET)
	public ModelAndView getOfferList(ModelAndView model, HttpServletRequest request) {
		List<ProductBean> offerList = offerService.fetchOffers(offerQuery);
	    model.addObject("offerList", offerList);
	    model.setViewName("offer-list");
	    model.addObject("operatorList", operators);
	    model.addObject("conditionList", conditions);
	    model.addObject("fieldList", fields);
	    model.addObject("resultQuery", updateQueryString());
	    model.addObject("isResultQueryEmpty", StringUtils.isBlank(updateQueryString()));
		return model;
	}
	
	@RequestMapping("/offer-list")
	public ModelAndView getOfferList(ModelAndView model) {
		List<ProductBean> offerList = offerService.fetchOffers(new ProductOfferQuery());
	    model.addObject("offerList", offerList);
	    model.addObject("operatorList", operators);
	    model.addObject("conditionList", conditions);
	    model.addObject("fieldList", fields);
	    model.addObject("resultQuery", updateQueryString());
	    model.setViewName("offer-list");
		return model;
	}
	
	@RequestMapping(value = "/remove-condition", method = RequestMethod.DELETE, produces=MediaType.APPLICATION_JSON_VALUE) 
	public ResponseEntity<AddConditionResult> addCondition() {
		if (offerQuery.hasConditions()) {
			offerQuery.removeLastCondition();
		}
		AddConditionResult entity = new AddConditionResult();
		entity.setResultQuery(updateQueryString());
		entity.setIsQueryEmpty(offerQuery.hasConditions());
		return new ResponseEntity<AddConditionResult>(entity, HttpStatus.OK);
	}
	
	
	@RequestMapping(value = "/add-condition", method = RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE) 
	public ResponseEntity<AddConditionResult> addCondition(
			@RequestParam("operator") String operator,
			@RequestParam("field") String field,
			@RequestParam("condition") String condition,
			@RequestParam("value") String value) {
		SingleCondition sc = new SingleCondition();
		if (!offerQuery.getConditions().isEmpty()) {
			sc.setOperator(Operator.valueOf(operator));
		}
		sc.setField(field);
		sc.setCondition(Condition.valueOf(condition));
		sc.setValue(value);
		offerQuery.addCondition(sc);
		
		AddConditionResult entity = new AddConditionResult();
		entity.setResultQuery(updateQueryString());
		entity.setIsQueryEmpty(offerQuery.hasConditions());
		return new ResponseEntity<AddConditionResult>(entity, HttpStatus.OK);
	}
	
	protected String updateQueryString() {
		String str = "";
		for (SingleCondition c : offerQuery.getConditions()) {
			str += c.toString();
		}
		return str;
	}
	
}
