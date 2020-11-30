package com.severell.plugins.inertia;

import com.severell.core.http.MiddlewareChain;
import com.severell.core.http.Request;
import com.severell.core.http.Response;
import com.severell.core.middleware.Middleware;

import javax.servlet.http.HttpServletRequest;

public class InertiaMiddleware implements Middleware {

    public Inertia inertia;

    public InertiaMiddleware(Inertia inertia) {
        this.inertia = inertia;
    }

    @Override
    public void handle(Request request, Response response, MiddlewareChain chain) throws Exception {

        chain.next();

        if (request.getHeader("X-Inertia") == null) {
            return;
        }

        if ("GET".equals(request.getMethod()) && !inertia.version().equals(request.getHeader("X-Inertia-Version"))) {
            response.setStatus(409);
            response.setHeader("X-Inertia-Location", getFullURL(request));
            return;
        }

        boolean isPutPatchOrDelete = request.getMethod().equals("PUT") || request.getMethod().equals("PATCH") || request.getMethod().equals("DELETE");
        if(response.getHeader("Location") != null && response.getStatus() == 302 && isPutPatchOrDelete) {
            response.setStatus(302);
        }

    }

    public static String getFullURL(HttpServletRequest request) {
        StringBuilder requestURL = new StringBuilder(request.getRequestURL().toString());
        String queryString = request.getQueryString();

        if (queryString == null) {
            return requestURL.toString();
        } else {
            return requestURL.append('?').append(queryString).toString();
        }
    }
}


