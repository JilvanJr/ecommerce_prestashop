package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CheckoutPage {
	
	private WebDriver driver;

	private By totalTaxIncTotal = By.cssSelector(".cart-total span.value");
	
	public CheckoutPage(WebDriver driver) {
		this.driver = driver;
	}
	
	public String obter_totalTaxIncTotal() {
		 return driver.findElement(totalTaxIncTotal).getText();
	 }
}
