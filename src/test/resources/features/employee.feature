#language: pt
Funcionalidade:  Gestao de Funcionario

  Cenario: Cadastro de funcionario
    Dado um funcionario
    Quando enviar uma requisicao do tipo POST para o recurso "/api/employees"
    Entao deve ser retornado o stutas code 201
    E no response deve conter o funcionario salvo
    E deve ser persistido na base de dados o funcionario

  Cenario: Listar funcionarios
    Dado dois funcionarios salvos
    Quando enviar uma requisicao do tipo GET para o recurso "/api/employees"
    Entao deve ser retornado o stutas code 200
    E no response deve conter a lista de funcionarios