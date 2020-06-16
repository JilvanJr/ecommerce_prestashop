package homepage;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.List;
import org.junit.jupiter.api.Test;
import base.BaseTests;
import pages.CarrinhoPage;
import pages.CheckoutPage;
import pages.LoginPage;
import pages.ModalProdutoPage;
import pages.PedidoPage;
import pages.ProdutoPage;
import util.Funcoes;

public class HomePageTests extends BaseTests {
	
	@Test
	public void testContarProdutos_oitoProdutosDiferentes() {
		carregarPaginaInicial();
		assertThat(homePage.contarProdutos(), is (8));
	}
	
	@Test
	public void testValidarCarrinhoZerado_ZeroItensNoCarrinho() {
		int produtosNoCarrinho = homePage.obterQuantidadeProdutosNoCarrinho();
		assertThat(produtosNoCarrinho, is (1));
	}
	
	ProdutoPage produtoPage;
	String nomeProduto_ProdutoPage;
	@Test
	public void testValidarDetalhesDoProduto_DescricaoEValorIguais() {
		int indice = 0;
		String nomeProduto_HomePage = homePage.obterNomeProduto(indice);
		String precoProduto_HomePage = homePage.obterPrecoProduto(indice);
		
		System.out.println(nomeProduto_HomePage);
		System.out.println(precoProduto_HomePage);
		
		produtoPage = homePage.clicarProduto(indice);
		
		nomeProduto_ProdutoPage = produtoPage.obterNomeProduto();
		String precoProduto_ProdutoPage = produtoPage.obterPrecoProduto();
		
		System.out.println(nomeProduto_ProdutoPage);
		System.out.println(precoProduto_ProdutoPage);
		
		assertThat(nomeProduto_HomePage.toUpperCase(), is(nomeProduto_ProdutoPage.toUpperCase()));
		assertThat(precoProduto_HomePage, is(precoProduto_ProdutoPage));
	}
	
	LoginPage loginPage;
	@Test
	public void testLoginComSucesso_UsuarioLogado() {
		loginPage = homePage.clicarBotaoSignIn();
		loginPage.preencherEmail("marcelo@teste.com");
		loginPage.preencherPassword("marcelo");
		loginPage.clicarBotaoSignIn();
		assertThat(homePage.estaLogado("Marcelo Bittencourt"), is (true));
		carregarPaginaInicial();
	}
	
	ModalProdutoPage modalProdutoPage;
	@Test
	public void testIncluirProdutosNoCarrinho_ProdutoIncluidoComSucesso() {
		String tamanhoProduto = "M";
		String corProduto = "Black";
		int quantidadeProduto = 2;
		
		if(!homePage.estaLogado("Marcelo Bittencourt")) {
			testLoginComSucesso_UsuarioLogado();
		}
		testValidarDetalhesDoProduto_DescricaoEValorIguais();
		
		List<String> listaOpcoes = produtoPage.obterOpcoesSelecionadas();
		System.out.println(listaOpcoes.get(0));
		System.out.println("Tamanho da lista:" + listaOpcoes.size());
		
		produtoPage.selecionaOpcaoDropDown(tamanhoProduto);
		listaOpcoes = produtoPage.obterOpcoesSelecionadas();
		System.out.println(listaOpcoes.get(0));
		System.out.println("Tamanho da lista:" + listaOpcoes.size());
		
		produtoPage.selecionarCorPreta();
		produtoPage.alterarQuantidade(quantidadeProduto);
		modalProdutoPage = produtoPage.clicarBotaoAddToCart();
		
		// Elemento retorna um ponto no inicio, abaixo me retorna apenas o final 
		assertTrue(modalProdutoPage.obterMensagemProdutoAdicionado().endsWith("Product successfully added to your shopping cart"));
		
		assertThat(modalProdutoPage.obterDescriçãoProduto().toUpperCase(), is(nomeProduto_ProdutoPage.toUpperCase()));
		
		String precoProdutoString = modalProdutoPage.obterPrecoProduto();
		precoProdutoString = precoProdutoString.replace("$", "");
		Double precoProduto = Double.parseDouble(precoProdutoString);
		
		assertThat(modalProdutoPage.obterTamanhoProduto(), is(tamanhoProduto));
		assertThat(modalProdutoPage.obterCorProduto(), is(corProduto));
		assertThat(modalProdutoPage.obterQuantidadeProduto(), is(Integer.toString(quantidadeProduto)));
		
		String subTotalString = modalProdutoPage.obterSubTotal();
		subTotalString = subTotalString.replace("$", "");
		Double subTotal = Double.parseDouble(subTotalString);
		
		Double subTotalCalculado = quantidadeProduto * precoProduto;
		assertThat(subTotal, is(subTotalCalculado));
	}

	String esperado_nomeProduto = "Hummingbird printed t-shirt";
	Double esperado_precoProduto = 19.12;
	String esperado_tamanhoProduto = "M";
	String esperado_cor_produto = "Black";
	int esperado_input_quantidadeProduto = 2;
	Double esperado_subtotalProduto = esperado_precoProduto * esperado_input_quantidadeProduto;
	
	int esperado_numeroItensTotal = esperado_input_quantidadeProduto;
	Double esperado_subtotalTotal = esperado_subtotalProduto;
	Double esperado_shippingTotal = 7.00;
	Double esperado_totalTaxExclTotal = esperado_subtotalTotal + esperado_shippingTotal; 
	Double esperado_totalTaxIncTotal = esperado_totalTaxExclTotal;
	Double esperado_taxesTotal = 0.00;
	
	String esperado_nomeCliente = "Marcelo Bittencourt";
	
	CarrinhoPage carrinhoPage;
 	@Test
	public void testIrParaCarrinho_InformacoesPersistidas() {
		testIncluirProdutosNoCarrinho_ProdutoIncluidoComSucesso();
		
		carrinhoPage = modalProdutoPage.clicarBotaoPreceedToCheckout();
		
		System.out.println("** ITENS DO CARRINHO ** ");
		System.out.println(carrinhoPage.obter_nomeProduto());
		System.out.println(Funcoes.removeCifraoDevolveDouble(carrinhoPage.obter_precoProduto()));
		System.out.println(carrinhoPage.obter_tamanhoProduto());
		System.out.println(carrinhoPage.obter_corProduto());
		System.out.println(carrinhoPage.obter_input_quantidadeProduto());
		System.out.println(Funcoes.removeCifraoDevolveDouble(carrinhoPage.obter_subtotalProduto()));
		
		System.out.println("** ITENS DE TOTAIS ** ");
		System.out.println(Funcoes.removeTextoItemsDevolveInt(carrinhoPage.obter_numeroItensTotal()));
		System.out.println(Funcoes.removeCifraoDevolveDouble(carrinhoPage.obter_subtotalTotal()));
		System.out.println(Funcoes.removeCifraoDevolveDouble(carrinhoPage.obter_shippingTotal()));
		System.out.println(Funcoes.removeCifraoDevolveDouble(carrinhoPage.obter_totalTaxExclTotal()));
		System.out.println(Funcoes.removeCifraoDevolveDouble(carrinhoPage.obter_totalTaxIncTotal()));
		System.out.println(Funcoes.removeCifraoDevolveDouble(carrinhoPage.obter_taxesTotal()));

		assertThat(carrinhoPage.obter_nomeProduto(), is(esperado_nomeProduto));
		assertThat(Funcoes.removeCifraoDevolveDouble(carrinhoPage.obter_precoProduto()), is(esperado_precoProduto));
		assertThat(carrinhoPage.obter_tamanhoProduto(), is(esperado_tamanhoProduto));
		assertThat(carrinhoPage.obter_corProduto(), is(esperado_cor_produto));
		assertThat(Integer.parseInt(carrinhoPage.obter_input_quantidadeProduto()), is(esperado_input_quantidadeProduto));
		assertThat(Funcoes.removeCifraoDevolveDouble(carrinhoPage.obter_subtotalProduto()), is(esperado_subtotalProduto));

		assertThat(Funcoes.removeTextoItemsDevolveInt(carrinhoPage.obter_numeroItensTotal()), is (esperado_numeroItensTotal));
		assertThat(Funcoes.removeCifraoDevolveDouble(carrinhoPage.obter_subtotalTotal()), is (esperado_subtotalTotal));
		assertThat(Funcoes.removeCifraoDevolveDouble(carrinhoPage.obter_shippingTotal()), is (esperado_shippingTotal));
		assertThat(Funcoes.removeCifraoDevolveDouble(carrinhoPage.obter_totalTaxExclTotal()), is (esperado_totalTaxExclTotal));
		assertThat(Funcoes.removeCifraoDevolveDouble(carrinhoPage.obter_totalTaxIncTotal()), is (esperado_totalTaxIncTotal));
		assertThat(Funcoes.removeCifraoDevolveDouble(carrinhoPage.obter_taxesTotal()), is (esperado_taxesTotal));
 	}
 	
 	CheckoutPage checkoutPage;
 	@Test
 	public void testIrParaCheckout_FreteMaisPagamentoEnderecoListadosOk() {
 		testIrParaCarrinho_InformacoesPersistidas();
 		
 		checkoutPage = carrinhoPage.clicarBotaoProceedToCheckout();
 		
 		assertThat(Funcoes.removeCifraoDevolveDouble(checkoutPage.obter_totalTaxIncTotal()), is(esperado_totalTaxExclTotal));
 		//assertThat(checkoutPage.obter_nomeCliente(), is(esperado_nomeCliente));
 		assertTrue(checkoutPage.obter_nomeCliente().startsWith(esperado_nomeCliente)); // startsWith = começa com
 		checkoutPage.clicarBotaoContinueAddress();
 		
 		String encontrado_shippingValor = checkoutPage.obter_shippingValor();
 		encontrado_shippingValor = Funcoes.removeTexto(encontrado_shippingValor, " tax excl.");
 		Double encontrado_shippingValor_Double = Funcoes.removeCifraoDevolveDouble(encontrado_shippingValor);
 		assertThat(encontrado_shippingValor_Double, is(esperado_shippingTotal));
 		checkoutPage.clicarBotaoContinueShipping();
 		
 		// selected option "Pay by check"
 		checkoutPage.selecionarRadioPayByCheck();
 		// validate check amount
 		String encontrado_amountPayByCheck = checkoutPage.obter_amountPayByCheck();
 		encontrado_amountPayByCheck = Funcoes.removeTexto(encontrado_amountPayByCheck, " (tax incl.)");
 		Double encontrado_amountPayByCheck_Double = Funcoes.removeCifraoDevolveDouble(encontrado_amountPayByCheck);
 		assertThat(encontrado_amountPayByCheck_Double, is(esperado_totalTaxIncTotal));
 		// click on the "I agree" option
 		checkoutPage.selecionarCheckBoxIAgree();
 		assertTrue(checkoutPage.estaSelecionadoCheckboxIAgree());
 	}
 	
 	@Test
 	public void testFinalizarPedido_pedidoFinalizadoComSucesso() {
 		testIrParaCheckout_FreteMaisPagamentoEnderecoListadosOk();
 		
 		// click the button to confirm order
 		PedidoPage pedidoPage = checkoutPage.clicarBotaoConfirmaPedido();
 		// validate screen values
 		assertTrue(pedidoPage.obter_textoPedidoConfirmado().endsWith("YOUR ORDER IS CONFIRMED"));
 		
 		assertThat(pedidoPage.obter_email(), is("marcelo@teste.com"));
 		assertThat(pedidoPage.obter_totalProdutos(), is(esperado_subtotalProduto));
 		assertThat(pedidoPage.obter_totalTaxIncl(), is(esperado_totalTaxIncTotal));
 		assertThat(pedidoPage.obter_metodoPagamento(), is("check"));
 		
 		
 	}
}
