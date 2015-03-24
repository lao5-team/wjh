package com.test.juxiaohui.common.dal;

import java.util.List;

import com.test.juxiaohui.mdxc.data.FlightData;
import com.test.juxiaohui.mdxc.data.FlightData.BEHAVIOR_TYPE;
import com.test.juxiaohui.mdxc.data.FlightSearchRequest;



/**
 * 
 * Create by qinyy on 2015/03/11
 *
 */
public interface IFlightManager 
{
	/**
	 *    查询航班信息，区分国内航班与国际航班
	 * @param data 
	 * @param type BEHAVIOR_TYPE.DOMISTIC or BEHAVIOR_TYPE.INTERNATIONAL
	 * @return 
	 */
	public List<FlightData> flightSearch(FlightSearchRequest request, BEHAVIOR_TYPE type);

	/**
	 *    仓位验证
	 * @param data 
	 * @param type BEHAVIOR_TYPE.DOMISTIC or BEHAVIOR_TYPE.INTERNATIONAL
	 * @return 
	 */
	public FlightData bookabilityRequest(FlightData data, BEHAVIOR_TYPE type);
	
	/**
	 *    价格验证
	 * @param data 
	 * @param type BEHAVIOR_TYPE.DOMISTIC or BEHAVIOR_TYPE.INTERNATIONAL
	 * @return 
	 */
	public FlightData repricingRequest(FlightData data, BEHAVIOR_TYPE type);
	
	/**
	 *    生成订单
	 * @param data 
	 * @param type BEHAVIOR_TYPE.DOMISTIC or BEHAVIOR_TYPE.INTERNATIONAL
	 * @return 
	 */
	public FlightData  createOrder(FlightData data, BEHAVIOR_TYPE type);
	
	/**
	 *    提交订单
	 * @param data
	 * @param type BEHAVIOR_TYPE.DOMISTIC or BEHAVIOR_TYPE.INTERNATIONAL 
	 * @return
	 */
	public FlightData submitOrder(FlightData data, BEHAVIOR_TYPE type);
	
	/**
	 *    取消订单
	 * @param data
	 * @param type BEHAVIOR_TYPE.DOMISTIC or BEHAVIOR_TYPE.INTERNATIONAL 
	 * @return
	 */
	public FlightData cancelOrder(FlightData data, BEHAVIOR_TYPE type);
	
	/**
	 *    订单列表查询
	 * @param data
	 * @return
	 */
	public FlightData queryOrderList(FlightData data);
	
	
	/**
	 *    订单详情查询
	 * @param data
	 * @param type BEHAVIOR_TYPE.DOMISTIC or BEHAVIOR_TYPE.INTERNATIONAL 
	 * @return
	 */
	public FlightData queryOrderDetail(FlightData data, BEHAVIOR_TYPE type);
	
	/**
	 *  获取某一条航班数据
	 * @param id
	 * @return
	 */
	public FlightData getFlightData(String id);
}
