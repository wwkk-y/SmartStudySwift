package com.sss.common.constant;

public class RmsConst {
    public static final String PLACE_ORDER_PREFIX = "order:place:"; // 用户下订单 redis key prefix

    public static final String AWARD_INVENTORY_PREFIX = "award:inventory:"; // 商品库存 redis key prefix

    public static final String ORDER_PLACE_TOPIC = "order-place-topic"; // 下单消息

    public static final String AWARD_INVENTORY_SYNC_LOCK = "lock:inventory"; // 同步库存 redis key 确保只有一个在同步, 且同步时不允许购买该商品

    private static final String ORDER_PLACE_KEY_GAP = "--place order-->";

    public static final String ORDER_AUTO_CANCEL_TOPIC = "order-auto-cancel-topic"; // 订单自动取消消息

    /**
     * 生成下单消息key
     * @param uid 用户id
     * @param awardId 奖品id
     * @return key
     */
    public static String generateOrderPlaceKey(long uid, long awardId){
        return uid + ORDER_PLACE_KEY_GAP + awardId;
    }

    /**
     * 解析下单消息key
     * @param key key
     * @return 解析成功 -> [用户id, 奖品id] | 解析失败 -> null
     */
    public static long[] parseOrderPlaceKey(String key){
        if(key == null){
            return null;
        }
        String[] split = key.split(ORDER_PLACE_KEY_GAP);
        if(split.length != 2){
            return null;
        }
        long[] ans = new long[2];
        try{
            ans[0] = Long.parseLong(split[0]);
            ans[1] = Long.parseLong(split[1]);
        } catch (NumberFormatException e){
            return null;
        }

        return ans;
    }
}
