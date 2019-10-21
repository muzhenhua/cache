package com.shop;


import com.shop.cache.http.HttpClientUtils;

public class CircuitBreakerTest {
	
	public static void main(String[] args) throws Exception {
		for(int i = 0; i < 15; i++) {
			String response = HttpClientUtils.sendGetRequest("http://localhost:8081/getCircuitBreaker?productId="+(i+1));
			System.out.println("第" + (i + 1) + "次请求，结果为：" + response);  
		}
		for(int i = 0; i < 14; i++) {
			String response = HttpClientUtils.sendGetRequest("http://localhost:8081/getCircuitBreaker?productId=-1");
			System.out.println("第" + (i + 1) + "次请求，结果为：" + response);  
		}
		Thread.sleep(5000);
		System.out.println("睡5秒钟------------------");
		for(int i = 0; i < 10; i++) {
			String response = HttpClientUtils.sendGetRequest("http://localhost:8081/getCircuitBreaker?productId=-2");
			System.out.println("第" + (i + 1) + "次请求，结果为：" + response);  
		}
		// 统计单位，时间窗口，到了一定比例，才会短路
		System.out.println("尝试等待2秒钟。。。。。。");
		Thread.sleep(2000);
		for(int i = 0; i < 10; i++) {
			String response = HttpClientUtils.sendGetRequest("http://localhost:8081/getCircuitBreaker?productId="+(i+1));
			System.out.println("第" + (i + 1) + "次请求，结果为：" + response);  
		}
		System.out.println("尝试等待3秒钟。。。。。。");
		Thread.sleep(3000);


		for(int i = 0; i < 20; i++) {
			String response1 = HttpClientUtils.sendGetRequest("http://localhost:8081/getCircuitBreaker?productId=-1");

			System.out.println("第" + (i + 1) + "次请求，结果为：" + response1);
			String response = HttpClientUtils.sendGetRequest("http://localhost:8081/getCircuitBreaker?productId="+(i+1));
			System.out.println("第" + (i + 1) + "次请求，结果为：" + response);

		}

		for(int i = 0; i < 6; i++) {
			String response = HttpClientUtils.sendGetRequest("http://localhost:8081/getCircuitBreaker?productId="+(i+1));
			System.out.println("第" + (i + 1) + "次请求，结果为：" + response);
		}
	}
	
}
