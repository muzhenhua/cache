package com.shop;


import com.shop.cache.http.HttpClientUtils;

public class FallBackTest {
	
	public static void main(String[] args) throws Exception {
		for(int i = 0; i < 30; i++) {
			new FallBackTest.TestThread(i).start();
		}
	}

	private static class TestThread extends Thread {

		private int index;

		public TestThread(int index) {
			this.index = index;
		}

		@Override
		public void run() {
			String response = HttpClientUtils.sendGetRequest("http://localhost:8081/getFallBack?brandId=1");
			System.out.println("第" + (index + 1) + "次请求，结果为：" + response);
		}

	}
	
}
