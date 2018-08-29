package com.artqiyi.doushouqi.game;

import com.artqiyi.doushouqi.common.constant.ResponseCodeConstant;
import com.artqiyi.doushouqi.game.enums.CopyWriterEnum;
import com.artqiyi.doushouqi.game.vo.GameUserRecord;
import com.artqiyi.doushouqi.response.UserResponse;
import com.artqiyi.doushouqi.user.domain.User;
import com.artqiyi.doushouqi.user.domain.UserExample;
import com.artqiyi.doushouqi.user.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@RestController
@RequestMapping("/api/game")
public class GameController {
    @Autowired
    private GameStatisticsService gameStatisticsService;
    @Autowired
    private IUserService userService;

    @GetMapping("/AI")
    public UserResponse getAIPlayer(){
        UserResponse response = new UserResponse();
        Random random = new Random();
        long r = random.nextInt(119)+1;
        UserExample userExample = new UserExample();
        userExample.or().andIdEqualTo(r);
        User user = userService.selectById(r);
        Map map = new HashMap(2);
        map.put("headUrl", user.getHeadPicUrl());
        map.put("nickName", user.getNickName());
        map.put("gender", user.getGender());
        response.setResult(map);
        response.setCode(ResponseCodeConstant.SUCCESS);
        return response;
    }

    @GetMapping("/statistics/{userId}")
    public UserResponse createRoom(@PathVariable("userId") Long userId){
        UserResponse response = new UserResponse();
        GameUserRecord userRecord = gameStatisticsService.getUserRecord(userId);
        response.setCode(ResponseCodeConstant.SUCCESS);
        Map map = new HashMap(2);
        map.put("playTimes", userRecord.getPlayTimes());
        response.setResult(map);
        return response;
    }

    @GetMapping("/copyWriters")
    public UserResponse get(){
        int length = CopyWriterEnum.values().length;

        Random r = new Random();
        int code = r.nextInt(length)+1;

        for (CopyWriterEnum enumi : CopyWriterEnum.values()) {
            if (enumi.getCode()== code){
                UserResponse response = new UserResponse();
                response.setCode(ResponseCodeConstant.SUCCESS);
                Map map = new HashMap(2);
                map.put("text",enumi.getText());
                map.put("url",enumi.getUrl());
                response.setResult(map);
                return response;
            }
        }

        return null;
    }

}
