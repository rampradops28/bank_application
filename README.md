# MiniBank — Quarkus REST API  

A lightweight banking example built with Quarkus, JPA/Hibernate (Panache) and YAML-driven product rules. 

## API Endpoints

Create account
- POST /api/v1/accounts
- Example request:
```json
{
  "productCode": "SAL1",
  "productType": "SALARY",
  "name": "John Doe",
  "age": 30,
  "accountNumber": "ACC001"
}
```

Post transaction
- POST /api/v1/transactions
- Example request:
```json
{
  "accountNumber": "ACC001",
  "paymentType": "DEBIT",
  "amount": 100,
  "postingDate": "2025-12-10"
}
```

## Product Rules (YAML)
Product rules are loaded from:
- src/main/resources/salary-products.yml
- src/main/resources/student-products.yml

They define product codes, age limits and transaction limits.

## Error Handling
API returns structured errors via a global exception mapper, for example:
```json
{
  "timeStamp": "2025-12-10T10:04:50Z",
  "errorCode": "InsufficientBalanceException",
  "message": "Not enough balance"
}
```

## License
MIT (or choose your preferred license)