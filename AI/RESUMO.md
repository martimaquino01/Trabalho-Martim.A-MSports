# Resumo do Projeto MSports - Loja Virtual

## 1. Visão Geral

Este projeto implementa uma **loja virtual desportiva** chamada **MSports**, desenvolvida em Java puro (sem frameworks), utilizando interação por consola (terminal). O programa simula um sistema de e-commerce onde clientes podem comprar e devolver produtos, e o dono da loja pode gerir o catálogo de produtos.

---

## 2. Lógica Geral do Projeto

A ideia central foi separar o projeto em **camadas de responsabilidade**:

1. **Dados/Modelo** → Classes que representam as entidades do sistema (`Product`, `Inventory`, `User`, `Client`, `Owner`)
2. **Lógica de Negócio** → A classe `Store` que contém toda a lógica de compra, devolução, validação e apresentação
3. **Interface** → A classe `StoreInterface` que lida com a leitura de input do utilizador e delega as ações à loja
4. **Ponto de Entrada** → A classe `Main` que inicializa tudo

Esta separação permite que cada classe tenha uma única responsabilidade, tornando o código mais organizado e fácil de manter.

---

## 3. Estrutura das Classes (8 ficheiros)

### 3.1 `Product.java` — Representa um produto

**Atributos:**
- `name` (String) — nome do produto
- `price` (double) — preço unitário
- `stock` (int) — quantidade em stock

**Métodos:**
- `Product(name, price, stock)` — construtor
- `getName()`, `getPrice()`, `getStock()` — getters
- `setName(name)`, `setPrice(price)`, `setStock(stock)` — setters
- `addStock(amount)` — aumenta o stock
- `removeStock(amount)` — diminui o stock

---

### 3.2 `Inventory.java` — Gere uma coleção de produtos

Utiliza um **array estático** de `Product` com capacidade máxima de 100 produtos, e um contador (`count`) para saber quantos estão ocupados.

**Atributos:**
- `products` (Product[]) — array de produtos
- `count` (int) — número de produtos atualmente no inventário
- `MAX_PRODUCTS` (100) — limite máximo

**Métodos:**
- `Inventory()` — construtor, cria array vazio
- `addProduct(product)` — adiciona um produto ao final do array; retorna `false` se cheio
- `removeProduct(index)` — remove produto numa posição; desloca os seguintes para a esquerda para preencher o espaço
- `getProduct(index)` — retorna o produto numa posição (ou `null` se inválido)
- `getCount()` — retorna quantos produtos existem
- `findProductByName(name)` — pesquisa linear pelo nome; retorna o índice ou -1 se não encontrado

**Lógica de remoção:** Quando um produto é removido, todos os produtos à direita são deslocados uma posição para a esquerda, garantindo que não ficam "buracos" no array.

---

### 3.3 `User.java` — Classe base para utilizadores

Classe pai que define os atributos comuns a todos os utilizadores.

**Atributos:**
- `name` (String) — nome do utilizador
- `balance` (double) — saldo monetário

**Métodos:**
- `User(name, balance)` — construtor
- `getName()`, `getBalance()` — getters
- `setName(name)`, `setBalance(balance)` — setters
- `addBalance(amount)` — adiciona dinheiro ao saldo
- `removeBalance(amount)` — remove dinheiro do saldo
- `getType()` — retorna `"User"` (será sobrescrito nas subclasses)

---

### 3.4 `Client.java` — Representa um cliente (herda de User)

**Herança:** `Client extends User`

**Atributos adicionais:**
- `inventory` (Inventory) — inventário pessoal do cliente (produtos comprados)

**Construtor:**
- `Client(name)` — chama `super(name, 100.0)`, ou seja, cada cliente começa com **100€ de saldo** e um inventário vazio

**Métodos:**
- `getInventory()` — retorna o inventário do cliente
- `getType()` — retorna `"Client"` (polimorfismo)

---

### 3.5 `Owner.java` — Representa o dono da loja (herda de User)

**Herança:** `Owner extends User`

**Construtor:**
- `Owner(name, balance)` — chama `super(name, balance)`, o saldo é definido na criação

**Métodos:**
- `getType()` — retorna `"Owner"` (polimorfismo)

**Diferença do Client:** O Owner não tem inventário pessoal, apenas gere a loja. O seu saldo aumenta quando clientes compram e diminui quando devolvem.

---

### 3.6 `Store.java` — Lógica central da loja

Esta é a classe mais importante. Contém toda a lógica de negócio e a apresentação da informação.

**Atributos:**
- `name` (String) — nome da loja
- `owner` (Owner) — dono da loja
- `inventory` (Inventory) — catálogo de produtos da loja
- `clients` (Client[]) — array de clientes registados (máx. 100)
- `clientCount` (int) — número de clientes
- `currentUser` (User) — utilizador atualmente ativo (pode ser Client ou Owner)

**Métodos de acesso:**
- `getName()`, `getOwner()`, `getInventory()`, `getCurrentUser()`, `getClientCount()`, `getClient(index)`
- `setName(name)`, `setCurrentUser(user)`
- `addClient(client)`, `addProduct(product)`, `removeProduct(index)`

**Métodos de lógica de negócio:**

- **`buyProduct(client, productIndex, quantity)`** — Lógica de compra:
  1. Valida se o índice do produto é válido
  2. Verifica se há stock suficiente
  3. Calcula o custo total (preço × quantidade)
  4. Verifica se o cliente tem saldo suficiente
  5. Remove stock do produto da loja
  6. Remove saldo do cliente e adiciona ao dono
  7. Adiciona o produto ao inventário do cliente (se já existir, soma a quantidade; senão, cria um novo)
  8. Retorna mensagem de sucesso ou erro

- **`returnProduct(client, invIndex)`** — Lógica de devolução:
  1. Valida o índice no inventário do cliente
  2. Verifica se o produto ainda existe na loja
  3. Calcula o reembolso (preço × quantidade total)
  4. Verifica se o dono tem saldo para reembolsar
  5. Devolve o stock à loja
  6. Adiciona saldo ao cliente e remove do dono
  7. Remove o produto do inventário do cliente

**Métodos de apresentação (print):**
- `printStoreView()` — Mostra o cabeçalho da loja, utilizador atual, lista de produtos e ações disponíveis
- `printClientActions()` — Lista as ações do cliente (store, buy, inv, return, client, owner, register)
- `printOwnerActions()` — Lista as ações do dono (store, add, remove, edit, stock, rename, client, register)
- `printClientInventory(client)` — Mostra os produtos comprados por um cliente
- `printClients()` — Lista todos os clientes registados com saldo

**Validação:**
- `isValidAction(action)` — Verifica se a ação digitada é válida consoante o tipo de utilizador atual (Client ou Owner têm ações diferentes)

---

### 3.7 `StoreInterface.java` — Interface de interação com o utilizador

Classe responsável por ler o input do utilizador e chamar as funções correspondentes.

**Atributos:**
- `store` (Store) — referência à loja
- `scanner` (Scanner) — para ler input do teclado

**Método principal:**
- `start()` — Mostra a loja e entra num **ciclo infinito** (`while(true)`) que:
  1. Pede ao utilizador uma ação
  2. Valida se a ação é válida
  3. Executa a ação correspondente via `switch/case`

**Métodos de ação (privados):**

| Método | Descrição |
|--------|-----------|
| `actionStore()` | Reimprime a vista da loja |
| `actionBuy()` | Pede índice do produto e quantidade, chama `store.buyProduct()` |
| `actionInventory()` | Mostra o inventário do cliente atual |
| `actionReturn()` | Mostra inventário, pede índice, chama `store.returnProduct()` |
| `actionClient()` | Mostra lista de clientes, pede índice, muda o utilizador atual |
| `actionOwner()` | Muda o utilizador atual para o dono da loja |
| `actionRegister()` | Pede nome, cria novo `Client` e adiciona à loja |
| `actionAdd()` | Pede nome e preço, cria `Product` com stock 20 e adiciona à loja |
| `actionRemove()` | Pede índice, remove o produto do catálogo |
| `actionEdit()` | Pede índice, novo nome (opcional) e novo preço (opcional), altera o produto |
| `actionStock()` | Pede índice e quantidade, aumenta o stock do produto |
| `actionRename()` | Pede novo nome, altera o nome da loja |

**Métodos auxiliares:**
- `readInt()` — Lê uma linha e tenta converter para `int`. Se falhar, retorna -1
- `readDouble()` — Lê uma linha, substitui vírgula por ponto (formato PT), e tenta converter para `double`. Se falhar, retorna -1

---

### 3.8 `Main.java` — Ponto de entrada do programa

Inicializa todo o sistema:
1. Cria o **Owner** "Martim" com saldo de 500€
2. Cria a **Store** "MSports"
3. Adiciona **7 produtos** desportivos ao catálogo (todos com stock de 20 unidades):
   - Bola de Futebol (8.90€)
   - Chuteiras de Futebol (74.00€)
   - Fato de Banho Mulher (15.90€)
   - Calções de Banho Homem (8.90€)
   - Sapatilhas de Corrida (39.90€)
   - Casaco de Ski (44.90€)
   - Luvas de Ski (9.90€)
4. Cria **3 clientes**: João, Maria e Pedro (cada um com 100€)
5. Define o utilizador inicial como o cliente João
6. Cria a `StoreInterface` e chama `start()` para iniciar o programa

---

## 4. Conceitos de Programação Orientada a Objetos Utilizados

### 4.1 Encapsulamento
Todos os atributos são `private` e acedidos através de **getters e setters**. Isto protege os dados internos de cada classe.

### 4.2 Herança
- `Client extends User` — herda nome e saldo, adiciona inventário
- `Owner extends User` — herda nome e saldo, sem atributos extras

### 4.3 Polimorfismo
O método `getType()` é definido em `User` e **sobrescrito** em `Client` (retorna "Client") e `Owner` (retorna "Owner"). A variável `currentUser` é do tipo `User` mas pode conter tanto um `Client` como um `Owner`, e o comportamento muda conforme o tipo real do objeto.

### 4.4 Uso de `instanceof`
Na classe `Store`, o operador `instanceof` é usado para verificar se o `currentUser` é `Client` ou `Owner`, mostrando ações diferentes conforme o caso.

---

## 5. Fluxo de Funcionamento do Programa

```
Main → cria Owner, Store, Products, Clients
     → define currentUser = client1
     → cria StoreInterface(store)
     → chama start()

StoreInterface.start()
     → mostra a loja (printStoreView)
     → loop infinito:
          → lê ação do utilizador
          → valida a ação (isValidAction)
          → executa a ação (switch/case)
               → delega lógica ao Store
               → mostra resultado
```

---

## 6. Estruturas de Dados Utilizadas

- **Arrays estáticos** (`Product[]`, `Client[]`) com tamanho fixo de 100 — não se usa `ArrayList` nem outras coleções Java
- **Contador manual** (`count`, `clientCount`) para controlar quantos elementos estão ocupados no array
- **Remoção com deslocamento** — ao remover um elemento do array, os seguintes são empurrados para a esquerda

---

## 7. Tratamento de Erros

- Validação de índices antes de aceder a arrays
- Retorno de `null` ou `-1` quando dados inválidos
- `try/catch` em `readInt()` e `readDouble()` para input mal formatado
- Substituição de vírgula por ponto em `readDouble()` para compatibilidade com formato numérico português
- Mensagens de erro claras para o utilizador em todas as situações (saldo insuficiente, stock insuficiente, índice inválido, etc.)
