# CamelExample
Demonstração de utilização de apache camel como middleware para classificação e transformações de arquivos.

1. Configuração do Ambiente
   - Instalar Java Development Kit (JDK) 11 ou superior.
   - Instalar Apache Maven.
   - *Lembrar de incluir as variáveis de ambiente para ambos caso seja primeira instalação

2. Execução
   - Criar um diretório chamado **orders** e um chamado **out** (no mesmo nível do pom.xml)
   - Criar três diretórios dentro de **out**, chamados **bar, checkout e kitchen**
   - ![image](https://github.com/user-attachments/assets/82bd0730-07d8-4c29-a75c-a823c5968e33)
   - Rodar no prompt: **mvn clean install -U**
   - Rodar no prompt: **mvn spring-boot:run**

3. Funcionamento
   - A ideia do program é como se fosse um sistema que recebe requisições de pedidos de um bar/restaurante no formato json e faz a transformação e encaminhamento para a determinada área responsável.
   - Na pasta **examples** temos jsons no formato aceito na aplicação e podem ser usados para teste também
   - Com a aplicação rodando, ao adicionar um arquivo json no formato aceito dentro da pasta **orders**, ele transformara em uma .txt com mensagem amigável e classificará para a pasta correspondente do seu tipo dentro do diretório **out**

## Interface para fazer os pedidos

1. Instalação
   - Instalar o python
   - Instalar o streamlit com `pip install streamlit`

2. Execução
   - Execute a aplicação com `streamlit run Inicio.py`
 