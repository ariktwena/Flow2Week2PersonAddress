/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cors;

import java.io.IOException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

/**
 *
 * @author Tweny
 */


@Provider
@PreMatching
public class CorsFilter implements ContainerRequestFilter, ContainerResponseFilter {
 
  @Override
  public void filter(ContainerRequestContext request) throws IOException {
 
   //If it's a preflight request, abort the request with a 200 status, and the CORS headers are added in the
   // response filter method below.
   if (isPreflightRequest(request)) {
     request.abortWith(Response.ok().build());
      return;
    }
  }
 
  // A preflight request is an OPTIONS request with an Origin header.
  private static boolean isPreflightRequest(ContainerRequestContext request) {
      return request.getHeaderString("Origin") != null
              && request.getMethod().equalsIgnoreCase("OPTIONS");
  }
 
  // Method for ContainerResponseFilter.
  @Override
  public void filter(ContainerRequestContext request, ContainerResponseContext response)
          throws IOException {
    // if there is no Origin header, then it is not a cross origin request - don't do anything.
    if (request.getHeaderString("Origin") == null) {
          return;
    }
 
    // If it is a preflight request add all the CORS headers here.
    if (isPreflightRequest(request)) {
        response.getHeaders().add("Access-Control-Allow-Credentials", "true");
        response.getHeaders().add("Access-Control-Allow-Methods","GET, POST, PUT, DELETE, OPTIONS, HEAD");
          response.getHeaders().add("Access-Control-Allow-Headers",
          // Whatever other non-standard/safe headers (see list above) 
          // you want the client to be able to send to the server,
          // put it in this list. And remove the ones you don't want.
          "Origin, Accept, Content-Type, Authorization,x-access-token");
    }
 
    /* Cross origin requests can be either simple requests or preflight request. We need to add this
     header to both types of requests. Only preflight requests need the previously added headers. */
     response.getHeaders().add("Access-Control-Allow-Origin", "*");
  }
}






//@PreMatching
//@Provider
//public class CorsFilter implements ContainerResponseFilter {
// @Override
// public void filter( ContainerRequestContext requestCtx, ContainerResponseContext res )
//   throws IOException {
//   res.getHeaders().add("Access-Control-Allow-Origin", "*" );
//   res.getHeaders().add("Access-Control-Allow-Credentials", "true" );
//   res.getHeaders().add("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT" );
//   res.getHeaders().add("Access-Control-Allow-Headers", "Origin, Accept, Content-Type, Authorization,x-access-token");
// }

