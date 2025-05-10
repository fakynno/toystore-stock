

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

