package com.shop.cache.controller;

import com.alibaba.fastjson.JSON;
import com.shop.cache.entity.ProductInfo;
import com.shop.cache.http.HttpClientUtils;
import com.shop.cache.hystrixcommad.CircuitBreakerCommand;
import com.shop.cache.hystrixcommad.GetBrandNameCommand;
import com.shop.cache.hystrixcommad.GetProductInfoCommand;
import com.shop.cache.hystrixcommad.GetProductInfosCommand;
import com.shop.cache.hystrixcommad.ThreadPoolCommand;
import com.shop.cache.hystrixcommad.TimeOutCommand;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import rx.Observable;
import rx.Observer;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixObservableCommand;
import rx.functions.Action1;

import java.util.concurrent.Future;

@Controller
public class CacheController {

	@RequestMapping("/change/product")
	@ResponseBody
	public String changeProduct(Long productId) {
		String url = "http://127.0.0.1:8081/getProductInfo?productId=" + productId;
		String response = HttpClientUtils.sendGetRequest(url);
		System.out.println(response);  
		
		return "success";
	}
	
	@RequestMapping("/getProductInfo_single")
	@ResponseBody
	public String getProductInfo(Long productId) {
		HystrixCommand<ProductInfo> getProductInfoCommand = new GetProductInfoCommand(productId);
		//execute()：以同步堵塞方式执行run()。调用execute()后，hystrix先创建一个新线程运行run()，接着调用程序要在execute()调用处一直堵塞着，直到run()运行完成
		ProductInfo productInfo = getProductInfoCommand.execute();
		System.out.println("execute运行方式");
		//queue()：以异步非堵塞方式执行run()。一调用queue()就直接返回一个Future对象，同时hystrix创建一个新线程运行run()，调用程序通过Future.get()拿到run()的返回结果，而Future.get()是堵塞执行的
//		Future<ProductInfo> future = getProductInfoCommand.queue();
//		System.out.println("queue运行方式");
//		ProductInfo productInfo =null;
//
//		try {
//			productInfo=future.get();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		
		System.out.println(productInfo);  
		return "success";
	}

	@RequestMapping("/getProductInfo_observe")
	@ResponseBody
	public String getProductInfoObserve(Long productId) {
		HystrixCommand<ProductInfo> getProductInfoCommand = new GetProductInfoCommand(productId);
		Observable<ProductInfo> observable = getProductInfoCommand.observe();
		System.out.println("observe方式运行");
		observable.subscribe(new Observer<ProductInfo>() {

			public void onCompleted() {
				System.out.println("获取完了所有的商品数据");
			}

			public void onError(Throwable e) {
				e.printStackTrace();
			}

			public void onNext(ProductInfo productInfo) {
				System.out.println(productInfo);
			}

		});
		System.out.println("to be calling...");
		observable.subscribe(new Action1<ProductInfo>() {
			@Override
			public void call(ProductInfo productInfo) {
				System.out.println("call:"+productInfo);
			}
		});

		return "success";
	}

	@RequestMapping("/getProductInfo_toObservable")
	@ResponseBody
	public String getProductInfoToObservable(Long productId) {
		HystrixCommand<ProductInfo> getProductInfoCommand = new GetProductInfoCommand(productId);
		Observable<ProductInfo> observable = getProductInfoCommand.toObservable();
		System.out.println("toObservable方式运行");
		observable.subscribe(new Observer<ProductInfo>() {

			public void onCompleted() {
				System.out.println("获取完了所有的商品数据");
			}

			public void onError(Throwable e) {
				e.printStackTrace();
			}

			public void onNext(ProductInfo productInfo) {
				System.out.println(productInfo);
			}

		});
		System.out.println("to be calling...");
//		observable.subscribe(new Action1<ProductInfo>() {
//			@Override
//			public void call(ProductInfo productInfo) {
//				System.out.println("call:"+productInfo);
//			}
//		});

		return "success";
	}
	
	/**
	 * 查询多条商品
	 */
	@RequestMapping("/getProductInfos")
	@ResponseBody
	public String getProductInfos(String productIds) {
		HystrixObservableCommand<ProductInfo> getProductInfosCommand = 
				new GetProductInfosCommand(productIds.split(","));
		//observe()：事件注册前执行run()/construct()。
		// 第一步是事件注册前，先调用observe()自动触发执行run()/construct()
		// （如果继承的是HystrixCommand，hystrix将创建新线程非堵塞执行run()；
		// 如果继承的是HystrixObservableCommand，将以调用程序线程堵塞执行construct()）
		// 第二步是从observe()返回后调用程序调用subscribe()完成事件注册，如果run()/construct()执行成功则触发onNext()和onCompleted()，如果执行异常则触发onError()
//		Observable<ProductInfo> observable = getProductInfosCommand.observe();
//		System.out.println("to be calling...");
		//toObservable()：事件注册后执行run()/construct()。
		// 第一步是事件注册前，一调用toObservable()就直接返回一个Observable<String>对象，
		// 第二步调用subscribe()完成事件注册后自动触发执行run()/construct()（
		// 如果继承的是HystrixCommand，hystrix将创建新线程非堵塞执行run()，调用程序不必等待run()；
		// 如果继承的是HystrixObservableCommand，将以调用程序线程堵塞执行construct()，调用程序等待construct()执行完才能继续往下走），
		// 如果run()/construct()执行成功则触发onNext()和onCompleted()，如果执行异常则触发onError()
		Observable<ProductInfo> observable = getProductInfosCommand.toObservable(); // 还没有执行，等到调用subscribe然后才会执行
		System.out.println("to be calling..."+Thread.currentThread().getName());
		observable.subscribe(new Observer<ProductInfo>() {

			public void onCompleted() {
				System.out.println("获取完了所有的商品数据");
			}

			public void onError(Throwable e) {
				e.printStackTrace();
			}

			public void onNext(ProductInfo productInfo) {
				System.out.println(productInfo);  
			}
			
		});
		return "success";
	}



	@RequestMapping("/getFallBack")
	@ResponseBody
	public String getFallBack(Long brandId) {
		HystrixCommand<String> getProductInfoCommand = new GetBrandNameCommand(brandId);
		String brandName = getProductInfoCommand.execute();
		return brandName;
	}

	@RequestMapping("/getCircuitBreaker")
	@ResponseBody
	public String getCircuitBreaker(Long productId) {
		HystrixCommand<ProductInfo> getProductInfoCommand = new CircuitBreakerCommand(productId);
		ProductInfo productInfo = getProductInfoCommand.execute();


		System.out.println(productInfo);
		return "success";
	}

	@RequestMapping("/getThreadPool")
	@ResponseBody
	public String getThreadPool(Long productId) {
		HystrixCommand<ProductInfo> getProductInfoCommand = new ThreadPoolCommand(productId);
		ProductInfo productInfo = getProductInfoCommand.execute();

		System.out.println(productInfo);
		return "success";
	}

	@RequestMapping("/getTimeOut")
	@ResponseBody
	public String getTimeOut(Long productId) {
		HystrixCommand<ProductInfo> getProductInfoCommand = new TimeOutCommand(productId);
		ProductInfo productInfo = getProductInfoCommand.execute();
		String s = JSON.toJSONString(productInfo);
		System.out.println(s);
		return "success";
	}
	
}
