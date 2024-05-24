API REST para Contas a Pagar
Este projeto consiste na implementação de uma API REST para um sistema simples de contas a pagar. A aplicação permite realizar operações CRUD (Create, Read, Update, Delete) em contas a pagar, além de fornecer funcionalidades específicas conforme descrito nos requisitos gerais e específicos abaixo.

Requisitos Gerais
Linguagem de Programação: Java, versão 17 ou superior.
Framework Utilizado: Spring Boot.
Banco de Dados: PostgreSQL.
Execução em Container: A aplicação deve ser executada em um container Docker.
Orquestração com Docker Compose: Todos os serviços necessários para executar a aplicação devem ser orquestrados utilizando Docker Compose.
Controle de Versão: O código do projeto deve ser hospedado no GitHub ou GitLab.
Mecanismo de Autenticação: Utilização de mecanismo de autenticação para proteger as APIs.
Organização com Domain Driven Design: Organização do projeto utilizando o Domain Driven Design.
Gestão de Migrações de Banco de Dados: Utilização do Flyway para criar a estrutura de banco de dados.
Acesso a Dados: Utilização de JPA para acesso ao banco de dados.
Paginação de APIs: Todas as APIs de consulta devem ser paginadas.
Requisitos Específicos
Tabela de Contas a Pagar: Criação de uma tabela no banco de dados para armazenar as contas a pagar, incluindo os seguintes campos:

id
data_vencimento
data_pagamento
valor
descricao
situacao
Implementação da Entidade Conta: Implementação da entidade "Conta" na aplicação, de acordo com a tabela criada anteriormente.

APIs Implementadas:

Cadastrar conta;
Atualizar conta;
Alterar a situação da conta;
Obter a lista de contas a pagar, com filtro de data de vencimento e descrição;
Obter conta filtrando o id;
Obter valor total pago por período.
Importação de Contas via Arquivo CSV:

Implementação de mecanismo para importação de contas a pagar via arquivo CSV, que será consumido via API.
Testes Unitários: Implementação de testes unitários para garantir a qualidade do código.

Execução
Para executar a aplicação, siga os passos abaixo:

Certifique-se de ter o Docker e o Docker Compose instalados em sua máquina.
Clone o repositório do projeto: git clone <URL_DO_REPOSITORIO>
Navegue até o diretório do projeto: cd <NOME_DO_DIRETORIO>
Execute o Docker Compose para criar e iniciar os containers: docker-compose up --build
Acesse a API através da URL: http://localhost:8080
Documentação da API

create: Este método POST é usado para criar uma nova conta a pagar. Ele recebe os dados da conta no formato de objeto JSON no corpo da solicitação e valida os dados utilizando a anotação @Valid. Em seguida, chama o serviço saveConta para salvar a conta no banco de dados e retorna a conta criada.
ex: http://localhost:8080/api/contas 

{
	"dataVencimento" : "2024-01-23", 
	"dataPagamento"  : "", 
	"valor"   		 : 15.71, 
	"descricao" 	 : "Conta de Roupa"
}


update: Este método PUT é usado para atualizar uma conta a pagar existente com base no seu ID. Recebe o ID da conta a ser atualizada como parte do caminho da URL e os novos dados da conta no corpo da solicitação. Novamente, os dados são validados com a anotação @Valid e o serviço updateConta é chamado para realizar a atualização.
ex:  http://localhost:8080/api/contas/1 

{
	"dataVencimento" : "2024-04-18", 
	"dataPagamento"  : "", 
	"valor"   		 : 1854.4, 
	"descricao" 	 : "Conta IPTU"	
}

Obs: acesso apenas liberado para usuários do tipo ADMIN. 


updateSituacao: Este método PATCH é usado para alterar a situação de uma conta existente com base no seu ID. Recebe o ID da conta como parte do caminho da URL e os novos dados da conta (apenas a situação) no corpo da solicitação. Chama o serviço updateSituacaoConta para efetuar a alteração na situação da conta.
Ex: http://localhost:8080/api/contas/1/situacao

	{
		"situacao": "PAGA"
	}

Obs: acesso apenas liberado para usuários do tipo ADMIN. 


findAll: Este método GET é usado para buscar todas as contas a pagar, com opção de filtrar por data de vencimento e descrição. Aceita parâmetros de consulta para data inicial, data final, descrição, página e quantidade de itens por página. Chama o serviço findByDataVencimentoBetweenAndDescricaoContaining para realizar a busca paginada das contas.
Ex: http://localhost:8080/api/contas/filtro

findById: Este método GET é usado para buscar uma conta a pagar específica pelo seu ID. Recebe o ID da conta como parte do caminho da URL e chama o serviço findById para recuperar a conta correspondente.
Ex: http://localhost:8080/api/contas/1


getTotalValorPagoPeriodo: Este método GET é usado para obter o valor total pago em um determinado período de tempo. Aceita parâmetros de consulta para data inicial e data final. Chama o serviço findTotalValorPagoByPeriodo para calcular e retornar o valor total pago no período especificado.
Ex: http://localhost:8080/api/contas/total-valor-pago?dataInicial=2024-01-01&dataFinal=2024-05-30


uploadContas: Este método POST é usado para importar um lote de contas a pagar a partir de um arquivo CSV. Recebe o arquivo CSV como parte do corpo da solicitação e chama o serviço uploadContas para processar e importar as contas do arquivo.

EX: http://localhost:8080/api/contas/upload 
	Multipart: flle -> selecionar arquivo conforme contém na pasta upload o arqui ContasUpload.csv
	
	
API de Autenticação/Autorização 

ogin: Este método POST é usado para autenticar um usuário e gerar um token de acesso JWT (JSON Web Token). Recebe as credenciais de login e senha do usuário no corpo da solicitação. Utiliza o AuthenticationManager para autenticar o usuário e, se as credenciais forem válidas, gera um token JWT usando o serviço tokenService. O token gerado é então retornado na resposta da solicitação.

Ex: http://localhost:8080/auth/login

{
	"login": "admin",
	"password":"123"
}

Obs: Retorna token de autenticação que é unilizado como Bearer para as as rotas PUT e PATCH. 


2. register: Este método POST é usado para registrar um novo usuário no sistema. Recebe os dados de registro do usuário, incluindo login, senha e papel (role), no corpo da solicitação. Verifica se já existe um usuário com o mesmo login na base de dados. Se não existir, encripta a senha usando o BCryptPasswordEncoder, cria um novo objeto de usuário e o salva no banco de dados através do repositório. Retorna uma resposta de sucesso se o registro for realizado com sucesso, ou uma resposta de erro com status 400 (Bad Request) se o login já estiver em uso.

Ex: http://localhost:8080/auth/register  

{
	"login": "admin",
	"password":"123", 
	"role" : "ADMIN" 
}


Testes Automatizados

1 -testSaveTest: Testa se uma nova conta é salva corretamente no banco de dados. Mocka o comportamento do método save do repositório para retornar uma entidade salva e verifica se o objeto retornado pelo adapter corresponde ao objeto salvo.

2 -findByDataVencimentoBetweenAndDescricaoContainingTest: Verifica se a busca por contas por intervalo de data de vencimento e descrição está funcionando corretamente. Mocka o comportamento do método findByDataVencimentoBetweenAndDescricaoContaining do repositório para retornar uma lista de entidades e compara se o número de contas retornadas pelo adapter é igual ao número de entidades retornadas pelo repositório.

3 -findTotalValorPagoByPeriodoTest: Testa se o cálculo do valor total pago por período está correto. Mocka o comportamento do método findTotalValorPagoByPeriodo do repositório para retornar um valor total e verifica se a resposta retornada pelo adapter é uma resposta HTTP 200 (OK) e se o valor total retornado é o esperado.

4 -updateTest: Testa se a atualização de uma conta está funcionando corretamente. Mocka o comportamento do método findById e save do repositório para retornar uma entidade existente e verifique se o objeto retornado pelo adapter corresponde ao objeto existente após a atualização.

5 -updateSituacaoContaTest: Verifica se a atualização da situação de uma conta está funcionando corretamente. Mocka o comportamento do método findById e save do repositório para retornar uma entidade existente e verifique se a situação da conta foi atualizada corretamente após a chamada do método no adapter.

6 -uploadContas: Testa se a importação de contas a partir de um arquivo CSV está funcionando corretamente. Mocka o comportamento do método saveAll do repositório para retornar uma lista de entidades salvas e verifica se a resposta retornada pelo adapter é uma resposta HTTP 200 (OK) e se o número de registros incluídos é o esperado.

7-  findTotalValorPagoByPeriodo retorna o valor total pago por um determinado período corretamente.
Cenário de Teste: Cria uma nova conta com uma data de vencimento, data de pagamento, valor e descrição específicos.
Configuração de Teste: Define o período de tempo desejado para calcular o valor total pago.
Execução de Teste: Chama o método findTotalValorPagoByPeriodo do repositório de contas para calcular o valor total pago durante o período especificado.
Verificação de Resultado: Verifica se o resultado retornado pelo método é o valor esperado para o período de tempo especificado.
Este teste garante que o cálculo do valor total pago por período está correto e retorna o valor esperado de acordo com as contas registradas no banco de dados.
