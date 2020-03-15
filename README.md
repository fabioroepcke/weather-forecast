# weather-forecast

### Sistema de Previsão do tempo

É um projeto maven utilizando [Spring Boot](https://spring.io/projects/spring-boot) com frontend [Thymeleaf](https://www.thymeleaf.org/), sendo padrão MVC e utilizando services para regras de negócio.

O projeto utiliza também [Lombok](https://projectlombok.org/) e [MongoDB](https://www.mongodb.com/).

As telas (frontend) interagem com a aplicação (backend) através dos controllers, estes por sua vez, acessam os services para tratar de validações e interagir com o respectivo repository, com o client da Api de Forecast e outros services.

Os dados obtidos pela Api de Forecast, no formato JSON é "parseado" para o objeto com os atributos desejados, de forma automática. Este projeto foram utilizados apenas alguns principais campos como exemplo.

Alguns parâmetros foram utilizados na URL da API para apresentação dos dados como o sistema de metragem (units=metric) que apresenta valores em ºC e metros, o idioma (lang=pt) que trás a descrição da previsão em português e o formato dos dados de retorno JSON (mode=json).

Após obtido os dados da Api, alguns ajustes são realizados:
- A data e hora são obtidos em UTC e também é recebido o timezone da cidade. O ajuste da data/hora é realizado adicionando o timezone à data/hora.
- É adicionada uma data formatada para a exibição na tela, utilizando o campo de data/hora já ajustado.
- É adicionado um campo com o dia da semana, baseado na data/hora ajustada, para exibição na tela.
- O ícone correspondente à previsão é utilizado para montagem da URL que será utilizada para apresentar o icone da previsão na tela. 
- A descrição da previsão é tratada para iniciar com letra maiúscula.

A aplicação conta com testes unitários para as classes services e a classe utilitária. Nesses testes são "mockados" as chamadas à Api.

#### Cadastro de cidades

A aplicação permite adicionar e excluir cidades. Os dados são salvos em um documento da base de dados MongoDB.

Ao cadastrar uma cidade, a aplicação:
- verifica se o nome da cidade foi informado. Apresenta mensagem de erro caso não tenha sido.
- normaliza o nome para o padrão de todo o nome iniciar com letra maiúscula. Isso permite que a validação dos nomes seja possível, caso contrário, seria possível cadastrar "Blumenau" e "blumenau".
- verifica se o nome informado ainda não está cadastrado. Apresenta mensagem de erro caso já tenha sido.
- verifica na Api se o nome informado retorna dados. Apresenta mensagem de erro caso não retorne dados ou a cidade não esteja disponível.
- realiza a gravação da cidade e atualiza a tela apresentando todas as cidades cadastradas em ordem alfabética.


#### Ver a previsão do tempo de uma cidade

Ao clicar no botão para ver a previsão do tempo para uma determinada cidade, é apresentada uma lista com os dados do serviço de Forecast para os próximos 5 dias (ou 40 próximos intervalos de previsão).

#### Procedimentos para importar e executar o projeto

##### Importar no Eclipse

Após clonar o repositório, basta importar um projeto maven existente.
Execute o comando "Maven install".

**Obs.: É necessário estar com o JDK 1.8 configurado no Eclipse.**

##### Criar o documento da base de dados MongoDB

- DATABASE: **weatherforecast**
- COLLECTION: **cidades**

Com o MongoDB rodando, estando na pasta bin do instalação do MongoDB, executar os seguintes comandos:
Conectar ao servidor: ./mongo
Comando para criar a base: use weatherforecast
Comando para criar a coleção: db.cidades

**Obs.: Se for necessário alterar as configurações de acesso à base, ajustar o arquivo "/weatherforecast/src/main/resources/application.properties"**

##### Executar no Eclipse

Acesse o "Run configuration", adicione uma "New Configuration" na opção "Java Application" 
Localize a classe principal do projeto "br.com.fabioroepcke.weatherforecast.WeatherforecastApplication".

Para acessar, na URL do navegador utilize o endereço: **http://localhost:8080/**

##### Executar fora do Eclipse

Basta copiar o arquivo "weatherforecast-0.0.1-SNAPSHOT.jar" que está na pasta "target" para o local desejado e executar o seguinte comando: **java -jar weatherforecast-0.0.1-SNAPSHOT.jar**

Para acessar, na URL do navegador utilize o endereço: **http://localhost:8080/**
