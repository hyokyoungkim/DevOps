<%@page contentType="text/html" pageEncoding="UTF-8" %>
    <% // response.sendRedirect("/SSF/loginForm.do"); String server=request.getServerName(); System.out.println("server=" + server);
    if (server.equals(" localhost")) { response.sendRedirect("/codeenator/main.do"); //
        response.sendRedirect("/SSF/loginForm.do"); } else { response.sendRedirect("/codeenator/main.do"); //
        response.sendRedirect("/SSF/loginForm.do"); } %>