package com.shop.cache.hystrixcommad;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.HystrixThreadPoolKey;
import com.netflix.hystrix.HystrixThreadPoolProperties;

public class GetBrandNameCommand extends HystrixCommand<String> {
	
	private Long brandId;
	
	public GetBrandNameCommand(Long brandId) {
		super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("BrandInfoService"))
				.andCommandKey(HystrixCommandKey.Factory.asKey("GetBrandNameCommand"))
				.andThreadPoolKey(HystrixThreadPoolKey.Factory.asKey("GetBrandInfoPool"))
				.andThreadPoolPropertiesDefaults(HystrixThreadPoolProperties.Setter()
						.withCoreSize(15)
						.withQueueSizeRejectionThreshold(10))
				.andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
						.withFallbackIsolationSemaphoreMaxConcurrentRequests(2))
				);  
		this.brandId = brandId;
	}
	
	@Override
	protected String run() throws Exception {
		System.out.println("brandCommad---run");
		throw new Exception();
	}
	
	@Override
	protected String getFallback() {
//		try {
//			Thread.sleep(500);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
		System.out.println("降级获取品牌，brandId=" + brandId);
		return "降级品牌";
	}

}
