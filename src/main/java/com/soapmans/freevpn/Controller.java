package com.soapmans.freevpn;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.UUID;

@RestController
public class Controller {

    @Resource
    RestTemplate restTemplate;

    @RequestMapping(value = "/")
    public JSONObject vpn(){

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


        HttpHeaders headers2 = new HttpHeaders();
        headers2.setContentType(MediaType.APPLICATION_JSON);
        headers2.add("Accept", MediaType.APPLICATION_JSON.toString());
        headers2.add("AccessToken", accessToken);

        JSONObject json2 = new JSONObject();
        json2.put("channelNumber", "1001");
        json2.put("protocol", protocol);
        json2.put("nodeId", nodeId);

        HttpEntity<String> formEntity2 = new HttpEntity<String>(json2.toString(), headers2);
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
}
