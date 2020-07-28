package net.faxuan.example;

import net.faxuan.bot.objectInfo.Case;
import net.faxuan.core.Http;
import net.faxuan.core.Response;
import net.faxuan.util.JsonHelper;
import net.faxuan.util.XmindUtil;
import org.xmind.core.CoreException;

import java.io.IOException;
import java.util.*;

public class Init {
    private String sessionId = "";
    private static List<Case> cases = new ArrayList<>();

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public List<Case> getCase() {
        return this.cases;
    }


    /**
     * 初始化sessionId
     */
    public String getSessionId() {
        Map<Object,Object> params = new HashMap<>();
        params.put("robotId","2");
        params.put("guestId","test");

        Map<Object,Object> header = new HashMap<>();
        header.put("chatKey","112233445566");
        Http.setHeader(header);

        Response response = Http.post("http://botms.t.faxuan.net/bot/1.0/qa/init",params);
        sessionId = JsonHelper.getValue(response.getBody(),"sessionId").toString();
        return sessionId;

    }


    /**
     * 初始化用例对象
     * @throws IOException
     * @throws CoreException
     */
    public void setCases(String xmindPaht)  throws IOException, CoreException {

        //获取xmxind对象数据，并获取标题名称
        XmindUtil.setList(new ArrayList<>());
        List<String> lists = XmindUtil.xmindToList(xmindPaht);
        String[] title1 = lists.get(0).split("————");
        String[] title2 = title1[0].split("\r\n");
        String title = title2[0].split("：")[1] + "-" + title2[1].split("：")[1];

        //打印所有问题详情
        for(String list:lists) {
            //用例对象
            Case case1 = new Case();
            //设置用例xmind标题
            case1.setName(title);
            //获取问答详情
            String[] QA = list.split("————");
            String[] QAs = new String[QA.length - 1];
            //去除xmind总标题
            System.arraycopy(QA, 1, QAs, 0, QA.length - 1);

            //记录文档详情
            List<Map<Object, Object>> questionAswers = new ArrayList<>();
            for (int i = 0; i < QAs.length; i += 2) {
                if (i == QAs.length - 1) {
                    case1.setReport(QAs[i].split("报告内容：")[1].replaceAll("\n","").replaceAll("\r","").replaceAll("　"," ").trim());
                } else {
                    Map<Object, Object> questionAswer = new HashMap<>();
                    String q = QAs[i];
                    String a = QAs[i + 1];
                    if (q.contains("报告标题") && q.contains("\n")) {
                        q = q.split("\n")[0];
                    }
                    if (q.contains("问题：")) {
                        q = q.split("问题：")[1];
                    }
                    if (a.contains("选项：")) {
                        a = a.split("选项：")[1];
                    }
                    //记录问答
//                    System.out.println("Q:" + q + "\tA:" + a);
                    questionAswer.put(q.trim(), a.trim());
                    questionAswers.add(questionAswer);
                    case1.setQuestionAnswer(questionAswers);
                }
            }
            cases.add(case1);
        }

        this.cases = cases;
    }




}
