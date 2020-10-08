package com.soapmans.freevpn;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;

@RestController
public class Controller {

    @Resource
    FreeVpnService freeVpnService;

    @RequestMapping(value = "/")
    public String vpn(){
        return freeVpnService.getInfo().toJSONString();
    }

    @RequestMapping(value = "/sub")
    public String sub() throws UnsupportedEncodingException {
        return freeVpnService.sub();
    }
}
