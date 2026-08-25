// Define o pacote ao qual essa classe pertence. Isso é útil para organização do código em projetos maioresl
package cadastro; 

// Importa a interface MongoClient do driver do MongoDB, usada para estabelecer a conexão com o banco de dados.
import com.mongodb.client.MongoClient;

// Importa a classe MongoClients, que possui métodos estáticos para criar instâncias de MongoClient.
import com.mongodb.client.MongoClients;

/* Importa a interface MongoCollection, que representa  
   uma coleção (equivalente a uma tabela) dentro de um banco de dados MongoDB */
import com.mongodb.client.MongoCollection;

/* Importa a interface MongoDatabase, que representa o 
	próprio banco de dados no MongoDB. */
import com.mongodb.client.MongoDatabase;


/* Importa a classe Document do BSON, que é usada para criar objetos
// que representam documentos (registros) no MongoDB.
// Um Document é similar a um Map<String, Object>, onde você pode armazenar pares chave-valor */
import org.bson.Document;


/* Importa a classe ObjectId do BSON, que representa o identificador único (_id)
   padrão gerado automaticamente pelo MongoDB para cada documento. */
import org.bson.types.ObjectId;


/* Importa a biblioteca Swing do Java, que é usada para construir interfaces gráficas (GUI).
   Aqui importa todos os componentes básicos do Swing (como JFrame, JButton, JTextFild, JLabel etc). */
import javax.swing.*;


/* Importa o modelo de tabela padrão do Swing (DefaultTableModel), usado para manipular
 os dados exibidos em uma JTable. */
import javax.swing.table.DefaultTableModel;


/* Importa a biblioteca AWT (Abstract Window Toolkit), usada aqui para gerenciar layouts
tamanhos, posicionamento e estilo visual dos componentes GUI.*/
import java.awt.*;


/* Importa a classe File, usada para representar e manipular
  arquivos no sistema de arquivos (leitura e escrita). */
import java.io.File;


/* Importa a classe PrintWriter que permite escrever texto
 em arquivos de forma mais simples.*/
import java.io.PrintWriter;


/* Importa a classe ArrayList, uma implementação de lista dinâmmica que 
 permite armazenar e acessar elementos em ordem */
import java.util.ArrayList;


/* Importa a interface List, que define uma lista ordenada de elementos 
e é a superclasse de ArrayList. */
import java.util.List;


/* Define a classe principal da aplicação chamada CadastroMongoApp
// Ela herda (extends) de JFrame, o que significa que é uma janela gráfica da biblioteca Swing. */
public class CadastroMongoApp extends JFrame {
	
	/* Declaração de um campo de texto para inserir o nome do usuário.
	// JTextField é um componente Swing que permite entrada de texto de linha única.*/
	private JTextField txtNome;
	
	// Campo de texto para inserir o e-mail do usuário.
	private JTextField txtEmail;
	
	// Campo de texto para inserir o telefone do usuário.
	private JTextField txtTelefone;
	
	// Campo de texto para inserir a data de nascimento
	private JTextField txtDataNascimento;
	
	/* Campo de texto para inserir um termo de busca, que será usado
	// para pesquisar registros existentes. */
	private JTextField txtPesquisa;
	
	// Botão para limpar os campos do formulário. Usado para iniciar um novo cadastro.
	private JButton btnNovo;
	
	
	// Botão para salvar um novo registro no banco de dados.
	private JButton btnSalvar;
	
	// Botão que ativa a busca com base no termo digitado em txtPesquisa
	private JButton btnPesquisar;
	
	/* Botão para atualizar os dados de um registro existente.
	// Inicialmente desativado (só é ativado ao selecionar um registro) */
	private JButton btnAtualizar;
	
	
	/* Botão para excluir um registro selecionado. Também é ativado apenas
	// quando há um item selecionado na tabela.*/
	private JButton btnExcluir;
	
	/* Componente de tabela que exibirá os registros existentes do 
	// banco de dados na interface gráfica */
	private JTable tabela;
	
	/* Modelo de dados da tabela. O DefaultTableModel permite
	// manipular dinamicamente o conteúdo exibido na JTable.
	// Ele define as colunas e linhas que a tabela irá exibir. */
	private DefaultTableModel modeloTabela;
	
	/* Objeto responsável por manter a conexão ativa com o MongoDB
	// A interface MongoClient representa a conexão do cliente com o servidor do MondoDB. */
	private MongoClient mongoClient;
	
	
	/* Representa o banco de dados dentro do servidor MongoDB.
	// Por exemplo um banco chamado "cadastro".*/
	private MongoDatabase database;
	
	
	/* Representa uma coleção (semelhante a uma tabela em bancos relacionais) dentro do banco.
	 Aqui será usada para armazenar os registros de pessoas (nome, email, telefone, etc).*/
	private MongoCollection<Document> colecao;
	
	
	/* Construtor da classe CadastroMongoApp
	 * Este método é executado automaticamente quando um novo objeto da classe é instanciado.
	 * Aqui é onde toda a interface gráfica e a conexão com o banco são inicializadas. */
	public CadastroMongoApp() {
		
		/* Chama o construtor da superclasse JFrame (Janela gráfica do Swing),
		 * passando como argumento o título da janela.
		 * Isso define o texto que aparece na barra de título da janela principal.*/
		super("Cadastro com MongoDB - Java Swing");
		
		/* Define o comportamento padrão ao fechar a janela.
		 * Neste caso, EXIT_ON_CLOSE encerra completamente o programa (fecha a aplicação Java) ao clicar no "X" 
		 * Isso é importante para evitar que o processo continue rodando em segundo plano após fechar a janela.*/
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		/* Define o tamanho da janela principal da aplicação : 800pixels de largura e 600 de altura
		 *  Esses valores são definidos manualmente para garantir que todos os componentes caibam confortavelmente na tela.*/
		setSize(800, 600);
		
		/* Centraliza a janela na tela, independentemente da resolução do monitor.
		 * O argumento 'null' faz com que a janela apareça no centro da tela do usuário 
		 * Isso melhora a experiência do usuário ao abrir a aplicação.*/
		setLocationRelativeTo(null);
		
		/*Chamada do método conectarMongo(), que é responsável por:
		 * - Criar uma conexão com o servidor MongoDB local (localhost:27017),
		 * - Selecionar o banco de dados "cadastro",
		 * - Selecionar a coleção "pessoas".
		 *  Esse método prepara a aplicação para trabalhar com o banco de dados.*/
		conectarMongo();
		
		
		/* Chamada do método criarInterface(), que é responsável por: 
		 * - Criar e organizar os componentes visuais (campos, botões, tabela, etc),
		 * - Definir os layouts dos painéis (com GridLayout, BorderLayout, etc), 
		 * - Associar os botões às suas funcionalidades usando ActionListeners, 
		 * - Adicionar os elementos visuais ao JFrame.*/
		criarInterface();
		
		
		/* Chamada do método carregarRegistros("") para exibir os dados da coleção na tabela.
		 * O argumento passado é uma string vazia, indicando que deve trazer todos os registros sem filtro.
		 * Isso garante que, ao abrir a aplicação, o usuário veja todos os cadastros já existentes */
		carregarRegistros("");
		
		
		/* Torna a janela visível ao usuário.
		 * Por padrão, uma janela JFrame é invisível até que esse método seja chamado.
		 * Se não fosse chamado, a interface não apareceria, mesmo com todos os componentes prontos. */
		setVisible(true);
		
	}
	
	
	/* Método privado chamado criarInterfaces.
	 * Esse método monta toda a interface gráfica da aplicação, incluindo os painéis, campos de entrada, rótulos e a organização visual da tela.*/
	private void criarInterface() {
		
		/*Cria o painel principal que servirá como contêiner raiz da janela.
		 * O layout utilizado é o BorderLayout. que divide o espaço em cinco regiões:
		 * 	- NORTH: (superior), SOUTH (inferior), EAST (direita), WEST(esquerda) e CENTER (centro). 
		 *  O construtor BorderLayout(10,10) define um espaçamento (gap) horizontal e vertical de 10 pixels entre os componentes adicionados nas regiões, 
		 *  garantindo que a interface não fique "colada". */
		JPanel painelPrincipal = new JPanel(new BorderLayout(10,10));
		
		
		/* Define uma margem interna (padding) de 10 pixels em todos os lados (superior, inferior, esquerdo e direito)
		 * dentro do painel principal. Isso evita que os componentes encostem diretamente nas bordas da janela. */
		painelPrincipal.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
		
		/* Cria um painel chamado painelForm, que conterá o formulário de cadastro (nome, e-mail, telefone, dataNascimento e botoes etc).
		 * Utiliza o layout GridLayout, que organiza os componentes em uma grande (tabela) .
		 * Aqui, define 4 linhas e 2 colunas, com um espaçamento horizontal e vertical de 5 pixels entre os componentes.
		 * Isso significa que teremos pares de (rótulo + campo de texto) organizados em colunas lado a lado.*/
		JPanel painelForm = new JPanel(new GridLayout(4, 2, 5, 5));
		
		/* Adiciona o primeiro componente ao painel de formulário: um JLabel com o texto "Nome": 
		 * O JLabel é um componente que exibe um texto estático na interface, geralmente, geralmente usado como rótulo para outros campos*/
		painelForm.add(new JLabel("Nome: "));
		
		/* Cria o campo de texto onde o usuário poderá digitar o nome.
		 * O argumento 20 indica o número sugerido de colunas visuais (largura aproximada).
		 * Isso influencia no tamanho inicial do campo, mas pode ser ajustado pelo layout.*/
		txtNome = new JTextField(20);
		
		/* Adiciona o campo de texto txtNome ao painel de formulário, ao lado do rótulo "Nome" */
		painelForm.add(txtNome);
		
		
		/* Adiciona o rótulo "E-mail:" ao painel de formulário.*/
		painelForm.add(new JLabel("E-mail: "));
		
		/* Cria o campo de texto para inserção do e-mail, com largura sugerida de 20 colunas. */
		txtEmail = new JTextField(20);
		
		/* Adiciona o campo txtEmail ao painel de formulário */
		painelForm.add(txtEmail);
		
		/* Adiciona o rótulo "Telefone:" ao painel */
		painelForm.add(new JLabel("Telefone: "));
		
		/* Cria o campo de texto para inserção do telefone, com largura sugerida de 15 colunas*/
		txtTelefone = new JTextField(15);
		
		/*Adiciona o campo de txtTelefone ao painel de formulário*/
		painelForm.add(txtTelefone);
		
		
		/* Adiciona o rótulo "Data de nascimento" ao painel.
		 * Esse campo será usado para que o usuário digite sua data de nascimento*/
		painelForm.add(new JLabel("Data Nascimento: "));
		
		/* Cria o campo de texto para a data de nascimento, com largura sugerida de 10 colunas*/
		txtDataNascimento = new JTextField(10);
		
		/* Adiciona o campo txtDataNacimento ao painel, logo ao lado do rótulo.*/
		painelForm.add(txtDataNascimento);
		
		
		/* Cria um novo painel chamado painelBotoesForm, que será usado para conter os botões relacionados ao formulário (Salvar, Limpar, Atualizar, Excluir, Exportar).
		 * Esse painel usa o layout FlowLayout com alinhamento à esquerda (FlowLayout.LEFT).
		 * O FlowLayout organiza os componentes da esquerda para a direita, em uma única linha, com quebras automáticas se necessário.
		 * O alinhamento LEFT garante que os botões fiquem "colados" à margem esquerda do painel, mantendo a ordem de adição.*/
		JPanel painelBotoesForm = new JPanel(new FlowLayout(FlowLayout.LEFT));
		
		/* Cria um botão chamado btnSalvar com o rótulo "Salvar
		 * Esse botão será utilizado para salvar um novo registro inserido no formulário no banco de dados MongoDB"*/
		btnSalvar = new JButton("Salvar");
		
		/* Cria um botão chamado btnNovo com rótulo "Limpar".
		 * Esse botão tem a função de "resetar" o formulário, limpando todos os campos de texto e desfazendo qualquer seleção da tabela.*/
		btnNovo = new JButton("Limpar");
		
		
		/* Cria um botão chamado btAtualizar com o rótulo "Atualizar". Esse botão será usado para modificar os dados de um registro já existente no banco.
		 * Por padrão, ele é desativado no inicio da aplicação (setEnabled(false)) porque só deve ser habilitado quando um registro da tabela for selecionado*/
		btnAtualizar = new JButton("Atualizar");
		
		btnAtualizar.setEnabled(false); /* Desativa inicialmente o botão*/
		
		/* Cria um botão chamado btnExcluir com o rótulo "Excluir". 
		 * Esse botão será utilizado para apagar um registro da base de dados, desde que um registro tenha sido selecionado na tabela
		 * Assim como o botão de atualizar, ele começa desativado e só será ativado ao selecionar uma linha na tabela.*/
		btnExcluir = new JButton("Excluir");
		btnExcluir.setEnabled(false); /* Desativa inicialmente o botão*/
		
		/* Cria um botão local chamado btnExportar com o rótulo "Exportar Excel". 
		 * Este botão permite exportar todos os registros exibidos na tabela para um arquivo `.csv` no estilo Excel.
		 * Note que este botão não é declarado como atributo da classe porque não precisa ser acessado de fora deste método. */
		JButton btnExportar = new JButton("Exportar Excel");
		
		/* Adiciona o botão "Salvar" ao painel de botões. 
		 * A ordem em que os botões são adicionados define a ordem em que eles aparecem visualmente na interface. */
		painelBotoesForm.add(btnSalvar);
		
		/*Adiciona o botão "Limpar" (btnNovo) ao painel */
		painelBotoesForm.add(btnNovo);
		
		/* Adiciona o botão "Atualizar" ao painel*/
		painelBotoesForm.add(btnAtualizar);
		
		/*Adiciona o botão "Excluir" ao painel*/
		painelBotoesForm.add(btnExcluir);
		
		/* Adiciona o botão "Exportar Excel" ao painel*/
		painelBotoesForm.add(btnExportar);
		
		
		/* Cria um novo painel chamado painelSuperior, que será o contêiner da parte superior, da interface gráfica.
		 * Esse painel irá agrupar tanto o formulário (com os campos Nome, E-mail, etc.) quanto os botões (Salvar, Atualizar, etc.)
		 * O layout utilizado é BorderLayout, que permite distribuir componentes nas regiões: NORTH, SOUTH, CENTER, EAST, WEST.
		 * Aqui, o objetivo é empilhar o formulário no centro e os botões na parte inferior (SOUTH). */
		JPanel painelSuperior = new JPanel(new BorderLayout());
		
		
		/* Adiciona o painel do formulário (painelForm), contendo os campos de texto (Nome, E-mail, etc.), na região CENTRAL do painelSuperior.
		 * Isso garante que o formulário ocupe a parte superior visível da tela, expandindo-se horizontalmente dentro do painel.*/
		painelSuperior.add(painelForm, BorderLayout.CENTER);
		
		
		/* Adiciona o painel de botões (painelBotoesForm) na parte inferior (SOUTH) do painelSuperior.
		 * Os botões de ação ficarão visivelmente logo abaixo do formulário, mantendo uma estrutura vertical clara e intuitiva para o usuário.*/
		painelSuperior.add(painelBotoesForm, BorderLayout.SOUTH);
		
		/* Cria um novo painel chamado painelPesquisa, que será responsável por conter os componentes da funcionalidade de pesquisa (rótulo, campo de texto, e botão.)
		 * O layout utilizado é FlowLayout alinhado à esquerda (FlowLayout.LEFT), o que faz com que os componentes adicionados fiquem organizados lado a lado
		 * em uma única linha, iniciando da margem esquerda*/
		JPanel painelPesquisa = new JPanel(new FlowLayout(FlowLayout.LEFT));
		
		
		/* Adiciona um rótulo de texto ao painel de pesquisa com a instrução "Pesquisar (nome ou email): "
		 * O JLabel é um componente usado para  exibir informações fixas na interface, servindo aqui como explicação sobre o campo a seguir.*/
		painelPesquisa.add(new JLabel("Pesquisar (nome ou email): "));
		
		/* Cria um campo de texto (JTextField) para que o usuário digite o termo de busca.
		 * O argumento 15 define uma largura inicial de 15 colunas visuais de caracteres, o que define o tamanho horizontal do campo.
		 * Esse campo aceitará tanto nomes quanto e-maisl como critério de pesquisa.*/
		txtPesquisa = new JTextField(15);
		
		/* Adiciona o campo de texto txtPesquisa ao painel de pesquisa posicionando-o logo ao lado do rótulo explicativo.*/
		painelPesquisa.add(txtPesquisa);
		
		/* Cria o botão btnPesquisar com o texto "Pesquisar", que será utilizado para executar a operação de busca.
		 * Quando clicado, ele acionará um método que filtra os registros mostrados na tabela com base no texto digitado no campo txtPesquisa*/
		btnPesquisar = new JButton("Pesquisar");
		
		/* Adiciona o botão btnPesquisar ao painel de pesquisa, completando a linha com: rótulo + campo de texto + botão.
		 * Isso cria uma barra de pesquisa funcional, visualmente acessível e clara ao usuário*/
		painelPesquisa.add(btnPesquisar);
		
		
		/*Cria uma nova instância de DefaultTableModel chamada modeloTabela. 
		 * O DefaultTableModel é a estrutura que armazena os dados que serão exibidos dentro do JTable (a tabela visível na interface).
		 * Ele define não apenas os valores das células, mas também os nomes das colunas e o comportamento das células (como edição).
		 * O construtor recebe dois parâmetros:
		 *  1º new Object[]{"_id", "Nome", "E-mail, "Telefone", "Data Nasc."}
		 *  	Esse é um array de objetos (Strings) que define os nomes das colunas da tabela.
		 *  	Ou seja o cabeçalho da tabela terá essas colunas.
		 *  		-> "_id": corresponde ao identificador único do MongoDB (ObjectID).
		 *  		-> "Nome": nome da pessoa cadastrada
		 *  		-> "Telefone ": número de telefone.
		 *  		-> "Data Nasc.": data de nascimento da pessoa 
		 *  2º o 0 -> Indica que , inicialmente, a tabela será criada sem nenhuma linha de dados. 
		 *  		  O conteúdo será preenchido dinamicamente depois. */
		modeloTabela = new DefaultTableModel(new Object[] {"_id", "Nome", "E-mail", "Telefone", "Data Nasc."}, 0) {
			
			/* Sobrescre o método isCellEditable para tornar as células da tabela **não editáveis** diretamente pelo usuário.
			 * Por padrão, se não for sobrescrito, o DefaultTableModel permite que o usuário edite os valores das células clicando nelas
			 * Nesse caso aqui, como os dados devem ser manipulados apenas através dos botões e formulários (Salvar, Atualizar), essa edição
			 * direta na tabela deve ser desativada para evitar inconsistências.*/
			@Override
			public boolean isCellEditable(int row, int col) {
				
				return false; /*Retorna false para todas as células, ou seja nenhuma célula da tabela poderá ser editada manualmente pelo usuário.*/
			
			}
		};
		
		
		/* JTable é um componente da biblioteca Swing utilizado para exibir dados em forma de tabela(linha e colunas).
		 * Ele permite que os dados sejam organizados visualmente e manipulados via cliques, seleções, etc.
		 * Aqui, a tabela será alimentada com o modelo de dados definido por modeloTabela. */
		tabela = new JTable(modeloTabela);
		
		/* Tabela é o objeto JTable criado anteriormente, que agora receberá uma configuração de comportamento.
		 * O método setSelectionMode define como o usuário pode selecionar linhas na tabela.
		 * Ele restringe ou libera a possibilidade de selecionar uma ou várias linhas ao mesmo tempo.*/
		tabela.setSelectionMode(
				     /*ListSelectioModel é uma interface que define os modos de seleção em listas, tabelas ou grades no Swing.
				      * Ela determina como o usuário pode interagir com as seleções de elementos visuais.
				      * Essa interface é utilizada aqui para configurar a JTable.*/
					ListSelectionModel.SINGLE_SELECTION
					                     /*SINGLE_SELECTION: é uma constantes definida em ListSelectionModel.
					                      * Essa constante idica que apenas *uma única lina* da tabela pode ser selecionada por vez.
					                      * Isso é útil para operações como atualizar ou excluir, que devem afetar apenas um registro por vez.*/
		);
		
		
		/*tabela.getColumnModel: retora o modelo de colunas da JTable, que permite acessar e manipular propriedades das colunas, como largura, visibilidade, ordem, etc.
		 * getColumn(0): retorna a primeira coluna da tabela, que no caso é a coluna "_id". A numeração das colunas começa em 0, então coluna 0 é "_id", coluna 1 é "Nome", e assim por diante 
		 * setMinWidth(0): define a largura mínima da coluna como 0 pixels. Isso é uma técnica para *esconder* visualmente a coluna sem removê-la da estrutura lógica da tabela. */
		tabela.getColumnModel().getColumn(0).setMinWidth(0);
		
		/*Mesmo explicação anterior, agora com setMaxWidth(0), que define a largura **máxima** da coluna como 0 pixels.
		 * Com a largura mínima e máxima iguais a 0, a coluna "_id" se torna invisível ao usuário, mas ainda pode ser acessada programaticamente
		 * (ex: para excluir um registro usando o _id do MongoDB)*/
		tabela.getColumnModel().getColumn(0).setMaxWidth(0);
		
		
		/*JScrollPane é um componente que cria uma "área de rolagem" ao redor de outro componente, como listas ou tabelas.
		 * Ele adiciona barras de rolagem horizontais e verticais automaticamente quando o conteúdo ultrapassa o espaço visível.
		 * Aqui, o scrollTabela é um JScrollPane que envolve a tabela, garantindo rolagem quando houver muitos registros. */
		JScrollPane scrollTabela = new JScrollPane(tabela);
		
		
		/* JPanel é um conteiner visual do Swing usado para agrupar componentes de forma organizada.
		 * Aqui, estamos criando o painel que conterá exclusivamente a tabela.
		 * O layout utilizado é BorderLayout com espaçamento de 10 pixels na horizontal e vertical, que dá um "respiro visual" entre os componentes.*/
		JPanel painelCentral = new JPanel(new BorderLayout(10,10));
		
		
		/*Adiciona o JScrollPane (que contém a JTable) à região central do painelCentral
		 * No BorderLayout, CENTER é a região que ocupa o maior espaço disponível do painel.
		 * Isso garante que a tabela fique centralizada e se expanda conforme o tamanho da janela.*/
		painelCentral.add(scrollTabela, BorderLayout.CENTER);
		
		/* Adiciona o painelSuperior à parte superior (NORTH) do painelPricipal.
		 * painelSuperior contém o formulário de entrada e os botões de ação.
		 * A posição NORTH no BorderLayout posiciona esse painel no topo da interface*/
		painelPrincipal.add(painelSuperior, BorderLayout.NORTH);
		
		
		/* Adiciona o painelCentral (com a tabela) à região central do painelPrincipal 
		 * Isso garante que a tabela seja o elemento principal visual da tela.*/
		painelPrincipal.add(painelCentral, BorderLayout.CENTER);
		
		
		/*Adiciona o painelPesquisa (barrade pesquisa com campo e botão) à parte inferior (SOUTH) do painelPrincipal.
		 * Isso posiciona o campo de pesquisa do rodape da aplicação.*/
		painelPrincipal.add(painelPesquisa, BorderLayout.SOUTH);
		
		
		/*setContentPane define qual painel será o conteúdo principal da janela JFrame.
		 * Aqui, substituímos o conteúdo padrão pelo painelPrincipal que contém todos os elementos visuais da aplicação.
		 * Isso é essencial para que tudo seja exibido corretamente na interface gráfica.*/
		setContentPane(painelPrincipal);
		
		
		/*btnSalvar é o botão responsável por inserir um novo registro no banco de dados MongoDB.
		 * O método addActionListener é utilizado para adicionar um ouvinte de eventos de ação ao botão.
		 * Esse ouvinte responde ao evento de clique do mouse.
		 * A expressão lambda ( e -> salvarRegistro()) é uma forma concisa de definir o que deve acontecer quando o botão for clicado.
		 * O método salvarRegistro() é chamado imediatamente quado o botão é clicado.
		 * Esse método coleta os dados dos campos de texto, valida-os, cria um documento e insere no banco*/
		btnSalvar.addActionListener(e -> salvarRegistro());
		
		
		/*btnNovo é o botão que limpa o formulário de entrada, permitindo iniciar um novo cadasto.
		 * O método addActionListener adiciona um ouvinte de clique para esse botão.
		 * A expressão lambda define duas ações que devem ocorrer quando o botão for clicado.*/
		btnNovo.addActionListener(e -> {
			
			/*O método limparCampos() apaga o conteúdo de todos os campos de entrada:
			 * txtNome, txtEmail, txtTelefone, e txtDataNascimento, deixando-os em branco.*/
			limparCampos();
			
			
			/*O método clearSelection() da JTable remove qualquer seleção de linha feita na tabela
			 * Isso garante que, ao limpar o formulário, nenhuma linha fique selecionada, o que também desativa os botões Atualizar e Excluir.*/
			tabela.clearSelection();
			
		});
		
		
		/*Adiciona um ouvinte de ação (ActionListener) ao botão btnPesquisar.
		 * Esse ouvinte será executado sempre que o botão "Pesquisar" for clicado*/ 
		btnPesquisar.addActionListener(e -> {
			
			/*txtPesquisa é o campo onde o usuário digita o termo de busca.
			 * O método getText() recupera o texto inserido no campo.
			 * O método trim()* remove os espaços em branco no início e fim da string, garantindo que a busca seja feita de forma limpa.*/
			String termo = txtPesquisa.getText().trim();
			
			
			/*Chama o método carregarRegistros passando como argumento o termo digitado.
			 * Esse método atualiza a tabela exibindo apenas os registros que contenham o termo no nome ou no e-mail.*/
			carregarRegistros(termo);
			
		});
		
		
		/* Adiciona um ouvinte de ação ao botão btnAtualizar.
		 * Ao clicar no botão "Atualizar", o método atualizarRegistro() será chamado.
		 * Esse método recupera os dados dos campos e atualiza o documento correspondente no MongoDB*/
		btnAtualizar.addActionListener(e -> atualizarRegistro()	);
		
		
		/*Adiciona um ouvinte de ação ao botão btnExcluir.
		 * Ao clicar no botão "Excluir", o método excluirRegistro()* será chamado.
		 * Esse método remove o documento selecionado da coleção no MongoDB após confirmação do usuário.*/
		btnExcluir.addActionListener(e -> excluirRegistro() );
		
		
		/* Adiciona um ouvinte de ação ao botão btnExportar. Ao clicar no botão "Exportar Excel", o método exportarDadosExcel() será chamado.
		 * Esse método exporta os dados da tabela para um arquivo CSV que pode ser aberto no EXCEL.*/
		btnExportar.addActionListener(e -> exportarDadosExcel());
		
		
		/*Adiciona um ouvinte de seleção de linha na tabela.
		 * O método getSelectioMode() retorna o modelo de seleção da JTable, responsável por monitorar quais linhas estão selecionadas.
		 * O método addListSelectionListener adiciona um listener que será executado sempre que o usuário clicar ou mudar a seleção de linha.*/
		tabela.getSelectionModel().addListSelectionListener(e -> {
			
			
			/* Verifica se o evento ainda está em fase de "ajuste". Quando o usuário ainda está arrastando ou navegando com o teclado, o evento pode ser disparado múltiplas vezes.
			 *  Essa verificação com !e.getValueIsAdjusting() garante que o código só executado após o ajuste final, ou seja quando a seleção realmente for concluída.*/
			if(!e.getValueIsAdjusting()) {
				
				/* Obtém o índice da linha atualmente selecionada na tabela.
				 * O índice começa em 0 para a primeira linha, 1 para segunda, e assim por diante.
				 * Caso nenhuma linha esteja selecionada, o valor retornado será -1.*/
				int linha = tabela.getSelectedRow();
				
				/* Verifica se uma linha válida está selecionada (índice maior ou igual a 0).
				 * Isso é necessário para evitar erros ao tentar acessar uma linha inexistente.*/
				if (linha >= 0 ) {
					
					/*Preenche o campo de texto txtNome com o valor da coluna "Nome" da linha selecionada.
					 * GetValueAt(linha, 1): a liha selecionada e a coluna 1 (segunda coluna, que é Nome)*/
					txtNome.setText( (String) modeloTabela.getValueAt(linha, 1));
					
					/*Preenche o campo txtEmail com o valor da coluna "E-mail" da linha selecionada*/
					txtEmail.setText( (String) modeloTabela.getValueAt(linha, 2));
					
					/*Preenche o campo txtTelefone com o valor da coluna "telefone" selecionada*/
					txtTelefone.setText( (String) modeloTabela.getValueAt(linha, 3));
					
					/*Preenche o campo txtDataNasicmento com o valor da coluna "Data Nasc" linha selecionada*/
					txtDataNascimento.setText( (String) modeloTabela.getValueAt(linha, 4));
					
					/*Ativa o botão "Atualizar", permitindo que o usuário altere o registro selecionado*/
					btnAtualizar.setEnabled(true);
					
					/*Ativa o botão "Excluir", permitindo que o usupario remova o registro selecionado.*/
					btnExcluir.setEnabled(true);
					
				} else {
					
					/*Caso nenhuma linha esteja selecionada (linha == -1), os botões "Atualizar" e "Excluir" são desativados. 
					 * Isso previne que o usuário clique nesses botões sem que haja um registro válido selecionado.*/
					btnAtualizar.setEnabled(false);
					btnExcluir.setEnabled(false);
				
				}
				
			}
			
		});
		
		
		
	} // Fim do método -> criarInterface()
	
	
	
	/* Declaração do método carregarRegistros, que recebe como parâmetro uma String chamada filtro.
	 * Esse método é responsável por buscar registros no MongoDB 
	 * com base no filtro fornecido, e exibi-los na tabela (JTable) da interface*/
	private void carregarRegistros(String filtro) {
		
		/* modeloTabela é o modelo de dados associado à JTable.
		 * O método setRowCount(0) limpa todas as linhas da tabela, ou seja, remove os dados existentes.
		 * Isso é importante para garantir que os novos registros não sejam adicionados por cima dos antigos, 
		 * evitando duplicação ao recarregar os dados.*/
		modeloTabela.setRowCount(0);
		
		
		/* Cria uma lista chamada resultados para armazenar os documentos (registros) retornados do banco de dados.
		 * A lista é do tipo Document, que representa cada documento do MongoDB.
		 * Será preenchida com os documentos encontrados na coleção conforme o filtro. */
		List<Document> resultados = new ArrayList<>();
		
		
		/* Verifica se o filtro passado como parâmetro está vazio.
		 * Se estiver vazio, significa que o usuário não digitou nenhum termo de busca, então
		 * todos os registros da coleção devem ser carregados.*/
		if (filtro.isEmpty()) {
			
			/*Recupera todos os documentos da coleção (MongoCollection).
			 * O método find() sem parâmetros retorna todos os registros.
			 * O métodointo(resultados) armazena os resultados diretamente na lista criada anteriormente.*/
			colecao.find().into(resultados);
		
		} else {
			
			/* Caso o filtro não esteja vazio, cria-se uma condição de busca usando operadores do MongoDB 
			 * Cria uma novo objeto Document chamado cond, que representa a condição de filtro.
			 * A chave "$or" indica que queremos buscar registros onde **pelo menos uma** das condições seja verdadeira*/
			Document cond = new Document("$or", List.of(
					
					/* Primeira condição: busca documentos cujo campo "nome" contenha o valor do filtro. 
					 * "$regex" indica uma expressão regular (busca parcial, não exata). 
					 * $options* com valor "i" define que a busca será **case-insensitive** (ignora maiúsculas e minúsculas). */
					new Document("nome", new Document("$regex", filtro).append("$options", "i")),
					
					
					/* Segunda condição: busca documentos cujo campo "email" contenha o valor do filtro,
					 * também de forma parcial e sem considerar maiúsculas/minúsculas */
					new Document("email", new Document("$regex", filtro).append("$options", "i"))
			));
			
			
			/* Executa a busca na coleção com base na codição construída.
			 * O método find(cond) retorna todos os documentos que correspondem ao filtro.
			 * Os resultados são armazenados na lista resultados. */
			colecao.find(cond).into(resultados);
		}
		
		
		/* Inicia um loop for-each que percorre todos os documentos retornados na lista resultados.
		 * Cada item da lista é um objeto do tipo Document, que representa um registro da coleção MongoDB.
		 * A variável d é usada como referência para o documento atual dentro do laço.*/
		for (Document d : resultados) {
			
			/* Recupera o valor do campo "_id" do documento atual.
			 * O método getObjectId("_id") retorna o identificador único do MongoDB no formato ObjectID.
			 * Esse campo é gerado automaticamente pelo MongoDB no momento da inserção do documento.*/
			Object idObj = d.getObjectId("_id");
			
			/* Recupera o valor do campo "nome" do documento atual.
			 * O método getString("nome") retorna o valor armazenado no campo "nome"
			 * no formato String.*/
			String nome = d.getString("nome");
			
			/*Recupera o valor do campo "email" do documento atual*/
			String email = d.getString("email");
			
			/*Recupera o valor do campo "telefone" do documento atual*/
			String tel = d.getString("telefone");
			
			/*Recupera o valor do campo "dataNascimento" do documento atual*/
			String data = d.getString("dataNascimento");
			
			/* Adiciona uma nova linha na tabela (modeloTabela) com os dados extraídos do documento.
			 * A linha é criada como um array de objetos contendo os seguintes dados, na ordem:
			 *  -idObj.toString(): converte o ObjectId para String (essa coluna está oculta na interface). 
			 *  - nome, email, tel, data: dados visíveis que serão exibidos nas colunas da JTable. */
			modeloTabela.addRow(new Object[] { idObj.toString(), nome, email, tel, data });
		}
		
	} /* Fim do método -> CarregarRegistros()*/
	
	
	
	
	/*Declaração do método salvarRegistro() com escopo private.
	 * Esse método é chamado quando o botão "Salvar" é clicado e tem como objetivo capturar os dados do formulário, validar e inseri-los 
	 * no banco de dados MongoDB.*/
	private void salvarRegistro() {
		
		/* Recupera o texto digitado o campo txtNome.
		 * O método getText() obtém o conteúdo do campo texto.
		 * O método trim() remove espaços em branco no início e no final da string,
		 * garantindo que nomes como "João" sejam tratados corretamente como "João"*/
		String nome = txtNome.getText().trim();
		
		/*Recupera o texto digitado no campo txtEmail, aplicando trim() da mesma forma.*/
		String email = txtEmail.getText().trim();
		
		/*Recupera o texto digitado no campo txtTelefone, aplicando o trim()*/
		String tel = txtTelefone.getText().trim();
		
		/* Recupera o texto txtDataNascimento, aplicando o trim()*/
		String data = txtDataNascimento.getText().trim();
		
		
		/*Verifica se os campos "nome" ou "email" estão vazios.
		 * O método isEmpty() retorna true se a string estiver vazia("").
		 * Como o nome e e-mail são considerados obrigatórios, essa validação impede que um registro sem essas infromações
		 * seja salvo no banco de dados.*/
		if (nome.isEmpty() || email.isEmpty()) {
			
			/*Exibe uma caixa de diálogo com uma mensagem de aviso.
			 * JOptionPane é uma classe utilitária do Swing que exibe janelas de mensagem.
			 * showMessageDialog(...) exibe um alerta com título conteúdo e ícone de tipo.
			 * Neste caso, o ícone WARNING_MESSAGE indica que é um aviso.*/
			JOptionPane.showMessageDialog(this, "Nome e E-Mail são obrigatórios!",  /*Mensagem de erro exibida ao usuário*/
												"Aviso", 						/*Título da caixa de diálogo*/
												JOptionPane.WARNING_MESSAGE);   /*Tipo do ícone: aviso (ícone amarelo com ponto de exclamação). */
			
			/* return encerra a execução do método, impedindo 
			 * que o registro seja salvo com campos inválidos*/ 
			return;
		}
		
		
		/* Cria um novo objeto do tipo Document, que representa um documento no formato BSON (estrutura interna do MongoDB).
		 * Esse objeto será inserido diretamente na coleção do MongoDB com um novo registro.
		 * O primerio par chave-valor inserido é: "nome" -> nome (conteúdo digitado no campo txtNome).*/
		Document doc = new Document("nome", nome)
												.append("email", email)  /* Adiciona ao documento o par "email" -> email. O método append adiciona uma nova chave e seu valor ao Document de forma encadeada.*/
												.append("telefone", tel) /* Adiciona ao documento o par "telefone" -> tel*/
												.append("dataNascimento", data); /* Adiciona ao documento o par "DataNascimento" -> data*/
		
		/*Insere o documento recém-criado na coleção MongoDB chamada coleção
		 * O método insertOne(Document) realiza a operação de inserção no banco.
		 * Após essa linha, o documento estará persistido na base de dados (em disco ou memória, dependendo da configuração).*/
		colecao.insertOne(doc);
		
		
		/*Exibe uma caixa de diálogo informando ao usuário que o registro foi inserido com sucesso.
		 * JOptionPane.showMessageDialog é usado para exibir mensagens com interface gráfica ao usuário.*/
		JOptionPane.showMessageDialog(this, /* O parâmetro this faz com que a mensagem seja exibida sobre a janela atual da aplicação. */
									  "Registro inserido com sucesso!", /* A string "Registro inserid ocom sucesso!" é o conteúdo da mensagem*/
									  "Sucesso",		/*É o titulo da caixa de diálogo.*/
									  JOptionPane.INFORMATION_MESSAGE); /*Define o ícone azul de informação (ícone de sucesso) */
		
		
		/* Chama o método limpar Campos(), que limpa os campos do formulário (nome, email, telefone e data).
		 * Isso prepara a tela para um novo cadastro, deixando os campos em branco.*/
		limparCampos();
		
		/*Chama o método carregarRegistros("") passando uma string vazia como parâmetro. 
		 * Isso faz com que todos os registros do banco de dados sejam carregados e exibidos na tabela novamente,
		 * incluindo o novo registro que acabou de ser inserido.*/
		carregarRegistros("");
		
	} /* Fim método salvarRegistro() */
	
	
	/* Declaração do método atualizarRegistro() com escopo private.
	 * Este método é chamado quando o botão "Atualizar" é clicado e tem como objetivo modificar os dados de um registro
	 * existente selecionado na tabela, atualizando-o na base de dados MongoDB.*/
	private void atualizarRegistro() {
		
		/* Obtém o índice da linha atualmente selecionada na JTable.
		 * O método getSelectedRow() retorna o número da linha selecionada (começando em 0).
		 * Se nenhuma linha estiver selecionada, o método retorna -1.*/
		int linha = tabela.getSelectedRow();
		
		/* Verifica se o valor retornado é menor que 0, ou seja, se nenhuma linha foi selecionada pelo usuário.*/
		if(linha < 0) {
			
			/* Se nenhuma linha foi selecionada, exibe uma mensagem de aviso ao usuário.
			 * JOptionPane.showMessaDialog exibe uma caixa de diálogo. */
			JOptionPane.showMessageDialog(this,  /*Parametro this: Indica que a janela atual será o "pai" da caixa de diálogo */
					"Selecione um registro na tabela para atualizar,", /*"Selecione um registro...": é o texto da mesagem a ser exibida*/
					"Aviso",  /*Titulo da caixa de diálogo */
					JOptionPane.WARNING_MESSAGE); /*ícone de aviso (ícone amarelo) */
			
			/* O comando return encerra imediatamente a execução do método.
			 * Isso impede que o código abaixo seja executado quando não houver linha selecionada, evitando erros 
			 * ou comportamentos inesperados. */
			return;	
		}
		
		
		/* Obtém o identificador único (_id) do registro selecionado na tabela.
		 *  modeloTabela é o modelo de dados associado à JTable.
		 * O método getValueAt(linha,0) retorna o valor da célula na linha selecionada e na primeira coluna
		 * (índice 0), que corresponde ao campo "_id" do documento no MongoDB.
		 * Esse valor é armazenado como uma String na variável idStr*/
		String idStr = (String) modeloTabela.getValueAt(linha, 0);
		
		/* Recupera o texto inserido no campo txtNome, que corresponde ao nome do contato.
		 * O método getText() obtém o conteúdo atual do JTextField.
		 * O método trim() remove quaisquer espaços em branco no início e no final da string, 
		 * garantindo que não haja espaços extras que possam interferir na validação ou no armazenamento dos dados.*/
		String nome = txtNome.getText().trim();
		
		/* Recupera o texto inserido no campo txtEmail, que corresponde ao e-mail do contato.
		 * Aplica-se o mesmo processo: getText() para obter o conteúdo e trim() para limpar espaços em branco desnecessários.*/
		String email = txtEmail.getText().trim();
		
		
		/*Recupera o texto inserido no campo txtTelefone, que corresponde ao telefone do contato.
		 * Novamente, utiliza-se getText() e trim() para obter e limpar o conteúdo do campo.*/
		String tel = txtTelefone.getText().trim();
		
		/* Recupera o texto inserido no campo txtDataNascimento, que corresponde à data de nascimento do contato.
		 * O mesmo processo é aplciado: getText() para obter o conteúdo e trim() para remover espaços extras.*/
		String data = txtDataNascimento.getText().trim();
		
		
		/* Verifica se os campos "nome" ou "email" estão vazios, pois são considerados obrigatórios para a atualização do registro.
		 * O método isEmpty() verifica se a string está vazia após a remoção dos espaços em branco
		 * Se qualquer um dos campos estiver vazio, exibe-se uma mensagem de aviso ao usuário e interrompe-se o processo de atualização. */
		if (nome.isEmpty() || email.isEmpty()) {
			
			JOptionPane.showMessageDialog(this,
					"Nome e E-mail são obrigatórios",
					"Aviso",
					JOptionPane.WARNING_MESSAGE);
			
			return; /*Interrompe a execução do método, pois não é possível prosseguir com a atualização sem esses campos.*/
		}
		
		
		/* Cria um novo objeto do tipo Document chamado novosValores.
		 * Este documento irá conter os novos dados que serão usados para atualizar o registro no banco de dados.
		 * A seguir, os campos "nome", "email", "telefone", e "dataNascimento" são adicionados
		 *  a este documento, com os respectivos valores informados nos campos de texto do formulário.*/
		Document novosValores = new Document()
											  .append("nome", nome)
											  .append("email", email)
											  .append("telefone", tel)
											  .append("dataNascimento", data);
		
		/*Converte a String idStr, obtida anteriormente da tabela, para um objeto ObjectId.
		 * O ObjectId é o tipo de dado utilizado internamente pelo MongoDB para representar o campo "_id".
		 * A conversão é necessária para que possamos localizar corretamente o documento original no banco.*/
		Object objId = new ObjectId(idStr);
		
		/* Cria um novo Document chamado filtro, que será usado como critério de busca para localizar o registro a ser atualizado.
		 * O filtro busca pelo campo "_id" com o valor igual ao objId convertido.*/
		Document filtro = new Document("_id", objId);
		
		/* Cria um novo Document chamado atualizacao, que contém a instrução de atualização.
		 * A chave "$set" é uma operação do MongoDB que substitui os valores de campos específicos de um documento.
		 * O valor associado a "$set" é o documento novosValores, ou seja, os novos dados que serão aplicados ao registro*/
		Document atualizacao = new Document("$set", novosValores);
		
		
		/* Executa a operação de atualização o MongoDB.
		 * O método updateOne aplica a modificação ao primeiro documento que casar com o filtro informado.
		 * Parâmetros: 
		 *   - filtro: define qual documento será atualizado (baseado no _id).
		 *   - atualizacao: define quais campos serão modificados e seus novos valores.*/
		colecao.updateOne(filtro, atualizacao);
		
		
		/* Exibe uma caixa de diálogo para informar que o registro foi atualizado com sucesso.
		 * Isso fornece feedback ao usuário de que a ação foi concluída corretamente. */
		JOptionPane.showMessageDialog(this,
				"Registro atualizado com sucesso!",  // Mensagem a ser exibida
				"Sucesso",							// Título da caixa de diálogo.
				JOptionPane.INFORMATION_MESSAGE);   // Ícone azul de sucesso.
		
		
		/* Atualiza a tabela da interface gráfica para refletir os dados atualizados.
		 * O método carregarRegistros("") com string vazia recarrega todos os registros sem aplicar filtro*/
		carregarRegistros("");
		
		
	} /*Fim método atualizarRegistro*/
	
	
	
	/* Declaração do método excluirRegistro() com escopo private.
	 * Esse método é acionado quando o botão "Excluir" é clicado,
	 * e tem como objetivo remover um documento selecionado da coleção MongoDB.*/
	private void excluirRegistro() {
		
		/* Obtém o índice da linha atualmente selecionada na tabela (JTable).
		 * O método getSelectedRow() retorna a posição da linha selecionada. 
		 * Se nenhuma linha estiver selecionada, o valor retornado será -1. */
		int linha = tabela.getSelectedRow();
		
		
		/* Verifica se nenhuma linha foi selecionada.
		 * A verificação é feita comparando se o valor de linha é menor que 0.
		 * Isso previne que uma exclusão ocorra sem um registro válido selecionado. */
		if (linha < 0) {
			
			/* Exibe uma mensagem de aviso para o usuário informando 
			 * que é necessário selecionar um registro antes de tentar excluir.*/
			 JOptionPane.showMessageDialog(this,       // Indica que o JFrame atual será o "pai" da caixa de diálogo
					 "Selecione um registro na tabela para excluir!",  // -> "Selecione um registro...": Mensagem mostrada ao usuário. 
					 "Atenção",						 // Título da janela de mensagem.
					 JOptionPane.WARNING_MESSAGE);   // WARNING_MESSAGE: Define o ícone como um ponto de exclamação (ícone de alerta).
			 
			 /* Interrompe a execução do método caso nenhuma linha esteja selecionada
			  *  Isso impede que as próximas etapas (como excluir do banco)
			  *   sejam executadas sem um alvo definido. */
			 return;
		}
		
		
		/* Recupera o valor da primeira coluna (índice 0) da linha selecionada na tabela.
		 * Essa coluna contém o "_id" do documento, que é o identificador único gerado pelo MongoDB.
		 * O valor retornado é convertido para String, pois é armazenado no modelo da tabela como texto. */
		String idStr = (String) modeloTabela.getValueAt(linha, 0);
		
		
		/* Exibe uma caixa de diálogo perguntando ao usuário se ele realmente  deseja excluir o registro. 
		 * JOptionPane.showConfirmDialog mostra uma caixa com botões de confirmação (Sim e Não).*/
		int confirm = JOptionPane.showConfirmDialog(this,  /* Janela pai da caixa de diálogo (neste caso, o JFrame atual).*/
				"Tem certeza que deseja excluir este registro?",  /*Mensagem principal exibida na caixa*/
				"Confirmar Exclusão",     /* Título da caixa de diálogo*/
				JOptionPane.YES_NO_OPTION);   /*Define os botões como "Sim" e "Não"*/
		
		
		/* Verifica se o usuário *NÃO clicou em "SIM".
		 * JOptionPane.YES_OPTION é uma constante que representa o clique no botão "SIM"
		 *  Se o valor de "confirm" for diferente de YES_OPTION, significa que o usuário clicou em "Não" ou fechou a janela.
		 *  Nesse caso, o método é encerrado com return, cancelando o processo de exclusão. */
		if (confirm != JOptionPane.YES_OPTION) {
			return;
		}
		
		
		/* Coverte a string idStr, obtida anteriormente da tabela, para um objeto ObjectId.
		 * O ObjectId é o tipo específico de identificador utilizado pelo MongoDB para o campo "_id". 
		 * Essa conversão é necessário porque a operação de exclusão exige que o filtro use um ObjectId válido*/
		ObjectId objId = new ObjectId(idStr);
		
		
		/* Cria um novo documento MongoDB chamado filtro. *Esse filtro será usado como critério 
		 * de busca para localizar exatamente o documento a ser excluído.
		 * Ele busca por um documento cujo campo "_id" seja igual ao objId criado acima. */
		Document filtro = new Document("_id", objId);
		
		
		/* Executa a operação de exclusão no banco de dados MongoDB.
		 * O método deteOne remove o primeiro documento que corresponde ao filtro especificado.
		 * Neste caso, como "_id", é único haverá no máximo um documento afetado.
		 * Essa é a linha que efetivamente remove o registro da base de dados. */
		colecao.deleteOne(filtro);
		
		
		/* Exibe uma caixa de diálogo informando que o registro foi excluído com sucesso.
		 * Isso fornece um feedback visual ao usuário de que a ação foi realizada corretamente.*/
		JOptionPane.showMessageDialog(this,  // Componente pai (a janela atual)
				"Registro excluído com sucesso!",   // Mensagem a ser exibida.
				"Sucesso",						// Título da caixa diálogo.
				JOptionPane.INFORMATION_MESSAGE ); // Ícone de informação (azul com 'i').
		
		
		/* Recarrega todos os registros da base de dados para atualizar a tabela exibida na interface.
		 * O parâmetro "" (string vazia) indica que a busca será 
		 * feita sem filtro, ou seja, todos os registros serão listados. */
		carregarRegistros("");
		
		/*Limpar os campos de txt, após a exclusão e carregamento de registros.*/
		limparCampos();
		
	} /* Fim do método excluirRegistro()*/
	
	
	
	/* Declaração do método exportarDadosExcel() com escopo private.
	 * Este método é responsável por exportar os dados exibidos na tabela da interface gráfica para um arquivo 
	 * no formato CSV (valores separados por ponto e vírgula), que pode ser aberto no Excel.*/
	private void exportarDadosExcel() {
		
		/*Cria um objeto JFileChooser, que é um componente gráfico
		 * Swing usado para abrir caixas de diálogo de seleção de arquivos e diretórios.
		 * Neste caso, será usado para permitir ao usuário escolher onde salvar o arquivo CSV.*/
		JFileChooser fileChooser = new JFileChooser();
		
		
		/* Define o título da janela de diálogo que será exibida ao usuário
		 * Isso ajuda a indicar claramente que o objetivo da caixa é salvar um arquivo CSV*/
		fileChooser.setDialogTitle("Salvar arquivo CSV");
		
		
		/* Exibe a caix de diálogo para o usuário escolher onde salvar o arquivo.
		 * O método showSaveDialog(this) exibe a caixa de diálogo de "Salvar como".
		 * O parâmetro `this` indica que a janela principal será usada como pai da caixa
		 * O método retorna um inteiro que representa a ação do usuário: se ele confirmou (OK) */
		int userSelection = fileChooser.showSaveDialog(this);
		
		/* Verifica se o usuário clicou no botão "Salvar" (OK).
		 * JFileChooser.APPROVE_OPTIONN é uma constante que indica
		 * que o usuário confirmou a ação.*/
		if(userSelection == JFileChooser.APPROVE_OPTION) {
			
			
			/*Obtém o arquivo selecionado pelo usuário na caixa de diálogo
			 *  Este é o local e nome do arquivo que será salvo no disco*/
			File fileToSave = fileChooser.getSelectedFile();
			
			
			/* Verifica se o nome do arquivo termina com ".csv" (independente de letras maiúsculas/minusculas).
			 * Caso contrário, adiciona automaticamente a extensão ".csv" ao final do nome.
			 * Isso garante que o arquivo salvo tenha o formato esperado para ser aberto no Excel*/
			if(!fileToSave.getName().toLowerCase().endsWith(".csv")){
				fileToSave = new File(fileToSave.getAbsolutePath()+ ".csv");
			}
			
			
			/*Inicia um bloco try-with-resources que cria um PrintWriter para escrever no arquivo selecionado.
			 * PrintWriter é uma classe utilizada para escrever texto em arquivos de forma simples
			 * O recurso (pw) será automaticamente fechado ao final do bloco, mesmo que ocorra uma exceção.
			 * Isso garante o fechamento correto do arquivo, evitando vazamento de recursos*/
			try(PrintWriter pw = new PrintWriter(fileToSave)) {
				
				for (int col = 0; col < modeloTabela.getColumnCount(); col++) {
					
					pw.print(modeloTabela.getColumnName(col));
					
					if(col < modeloTabela.getColumnCount() -1) {
						
						pw.print(";");
						
					}
					
				}
				
				pw.println();
				
				for( int row = 0; row < modeloTabela.getRowCount(); row ++ ) {
					
					for ( int col = 0; col < modeloTabela.getColumnCount(); col++ ) {
						
						pw.print(modeloTabela.getValueAt(row, col));
						
						if(col < modeloTabela.getColumnCount() - 1) {
							pw.print(";");
						}
					}
					
					pw.println();
				}
				
				JOptionPane.showMessageDialog(this, 
												"Dados exportados com sucesso!",
												"Sucesso",
												JOptionPane.INFORMATION_MESSAGE);
				
			} catch(Exception ex) {
				
				JOptionPane.showMessageDialog(this, 
												"Erro ao exportar os dados: " + ex.getMessage(),
												"Erro",
												JOptionPane.ERROR_MESSAGE);
			} // FIM try
		}
		
	} /* Fim do método exportarDadosExcel() */
	
	
	private void limparCampos() {
		txtNome.setText("");
		txtEmail.setText("");
		txtTelefone.setText("");
		txtDataNascimento.setText("");
	}
	
	/* Método privado chamado conectarMongo.
	 *  Sua responsabilidade é estabelecer a conexão com o servidor MongoDB local, selecionar o banco de dados correto e acessar a coleção onde os dados serão armazenados.
	 *  O escopo do método é `private` porque ele só será usado internamente pela classe `CadastroMongoApp` */
	private void conectarMongo() {
		
		/* Cria uma instância do cliente MongoDB utilizando a URI padrão para conexão local.
		 *  "mongoDB://localhost:27017" indica que o servidor mongoDB está rodando localmente na porta padrão 27017, 
		 *  sem autenticação ou paramêtros adicionais.
		 *  
		 *  MongoClients.create(...) é um método estático que retorna uma implementação de MongoClient, 
		 *  permitindo iniciar a comunicação com o servidor MongoDB. */
		mongoClient = MongoClients.create("mongodb://localhost:27017");
		
		
		/* Seleciona (ou cria, se ainda não existir) o banco de dados chamado "cadastro".
		 *  O método `getDatabase(String nome)` retorna uma instância de `MongoDatabase`, que permite executar operações 
		 *  como criar coleções, consultar documentos, etc.
		 *  No MongoDB, o banco de dados é criado "sob demanda" - ou seja, ele só será criado de fato
		 *  quando um documento for inserido dentro de alguma coleção.  */
		database = mongoClient.getDatabase("cadastro");
		
		
		/*  Acessa (ou cria, se ainda não existir) a coleção chamada "pessoas" dentro do banco "cadastro". 
		 * 	Uma coleção do MongoDB é equivalente a uma "tabela" em banco de dados relacionais.
		 *  Aqui, os documentos inseridos conterão dados como nome, e-mail, telefone, etc.
		 *  O tipo da coleção é `MongoCollection<Document>`, o que significa que os registros armazenados nela são do tipo `Document`,
		 *   estrutura de dados do MongoDB semelhante a JSON* */
		colecao = database.getCollection("pessoas");
	}
	
	
	// Método principal da aplicação Java.
	// public: acessível de qualquer lugar.
	// static: não depende de uma instância da classe para ser executado.
	// void: não retorna nenhum valor.
	// String[] args: parâmetro que permite a passagem de argumentos via linha de comando.
	public static void main(String[] args) {
		
		// SwingUtilities.invokerLater é um método estático da classe SwingUtilities.
		// Ele serve para garantir que a criação e manipulação de componentes Swing seja feita na Event Dispatch Thread (EDT), que é a thread segura para acesso á interface gráfica em Java.
		// Isso evita problemas de concorrência ou comportamento inesperado ao interagir com a GUI.
		SwingUtilities.invokeLater(
				
				// Expressão lambda que implementa a interface Runnable.
				// O código dentro dessa lambda será executado assim que possível na EDT
				() ->
				
					// Cria uma nova instância da classe CadastroMongoApp (a janela principal da aplicação).
					// O construtor da classe inicializa a interface gráfica, conecta ao MongoDB e carrega os dados iniciais.
					// setVisible(true): Torna a janela visível para o usuário. Sem essa chamada, a janela é criada mas não exibida na tela.
					new CadastroMongoApp().setVisible(true)
				);

	}

}
