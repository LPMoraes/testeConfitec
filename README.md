# testeConfitec

API Rest desenvolvida em Spring Boot (Java) para calcular plano de pagamento de uma apólice.

`Endpoint:` /confitec/teste/parcelamento.

`Método:` POST.

`Link para acessa documentação Swagger:` http://localhost:8080/swagger-ui.html.

`JSON exmpleto de entrada:`

```json
{
  "listCobertura": [
    {
      "cobertura": 0,
      "valor": 0
    }
  ],
  "listOpcaoParcelamento": [
    {
      "juros": 0,
      "quantidadeMaximaParcelas": 0,
      "quantidadeMinimaParcelas": 0
    }
  ]
}
```

`JSON xemplo de saída:`
```json
[
  {
    "quantidadeParcelas": 0,
    "valorDemaisParcelas": 0,
    "valorParcelamentoTotal": 0,
    "valorPrimeiraParcela": 0
  }
]
```


