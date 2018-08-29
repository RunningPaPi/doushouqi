package com.artqiyi.doushouqi.game;

import com.artqiyi.doushouqi.game.vo.PosData;
import sun.dc.pr.PRError;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public enum DoushouqiGameService {
    INSTANCE;

    private Map<Long, Long> userAgainstMap = new ConcurrentHashMap<>();

    private Map<Long, String> fightRoomNo = new ConcurrentHashMap<>();

    private Long matchUserId;

    private Map<Long, String> giveUp = new ConcurrentHashMap<>();

    public void addAgainst(Long userId, Long againstId) {
        userAgainstMap.put(userId, againstId);
    }

    public Long getAgainstId(Long userId) {
        return userAgainstMap.get(userId);
    }

    public void removeAgainst(Long userId) {
        userAgainstMap.remove(userId);
    }


    public void addRoomNo(Long userId, String roomNo) {
        fightRoomNo.put(userId, roomNo);
    }

    public void removeRoomNo(Long userId) {
        fightRoomNo.remove(userId);
    }

    public String getRoomNo(Long userId) {
        return fightRoomNo.get(userId);
    }

    public Long getMatchUserId() {
        return matchUserId;
    }

    public void setMatchUserId(Long matchUserId) {
        this.matchUserId = matchUserId;
    }

    private static List<Integer> animals = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7);

    public List<PosData> genGameData(Long userId, Long againstId) {
        List data = new ArrayList();
        genData(userId, data);
        genData(againstId, data);

        Collections.shuffle(data);
        Collections.shuffle(data);
        return data;
    }

    private void genData(Long userId, List data) {
        for (int i = 0; i < animals.size(); i++) {
            PosData posData = new PosData();
            posData.setUserId(userId);
            posData.setType(i);
            data.add(posData);
        }
    }

    public String getGiveUp(Long userId) {
        return giveUp.get(userId);
    }

    public void removeGiveUp(Long userId){
        giveUp.remove(userId);
    }

    public static void main(String[] args) {
        System.out.println(INSTANCE.genGameData(1L, 2L));
    }

    public void addGiveUp(Long userId) {
        giveUp.put(userId,"gg");
    }


}
