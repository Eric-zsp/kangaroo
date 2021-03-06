package io.github.pactstart.simple.web.framework.externs;

import com.google.common.collect.Maps;
import io.github.pactstart.commonutils.JsonUtils;
import io.github.pactstart.simple.web.framework.auth.AuthenticationInfo;
import io.github.pactstart.simple.web.framework.common.RequestHolder;
import io.github.pactstart.simple.web.framework.interceptor.HttpInterceptor;
import io.github.pactstart.simple.web.framework.utils.IpUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.Map;

@Slf4j
public class DefaultRequestCompleteCallbackServiceImpl implements RequestCompleteCallbackService {

    @Override
    public void callback(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex, boolean servletRequestWrapperEnabled) throws Exception {
        Map<String, Object> data = Maps.newHashMap();
        String url = request.getRequestURI().toString();
        long start = (Long) request.getAttribute(HttpInterceptor.START_TIME);
        long end = System.currentTimeMillis();

        String params = "";

        MediaType mediaType = null;
        if (request.getContentType() != null) {
            mediaType = MediaType.valueOf(request.getContentType());
        }
        if (mediaType != null && (MediaType.APPLICATION_JSON.equals(mediaType) || MediaType.APPLICATION_JSON_UTF8.equals(mediaType))) {
            InputStream in = request.getInputStream();
            // 框架已使用流，指针已指向到末尾，servlet流不支持复位，读取之后再读取会抛出异常，HttpMessageNotReadableException Required request body is missing
            // 开启了wrapper才能对流进行复位，否则抛异常java.io.IOException: mark/reset not supported
            if (servletRequestWrapperEnabled && in.markSupported()) {
                in.reset();
                StringWriter sw = new StringWriter();
                IOUtils.copy(in, sw);
                params = sw.toString();
            }
        } else {
            params = JsonUtils.obj2String(request.getParameterMap());
        }
        data.put("requestMethod", request.getMethod());
        data.put("contentType", request.getContentType());
        data.put("startTime", start);
        data.put("endTime", end);
        data.put("cost", end - start);
        data.put("params", params);
        data.put("url", url);
        data.put("ip", IpUtils.getClientIpAddr(request));
        data.put("device", request.getHeader("device"));
        AuthenticationInfo authenticationInfo = RequestHolder.getAuthenticationInfo();
        if (authenticationInfo != null) {
            data.put("userId", authenticationInfo.getUserId());
            data.put("userName", authenticationInfo.getUserName());
        }
        //在GlobalExceptionHandler中设置
        Object e = request.getAttribute("ex");
        boolean hasEx = e != null;
        data.put("hasEx", hasEx);
        if (hasEx) {
            Exception exception = (Exception) e;
            data.put("exceptionClass", exception.getClass().getName());
            data.put("exceptionMessage", exception.getMessage());
            request.removeAttribute("ex");
        }
        log.info(JsonUtils.obj2String(data));
        call(request, response, handler, ex, servletRequestWrapperEnabled, data);
    }

    /**
     * 模板方法，供子类覆盖
     *
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @param servletRequestWrapperEnabled
     * @param data
     */
    public void call(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex, boolean servletRequestWrapperEnabled, Map<String, Object> data) {

    }
}
