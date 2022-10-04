package br.com.diogenesjardim;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import io.restassured.http.ContentType;
import io.restassured.path.xml.XmlPath;
import io.restassured.path.xml.XmlPath.CompatibilityMode;





public class AuthTest {
	
	@Test
	public void deveAcessarSWPI() {
		given()
		     .log().all()
		.when()
		     .get("https://swapi.dev/api/people/6")
		.then()
		     .log().all()
		     .statusCode(200)
		     .body("name", is("Owen Lars"))
	         .body("gender", is("male"))
             .body("homeworld", is("https://swapi.dev/api/planets/1/"))
	         .body("created", is("2014-12-10T15:52:14.024000Z"))
		  
		;
	}
	
	// https://api.openweathermap.org/data/2.5/weather?q=fortaleza,BR&appid=bfea8929b1a674c51b0b2760793df658&units=metric
	
	@Test
	public void deveObterClima() {
		given()
	         .log().all()
	         .queryParam("q", "Fortaleza,BR")
	         .queryParam("appid", "bfea8929b1a674c51b0b2760793df658")
	         .queryParam("units", "metric")
	   .when()
	         .get("http://api.openweathermap.org/data/2.5/weather")
	   .then()
	         .log().all()
	         .statusCode(200)
	         .body("name", is("Fortaleza"))
	         .body("coord.lon", is(-38.5247f))
	         .body("main.temp", greaterThan(25f))
	         
	     
	     ;
		

	}
	
	@Test
	public void deveFazerAutenticaoBasico() {
		given()
	         .log().all()
	    .when()
	         .get("https://admin:senha@restapi.wcaquino.me/basicauth")
	    .then()
	         .log().all()
	         .statusCode(200)
	         .body("status", is("logado"))
	   
	  
	;
		
	}
	
	@Test
	public void naoDeveAcessarSemSenha() {
		given()
		     .log().all()
		.when()
		     .get("https://restapi.wcaquino.me/basicauth")
		.then()
		     .log().all()
		     .statusCode(401)
		    
		;
	}
	
	@Test
	public void deveFazerAutenticaoBasico2() {
		given()
	         .log().all()
	         .auth().basic("admin", "senha")
	    .when()
	         .get("https://restapi.wcaquino.me/basicauth")
	    .then()
	         .log().all()
	         .statusCode(200)
	         .body("status", is("logado"))
	   
	  
	;
		
	}
	
	@Test
	public void deveFazerAutenticaoBasicoChallenge() {
		given()
	         .log().all()
	         .auth().preemptive().basic("admin", "senha")
	    .when()
	         .get("https://restapi.wcaquino.me/basicauth2")
	    .then()
	         .log().all()
	         .statusCode(200)
	         .body("status", is("logado"))
	   
	  ;
		
	}
	
	@Test
	public void deveFazerAutenticacaoComTokenJWT() {
		Map<String, String> login = new HashMap<String, String>();
		login.put("email", "diogenes.jj@hotmail.com");
		login.put("senha", "dIOGENES@1989");
		
		//Login na api
		//Receber o token
		String token = given()
            .log().all()
            .body(login)
            .contentType(ContentType.JSON)
        
       .when()
           .post("https://barrigarest.wcaquino.me/signin")
       .then()
           .log().all()
           .statusCode(200)
           .extract().path("token")
       
  
 ;
		
		
		
		//Obter as contas
	given()
        .log().all()
        .header("Authorization", "JWT " + token)
   .when()
        .get("http://barrigarest.wcaquino.me/contas")
   .then()
        .log().all()
        .statusCode(200)
        .body("nome", hasItem("Conta de testes"))
        
    //    ***** Exemplos *****
   // .body("nome"),  hasIntem("Contas de testes ") , quando tem varias colecoes
  //  .body("nome"), is("Contas de testes"), nao teme colecao

;
	
	}
	
	@Test
	public void deveAcessarAplicacaoWeb() {
	   //Login
    String cookie = given()
        .log().all()
        .formParam("email", "diogenes.jj@hotmail.com")
        .formParam("senha", "dIOGENES@1989")
        .contentType(ContentType.URLENC.withCharset("UTF-8"))
   .when()
        .post("https://seubarriga.wcaquino.me/logar")
   .then()
        .log().all()
        .statusCode(200)
        .extract().header("set-cookie")
      
        
        
        ;
    
        cookie = cookie.split("=")[1].split(";")[0];
        System.out.println(cookie);
    
        //obter conta
     
        String body = given()
        .log().all()
        .cookie("connect.sid", cookie)
   .when()
        .get("https://seubarriga.wcaquino.me/contas")
   .then()
        .log().all()
        .statusCode(200)
        .body("html.body.table.tbody.tr[0].td[0]", is("Conta de testes"))
        .extract().body().asString();
        
        ;
        
        System.out.println("**************************");
        XmlPath xmlPath = new XmlPath(CompatibilityMode.HTML, body);
        System.out.println(xmlPath.getString("html.body.table.tbody.tr[0].td[0]"));
        
        
		
	}
	
	
	}


