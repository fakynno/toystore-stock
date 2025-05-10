

## Documentação sobre mongo
#### https://spring.io/projects/spring-data-mongodb

## Instalação do mongo para usar(docker)
### Baixar e rodar ou apenas rodar
Instalar: docker run --name meu_mongo -d -p 27017:27017 mongo  
Iniciar: docker start meu_mongo  
Stopar: docker stop meu_mongo  
Valiar: docker ps  

### Uso do shell
Caso esteja utilizando com docker, ele já vem com o shell na imagem. Do contrário, necessário baixar o shell separado  
Utilizar o shell: docker exec -it meu_mongo mongosh
Acessar console do container: docker exec -it meu_mongo bash


## Executando o projeto com Docker
### Baixar e rodar ou apenas rodar
A imagem docker será criada conforme instruções no [DockerFile](Dockerfile)

### Executando projeto local
Criar Imagem docker do projeto: docker build -t toy-store-stock .  
Executar a Imagem docker localmente: docker run -p 8080:8080 reservei-app  
Verificar container criado: http://localhost:8080/stock/swagger-ui/index.html#/

### Executando projeto via imagem no dockerhub
#### Criando Imagem no docker hub
Tagueando imagem: docker tag reservei-app:latest majorv22/toy-store-stock:1.0  
Fazendo deploy no dockerhub: docker push majorv22/toy-store-stock:1.0
