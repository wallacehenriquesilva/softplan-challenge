# Softplan Challenge
Foi desenvolvido um sistema de gerenciamento de pessoas utilizando a linguagem de programação Java e React, onde é possível
realizar as operações padrões de inserção, remoção, atualização e busca de pessoas. 
Todo o sistema foi desenvolvido com a arquitetura de micro serviços e boas práticas de programação.


[Click aqui para acessar o sistema](http://165.227.3.54:3000)


# Arquitetura
Todo o sistema foi desenvolvido seguindo a arquitetura de micro serviços, design patters e as APIS, seguindo as bases do modelo 
de maturidade descrito por Richardson e também muito citado por Martin Fowler.

## Micro Serviços
Todos os micro serviços foram desenvolvidos utilizando JAVA com Spring, utilizando o SpringBoot. 
Foi utilizado o Eureka como service registre para fazer o gerenciamento de registro dos serviços, o zuul
como proxy no serviço de gateway, hystrix como Circuit Breaker para controle dos fallbacks na comunicação
entre os micro serviços caso ocorra algum tipo de falha e também, utilizado o Ribbon para fazer o load balance entre 
as intâncias de micro serviços, utilizando a estratégia WeightedResponseTimeRule, que decide qual o melhor serviço a se 
chamar de acordo com seu tempo de resposta nas ultimas requisições. 

## Service Registrer
Serviço de registro, é o servidor eureka onde as aplicações ficarção hospedadas e registradas para que não seja necessário,
para realizar chamadas entre os serviços, a utilização do IP, mas sim, apenas o nome em que estão registrados no eureka.

## Gateway
Micro serviço responsável por ser o gateway da comunicação entre o front end e os micro serviços. 

Todas as requisições necessitam de autenticação, um token gerado por esse serviço, que deve estar presente no cabeçalho 
header de cada requisição, o gateway faz o proxy dos serviços utilizando o zuul, e filtra todas as requisições, caso o token
não seja válido, responde ao cliente o erro, caso o token seja válido, realiza a chamada do micro serviço solicitado e segue o fluxo normalmente.



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