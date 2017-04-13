package com.wechat.web;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.thoughtworks.xstream.XStream;
import com.wechat.bean.TextMsg;

@Controller
public class AuthController {
	
	@RequestMapping(value = "/auth/authToken")
	@ResponseBody
	public String authToken(
			@RequestParam(value = "signature",required = false) String signature,
			@RequestParam( value = "timestamp",required = false) String timestamp,
			@RequestParam( value = "nonce",required = false) String nonce,
			@RequestParam( value = "echostr",required = false) String echostr,
			HttpServletRequest request,
			HttpServletResponse reponse) {
		if(request.getMethod().toLowerCase().equals("get")){
			String array[] = new String[]{signature,timestamp,nonce};
			Arrays.sort(array);
			//if(signature == ShaUtils.getSha1(StringUtils.join(array))){
			//}
			return echostr;
		}else{
			
			try {
				Map<String,String> resData = xmlToMap(request.getInputStream());
				TextMsg textMsg = new TextMsg();
				textMsg.setContent("hello");
				textMsg.setFromUserName(resData.get("ToUserName"));
				textMsg.setToUserName(resData.get("FromUserName"));
				textMsg.setCreateTime(new Date().getTime());
				textMsg.setMsgType("text");
				XStream xs = new XStream();
				xs.alias("xml", textMsg.getClass());
				return xs.toXML(textMsg);
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return "success";
		}
	}
	
	private Map<String,String> xmlToMap(InputStream in){
		Map<String,String> result = new HashMap<String,String>();
		SAXReader reader = new SAXReader();
		Document document = null;
		try{
			document = reader.read(in);
		}catch (DocumentException e1) { 
			e1.printStackTrace();
		} 
		Element root = document.getRootElement();
		List<Element> elementList = root.elements();
		for(Element e: elementList){
			result.put(e.getName(), e.getText());
		}
		return result;
	}
	
}
