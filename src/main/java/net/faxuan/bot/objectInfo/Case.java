package net.faxuan.bot.objectInfo;
import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;
import java.util.Map;

/**
 * Created by song on 2018/12/25.
 */
public class Case {
    @JSONField(name = "botName",ordinal = 1)
    private String name;
    @JSONField(name = "questionAnswer",ordinal = 2)
    private List<Map<Object,Object>> questionAnswer;
    @JSONField(name = "report",ordinal = 3)
    private String report;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Map<Object, Object>> getQuestionAnswer() {
        return questionAnswer;
    }

    public void setQuestionAnswer(List<Map<Object, Object>> questionAnswer) {
        this.questionAnswer = questionAnswer;
    }

    public String getReport() {
        return report;
    }

    public void setReport(String report) {
        this.report = report;
    }

    @Override
    public String toString() {
        return "Xmind名称："+name + "\t测试问答："+questionAnswer + "\t问答报告："+report;
    }
}
