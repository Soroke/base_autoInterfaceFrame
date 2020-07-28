package net.faxuan.bot.objectInfo;

import java.util.List;
import java.util.Map;

/**
 * 问答接口返回 对象
 */
public class QAResponse {
    //本次请求的问题
    private String xmindTitle;
    private String sessionId;

    private String msg;

    //返回结果中的问题
    private String question;
    //返回结果中的选项列表
    private List<Map<Object, Object>> roundsSelList;

    private String isEnd = "false";

    public String getXmindTitle() {
        return xmindTitle;
    }

    public void setXmindTitle(String xmindTitle) {
        this.xmindTitle = xmindTitle;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<Map<Object, Object>> getRoundsSelList() {
        return roundsSelList;
    }

    public void setRoundsSelList(List<Map<Object, Object>> roundsSelList) {
        this.roundsSelList = roundsSelList;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getIsEnd() {
        return isEnd;
    }

    public void setIsEnd(String isEnd) {
        this.isEnd = isEnd;
    }
}
