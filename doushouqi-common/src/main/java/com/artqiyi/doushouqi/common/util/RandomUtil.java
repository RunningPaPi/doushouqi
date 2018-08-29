package com.artqiyi.doushouqi.common.util;

import java.util.HashSet;
import java.util.Set;

/**
 * 随机数生成工具类
 */
public class RandomUtil {

    private static final char chars[] = {'J', '4', 'B', 'X', 'E', 'V', 'F', 'G', 'W', 'I', 'A', 'K', '7', '5', 'N', 'P', 'Q', 'R',
            'S', '3', 'U', '1', 'C', '2', 'T', '6', 'H', 'Y', '8', 'M', 'L', 'D'};
    private static final char nums[] = {'4', '2', '5', '1', '0', '6', '9', '7', '3', '8'};

    public static Long genUserId(long id, int len) {
        char[] a = new char[len];
        for (int i = 0; i < len; i++) {
            int pow = (int) Math.pow(nums.length, i);
            a[i] = charAtNums0((int) (id / pow % nums.length) + i);
        }
        for (int i = 0; i < len/2; i++) {
            char temp = a[i];
            a[i] = a[len-1-i];
            a[len-1-i] = temp;
        }
        return Long.valueOf(String.valueOf(a));
    }
    private static char charAtNums0(int index) {
        return index < nums.length ? nums[index] : nums[index - nums.length];
    }

    /**
     * 根据id生成邀请码
     *
     * @param id
     * @param len
     * @return
     */
    public static String genInviteCode(long id, int len) {
        char[] a = new char[len];
        for (int i = 0; i < a.length; i++) {
            int pow = (int) Math.pow(chars.length, i);
            a[i] = charAtStuff((int) (id / pow % chars.length) + i);
        }
        return String.valueOf(a);
    }

    private static char charAtStuff(int index) {
        return index < chars.length ? chars[index] : chars[index - chars.length];
    }


    public static void main(String[] args) {
//        Set set = new HashSet<Integer>();
//        System.out.println(genUserId(1, 6));
//        System.out.println(genUserId(900000, 6));
//        System.out.println(genUserId(900001, 6));
//        System.out.println(genUserId(900002, 6));
//        System.out.println(genUserId(3, 6));
//        for (int i = 0; i < 900000; i++) {
//            Long aLong = genUserId(i, 6);
//            if (aLong<100000){
//                System.out.println(i);
//                break;
//            }
//            set.add(aLong);
//
//        }
//        System.out.println(set.size());

        System.out.println("80e71e8ffe2379864552416823ea3996".length());
        System.out.println(genInviteCode(12151515555L,32));
    }

}
