/*
  File: StatefulServlet

  Copyright 2018, Steven W. Anderson
*/
package net.swahome.jast.front.server;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.UUID;

@WebServlet("/stateful")
public class StatefulServlet extends HttpServlet {
    private static final String UUID_ATTR = "UUID";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        String uuid = (String) session.getAttribute(UUID_ATTR);
        String source = "FROM SESSION";
        if (uuid == null) {
            uuid = UUID.randomUUID().toString();
            session.setAttribute(UUID_ATTR, uuid);
            source = "CREATED";
        }
        resp.getWriter().println("<html><body><p>UUID=" + uuid + "</p><p>source=" + source + "</p></body></html>");
    }
}
