package com.bezkoder.springjwt.security.jwt;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bezkoder.springjwt.Utils.ErrorCode;
import com.bezkoder.springjwt.Utils.constants.Constant;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
public class AuthEntryPointJwt implements AuthenticationEntryPoint {

	private static final Logger logger = LoggerFactory.getLogger(AuthEntryPointJwt.class);

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {
//		response.setContentType("application/json");
//		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//		response.getOutputStream().println("{ \"error\": \"" + "trieunv error role" + "\" }");
//		logger.error("Unauthorized error: {}", authException.getMessage());
//		response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage());
		logger.error("Unauthorized error: {}", authException.getMessage());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		String errorApp = (String) request.getAttribute("errorxxx");
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		final Map<String, Object> body = new HashMap<>();
		body.put("status", HttpServletResponse.SC_UNAUTHORIZED);
		body.put("error", "Unauthorized");
		if ("Bad credentials".equals(authException.getMessage())) {
			body.put("message", ErrorCode.ERR_USER_NOT_FOUND);
		} else if ("Full authentication is required to access this resource".equals(authException.getMessage())) {
			if (StringUtils.isNoneEmpty(errorApp) && Constant.ERR_TIME_OUT.equals(errorApp)) {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				body.put("message", ErrorCode.ERR_TIME_OUT.getMessage());
				body.put("status", HttpServletResponse.SC_BAD_REQUEST);
				body.put("error", ErrorCode.ERR_TIME_OUT);
			} else {
				body.put("message", ErrorCode.ERR_ACC_DISABLED.getMessage());
			}
		} else {
			body.put("message", authException.getMessage());
		}

		body.put("path", request.getServletPath());
		final ObjectMapper mapper = new ObjectMapper();
		mapper.writeValue(response.getOutputStream(), body);
	}

}
