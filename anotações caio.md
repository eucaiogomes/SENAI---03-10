# 🎓 APRENDA OS CONCEITOS BÁSICOS

> Este documento explica conceitos que você vai ver no código, de forma bem simples!

---

## 1️⃣ O QUE É PROGRAMAÇÃO?

Imagina que você tá dando **instruções para um robô muito burro**.

Cada linha de código é uma instrução:

```java
desperta();           // "Ei robô, acorda!"
toma_cafe();          // "Bebe café!"
vai_trabalhar();      // "Vai pro trabalho!"
```

O robô (o computador) executa **na ordem** e **literalmente** como você escreve.

---

## 2️⃣ O QUE É VARIÁVEL?

Uma variável é **uma caixa que guarda um valor**.

```java
String nome = "João";        // Caixa chamada "nome", com o valor "João" dentro

nome = "Maria";              // Tirou "João", colocou "Maria"
```

**Tipos comuns**:

- `String` = texto ("João", "Maria", "oi")
- `Long` = número grande (1, 100, 999999)
- `LocalDate` = data (15/11/2024)
- `LocalTime` = hora (14:30)
- `boolean` = true/false (sim/não)

---

## 3️⃣ O QUE É CLASSE?

Uma classe é como um **"projeto de casa"**.

A classe diz: "Uma casa tem: porta, janela, telhado..."

```java
// Projeto (classe)
public class Casa {
    private String cor;           // Tem uma cor
    private int numJanelas;       // Tem número de janelas

    // Método pra pintar
    public void pintar(String novaCor) {
        this.cor = novaCor;
    }
}

// Construção (objeto)
Casa minhaHouse = new Casa();   // Construiu uma casa
minhaHouse.pintar("azul");      // Pintou de azul
```

---

## 4️⃣ O QUE É OBJECT / OBJETO?

Um objeto é uma **instância de uma classe**. É a casa **construída** baseada no projeto.

```java
// Projeto
public class Usuario {
    String nome;
}

// Objetos construídos baseados no projeto
Usuario joao = new Usuario();     // Primeira casa
joao.nome = "João";

Usuario maria = new Usuario();    // Segunda casa (diferente!)
maria.nome = "Maria";
```

---

## 5️⃣ O QUE É MÉTODO / FUNÇÃO?

Um método é como uma **receita de bolo**.

```java
// Receita
public void fazerBolo() {
    mistura_ingredientes();
    coloca_no_forno();
    espera_40_minutos();
    tira_do_forno();
}

// Usando a receita
fazerBolo();  // Executa TODAS as instruções da receita
```

**Método com parâmetro**:

```java
public void fazerBolo(String sabor) {  // sabor é um PARÂMETRO
    if (sabor.equals("chocolate")) {
        mistura_chocolate();
    } else {
        mistura_baunilha();
    }
}

fazerBolo("chocolate");  // Passa "chocolate" como argumento
```

---

## 6️⃣ O QUE É GETTER E SETTER?

São **métodos pra pegar e colocar** valores nas variáveis.

```java
public class Usuario {
    private String nome;        // PRIVATE = ninguém mexe direto!

    // Getter = pega o valor
    public String getNome() {
        return nome;
    }

    // Setter = coloca um novo valor
    public void setNome(String novoNome) {
        this.nome = novoNome;
    }
}

// Usando
Usuario joao = new Usuario();
joao.setNome("João Silva");     // Coloca "João Silva"
String nomedele = joao.getNome(); // Pega e guarda em nomedele
```

**Por que usar getter/setter?**

- Você consegue fazer validações (tipo: nome não pode ser vazio)
- Fica mais seguro

---

## 7️⃣ O QUE É ARRAY / LISTA?

Uma lista é como uma **fila de coisas**.

```java
// Array (tamanho fixo)
String[] nomes = new String[3];
nomes[0] = "João";
nomes[1] = "Maria";
nomes[2] = "Pedro";
nomes[0];  // Tira "João"

// List (tamanho variável, é melhor!)
List<String> nomes = new ArrayList<>();
nomes.add("João");
nomes.add("Maria");
nomes.add("Pedro");
nomes.get(0);  // Tira "João"
nomes.remove(1);  // Apaga "Maria"
```

---

## 8️⃣ O QUE É IF / ELSE?

É uma **decisão**: "Se isso acontecer, faça isso. Senão, faça aquilo."

```java
if (idade >= 18) {
    entra_na_festa();
} else {
    fica_em_casa();
}
```

**Operadores de comparação**:

- `==` = igual?
- `!=` = diferente?
- `>` = maior que?
- `<` = menor que?
- `>=` = maior ou igual?
- `&&` = E (os dois?)
- `||` = OU (um deles?)

```java
if (idade >= 18 && temDocumento) {
    entra();  // Executa só se AMBOS são true
}

if (nome.equals("João") || nome.equals("Maria")) {
    saudacao();  // Executa se for João OU Maria
}
```

---

## 9️⃣ O QUE É LOOP / WHILE / FOR?

É **repetir** uma ação várias vezes.

```java
// FOR: repete um número conhecido de vezes
for (int i = 0; i < 5; i++) {
    println("Olá " + i);  // Imprime "Olá 0", "Olá 1", ..., "Olá 4"
}

// WHILE: repete enquanto uma condição for verdadeira
int contador = 0;
while (contador < 5) {
    println("Olá " + contador);
    contador = contador + 1;
}

// FOR com lista: repete pra cada item da lista
List<String> nomes = ["João", "Maria", "Pedro"];
for (String nome : nomes) {
    println(nome);  // Imprime cada nome
}
```

---

## 🔟 O QUE É EXCEPTION / ERRO?

Uma exception é quando algo **dá errado** no código.

```java
// Dá erro se dividir por zero!
int resultado = 10 / 0;  // ❌ Exception!

// TRY = tenta fazer isso
// CATCH = se der erro, pega aqui
try {
    int resultado = 10 / 0;
} catch (Exception erro) {
    println("Deu erro: " + erro.getMessage());
}
```

**Lançar exceção**:

```java
public void validarIdade(int idade) {
    if (idade < 0) {
        throw new RuntimeException("Idade não pode ser negativa!");
    }
}
```

---

## 1️⃣1️⃣ O QUE É INTERFACE?

Uma interface é um **contrato** que diz: "Quem implementar isso, PRECISA ter esses métodos!"

```java
// O contrato
public interface IVeiculo {
    void acelerar();
    void frear();
}

// Implementando (assinando o contrato)
public class Carro implements IVeiculo {
    // OBRIGATÓRIO ter esses dois métodos!

    @Override
    public void acelerar() {
        System.out.println("Vroooom!");
    }

    @Override
    public void frear() {
        System.out.println("Piiiii!");
    }
}
```

---

## 1️⃣2️⃣ O QUE SÃO ANNOTATIONS (@)?

Annotations são **marcadores** que dizem ao Spring o que fazer.

```java
@Entity              // "Ó Spring, essa classe é uma tabela do banco!"
public class Usuario {

    @Id              // "Esse campo é a chave primária!"
    private Long id;

    @Column(nullable = false)  // "Esse campo é obrigatório!"
    private String nome;
}

@Controller          // "Ó Spring, essa classe é um Controller!"
public class LoginController {

    @GetMapping("/login")  // "Quando alguém digita /login, chama esse método!"
    public String viewLogin() {
        return "login";
    }
}
```

---

## 1️⃣3️⃣ O QUE É DEPENDENCY INJECTION?

É quando a Spring **traz um objeto pronto pra você usar**.

```java
@Service
public class UserService {

    @Autowired  // "Spring, me traz um UserRepository pronto!"
    private UserRepository userRepository;

    // Pronto! Agora userRepository já tá preenchido e funcionando!
}
```

---

## 1️⃣4️⃣ O QUE É THIS?

`this` significa **"este objeto aqui"**.

```java
public class Usuario {
    private String nome;

    public void setNome(String nome) {  // Aqui "nome" é o parâmetro
        this.nome = nome;               // "this.nome" é o da classe
    }
}

// Sem "this" ficaria confuso qual "nome" tá falando!
```

---

## 1️⃣5️⃣ O QUE É STATIC?

`static` significa que **não precisa de um objeto pra usar**.

```java
// Normal (precisa de objeto)
public class Usuario {
    public void saudacao() {
        println("Olá!");
    }
}
Usuario joao = new Usuario();
joao.saudacao();

// Static (não precisa de objeto)
public class Usuario {
    public static void saudacao() {
        println("Olá!");
    }
}
Usuario.saudacao();  // Direto na classe!
```

---

## 1️⃣6️⃣ O QUE É PRIVATE, PUBLIC?

São **níveis de acesso**:

```java
public class Usuario {

    public String nome;        // Qualquer pessoa consegue acessar
    private String senha;      // Só a CLASSE consegue acessar
    protected String documento; // Só a CLASSE e SUBCLASSES conseguem
}

// Acessando
Usuario joao = new Usuario();
joao.nome = "João";            // ✅ Funciona (public)
joao.senha = "123";            // ❌ Erro! (private)
```

---

## 1️⃣7️⃣ O QUE É NULL?

`null` significa **vazio / nada**.

```java
String nome = null;            // Nada, vazio

if (nome == null) {
    println("Nome tá vazio!");
}

if (nome != null) {
    println(nome);             // Só executa se nome não for vazio
}
```

---

## 1️⃣8️⃣ O QUE É OPTIONAL?

`Optional` é um "container" que pode ter valor ou não.

```java
// Sem Optional (pode retornar null e dá erro)
public Usuario buscarPorId(Long id) {
    return banco.procura(id);  // Pode retornar null!
}

Usuario joao = buscarPorId(1);
joao.getNome();  // ❌ Dá erro se joao é null!

// Com Optional (mais seguro)
public Optional<Usuario> buscarPorId(Long id) {
    return banco.procura(id);
}

Optional<Usuario> joao = buscarPorId(1);

if (joao.isPresent()) {       // Tem valor?
    println(joao.get().getNome());  // Pega o valor
} else {
    println("Não encontrou!");
}

// Ou melhor ainda:
joao.ifPresent(usuario -> println(usuario.getNome()));
```

---

## 1️⃣9️⃣ O QUE É LAMBDA?

Lambda é uma **função rápida e sem nome**.

```java
// Normal (criando um método)
public void saudacao(String nome) {
    println("Olá " + nome);
}
saudacao("João");

// Lambda (função rápida)
Consumer<String> saudacao = (nome) -> println("Olá " + nome);
saudacao.accept("João");

// Com Optional (exemplo real)
Optional<Usuario> usuario = buscarPorId(1);
usuario.ifPresent(u -> println(u.getNome()));  // Lambda!
```

---

## 2️⃣0️⃣ O QUE É STREAM?

Stream é **processar uma lista de forma elegant**.

```java
List<Usuario> usuarios = [João, Maria, Pedro, Ana];

// Normal
List<String> nomes = new ArrayList<>();
for (Usuario u : usuarios) {
    nomes.add(u.getNome());
}

// Stream (mais elegante)
List<String> nomes = usuarios
    .stream()
    .map(u -> u.getNome())  // Transforma cada usuário no nome dele
    .filter(n -> n.startsWith("M"))  // Filtra nomes que começam com "M"
    .collect(Collectors.toList());

// Resultado: ["Maria"]
```

---

## 🎯 RESUMÃO DOS CONCEITOS

```
Classe       = Projeto
Objeto       = Coisa construída baseada no projeto
Método       = Receita / Ação
Variável     = Caixa que guarda valor
Getter/Setter = Formas de pegar/colocar valores
Array/List   = Fila de coisas
If/Else      = Decisão
Loop         = Repetição
Exception    = Erro
Interface    = Contrato
Annotation   = Marcador pra Spring
This         = "Este objeto"
Static       = Sem precisa de objeto
Private      = Só a classe acessa
Null         = Vazio
Optional     = Container que pode ter valor ou não
Lambda       = Função rápida sem nome
Stream       = Processar lista elegantemente
```

---

## 🧠 PRÓXIMO PASSO

Agora que você sabe os conceitos, releia o código com esses conhecimentos em mente!

Procure por:

- Classes com `@Entity`
- Métodos com `@GetMapping` e `@PostMapping`
- `if` para decisões
- `for` para loops
- `try/catch` para tratamento de erros
- `Optional` para valores que podem não existir
- `List` para coleções

**Boa sorte! 🚀**
