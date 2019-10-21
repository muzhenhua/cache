package com.shop.cache.hystrixcommad;

import com.shop.cache.entity.ProductInfo;
import com.shop.cache.http.HttpClientUtils;
import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

import com.alibaba.fastjson.JSONObject;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixObservableCommand;

public class GetProductInfosCommand extends HystrixObservableCommand<ProductInfo> {

	private String[] productIds;
	
	public GetProductInfosCommand(String[] productIds) {
		super(HystrixCommandGroupKey.Factory.asKey("GetProductInfoGroup"));
		this.productIds = productIds;
	}
	
	@Override
	protected Observable<ProductInfo> construct() {
        System.out.println("in construct! thread:" + Thread.currentThread().getName());
		return Observable.create(new Observable.OnSubscribe<ProductInfo>() {

			public void call(Subscriber<? super ProductInfo> observer) {
				try {
                    System.out.println("in call of construct! thread:" + Thread.currentThread().getName());
                    if (!observer.isUnsubscribed()) {
                        for(String productId : productIds) {
                            String url = "http://127.0.0.1:8081/getProductInfo?productId=" + productId;
                            String response = HttpClientUtils.sendGetRequest(url);
                            ProductInfo productInfo = JSONObject.parseObject(response, ProductInfo.class);
                            observer.onNext(productInfo);
                            System.out.println("onNext! thread:" + Thread.currentThread().getName());
                        }
                    }
					observer.onCompleted();
				} catch (Exception e) {
					observer.onError(e);  
				}
			}
			
		}).subscribeOn(Schedulers.io());
	}

}
