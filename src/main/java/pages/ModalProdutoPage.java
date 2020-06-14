package pages;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;

public class ModalProdutoPage {
	
	private WebDriver driver;
	
	private By mensagemProdutoAdicionado = By.id("myModalLabel");
	
	public ModalProdutoPage(WebDriver driver) {
		this.driver = driver;
	}
	
	public String obterMensagemProdutoAdicionado() {
		// Incluindo espera para que meu elemento seja carregado na tela 
		//Informo que meu FluentWait � conectado a um driver
		FluentWait wait = new FluentWait(driver)
		// Dura��o de 5 segundos
				.withTimeout(Duration.ofSeconds(5)).
		// Verifica de 1 em 1 segundo, at� 5
				pollingEvery(Duration.ofSeconds(1)).
		// Ignora a exce��o que retornou no console "NoSuchElementException" para conseguir prosseguir
				ignoring(NoSuchElementException.class);
		// Esperar at� que minha condi��o esperada este visivel, meu elemento
		wait.until(ExpectedConditions.visibilityOfElementLocated(mensagemProdutoAdicionado));
		
		return driver.findElement(mensagemProdutoAdicionado).getText();
	}

}
