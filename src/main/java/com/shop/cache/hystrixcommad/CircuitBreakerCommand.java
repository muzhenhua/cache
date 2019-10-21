package com.shop.cache.hystrixcommad;

import com.alibaba.fastjson.JSONObject;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.HystrixThreadPoolKey;
import com.netflix.hystrix.HystrixThreadPoolProperties;
import com.shop.cache.entity.ProductInfo;
import com.shop.cache.http.HttpClientUtils;

public class CircuitBreakerCommand extends HystrixCommand<ProductInfo> {

	private Long productId;

	public CircuitBreakerCommand(Long productId) {
		super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("ProductInfoService"))
				.andCommandKey(HystrixCommandKey.Factory.asKey("GetProductInfoCommand"))
				.andThreadPoolKey(HystrixThreadPoolKey.Factory.asKey("GetProductInfoPool"))
				.andThreadPoolPropertiesDefaults(HystrixThreadPoolProperties.Setter()
						.withCoreSize(15)
						.withQueueSizeRejectionThreshold(10))
				.andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
						.withCircuitBreakerRequestVolumeThreshold(30)//至少30个请求熔断器才进行错误率的计算
						.withCircuitBreakerErrorThresholdPercentage(40)//异常请求量的百分比，达到开启熔断保护
						.withCircuitBreakerSleepWindowInMilliseconds(3000)  //短路后，3秒内直接reject请求，3秒后会进入半打开状态,放部分流量过去重试
				        .withMetricsRollingStatisticalWindowInMilliseconds(2000)) //配置采样统计滚转时间窗口，默认为10s。
		);
		this.productId = productId;
	}
	
	@Override
	protected ProductInfo run() throws Exception {
		System.out.println("调接口，查商品，productId=" + productId);

		if(productId.equals(-1L)) {
			throw new Exception();
		}

		if(productId.equals(-2L)) {
			throw new Exception();
		}

		String url = "http://127.0.0.1:8081/getProductInfo?productId=" + productId;
		String response = HttpClientUtils.sendGetRequest(url);
		return JSONObject.parseObject(response, ProductInfo.class);  
	}

	@Override
	protected ProductInfo getFallback() {
		ProductInfo productInfo = new ProductInfo();
		productInfo.setName("降级商品");
		return productInfo;
	}

}
