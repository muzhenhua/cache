package com.shop.cache.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ProductController {

	@RequestMapping("/getProductInfo")
	@ResponseBody
	public String getProductInfo(Long productId) {
		return "{\"id\": " + productId + ", \"name\": \"华为\", \"price\": 9999, \"color\": \"红色\", \"size\": \"6.5\"}";
	}
	
}
