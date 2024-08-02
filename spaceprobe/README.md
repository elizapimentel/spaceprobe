# Space probe -JAVA API

## **Funcionalidades**

- [x] Exibir os planetas.
- [x] Exibir as sondas existentes.
- [x] Cadastrar planetas.
- [x] Cadastrar novas sondas.
- [x] Buscar planetas por id.
- [x] Buscar sondas por id.
- [x] Mover sondas através de comandos.
- [x] Atualizar o planeta que a sonda está alocada.
- [x] Ligar ou desligar a sonda.
- [x] Adicionar um rover a um planeta.
- [x] Checar se o planeta está com a coordenada ocupada.


## **Tecnologias**

- Java 17
- MySQL
- Maven
- Docker
- ModelMapper
- Lombok
- jUnit5 - Teste
- Mockito
- Jacoco
- Spring-boot 3.3.2
- Actuator
- HATEOAS
- Swagger
- Spring Data JPA


## **Como instalar**

### Backend

- Baixe ou clone o repositório do backend usando *git clone* https://github.com/elizapimentel/spaceprobe.git


## Como executar

Execute no terminal `docker-compose up --build` para subir as imagens e container no Docker.


## **Regras de negócio**

- A sonda não pode se mover para uma coordenada que já está ocupada por outra sonda.
- A sonda não pode se mover para uma coordenada que está fora do planeta.
- Um planeta pode ter até 5 sondas.
- Os comandos permitidos para a sonda são: L, R, M.
- A sonda possui números de comandos ilimitados.
- As sondas podem ser ligadas ou desligadas, mas não excluídas.
- As sondas podem ser transferidas para outros planetas.
- Um planeta não pode ser deletado.


##  ROTAS


O projeto foi estruturado seguindo modelo da estrura de Arquitetura de Software Rest/Restful, utilizando os protocolos HTTP - POST, GET, PUT, DELETE - CRUD.

<br>

###  Método GET

<div align = "center">

|  Método  |                      Rota                       |                      Descrição                      |
| :------: |:-----------------------------------------------:|:---------------------------------------------------:|
|  `GET`   |      http://localhost:8080/api/planets/all      |         Lista que retorna todos os planetas         |
|  `GET`   |      http://localhost:8080/api/rovers/all       |          Lista que retorna todos as sondas          |
|  `GET`   |      http://localhost:8080/api/planets/ID       |                Busca planeta por ID                 |
|  `GET`   |       http://localhost:8080/api/rovers/ID       |                 Busca sonda por ID                  |
|  `GET`   | http://localhost:8080/api/planets/ID/isOccupied | Retorna se o comando informando está ocupado ou não |

<br>
</div>

### Método POST

<div align = "center">

|  Método  |                    Rota                    |         Descrição         |
| :------: |:------------------------------------------:|:-------------------------:|
|  `POST`  |   http://localhost:8080/api/planets/add    |  Cadastra novo registro   |
|  `POST`  |    http://localhost:8080/api/rovers/add    |  Cadastra novo registro   |
|  `POST`  |   http://localhost:8080/api/rovers/move    |  Move sonda por comandos  |
|  `POST`  | http://localhost:8080/api/planets/planetId | Adidiona sonda ao planeta |

<br>
</div>

###  Método PUT

<div align = "center">

|  Método  |                       Rota                        |                 Descrição                  |
| :------: |:-------------------------------------------------:|:------------------------------------------:|
|   `PUT`  |     http://localhost:8080/api/rovers/ID/plug      |   Atualiza se a sonda está ligada ou não   |
|   `PUT`  | http://localhost:8080/api/roverId/planet/planetId | Atualiza movendo a sonda pra outro planeta |

<br>
</div>


## Dúvidas
<br>

<div align = "center">
<a href="https://www.linkedin.com/in/eliza-pimentel/">
<img alt="linkedin" src="https://img.shields.io/badge/LinkedIn-0077B5?style=for-the-badge&logo=linkedin&logoColor=white"/>
</a> 
</div > 
