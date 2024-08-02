**Seja bem-vindo candidato!**

Como um desenvolvedor Back-End na Stoom uma das maiores responsabilidades que você vai ter é desenvolver funcionalidades
e corrigir bugs em sistemas de e-commerce de larga escala que utilizam Spring Boot. Com base nisso, precisamos de sua
ajuda para construir a nossa loja Stoom, que deve conter as seguintes funcionalidades:

1. Deve ser desenvolvida uma API de CRUD de produtos
2. Os produtos devem ser enriquecidos com as informações que você julgar relevante para o funcionamento em uma loja,
   algumas são obrigatórias:
    - Categorias
    - Marca
    - Preços
3. Deve existir um endpoint na API para a busca de produtos que será utilizada na loja
4. Deve existir um endpoint que lista os produtos de uma determinada Marca
5. Deve existir um endpoint que lista os produtos de uma determinada Categoria
6. Produtos podem ser inativados para não aparecerem na busca ou nas listagens sem a necessidade de serem deletados para
   poderem ser reativados posteriormente
7. Marcas e categorias também podem ser inativados para não aparecerem na loja

**Informações relevantes**:

- Atente-se à todos os pré-requisitos estabelecidos, porém não limite-se a eles, ideias novas ou melhorias são sempre
  bem-vindas :smiley:
- Você tem total liberdade para fazer qualquer tipo de alteração em qualquer ponto do código (contanto que não alterem a
  maneira de execução da aplicação)
- Se possível, adicione uma collection do Postman no repositório para conseguirmos testar o código da mesma forma que
  você
- Boas práticas, legibilidade, testes e performance são alguns dos pontos que serão considerados durante a avaliação

**Boa sorte!**

# Requisitos para executar o projeto

- [Git](https://git-scm.com/)
- [Docker](https://www.docker.com/)
- [JDK 17+](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)

# Como executar o projeto

- Clone o projeto.

```bash
  git clone https://github.com/dayvidsonveiga/teste-stoom.git
```

- Abra um terminal na raiz do projeto e execute o comando abaixo para iniciar o banco de dados FireBird no docker.

```bash
  cd docker && docker-compose up -d
```

- Abra a IDE de sua preferência e importe o projeto clonado e aguarde o download de todas dependências do projeto

- Execute o arquivo com a classe main DeliveryMsApplication.java

- Acesse a interface dos recursos do backend através do swagger usando o endereço local http://localhost:8080

# Rotas

## Brand Controller

| Método | Path                         | Descrição                       |
|--------|------------------------------|---------------------------------|
| POST   | /api/v1/brands               | Criar marca                     |
| GET    | /api/v1/brands/{id}          | Listar marca por id             |
| GET    | /api/v1/brands               | Listar todas as marcas          |
| PUT    | /api/v1/brands/{id}          | Atualizar status marca por id   |
| GET    | /api/v1/brands/{id}/products | Listar todos produtos por marca |

## Category Controller

| Método | Path                             | Descrição                           |
|--------|----------------------------------|-------------------------------------|
| POST   | /api/v1/categories               | Criar categoria                     |
| GET    | /api/v1/categories/{id}          | Listar categoria por id             |
| GET    | /api/v1/categories               | Listar todas as categoria           |
| PUT    | /api/v1/categories/{id}          | Atualizar status categoria por id   |
| GET    | /api/v1/categories/{id}/products | Listar todos produtos por categoria |

## Product Controller

| Método | Path                  | Descrição                       |
|--------|-----------------------|---------------------------------|
| POST   | /api/v1/products      | Criar produto                   |
| GET    | /api/v1/products/{id} | Listar produto por id           |
| GET    | /api/v1/products      | Listar todos os produtos        |
| PUT    | /api/v1/products/{id} | Atualizar status produto por id |

## Planos para as próximas versões do serviço

- Testes de integração
