package homepage;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.util.List;

import org.junit.jupiter.api.Test;

import base.BaseTests;
import pages.LoginPage;
import pages.ProdutoPage;

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
	@Test
	public void testValidarDetalhesDoProduto_DescricaoEValorIguais() {
		int indice = 0;
		String nomeProduto_HomePage = homePage.obterNomeProduto(indice);
		String precoProduto_HomePage = homePage.obterPrecoProduto(indice);
		
		System.out.println(nomeProduto_HomePage);
		System.out.println(precoProduto_HomePage);
		
		produtoPage = homePage.clicarProduto(indice);
		
		String nomeProduto_ProdutoPage = produtoPage.obterNomeProduto();
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
	
	@Test
	public void incluirProdutosNoCarrinho_ProdutoIncluidoComSucesso() {
		if(!homePage.estaLogado("Marcelo Bittencourt")) {
			testLoginComSucesso_UsuarioLogado();
		}
		testValidarDetalhesDoProduto_DescricaoEValorIguais();
		
		List<String> listaOpcoes = produtoPage.obterOpcoesSelecionadas();
		System.out.println(listaOpcoes.get(0));
		System.out.println("Tamanho da lista:" + listaOpcoes.size());
		
		produtoPage.selecionaOpcaoDropDown("M");
		listaOpcoes = produtoPage.obterOpcoesSelecionadas();
		System.out.println(listaOpcoes.get(0));
		System.out.println("Tamanho da lista:" + listaOpcoes.size());
		
		produtoPage.selecionarCorPreta();
		produtoPage.alterarQuantidade(2);
		produtoPage.clicarBotaoAddToCart();
	}
}
