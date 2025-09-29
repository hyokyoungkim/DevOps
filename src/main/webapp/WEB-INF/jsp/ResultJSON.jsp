<%@page import="java.util.List"%>
<%@page import="com.codeenator.model.Response"%>
<%@page import="com.ithows.ResultMap"%>
<%@page import="org.json.JSONObject"%>
<%@page import="java.io.PrintWriter"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%  
    JSONObject jsonObject = (JSONObject) request.getAttribute(Response.RESULT);
    
    if (jsonObject == null) {
        jsonObject = new JSONObject();
    }
    
    PrintWriter writer = response.getWriter();
    writer.print(jsonObject);
%>
