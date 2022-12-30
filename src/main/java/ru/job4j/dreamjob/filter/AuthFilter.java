package ru.job4j.dreamjob.filter;

import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Component
public class AuthFilter implements Filter {

    private final static List<String> URI_LIST = List.of(
            "loginPage",
            "login",
            "index",
            "formAddUser",
            "success",
            "fail",
            "registration"
    );

    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        String uri = req.getRequestURI();
        if (uriMatch(uri)) {
            chain.doFilter(req, res);
            return;
        }
        if (req.getSession().getAttribute("user") == null) {
            res.sendRedirect(req.getContextPath() + "/loginPage");
            return;
        }
        chain.doFilter(req, res);
    }

    public boolean uriMatch(String uri) {
        boolean flag = false;
        for (String str : URI_LIST) {
            if (uri.endsWith(str)) {
                flag = true;
                break;
            }
        }
        return flag;
    }
}
