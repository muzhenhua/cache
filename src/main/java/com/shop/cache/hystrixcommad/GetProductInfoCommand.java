package com.shop.cache.hystrixcommad;

import com.alibaba.fastjson.JSONObject;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.shop.cache.entity.ProductInfo;
import com.shop.cache.http.HttpClientUtils;

public class GetProductInfoCommand extends HystrixCommand<ProductInfo> {

	private Long productId;
	
	public GetProductInfoCommand(Long productId) {
		super(HystrixCommandGroupKey.Factory.asKey("GetProductInfoGroup"));
		this.productId = productId;
	}
	
	@Override
	protected ProductInfo run() throws Exception {
		System.out.println("GetProductInfoCommand运行run");
		String url = "http://127.0.0.1:8081/getProductInfo?productId=" + productId;
		String response = HttpClientUtils.sendGetRequest(url);
		return JSONObject.parseObject(response, ProductInfo.class);  
	}

}
