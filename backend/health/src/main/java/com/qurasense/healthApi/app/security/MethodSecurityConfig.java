package com.qurasense.app.security;

import org.aopalliance.intercept.MethodInvocation;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class MethodSecurityConfig extends GlobalMethodSecurityConfiguration {

    @Override
    protected MethodSecurityExpressionHandler createExpressionHandler() {
        CustomMethodSecurityExpressionHandler expressionHandler =
                new CustomMethodSecurityExpressionHandler();
        return expressionHandler;
    }

    public class CustomMethodSecurityExpressionHandler
            extends DefaultMethodSecurityExpressionHandler {
        private AuthenticationTrustResolver trustResolver =
                new AuthenticationTrustResolverImpl();

        @Override
        protected MethodSecurityExpressionOperations createSecurityExpressionRoot(
                Authentication authentication, MethodInvocation invocation) {
            CustomMethodSecurityExpressionRoot root =
                    new CustomMethodSecurityExpressionRoot(authentication);
            root.setPermissionEvaluator(getPermissionEvaluator());
            root.setTrustResolver(this.trustResolver);
            root.setRoleHierarchy(getRoleHierarchy());
            return root;
        }
    }

    public class CustomMethodSecurityExpressionRoot
            extends SecurityExpressionRoot implements MethodSecurityExpressionOperations {

        private Object filterObject;
        private Object returnObject;
        private Object target;

        public CustomMethodSecurityExpressionRoot(Authentication authentication) {
            super(authentication);
        }

        public boolean isCurrentUser(String aUserId) {
            if (aUserId == null) {
                return false;
            }
            User user = (User) authentication.getPrincipal();
            return aUserId.equalsIgnoreCase(user.getUsername());
        }


        public void setFilterObject(Object filterObject) {
            this.filterObject = filterObject;
        }

        public Object getFilterObject() {
            return filterObject;
        }

        public void setReturnObject(Object returnObject) {
            this.returnObject = returnObject;
        }

        public Object getReturnObject() {
            return returnObject;
        }

        /**
         * Sets the "this" property for use in expressions. Typically this will be the "this"
         * property of the {@code JoinPoint} representing the method invocation which is being
         * protected.
         *
         * @param target the target object on which the method in is being invoked.
         */
        void setThis(Object target) {
            this.target = target;
        }

        public Object getThis() {
            return target;
        }
    }
}