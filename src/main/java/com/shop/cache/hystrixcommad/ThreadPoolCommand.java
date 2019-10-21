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

public class ThreadPoolCommand extends HystrixCommand<ProductInfo> {

	private Long productId;

	public ThreadPoolCommand(Long productId) {
		super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("ProductInfoService"))
				.andCommandKey(HystrixCommandKey.Factory.asKey("GetProductInfoCommand"))
				.andThreadPoolKey(HystrixThreadPoolKey.Factory.asKey("GetProductInfoPool"))
				.andThreadPoolPropertiesDefaults(HystrixThreadPoolProperties.Setter()
						.withCoreSize(10)
						.withMaxQueueSize(12)
						.withQueueSizeRejectionThreshold(15)) //这个优先级没有maxQueueSize的优先级高
				.andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
						/*.withExecutionIsolationStrategy(HystrixCommandProperties.ExecutionIsolationStrategy.THREAD)*/ //并发量采用线程策略
						/*.withExecutionIsolationStrategy(HystrixCommandProperties.ExecutionIsolationStrategy.SEMAPHORE) //信号量策略*/
						.withCircuitBreakerRequestVolumeThreshold(3000)
						.withCircuitBreakerErrorThresholdPercentage(40)
						.withCircuitBreakerSleepWindowInMilliseconds(3000)
						.withExecutionTimeoutInMilliseconds(20000)
						.withFallbackIsolationSemaphoreMaxConcurrentRequests(30)
				)
		);
		this.productId = productId;
	}
	
	@Override
	protected ProductInfo run() throws Exception {
		System.out.println("调用接口，查询商品数据，productId=" + productId);

		if(productId.equals(-1L)) {
			throw new Exception();
		}

		if(productId.equals(-2L)) {
			Thread.sleep(3000);
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
