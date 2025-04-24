    package com.example.stu.Core.Auth;

    import com.example.stu.Core.Enums.UserType;
    import com.example.stu.Core.Exceptions.AccessDeniedException;
    import com.example.stu.Core.Exceptions.UnauthorizedException;
    import jakarta.servlet.http.HttpServletRequest;
    import jakarta.servlet.http.HttpServletResponse;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.http.HttpStatus;
    import org.springframework.stereotype.Component;
    import org.springframework.web.method.HandlerMethod;
    import org.springframework.web.servlet.HandlerInterceptor;

    @Component
    public class JwtAuthInterceptor implements HandlerInterceptor {

        private final JwtUtility jwtUtility;

        @Autowired
        public JwtAuthInterceptor(JwtUtility jwtUtility) {
            this.jwtUtility = jwtUtility;
        }

        @Override
        public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
            System.out.println("JwtAuthInterceptor.preHandle: " + request.getRequestURI());

        System.out.println("JwtAuthInterceptor.preHandle: Method = " + request.getMethod());
            // Skip OPTIONS requests (for CORS)
            if (request.getMethod().equals("OPTIONS")) {
                return true;
            }

            // Check if the handler is a method handler
            if (!(handler instanceof HandlerMethod)) {
                System.out.println("JwtAuthInterceptor: Not a method handler");
                return true;
            }

            HandlerMethod handlerMethod = (HandlerMethod) handler;

            // Check if the method or class is marked as public
            if (handlerMethod.getMethod().isAnnotationPresent(PublicApi.class) ||
                handlerMethod.getBeanType().isAnnotationPresent(PublicApi.class)) {
                System.out.println("JwtAuthInterceptor: Public endpoint");
                return true;
            }

            // Get the Authorization header
            String authHeader = request.getHeader("Authorization");

            // Check if the header exists and starts with "Bearer "
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                return false;
            }

            // Extract the token
            String token = authHeader.substring(7);

            try {
                // Validate the token
                String username = jwtUtility.extractUsername(token);
                String tokenType = jwtUtility.extractTokenType(token);

                if (username == null || !jwtUtility.validateAccessToken(token, username)) {
                    response.setStatus(HttpStatus.UNAUTHORIZED.value());
                    return false;
                }

                // Store user information in request attributes for later use
                request.setAttribute("username", username);
                request.setAttribute("userId", jwtUtility.extractUserId(token));

                // Check if the method or class requires a specific user type
                RequireUserType requireUserType = handlerMethod.getMethodAnnotation(RequireUserType.class);
                if (requireUserType == null) {
                    requireUserType = handlerMethod.getBeanType().getAnnotation(RequireUserType.class);
                }

                if (requireUserType != null) {
                    // Extract user type from token
                    String userTypeStr = jwtUtility.extractUserType(token);

                    if (userTypeStr == null) {
                        response.setStatus(HttpStatus.FORBIDDEN.value());
                        return false;
                    }

                    try {
                        UserType userType = UserType.valueOf(userTypeStr.toUpperCase());
                        UserType[] allowedTypes = requireUserType.value();

                        boolean hasAccess = false;
                        for (UserType allowedType : allowedTypes) {
                            if (userType == allowedType) {
                                hasAccess = true;
                                break;
                            }
                        }

                        if (!hasAccess) {
                            response.setStatus(HttpStatus.FORBIDDEN.value());
                            return false;
                        }
                    } catch (IllegalArgumentException e) {
                        response.setStatus(HttpStatus.FORBIDDEN.value());
                        return false;
                    }
                }

                return true;
            } catch (Exception e) {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                return false;
            }
        }
    }