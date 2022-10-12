# testeConfitec

API Rest desenvolvida em Spring Boot (Java) para calcular plano de pagamento de uma apólice.

`Endpoint:` /confitec/teste/parcelamento.

`Método:` POST.

`Link para acessar documentação via Swagger:` http://localhost:8080/swagger-ui.html.

`JSON exemplo de entrada:` Request: https://pastecode.io/s/fz606jgw
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

`JSON exemplo de saída:` Response: https://pastecode.io/s/0x8w819p
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


