package com.iig.gcp.constants;

import java.util.Random;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Test {

	private String[] jsonRandomArr() {
		// TODO Auto-generated method stub
		JSONObject jsonObject= new JSONObject();
		Random rand = new Random();
		int n = rand.nextInt(50);
		
		jsonObject.put("psid", "43951158");
		jsonObject.put("roll_cnt", "3438586349");
		jsonObject.put("name", "test");
		jsonObject.put("loc", "london");
		String[] b=new String[n];
		
		for(int i=1;i<b.length;++i)
		{
			System.out.println(i);
			b[i]=jsonObject.toString();
			System.out.println("inside");
		}
		n += 1;
		return b;
		
	}
	public static void main(String args[]){
		
		Test test=new Test();
		
		String[] a=test.jsonRandomArr();
		//System.out.println(a[1]);
		for(int i=1;i<a.length;++i)
		{
			//JSONObject jsonObject1= new JSONObject(a[i]);
			
			JSONParser parser = new JSONParser();
			JSONObject json;
			try {
				json = (JSONObject) parser.parse(a[i]);
				System.out.println(json.get("psid"));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
