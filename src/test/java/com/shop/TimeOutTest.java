package com.shop;


import com.shop.cache.http.HttpClientUtils;

public class TimeOutTest {
	
	public static void main(String[] args) throws Exception {
			HttpClientUtils.sendGetRequest("http://localhost:8081/getTimeOut?productId=-2");
	}
	
}
