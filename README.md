# EsPub
Service for sharing essay
# Запуск проекта в docker
Для запуска оркестрации необходимо ввести команду:  
```
docker-compose up -d --build
```

Бд создается со следующими параметрами:  

```
host=localhost
user=postgres
password=password
port=5432
db=epub
```

Для разработки проекта вне докер контейнера необходимо предварительно поменять хост бд на localhost в файле application.properties.  
Точно так же нужно не забыть вернуть хост на epub_postgres перед очередной упаковкой в контейнер.  
  
P. S. Можно всегда запускать проект через докер. Тогда менять ничего не нужно.
