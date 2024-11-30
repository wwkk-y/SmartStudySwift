package com.sss.cms.service;

import cn.hutool.json.JSONUtil;
import com.alibaba.nacos.shaded.com.google.common.collect.Lists;
import com.sss.cms.feign.SssKaFeign;
import com.sss.cms.vo.MessageQueryVo;
import com.sss.cms.vo.MessageSendVo;
import com.sss.common.vo.KaMsgSendVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class MsgNoticeService {
    @Resource
    private SssKaFeign sssKaFeign;

    /**
     * 发送聊天信息给用户
     */
    public void sendChatMsgToUser(List<Long> userIds, MessageQueryVo msg){
        KaMsgSendVo kaMsgSendVo = new KaMsgSendVo();
        kaMsgSendVo.setUserIds(userIds);
        kaMsgSendVo.setPath("/notice/msg/chat"); // FIXME
        kaMsgSendVo.setBody(JSONUtil.toJsonStr(msg));
        sssKaFeign.send(kaMsgSendVo);
    }
}
