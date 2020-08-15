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
	private By descricaoProduto = By.className("product-name");
	private By precoProduto = By.cssSelector("div.modal-body p.product-price");
	private By listaValoresInformados = By.cssSelector("div.divide-right .col-md-6:nth-child(2) span strong");
	private By subTotal = By.cssSelector("div.cart-content p:nth-child(2) span.value");
	private By botaoProceedToCheckout = By.cssSelector("div.cart-content-btn a.btn-primary");
	
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
	
	public String obterDescricaoProduto() {
		return driver.findElement(descricaoProduto).getText();
	}
	
	public String obterPrecoProduto() {
		return driver.findElement(precoProduto).getText();
	}
	
	public String obterTamanhoProduto() {
		return driver.findElements(listaValoresInformados).get(0).getText();
	}
	
	public String obterCorProduto() {
		if (driver.findElements(listaValoresInformados).size() == 3)
			return driver.findElements(listaValoresInformados).get(1).getText();
		else
			return "N/A";
	}
	
	public String obterQuantidadeProduto() {
		if (driver.findElements(listaValoresInformados).size() == 3)
			return driver.findElements(listaValoresInformados).get(2).getText();
		else 
			return driver.findElements(listaValoresInformados).get(1).getText();
	}
	
	public String obterSubTotal() {
		return driver.findElement(subTotal).getText();
	}
	
	public CarrinhoPage clicarBotaoPreceedToCheckout() {
		driver.findElement(botaoProceedToCheckout).click();
		return new CarrinhoPage(driver);
	}
}
