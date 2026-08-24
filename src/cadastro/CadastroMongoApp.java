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
		//carregarRegistros("");
		
		
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
	
	
	
	private void carregarRegistros(String filtro) {
		
	}
	
	
	private void salvarRegistro() {
		txtNome.setText("");
	}
	
	
	private void atualizarRegistro() {
		txtNome.setText("");
	}
	
	
	private void excluirRegistro() {
		txtNome.setText("");
	}
	
	
	private void exportarDadosExcel() {
		txtNome.setText("");
	}
	
	
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
		 *  "mongoDB://localhost:27017" indica que o servidor mongoDB está rodando localmente na porta padrão 27017, sem autenticação ou paramêtros adicionais.
		 *  
		 *  MongoClients.create(...) é um método estático que retorna uma implementação de MongoClient, permitindo iniciar a comunicação com o servidor MongoDB. */
		mongoClient = MongoClients.create("mongodb://localhost:27017");
		
		
		/* Seleciona (ou cria, se ainda não existir) o banco de dados chamado "cadastro".
		 *  O método `getDatabase(String nome)` retorna uma instância de `MongoDatabase`, que permite executar operações como criar coleções, consultar documentos, etc.
		 *  No MongoDB, o banco de dados é criado "sob demanda" - ou seja, ele só será criado de fato quando um documento for inserido dentro de alguma coleção.  */
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
