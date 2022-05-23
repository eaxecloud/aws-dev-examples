# Linux
``` shell
curl -H "Content-Type:application/json" -X POST --data '{"firstName":"Elon","lastName":"Musk"}' "http://localhost:8080/user"·
```

#Windows
```shell
curl -H "Content-Type:application/json" -X POST --data "{"""firstName""":"""Elon""","""lastName""":"""Musk"""}" "http://localhost:8080/user"
```

```shell
curl -X GET "http://localhost:8080/user"
curl -X GET "http://localhost:8080/user?firstName=Elon&lastName=Musk"
curl -X GET "http://localhost:8080/user?firstName=Elon"
curl -X GET "http://localhost:8080/user?lastName=Musk"
curl -X GET "http://localhost:8080/user?lastName=Tom"
```

