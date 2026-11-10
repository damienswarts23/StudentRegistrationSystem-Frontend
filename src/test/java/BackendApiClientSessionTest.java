import com.sun.net.httpserver.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicBoolean;
import za.ac.mycput.studentregistrationsystem.frontend.service.BackendApiClient;
/** Standalone transport regression test. Uses only the JDK; no database needed. */
public class BackendApiClientSessionTest {
 public static void main(String[] args) throws Exception {
  HttpServer server=HttpServer.create(new InetSocketAddress("127.0.0.1",0),0);
  AtomicBoolean loggedIn=new AtomicBoolean(false);AtomicBoolean saved=new AtomicBoolean(false);
  server.createContext("/api/",e->{
   try {
    String path=e.getRequestURI().getPath(),body=new String(e.getRequestBody().readAllBytes(),StandardCharsets.UTF_8);
    if(path.equals("/api/auth/csrf")) {e.getResponseHeaders().add("Set-Cookie","JSESSIONID="+(loggedIn.get()?"signedin":"anonymous")+"; Path=/; HttpOnly");reply(e,200,loggedIn.get()?"token2":"token1");return;}
    String csrf=e.getRequestHeaders().getFirst("X-CSRF-TOKEN");String cookie=e.getRequestHeaders().getFirst("Cookie");
    if(path.equals("/api/auth/login")) {
     if(!"token1".equals(csrf) || cookie==null || !cookie.contains("anonymous")) {reply(e,403,"Missing CSRF or session");return;}
     if(!body.contains("password=Admin%402026%21")){reply(e,401,"Wrong password");return;}
     loggedIn.set(true);e.getResponseHeaders().add("Set-Cookie","JSESSIONID=signedin; Path=/; HttpOnly");reply(e,204,"");return;
    }
    if(!loggedIn.get() || !"token2".equals(csrf) || cookie==null || !cookie.contains("signedin")){reply(e,401,"Missing authenticated session");return;}
    switch(path){
     case "/api/auth/me" -> reply(e,200,"admin@cput.ac.za\nADMIN\n"+(saved.get()?"Damien & Team":"Damien Swarts")+"\n\n");
     case "/api/auth/profile" -> {saved.set(body.equals("displayName=Damien+%26+Team"));reply(e,saved.get()?200:400,"");}
     case "/api/auth/logout" -> {loggedIn.set(false);reply(e,204,"");}
     case "/api/students/1" -> reply(e,403,"Denied");
     default -> reply(e,404,"Unknown path");
    }
   } catch(Exception ex){reply(e,500,ex.toString());}
  });
  server.start();
  try {
   System.setProperty("srs.api.url","http://127.0.0.1:"+server.getAddress().getPort()+"/api");
   check("ADMIN".equals(BackendApiClient.login("admin@cput.ac.za","Admin@2026!")),"role from server");
   check("Damien Swarts".equals(BackendApiClient.currentName()),"profile name");
   BackendApiClient.saveName("Damien & Team");check("Damien & Team".equals(BackendApiClient.currentName()),"encoded name and refresh");
   try {BackendApiClient.deleteStudent(1);throw new AssertionError("403 should fail");}catch(java.io.IOException expected){check(expected.getMessage().contains("Access denied"),"readable permission message");}
   BackendApiClient.logout();check(!loggedIn.get() && BackendApiClient.currentRole().isEmpty(),"server and local logout");
   try {BackendApiClient.login("admin@cput.ac.za","wrong");throw new AssertionError("wrong password accepted");}catch(java.io.IOException expected){check(BackendApiClient.currentRole().isEmpty(),"failed login clears profile");}
   System.out.println("PASS: login, cookie/CSRF rotation, profile, form encoding, 403, logout, bad password.");
  } finally {server.stop(0);}
 }
 private static void check(boolean condition,String label){if(!condition)throw new AssertionError(label);}
 private static void reply(HttpExchange e,int status,String body) throws java.io.IOException {
  byte[] bytes=body.getBytes(StandardCharsets.UTF_8);e.sendResponseHeaders(status,status==204?-1:bytes.length);
  if(status!=204)e.getResponseBody().write(bytes);e.close();
 }
}
