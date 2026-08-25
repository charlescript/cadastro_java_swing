# 📋 Cadastro de Pessoas com Java Swing e MongoDB

Aplicação Desktop desenvolvida em Java utilizando a biblioteca gráfica Swing para a interface do usuário e o banco de dados NoSQL MongoDB para persistência de dados. O projeto contempla um CRUD completo com buscas dinâmicas e exportação de dados.

---

## 🎥 Demonstração do Funcionamento

<div align="center">
  <a href="https://drive.google.com/file/d/1Pup7cjcaizBBFlF1RE8937AMOAZDLfT1/view?usp=sharing" target="_blank">
    <img src="https://img.shields.io/badge/▶️_Assistir_Vídeo_de_Demonstração_no_Google_Drive-000000?style=for-the-badge&logo=googledrive&logoColor=white" target="_blank" alt="Assista ao vídeo no Google Drive" />
  </a>

  <br/><br/>
  
  💡 <i><h2>Clique no botão acima para assistir à demonstração completa do sistema no Google Drive.</h2></i>
</div>
---

## 🚀 Funcionalidades

- **CRUD Completo:** Cadastro, listagem, atualização e exclusão de registros.
- **Validação de Entrada:** Controle de campos obrigatórios (Nome e E-mail).
- **Busca em Tempo Real:** Pesquisa parcial e *case-insensitive* por nome ou e-mail utilizando operadores `$regex` e `$or` do MongoDB.
- **Interface Intuitiva:** Tabela interativa (`JTable`) com ocultação do campo interno `_id` (`ObjectId`).
- **Exportação de Dados:** Exportação da base de dados para formato CSV (compatível com Excel).

---

## 🛠️ Tecnologias Utilizadas

* **Linguagem:** Java (JDK 17+)
* **GUI:** Java Swing / AWT
* **Banco de Dados:** MongoDB (Driver Oficial `mongodb-driver-sync`)
* **Gerenciamento de Dependências:** Maven / Gradle

---

## 📦 Como Executar o Projeto

### Pré-requisitos
1. **Java Development Kit (JDK)** instalado (versão 17 ou superior).
2. **MongoDB Community Server** instalado e executando na porta padrão (`localhost:27017`).

