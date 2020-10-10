package com.soapmans.freevpn;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Base64;
import java.util.UUID;

@Service
public class FreeVpnService {


    @Resource
    RestTemplate restTemplate;

    public JSONObject getInfo() throws InterruptedException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());

        JSONObject json = new JSONObject();
        json.put("inviterCode", "WQHMC7");
        json.put("channelNumber", "1001");
        json.put("password", "a123456");
        json.put("deviceId", UUID.randomUUID().toString());
        json.put("appId", "1001");
        json.put("email", UUID.randomUUID().toString() + "@qq.com");

        HttpEntity<String> formEntity = new HttpEntity<String>(json.toString(), headers);
        String response =  restTemplate.postForObject("https://api.1jainlian.xyz:21987/api/app/email/login", formEntity, String.class);
        JSONObject loginJson = JSON.parseObject(response);

        JSONObject loginData = loginJson.getJSONObject("data");
        JSONObject userProfile = loginData.getJSONObject("userProfile");
        JSONObject defaultNode = loginData.getJSONObject("defaultNode");

        String accessToken = loginData.getString("accessToken");
        String expireTime = userProfile.getString("expireTime");

        String protocol = defaultNode.getString("protocol");
        String nodeId = defaultNode.getString("nodeId");


        headers.add("AccessToken", accessToken);

        JSONObject json2 = new JSONObject();
        json2.put("channelNumber", "1001");
        json2.put("protocol", protocol);
        json2.put("nodeId", nodeId);

        HttpEntity<String> formEntity2 = new HttpEntity<String>(json2.toString(), headers);

        Thread.sleep(1*1000);

        String response2 =  restTemplate.postForObject("https://api.1jainlian.xyz:21987/api/app/auth/node/connect", formEntity2, String.class);
        JSONObject nodeJson = JSON.parseObject(response2);

        JSONObject resultJson = new JSONObject();
        resultJson.put("serverIp", nodeJson.getJSONObject("data").getString("serverIp"));
        resultJson.put("serverPort", nodeJson.getJSONObject("data").getString("serverPort"));
        resultJson.put("username", nodeJson.getJSONObject("data").getString("username"));
        resultJson.put("password", nodeJson.getJSONObject("data").getString("password"));
        resultJson.put("expireTime", expireTime);

        return resultJson;
    }

    public String sub() throws UnsupportedEncodingException, InterruptedException {
        JSONObject jsonObject = getInfo();
        String expireTime = jsonObject.getString("expireTime").substring(11);
        String info = jsonObject.getString("username") + ":" + jsonObject.getString("password") + "@" + jsonObject.getString("serverIp") + ":" + jsonObject.getString("serverPort");
        String result = "https://" + Base64.getEncoder().encodeToString(info.getBytes()) + "?cert=&peer=#" + expireTime;
        return Base64.getEncoder().encodeToString(result.getBytes());
    }

}
