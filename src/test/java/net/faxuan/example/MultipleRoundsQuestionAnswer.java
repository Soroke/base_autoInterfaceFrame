package net.faxuan.example;

import net.faxuan.bot.objectInfo.Case;
import net.faxuan.bot.objectInfo.QAResponse;
import net.faxuan.core.Http;
import net.faxuan.core.Response;
import net.faxuan.util.JsonHelper;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.File;
import java.util.*;

public class MultipleRoundsQuestionAnswer extends Init {

    /**
     * BOT问答接口和任务对话接口
     */
    private static final String BOTQA = "http://botms.t.faxuan.net/bot/1.0/qa/answer";
    private static final String BOTRoundsquestionAswer = "http://botms.t.faxuan.net/bot/1.0/qa/answer/roundsquestion";


    @Test(dataProvider = "cases",priority = 1)
    public void bot2(Case caseInfo) {
		String sessionId = super.getSessionId();
        String title = caseInfo.getName();
        QAResponse qaResponse = taskQA20(title,sessionId,"true");
        //xmind读取问答
        for (Map<Object,Object> qas:caseInfo.getQuestionAnswer()) {
            for (Object key:qas.keySet()) {
                //接口返回数据选项列表
                String q = key.toString();
                for (Map<Object,Object> list:qaResponse.getRoundsSelList()) {
                    //校验问题是否一致
//                    Assert.assertEquals(key.toString().trim(),qaResponse.getQuestion().trim());
                    //校验选项，获取选项内容，并请求接口
                    if (qas.get(key).equals(list.get("title"))) {
                        qaResponse = taskQA20(list.get("payload").toString().replaceAll("'","\\\\\""),sessionId,"false");
                    }
                }
            }
        }
        //校验流程是否问答完毕 ，完毕后对比报告内容
        if (qaResponse.getIsEnd().equals("true")) {
            //返回结果中的问题，最后一次为报告内容
            Assert.assertEquals(qaResponse.getQuestion().replaceAll("\r","").replaceAll("　"," ").trim(),caseInfo.getReport().replaceAll("\n","").replaceAll("\r","").replaceAll("　"," ").trim());
        }
    }


    /**
     * 获取xmind中的用例的信息；并将其装载到测试方法中
	 * parallel=true,标识支持多线程运行用例,具体线程数量在testNG.xml文件中配置
     * @return 用例的集合
     */
    @DataProvider(name = "cases",parallel=true)
    @BeforeClass
    public Iterator<Object[]> getCases() {
        //获取xmind文件的路径(xmind在resources目录下)
        String rootPath = System.getProperty("user.dir");
//        String resourcesPath = rootPath + "/classes/xmind/"; //maven运行使用
        String resourcesPath = rootPath + "/src/main/resources/xmind/"; //测试运行使用

        //循环读取所有xmind中的所有问答流程
        File file = new File(resourcesPath);
        File[] files = file.listFiles();
        for (File f:files) {
            try {
                super.setCases(f.getPath());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //将所有问答流程项，做一个形式的转换然后给测试方法
        List<Object[]> cases = new ArrayList<Object[]>();
        for (Object caseInfo : super.getCase()) {
            //做一个形式转换
            cases.add(new Object[] { caseInfo });
        }
        return cases.iterator();
    }


    /**
     * 任务对话接口请求
     * @param question 问题
     * @return 问答对象
     */
    public QAResponse taskQA20(String question, String sessionId ,String isMultiStart) {
        Map<Object,Object> params = new HashMap<>();
        params.put("robotId","2");
        params.put("sessionId",sessionId);
        params.put("payload",question);
        params.put("isMultiStart",isMultiStart);

        Map<Object,Object> header = new HashMap<>();
        header.put("chatKey","112233445566");
        header.put("Content-Type","application/x-www-form-urlencoded");
        Http.setHeader(header);

        Response response = Http.post(BOTRoundsquestionAswer,params);
        return getRoundsquestionAswer(response,question);
    }

    /**
     * 解析任务对话接口返回
     * @param response
     * @param que
     * @return
     */
    private QAResponse getRoundsquestionAswer(Response response, String que) {
        //定义返回对象
        QAResponse QAResponse = new QAResponse();
        //记录请求问题
        QAResponse.setXmindTitle(que);
        //获取返回结果
        String body = response.getBody();

        //解析结果内容(返回结果中的问题、sessionId msg)
        Object question = JsonHelper.getValue(body,"roundAnswer");
        Object sessionId = JsonHelper.getValue(body,"sessionId");
        Object msg = JsonHelper.getValue(body,"msg");
        QAResponse.setQuestion(question.toString());
        QAResponse.setSessionId(sessionId.toString());
        QAResponse.setMsg(msg.toString());

        Object isEnd = JsonHelper.getValue(body,"isEnd");
        QAResponse.setIsEnd(isEnd.toString());
        //获取返回结果中选项列表
        //如果流程解释不再记录选项列表
        if (isEnd.toString().equals("false")) {
            Object aswer = JsonHelper.getValue(body,"roundsSelList");
            //去掉多余字符
            aswer = aswer.toString().replaceAll("\\[","");
            aswer = aswer.toString().replaceAll("]","");
            aswer = aswer.toString().substring(1);
            aswer = aswer.toString().substring(0,aswer.toString().length()-1);
            String[] aswers = aswer.toString().split("},\\{");

            //收集本次请求返回选项
            List<Map<Object,Object>> aswerss = new ArrayList<>();
            for (String as:aswers) {
                //替换\"
                String mas = as.replaceAll("\\\\\"","'");
                //记录选项内容
                Map<Object,Object> qa = new HashMap<>();
                qa.put("title",JsonHelper.getValueN(mas + "}","title").toString());
                qa.put("payload",JsonHelper.getValueN(mas + "}","payload").toString());
                aswerss.add(qa);
            }
            QAResponse.setRoundsSelList(aswerss);
        }

        return QAResponse;
    }
}
