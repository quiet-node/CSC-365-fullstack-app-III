## After clone the repo, run docker to get the appropriate working environment

#### Mysql for apple m1

```
docker pull --platform linux/amd64 mysql
```

#### run mysql image

```
docker run --name myYelpSql -e MYSQL_ROOT_PASSWORD=password -e MYSQL_DATABASE=myYelpDB -d -p 3306:3306 mysql:latest
```

### Get to mysql bash

```
docker exec -it myYelpSql bash
```

### In the bash, to log into mysql

```
mysql -u root -p
```

Then insert password, which is "password"

### SQL commands

```
show databases;
```

```
USE myYelpDB;
SHOW TABLEs;
SELECT * FROM business_model;
```

## Attention

because the categories are too big, make sure to change the categories column data type to longblob

first, in JsonParser class, specify a random JPA query to create the table.

```repository.findAll()``` in run method;

Then alter the table

```
ALTER TABLE yelp MODIFY categories LONGBLOB;
```

### Make sure yelp-backend/src/main/resources/application.properties is filled with:

    spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
    spring.datasource.username=root
    spring.datasource.password=password
    spring.datasource.url=jdbc:mysql://localhost:3306/mydb
    spring.jpa.hibernate.ddl-auto=update
