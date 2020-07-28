package net.faxuan.util;
import org.testng.annotations.Test;
import org.xmind.core.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XmindUtil {


    @Test
    public void so() throws IOException, CoreException {
        System.out.println(getXmind("C:/Users/123/Desktop/bot2.0需求/xmind/05消费侵权-快递纠纷20190708.xmind"));
    }
    /**
     * 解析xmind
     * @throws IOException
     * @throws CoreException
     */
    public Xmind getXmind(String xmindPaht)  throws IOException, CoreException {

        Xmind xmind = new Xmind();
        //获取xmxind对象数据，并获取标题名称
        XmindUtil.setList(new ArrayList<>());
        List<String> lists = XmindUtil.xmindToList(xmindPaht);
        String[] title1 = lists.get(0).split("————");
        String[] title2 = title1[0].split("\r\n");
        String title = title2[0].split("：")[1] + "-" + title2[1].split("：")[1];
        xmind.setTitle(title);

        Map<Integer, List<String>> pro = new HashMap<>();

        for (int i=0;i<lists.size();i++) {
//        for(String list:lists) {
            List<String> words = new ArrayList<>();
            //获取流程
            String[] process = lists.get(i).split("————");
            String[] proceses = new String[process.length - 1];
            //去除xmind总标题
            System.arraycopy(process, 1, proceses, 0, process.length - 1);
            for (String word:proceses) {
                words.add(word);
            }
            pro.put(i,words);
        }
        xmind.setProceses(pro);
        return xmind;
    }


    /**
     * 获取工作簿
     * IWorkbook：表示整个思维导图
     * @param xmindPath:xmind文件路径
     */
    public static IWorkbook getIWorkbook(String xmindPath) throws IOException, CoreException {
        if (builder == null){
            builder = Core.getWorkbookBuilder();// 初始化builder
        }
        return builder.loadFromFile(new File(xmindPath));
    }

    /**
     * 获取根节点
     * @param  iWorkbook:加载的思维导图
     */
    public static ITopic getRootTopic(IWorkbook iWorkbook){
        return iWorkbook.getPrimarySheet().getRootTopic();
    }

    /**
     * 获取从根目录到每一个叶子节点的的路径
     */
    public static List<String> getAllPath(ITopic rootTopic){
        return getAllPathIter(rootTopic.getTitleText(),rootTopic.getAllChildren());
    }

    public static List<String> getAllPathIter(String parentContext, List<ITopic> childrens){
        for(ITopic children:childrens){
            if(children.getAllChildren().size() == 0){
                list.add(parentContext+"————"+children.getTitleText());
            }else {
                getAllPathIter(parentContext+"————"+children.getTitleText(), children.getAllChildren());
            }
        }
        return list;
    }

    /**
     * 解析Xmind文件
     */
    public static List<String> xmindToList(String xmindPath) throws IOException, CoreException {
        return getAllPath(getRootTopic(getIWorkbook(xmindPath)));
    }

    private static IWorkbookBuilder builder = null;
    private static List<String> list = new ArrayList<>();

    public static List<String> getList() {
        return list;
    }

    public static void setList(List<String> list) {
        XmindUtil.list = list;
    }
}

class Xmind{
    private String title;
//    private List<String> process;

    private Map<Integer,List<String>> proceses;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Map<Integer, List<String>> getProceses() {
        return proceses;
    }

    public void setProceses(Map<Integer, List<String>> proceses) {
        this.proceses = proceses;
    }

    @Override
    public String toString() {
        String info = "";
        info += "xmind标题：" + title + "\t流程如下\n";
        for (int i=0;i<proceses.size();i++) {
            List<String> p = proceses.get(i);
            for (String w:p) {
                info += w + "\t";
            }
            info += "\n";
        }

        return info;
    }
}