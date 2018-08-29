package com.artqiyi.doushouqi.system.controller;

import com.artqiyi.doushouqi.common.constant.ResponseCodeConstant;
import com.artqiyi.doushouqi.response.UserResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/system")
public class SystemController {

    @GetMapping("/now")
    public UserResponse nowTime(){
        UserResponse rsp = new UserResponse();
        Map map = new HashMap(2);
        map.put("now",System.currentTimeMillis());
        rsp.setResult(map);
        rsp.setCode(ResponseCodeConstant.SUCCESS);
        return rsp;
    }
}
