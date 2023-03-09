import Controller.BankController;
import Model.BankUser;
import Model.Account;
import Model.Transaction;
import Util.ConnectionSingleton;

import org.junit.Test;
import org.junit.Assert;
import org.junit.Before;
import org.junit.After;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.Javalin;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class BankTest {
	BankController bankControl;
	HttpClient webClient;
	ObjectMapper mapper;
	Javalin app;

	@Before
	public void setUp() throws InterruptedException {
		ConnectionSingleton.resetTestDatabase();
		bankControl = new BankController();
		app = bankControl.startAPI();
		webClient = HttpClient.newHttpClient();
		mapper = new ObjectMapper();
		app.start(8080);
		Thread.sleep(1000);
	}

	@After
	public void stop() {
		app.stop();
	}

	/**
	* POST to localhost:8080/register
	* Valid new user (username = "user", password = "password") should respond 200 with user as body.
	*/
	@Test
	public void registerTest() throws IOException, InterruptedException {
		HttpRequest postRequest = HttpRequest.newBuilder()
			.uri(URI.create("http://localhost:8080/register"))
			.POST(HttpRequest.BodyPublishers.ofString(
				"{\"username\": \"user\", \"password\": \"password\"}"
			)).header("Content-Type", "application/json").build();
		HttpResponse response = webClient.send(postRequest, HttpResponse.BodyHandlers.ofString());
		Assert.assertEquals(200, response.statusCode());
		BankUser expectedUser = new BankUser(1, "user", "password");
		BankUser actualUser = mapper.readValue(response.body().toString(), BankUser.class);
		Assert.assertEquals(expectedUser, actualUser);
	}

	/**
	* A blank username (username = "") should respond 400
	*/
	@Test
	public void registerBlankUsernameTest() throws IOException, InterruptedException {
		HttpRequest postRequest = HttpRequest.newBuilder()
			.uri(URI.create("http://localhost:8080/register"))
			.POST(HttpRequest.BodyPublishers.ofString(
				"{\"username\": \"\", \"password\": \"password\"}"
			)).header("Content-Type", "application/json").build();
		HttpResponse response = webClient.send(postRequest, HttpResponse.BodyHandlers.ofString());
		Assert.assertEquals(400, response.statusCode());
	}

	/**
	* A password length < 4 should respond 400.
	*/
	@Test
	public void registerShortPasswordTest() throws IOException, InterruptedException {
		HttpRequest postRequest = HttpRequest.newBuilder()
			.uri(URI.create("http://localhost:8080/register"))
			.POST(HttpRequest.BodyPublishers.ofString(
				"{\"username\": \"user\", \"password\": \"p\"}"
			)).header("Content-Type", "application/json").build();
		HttpResponse response = webClient.send(postRequest, HttpResponse.BodyHandlers.ofString());
		Assert.assertEquals(400, response.statusCode());
	}

	/**
	* Trying to add a user with duplicate username should respond 400.
	*/
	@Test
	public void registerDuplicateUserTest() throws IOException, InterruptedException {
		registerTest();
		HttpRequest postRequest = HttpRequest.newBuilder()
			.uri(URI.create("http://localhost:8080/register"))
			.POST(HttpRequest.BodyPublishers.ofString(
				"{\"username\": \"\", \"password\": \"password\"}"
			)).header("Content-Type", "application/json").build();
		HttpResponse response = webClient.send(postRequest, HttpResponse.BodyHandlers.ofString());
		Assert.assertEquals(400, response.statusCode());
	}

	/**
	* POST to localhost:8080/login
	* Valid existing user (register user first) should respond 200 with user as body.
	*/
	@Test
	public void loginTest() throws IOException, InterruptedException {
		registerTest();
		HttpRequest postLoginRequest = HttpRequest.newBuilder()
			.uri(URI.create("http://localhost:8080/login"))
			.POST(HttpRequest.BodyPublishers.ofString(
			"{\"username\": \"user\", \"password\": \"password\"}"
			)).header("Content-Type", "application/json")
			.build();
		HttpResponse loginResponse = webClient.send(postLoginRequest, HttpResponse.BodyHandlers.ofString());
		Assert.assertEquals(200, loginResponse.statusCode());
		BankUser expectedUser = new BankUser(1, "user", "password");
		Assert.assertEquals(expectedUser, mapper.readValue(loginResponse.body().toString(), BankUser.class));
	}
	
	/**
	* Login to nonexisting account should fail (respond 401).
	*/
	@Test
	public void loginNonexistTest() throws IOException, InterruptedException {
		HttpRequest postRequest = HttpRequest.newBuilder()
			.uri(URI.create("http://localhost:8080/login"))
			.POST(HttpRequest.BodyPublishers.ofString(
				"{\"username\": \"user\", \"password\": \"password\"}"
			)).header("Content-Type", "application/json")
			.build();
		HttpResponse response = webClient.send(postRequest, HttpResponse.BodyHandlers.ofString());
		Assert.assertEquals(401, response.statusCode());
	}

	/**
	* Login attempt with incorrect password should fail (respond 401).
	*/
	@Test
	public void loginIncorrecPasswordTest() throws IOException, InterruptedException {
		registerTest();
		HttpRequest postLoginRequest = HttpRequest.newBuilder()
		.uri(URI.create("http://localhost:8080/login"))
		.POST(HttpRequest.BodyPublishers.ofString(
			"{\"username\": \"user\", \"password\": \"wrongpassword\"}"
		)).header("Content-Type", "application/json")
		.build();
		HttpResponse loginResponse = webClient.send(postLoginRequest, HttpResponse.BodyHandlers.ofString());
		Assert.assertEquals(401, loginResponse.statusCode());
	}
}
