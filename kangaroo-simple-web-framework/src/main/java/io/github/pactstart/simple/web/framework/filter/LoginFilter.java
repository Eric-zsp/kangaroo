package io.github.pactstart.simple.web.framework.filter;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.ufang.common.errorcode.ResponseCode;
import com.ufang.common.utils.SpringContextHolder;
import com.ufang.web.framework.auth.AuthenticationInfo;
import com.ufang.web.framework.auth.AuthenticationService;
import com.ufang.web.framework.common.RequestHolder;
import com.ufang.web.framework.utils.ResponseUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Set;

@Slf4j
public class LoginFilter implements Filter {

    private static Set<String> anonUrlSet = Sets.newConcurrentHashSet();

    private static String authUrl = "";

    private static AntPathMatcher matcher = new AntPathMatcher();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        authUrl = filterConfig.getInitParameter("authUrl");
        String exclusionUrls = filterConfig.getInitParameter("anonUrls");
        anonUrlSet.addAll(Lists.newArrayList("/swagger**/**", "/webjars/**", "/v2/api-docs"));
        List<String> exclusionUrlList = Splitter.on(",").trimResults().omitEmptyStrings().splitToList(exclusionUrls);
        anonUrlSet.addAll(exclusionUrlList);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        AuthenticationService authenticationService = SpringContextHolder.getBean(AuthenticationService.class);
        AuthenticationInfo authenticationInfo = authenticationService.auth(request);

        String servletPath = request.getServletPath();
        if (anonUrlSet.stream().anyMatch((url) -> matcher.match(url, servletPath))) {
            RequestHolder.add(request);
            if (authenticationInfo != null) {
                RequestHolder.add(authenticationInfo);
            }
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
        if (authenticationInfo == null) {
            if (servletPath.endsWith(".json")) {
                ResponseUtils.outputJson(response, ResponseCode.NOT_YET_LOGIN);
                return;
            } else {
                ResponseUtils.clientRedirect(authUrl, response);
                return;
            }
        } else {
            RequestHolder.add(authenticationInfo);
            RequestHolder.add(request);
        }
        filterChain.doFilter(servletRequest, servletResponse);
        return;
    }

    @Override
    public void destroy() {

    }
}
