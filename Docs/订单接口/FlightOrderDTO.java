package com.travel.flight.order.dto;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;

import com.travel.entity.flight.FlightOrder;
import com.travel.entity.flight.FlightPassenger;
import com.travel.entity.flight.FlightTrip;

/**
 * the object for form post
 * @author David.zhu
 *
 */
public class FlightOrderDTO {

	/**
	 * order id
	 */
    private Integer id;

    /**
     * amount of order
     */
    private Double amount;

    /**
     * user id, default 0 for visitor
     */
    private String userId;

    /**
     * trip type,0:one way，1：round trip，2：multi-city
     */
    private Integer tripType;

    /**
     * order state,0:created,10:ticket issued,11:system refund,20:has paid,21:user refund
     */
    private Integer state;

    /**
     * order source, 0:websit,10: mobile explorer,11:android app,12:ios app,3:others
     */
    private Integer source;

    /**
     * contacts name, format:"first name,middle name,last name", 
     * if there is middle,please fill in "", then the input likes this""
     */
    private String contactName;

    /**
     * contacts's country code, for example the china is +86 
     */
    private String contCountryCode;

    /**
     * contacts phone number
     */
    private String contPhone;

    /**
     * contacts email
     */
    private String contEmail;

    /**
     * recipient name, the format same as to Contacts Name
     */
    private String recipient;

    /**
     * recipient's phone number
     */
    private String reciPhone;

    /**
     * recipient's address
     */
    private String reciAddress;

    /**
     * recipient's postal code
     */
    private String reciPostalCode;

    /**
     * when to pick up the ticket
     */
    private String pickUpTime;

    /**
     * special request
     */
    private String specialReq;

    /**
     * when to pay
     */
    private String payTime;

    /**
     * what time the recipient can receive the ticket
     */
    private String receiveTime;

    /**
     * order create time
     */
    private String createTime;
    
    /**
     * flights, data format:json
     * example:[{id:"123",flight:"123",departTime:"20150502 17:12:12",arrivalTime:"20150502 17:12:12",price:1801.72,
     * currency:"RMB",from:"shanghai",to:"beijing",airline:"china united airlines",clazz:"economy",sortNo:"1"},....]
     */
    private String trips;
    
    public String getTrips() {
		return trips;
	}

	public void setTrips(String trips) {
		this.trips = trips;
	}

	/**
     * passengers, data format:json
     * example:[{id:"123",name"zhu,xin,ze",nationality:"china",idType:"passport",idNo:"123456",
     * age_group:"adult",gender:"male",birthday:"19990203",freqFlyerProgram:"",freqFlyerNo:""},....]
     */
    private String passengers;
    
    /**
     * get the entity of flight order information
     * @return
     */
    public FlightOrder drawFlightOrder(){
    	FlightOrder order = new FlightOrder();
    	
    	order.setAmount(this.getAmount());
    	order.setContactName(this.getContactName());
    	order.setContCountryCode(this.getContCountryCode());
    	order.setContEmail(this.getContEmail());
    	order.setContPhone(this.getContPhone());
    	if(StringUtils.isNotBlank(this.getPickUpTime())){
    		order.setPickUpTime(this.parseDate(this.getPickUpTime()));
    	}
    	if(StringUtils.isNotBlank(this.getReceiveTime())){
    		order.setReceiveTime(this.parseDate(this.getReceiveTime()));
    	}
    	order.setReciAddress(this.getReciAddress());
    	order.setReciPhone(this.getReciPhone());
    	order.setRecipient(this.getRecipient());
    	order.setReciPostalCode(this.getReciPostalCode());
    	order.setSource(this.getSource());
    	order.setSpecialReq(this.getSpecialReq());
    	order.setTripType(this.getTripType());
    	order.setUserId(this.getUserId());
    	return order;
    }
    
    /**
     * get entity of flight trip information
     * @return
     */
    @SuppressWarnings("unchecked")
	public List<FlightTrip> drawFlightTrip(){
    	List<FlightTrip> listRes = new ArrayList<FlightTrip>();
    	if(StringUtils.isNotBlank(this.getTrips())){
    		JSONArray jsonArray = JSONArray.fromObject(this.getTrips());
    		Iterator<JSONObject> iterator = jsonArray.iterator();
    		while(iterator.hasNext()){
    			JSONObject jsonObj = iterator.next();
    			FlightTrip flightTrip = new FlightTrip();
    			flightTrip.setAirline(jsonObj.getString("airline"));
    			
    			if(StringUtils.isNotBlank(jsonObj.getString("arrivalTime"))){
    				flightTrip.setArrivalTime(this.parseDate(jsonObj.getString("arrivalTime")));
    			}
    			
    			flightTrip.setClazz(jsonObj.getInt("clazz"));
    			flightTrip.setCurrency(jsonObj.getString("currency"));
    			
    			if(StringUtils.isNotBlank(jsonObj.getString("departTime"))){
    				flightTrip.setDepartTime(this.parseDate(jsonObj.getString("departTime")));
    			}
    			
    			flightTrip.setFlight(jsonObj.getString("flight"));
    			flightTrip.setFrom(jsonObj.getString("from"));
    			flightTrip.setTo(jsonObj.getString("to"));
    			flightTrip.setPrice(jsonObj.getDouble("price"));
    			flightTrip.setSortNo(jsonObj.getInt("sortNo"));
    			listRes.add(flightTrip);
    		}
    	}
    	return listRes;
    }
    
    /**
     * get entity information of flight passenger
     * @return
     */
    @SuppressWarnings("unchecked")
	public List<FlightPassenger> drawFlightPassenger(){
    	List<FlightPassenger> listRes = new ArrayList<FlightPassenger>();
    	if(StringUtils.isNotBlank(this.getPassengers())){
    		JSONArray jsonArray = JSONArray.fromObject(this.getPassengers());
    		Iterator<JSONObject> it = jsonArray.iterator();
    		while(it.hasNext()){
    			JSONObject jsonObj = it.next();
    			FlightPassenger flightPassenger = new FlightPassenger();
    			if(StringUtils.isNotBlank(jsonObj.getString("birthday"))){
    				flightPassenger.setBirthday(this.parseDate(jsonObj.getString("birthday")));
    			}
    			flightPassenger.setFreqFlyerNo(jsonObj.getString("freqFlyerNo"));
    			flightPassenger.setFreqFlyerProgram(jsonObj.getString("freqFlyerProgram"));
    			flightPassenger.setGender(jsonObj.getInt("gender"));
    			flightPassenger.setIdNo(jsonObj.getString("idNo"));
    			flightPassenger.setIdType(jsonObj.getString("idType"));
    			flightPassenger.setName(jsonObj.getString("name"));
    			flightPassenger.setNationality(jsonObj.getString("nationality"));
    			listRes.add(flightPassenger);
    		}
    	}
    	return listRes;
    }
    
    /**
     * convert from java object to string in JSON format
     * @param flightOrder
     * @param pass
     * @param trips
     * @return
     */
    public String drawDTO(FlightOrder flightOrder, List<FlightPassenger> pass, List<FlightTrip> trips){
    	this.setAmount(flightOrder.getAmount());
    	this.setContactName(flightOrder.getContactName());
    	this.setContCountryCode(flightOrder.getContCountryCode());
    	this.setContEmail(flightOrder.getContEmail());
    	this.setContPhone(flightOrder.getContPhone());
    	this.setCreateTime(this.format(flightOrder.getCreateTime()));
    	this.setId(flightOrder.getId());
    	if(flightOrder.getPayTime()!=null){
    		this.setPayTime(this.format(flightOrder.getPayTime()));
    	}
    	if(flightOrder.getPickUpTime()!=null){
    		this.setPickUpTime(this.format(flightOrder.getPickUpTime()));
    	}
    	this.setReceiveTime(this.format(flightOrder.getReceiveTime()));
    	this.setReciAddress(flightOrder.getReciAddress());
    	this.setReciPhone(flightOrder.getReciPhone());
    	this.setRecipient(flightOrder.getRecipient());
    	this.setReciPostalCode(flightOrder.getReciPostalCode());
    	this.setSource(flightOrder.getSource());
    	this.setSpecialReq(flightOrder.getSpecialReq());
    	this.setState(flightOrder.getState());
    	this.setTripType(flightOrder.getTripType());
    	this.setUserId(flightOrder.getUserId());
    	JSONObject jsonRes = JSONObject.fromObject(this);
    	
    	if(CollectionUtils.isNotEmpty(pass)){
    		JSONArray jsonArrayPass = JSONArray.fromObject(pass);
    		jsonRes.accumulate("passengers", jsonArrayPass);
    	}
    	
    	if(CollectionUtils.isNotEmpty(trips)){
    		JSONArray jsonArrayTrips = JSONArray.fromObject(trips);
    		jsonRes.accumulate("trips", jsonArrayTrips);
    	}
    	return jsonRes.toString();
    }
    
    private Date parseDate(String date){
    	Date parseDate = null;
		try {
			parseDate = DateUtils.parseDate(date, new String[]{"yyyyMMddHHmmss","yyyyMMdd"});
		} catch (ParseException e) {
			e.printStackTrace();
		}
    	return parseDate;
    }
    
    private String format(Date date){
    	String strRes = DateFormatUtils.format(date, "yyyyMMddHHmmss");
    	return strRes;
    }
    
    public static void main(String[] args) {
//    	JSONObject jsonObject = new JSONObject();
//    	jsonObject.put("flight", "123");
//    	jsonObject.put("departTime", "20150502171212");
//    	jsonObject.put("arrivalTime", "20150502171212");
//    	jsonObject.put("price", 1233.68);
//    	jsonObject.put("currency", "RMB");
//    	jsonObject.put("from", "shanghai");
//    	jsonObject.put("to", "beijing");
//    	jsonObject.put("airline", "china united airlines");
//    	jsonObject.put("clazz", "economy");
//    	jsonObject.put("sortNo", "1");
//    	System.out.println(jsonObject.toString());
        
    	JSONObject jsonObject = new JSONObject();
    	jsonObject.put("name", "zhu,xin,ze");
    	jsonObject.put("nationality", "china");
    	jsonObject.put("idType", "passport");
    	jsonObject.put("idNo", "123456");
    	jsonObject.put("age_group", "adult");
    	jsonObject.put("gender", "0");
    	jsonObject.put("birthday", "19990203");
    	jsonObject.put("freqFlyerProgram", "test");
    	jsonObject.put("freqFlyerNo", "test");
    	System.out.println(jsonObject.toString());
    	
//		try {
//			Date parseDate = DateUtils.parseDate("20150501", new String[]{"yyyyMMddHHmmss","yyyyMMdd"});
//			System.out.println(parseDate);
//		} catch (ParseException e) {
//			e.printStackTrace();
//		}
    	
	}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public Integer getTripType() {
        return tripType;
    }

    public void setTripType(Integer tripType) {
        this.tripType = tripType;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getSource() {
        return source;
    }

    public void setSource(Integer source) {
        this.source = source;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName == null ? null : contactName.trim();
    }

    public String getContCountryCode() {
        return contCountryCode;
    }

    public void setContCountryCode(String contCountryCode) {
        this.contCountryCode = contCountryCode == null ? null : contCountryCode.trim();
    }

    public String getContPhone() {
        return contPhone;
    }

    public void setContPhone(String contPhone) {
        this.contPhone = contPhone == null ? null : contPhone.trim();
    }

    public String getContEmail() {
        return contEmail;
    }

    public void setContEmail(String contEmail) {
        this.contEmail = contEmail == null ? null : contEmail.trim();
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient == null ? null : recipient.trim();
    }

    public String getReciPhone() {
        return reciPhone;
    }

    public void setReciPhone(String reciPhone) {
        this.reciPhone = reciPhone == null ? null : reciPhone.trim();
    }

    public String getReciAddress() {
        return reciAddress;
    }

    public void setReciAddress(String reciAddress) {
        this.reciAddress = reciAddress == null ? null : reciAddress.trim();
    }

    public String getReciPostalCode() {
        return reciPostalCode;
    }

    public void setReciPostalCode(String reciPostalCode) {
        this.reciPostalCode = reciPostalCode == null ? null : reciPostalCode.trim();
    }

    public String getPickUpTime() {
        return pickUpTime;
    }

    public void setPickUpTime(String pickUpTime) {
        this.pickUpTime = pickUpTime;
    }

    public String getSpecialReq() {
        return specialReq;
    }

    public void setSpecialReq(String specialReq) {
        this.specialReq = specialReq == null ? null : specialReq.trim();
    }

    public String getPayTime() {
        return payTime;
    }

    public void setPayTime(String payTime) {
        this.payTime = payTime;
    }

    public String getReceiveTime() {
        return receiveTime;
    }

    public void setReceiveTime(String receiveTime) {
        this.receiveTime = receiveTime;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

	public String getPassengers() {
		return passengers;
	}

	public void setPassengers(String passengers) {
		this.passengers = passengers;
	}
}
