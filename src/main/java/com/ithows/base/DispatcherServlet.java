package com.ithows.base;

import com.google.common.base.Predicates;
import com.ithows.*;
import com.ithows.BaseLogger;

import java.io.*;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Set;
import javax.servlet.*;
import javax.servlet.http.*;
//import org.w3c.dom.*;
import com.ithows.util.HttpUtils;
import java.util.logging.Logger;
import org.apache.commons.io.FilenameUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.*;
import org.reflections.ReflectionUtils;
import org.reflections.Reflections;

/**
 * 클라이언트의 요청을 전달받는다. 컨트롤러에게 클라이언트의 요청을 전달하고 컨트롤러가 반환한 결과 값을 분석하여 알맞은 응답을 생성하도록
 * 한다.
 *
 * @author dreamct2
 */
public class DispatcherServlet extends HttpServlet {

    static {
        try {
            Class.forName("com.ithows.AppConfig");

        } catch (ClassNotFoundException ex) {
            BaseDebug.log(ex, "App1과 App2를 로딩할 수 없습니다.");
        }
    }
    private PageBeanContainer container = null;

    public void init(ServletConfig config) throws ServletException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setIgnoringElementContentWhitespace(true);

        try {
//            String fileName = config.getServletContext().getRealPath("/") + "WEB-INF\\dispatcher-servlet.xml";
            String fileName = config.getServletContext().getRealPath("/") + "WEB-INF/dispatcher-servlet.xml";
            DocumentBuilder dBuilder = factory.newDocumentBuilder();
            Document document = dBuilder.parse(fileName);
            BaseDebug.info("--------------dispacher-servlet.xml 로딩 완료---------------------");

            if (this.container == null) {
                this.container = new PageBeanContainer();
            }
            this.container.clear();//모든 요소를 제거한다.

            // 레이아웃 템플릿을 담는다.
            NodeList tmpList = document.getElementsByTagName("template");
            TemplateContainer tmpContainer = new TemplateContainer();
            if (tmpList != null) {
                for (int i = 0; i < tmpList.getLength(); i++) {
                    Node templateNode = tmpList.item(i);
                    TemplateBean tmpBean = new TemplateBean();
                    tmpBean.setId(templateNode.getAttributes().getNamedItem("id").getNodeValue());
                    tmpBean.setTop(templateNode.getAttributes().getNamedItem("top").getNodeValue());
                    tmpBean.setBottom(templateNode.getAttributes().getNamedItem("bottom").getNodeValue());
                    tmpBean.setTemplatePage(templateNode.getAttributes().getNamedItem("templatePage").getNodeValue());
                    tmpContainer.add(tmpBean.getId(), tmpBean);
                    System.out.println("[" + i + "] Design Templete : " + tmpBean.getId());
                }
            }//controller

            HashMap<Class, Object> ctrlContainer = new HashMap<Class, Object>();

            // 컨트롤러에 있는 어노테이션을 읽어 처리
            Reflections reflections = new Reflections("com.ithows.controller");
            Set<Class<?>> annotated = reflections.getTypesAnnotatedWith(ControllerClassInfo.class);
            String controllerPage = "";
            ControllerClassInfo cci = null;

            for (Class<?> c : annotated) {

                try {
                    cci = c.getAnnotation(ControllerClassInfo.class);
                    controllerPage = cci.controllerPage();
                } catch (Exception e) {
                }
                if (!ctrlContainer.containsKey(c)) {
                    try {
                        Object ctrlObject = c.newInstance();//Controller 객체 설정
                        ctrlContainer.put(c, ctrlObject);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                Set<Method> mset = reflections.getAllMethods(c, Predicates.and(ReflectionUtils.withAnnotation(ControllerMethodInfo.class)));

                for (Method m : mset) {
                    ControllerMethodInfo cm = m.getAnnotation(ControllerMethodInfo.class);

                    PageBean pageBean = new PageBean();
                    pageBean.setId(cm.id());
                    pageBean.setCommandName(cm.commandName());
                    pageBean.setCommandClass(cm.commandClass());
                    pageBean.setVersion(cm.version());

                    //pageBean.setMethodName(m.getName());/*Controller Method Name is saved*/
                    String cPage = cm.controllerPage();
                    if (!cPage.isEmpty()) {
                        controllerPage = cPage;
                    }
                    pageBean.setControllerPage(controllerPage);//Controller Page 설정

                    try {
                        String ctrlClass = cm.controllerClass();
                        if (!ctrlClass.isEmpty() && !ctrlContainer.containsKey(c)) {//Controller Object do not exist
                            Class tmpClass = Class.forName(ctrlClass);
                            Object ctrlObject = tmpClass.newInstance();
                            ctrlContainer.put(tmpClass, ctrlObject);
                            pageBean.setController(ctrlContainer.get(tmpClass));//Set Controller Object
                        } else { //Controller Object do exist
                            pageBean.setController(ctrlContainer.get(c));//Set Controller Object
                        }
                    } catch (Exception e) {
                        BaseDebug.log(e, "Can not create and load Controller Instance!");
                    }
//                    String template = cm.template();
//                    if (!template.isEmpty()) {
//                        pageBean.setTemplate(tmpContainer.get(template));
//                    }
                    //이미 키를 포함하고 있다면
                    if (container.containsKey(pageBean.getId())) {
                        PageBean old = container.get(pageBean.getId());
                        if (old.getVersion() < pageBean.getVersion()) { //새로운 버전이다면 집어 넣는다.
                            container.add(pageBean.getId(), pageBean);
                        }
                    } else { //새로운 키라면
                        container.add(pageBean.getId(), pageBean);
                    }
                    System.out.println(pageBean);
                }
            }

            BaseDebug.info("***DispacherServlet Initializing OK");
        } catch (Exception ex) {
            throw new ServletException(ex + "확인하십시오");
        }
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        process(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        process(request, response);
    }

    private void process(HttpServletRequest request, HttpServletResponse response) throws ServletException , IOException {
        SessionInfo sInfo = HttpUtils.getSessionInfo(request.getSession());
        request.setAttribute("servletPath", request.getContextPath());
        request.setCharacterEncoding("UTF-8");
                
//        //교차 프레임 스크립팅 방어 누락 대응
//        response.setHeader("X-Frame-Options", "SAMEORIGIN");
//
//        //HTTP Strict-Transport-Security 헤더 누락 대응
//        response.setHeader("Strict-Transport-Security", "max-age=31536000; includeSubDomains; preload");
//
//        //"X-Content-Type-Options" header 누락 대응 : 캐시허용하지 않음
//        response.setHeader("X-Content-Type-Options", "nosniff");
//
//        //"X-XSS-Protection" header 누락 대응
//        response.setHeader("X-XSS-Protection", "1");

//        // Cors 해제 설정
//        response.setHeader("Access-Control-Allow-Origin", "*");
//        response.setHeader("Access-Control-Allow-Origin", "http://localhost:9000");
//        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
//        response.setHeader("Access-Control-Max-Age", "3600");
//        response.setHeader("Access-Control-Allow-Headers", "authorization, content-type, xsrf-token");
//        response.addHeader("Access-Control-Expose-Headers", "xsrf-token");
        

        String cmd = request.getRequestURI();
        cmd = cmd.substring(request.getContextPath().length());
        PageBean pb = container.get(cmd); //list.do에 해당하는 데이터를 얻어낸다.
        String view = "";
        if (pb != null) {
            String pageName = pb.getControllerPage();
//            view = pb.getControllerPage().substring(0, pb.getControllerPage().length() - 4); //예를 들어 bbs.do에서 bbs를 추출한다.
            view = FilenameUtils.getFullPath(pageName) + "/" +  FilenameUtils.getBaseName(pageName);  //예를 들어 bbs.do에서 bbs를 추출한다.
            request.setAttribute("pageBean", pb);
            /*
             * Command Data를 분석해서 Command 객체로 만든다. 그리고 내부에서 Command 객체를 request에
             * 등록한다.
             */
            if (pb.getCommandClass() != null && !pb.getCommandClass().equals("")) {
                CommandManager cm = new CommandManager();
                cm.setCommandClass(pb.getCommandClass());
                cm.setCommandName(pb.getCommandName());
                cm.setRequestResponse(request, response);
            }
        } else {  // 컨트롤러를 거치지 않고 jsp로 바로 넘어가는 경우
//            view =  cmd.substring(0, cmd.length() - 3);
            view =  FilenameUtils.getFullPath(cmd) + "/" + FilenameUtils.getBaseName(cmd);
        }
        view = "/" + view + ".jsp";
        String loginStatus = AppConfig.getConf("log_siteLogger");
        if (loginStatus != null && loginStatus.equals("on")) {
            Logger.getLogger("siteLogger").info(BaseLogger.getLog(request));
        }

        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp" + view);   // @@ JSP 경로 지정
        dispatcher.forward(request, response);

    }

    public static String getBody(HttpServletRequest request) throws IOException {

        String body = null;
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = null;

        try {
            InputStream inputStream = request.getInputStream();
            if (inputStream != null) {
                 bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                char[] charBuffer = new char[128];
                int bytesRead = -1;
                while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
                    stringBuilder.append(charBuffer, 0, bytesRead);
                }
            }
        } catch (IOException ex) {
            throw ex;
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException ex) {
                    throw ex;
                }
            }
        }

        body = stringBuilder.toString();
        return body;
    }

}
