# Softplan Challenge
Foi desenvolvido um sistema de gerenciamento de pessoas utilizando a linguagem de programação Java e React, onde é possível
realizar as operações padrões de inserção, remoção, atualização e busca de pessoas. 
Todo o sistema foi desenvolvido com a arquitetura de micro serviços e boas práticas de programação.


[Click aqui para acessar o sistema](http://165.227.3.54:3000)


# Arquitetura
Todo o sistema foi desenvolvido seguindo a arquitetura de micro serviços, focando na escalabilidade e desempenho, design patters e as APIS, seguindo as bases do modelo 
de maturidade descrito por Richardson e também muito citado por Martin Fowler, como uso correto dos verbos Http, tráfego de arquivos intercambiáveis
e controle de hipermídia.

## Micro Serviços
Todos os micro serviços foram desenvolvidos utilizando JAVA com Spring, utilizando o SpringBoot. 
Foi utilizado o Eureka como service discovery para fazer o gerenciamento de registro dos serviços, o zuul
como proxy no serviço de gateway, hystrix como Circuit Breaker para controle dos fallbacks na comunicação
entre os micro serviços caso ocorra algum tipo de falha e também, utilizado o Ribbon para fazer o load balance entre 
as intâncias de micro serviços, utilizando a estratégia WeightedResponseTimeRule, que decide qual o melhor serviço a se 
chamar de acordo com seu tempo de resposta nas ultimas requisições. 

## Service Discovery
Service Discovery, é o servidor eureka onde as aplicações ficarção hospedadas e registradas para que não seja necessário,
para realizar chamadas entre os serviços, a utilização do IP, mas sim, apenas o nome em que estão registrados no eureka.

## Gateway
Micro serviço responsável por ser o gateway da comunicação entre o front end e os micro serviços. 

Todas as requisições necessitam de autenticação, um token gerado por esse serviço, que deve estar presente no cabeçalho 
header de cada requisição, o gateway faz o proxy dos serviços utilizando o zuul, e filtra todas as requisições, caso o token
não seja válido, responde ao cliente o erro, caso o token seja válido, realiza a chamada do micro serviço solicitado e segue o fluxo normalmente.

O Gateway é o responsável por todo o serviço de gerenciamento de usuários, tanto o cadastro quanto a geração de token e gerenciamento
de autenticação com OAuth2.

**OBS Atualmente esta habilitado apenas o login OAuth2 do github.**


## Data Service
Micro serviço de gerenciamento de dados, onde são realizadas as operações de CRUD de pessoas. Esse micro serviço também faz
o gerenciamento do controle de hipermídia, tirando a responsabilidade do front de saber onde estão as operações do back, pois
em todas as requisições é retornada a lista de links com as devidas operações para as pessoas. Como o exemplo abaixo:
    
  ```javascript
{"name":"Teste","email":"teste@teste.com","bornDate":"1990-07-02","naturality":"Amsterdam","nacionality":"Holanda","cpf":"125.688.454-56","createdAt":"2019-11-26T00:03:46.739","updatedAt":"2019-11-26T00:03:46.739","sex":1,"_links":{"self":{"href":"http://localhost/api/v1/service/data/persons/4"},"update":{"href":"http://localhost/api/v1/service/data/persons/4"},"delete":{"href":"http://localhost/api/v1/service/data/persons/4"}}}
```
Todas as regras de validação de pessoa são realizadas por esse micro serviço, que contém um exception handler (ControllerAdvice) para controlar
as exeções ocorridas durante a execução e retornar ao cliente a resposta correta.

# Front

O front end foi desenvolvido utilizando o react. A escolha do react foi pelo fato de ser um dos maiores bibliotecas Javascript de front end.
Bem como sua simplicidade na criação de componentes. 

Os estilos foram inspirados nas dashboards de Flatlogic Dashboards.

# Base de dados

Foi escolhido o MySql como base de dados utilizada para o projeto.

# Segurança

A segurança do projeto foi feita utilizando o Spring Security, possibilitando o login padrão e também 
por OAuth2 (Nesse caso, utilizando o github, porém o projeto já esta pronto para ativar as outras, necessitando apenas de colocar
os devidos IDs). Foi utilizando o JWT como token da aplicação. Todas as configurações de segurança
podem ser encontradas no pacote security do micro serviço gateway.

Os usuários tem permissão apenas de gerencias as pessoas cadastradas por ele próprio, apenas o usuário com credenciais
de administrador que pode gerenciar todos os registros.

# Docker
Todos os micro serviços geram automaticamente ao serem compilados imagens docker, e estão hospedados no docker hub, bem 
como o front end e podem ser baixados pelos comandos abaixo.

### Front
[Link Docker Hub](https://hub.docker.com/r/wallacehenriquee/softplan-challenge-front)

    docker run --network=host 
    -itd wallacehenriquee/softplan-challenge-front:0.0.1-SNAPSHOT

### Service Discovery
[Link Docker Hub](https://hub.docker.com/repository/docker/wallacehenriquee/service-discovery)

    docker run --network=host 
    -itd wallacehenriquee/service-discovery:0.0.1-SNAPSHOT

### Gateway
[Link Docker Hub](https://hub.docker.com/repository/docker/wallacehenriquee/softplan-challenge-gateway)

    docker run --network=host 
    -itd wallacehenriquee/softplan-challenge-gateway:0.0.1-SNAPSHOT

### Data Service
[Link Docker Hub](https://hub.docker.com/repository/docker/wallacehenriquee/softplan-challenge-data-service)

    docker run --network=host 
    -itd wallacehenriquee/softplan-challenge-data-service:0.0.1-SNAPSHOT

# Execução

Para executar os containers, basta fazer o pull dos mesmos do docker hub, ou seguir os comandos acima
para que sejam baixados e executados automaticamente. Os micro serviços serão executados de maneira automática, e caso necessário subir 
mais instâncias do data service por exemplo, é só escalar o container que ele automaticamente entrará no projeto
e será chamado de acordo com as regras de load balance.

# Documentação

A documentação do código foi toda feita utilizando o Javadoc, todos os métodos, funções, classes, etc. Possuem comentários.
A documentação das APIs foi toda feita utilizando o Swagger, que pode ser acessado pelo [Link](http://165.227.3.54:2207/swagger-ui.html).

Mesmo se tratando de micro serviços, o swagger da aplicação esta centralizado, podemos navegar entre os micro serviços 
pela aba **Select a spec**, no canto superior direito da página de documentação swagger.


# Telas do sistema
## Tela de login
![Tela de login](/imagens/login.png)

Na imagem acima, podemos ver a tela de login onde o usuário pode escolher em 
se cadastrar, logar com uma conta já existente, ou então, logar utilizando uma rede social
utilizando o padrão OAuth2.

## Tela de cadastro de usuário
![Tela de cadastro de novo usuário](/imagens/cadastro.png)

Caso o usuário opte por se cadastrar, ele precisará preencher o formulário
com os dados necessários, sendo eles, username, email e senha.


## Dashboard
![Dashboard](/imagens/dashboard.png)

Logo após realizar o login, o usuário é encaminhado a dashboard, onde são listadas algumas informações
bem como o top 5 de usuários cadastrados. E a lista de usuários cadastrados dizendo se estão online ou não
no chat **O chat estará disponível na próxima versão**.

## Lista de pessoas
![Lista de pessoas](/imagens/lista_pessoas.png)

Tela de listagem de pessoas, onde é possível fazer operações como filtros
edição e excluir as pessoas já existentes.


## Cadastro de pessoa
![Cadastro de pessoa](/imagens/cadastro_nova_pessoa.png)

A imagem acima apresenta o formulário de cadastro de pessoas.

# Dependências do projeto


##Melhorias para uma nova versão