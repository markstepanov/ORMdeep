# ORMdeep

ORMdeep is a .txt based database and object-relational mapper (ORM) made specifically for the "Object Oriented Development with Java" module at APU University.

To use this ORM, you should add this repository to your java project.
In order to create a model you should extend from the "BaseClass" class provided in this library
```java
public class CarDAO extends BaseClass{
    
    @StringField
    String model;
    
    @IntegerField
    int horsePowers;
    
    @FloatField
    float priceInUSD;
    
    @BooleanField
    boolean isInStock;
    
}
```

To explicitly specify which fields of the object should be saved in a .txt file it is required to add annotations to the existing field.
There are 6 for your consideration:
```java
@IntegerField
int someInt;

@FloatField
float someFloat;

@StringField
String someString;

@LocalTimeField
LocalTime someTimeField;

@BoolenField
boolean isActive;

@IdField
int id;
```

>IMPORTANT NOTICE!
>@IdField is an Internal Annotation and every child of BaseClass already has it. If you will add a new @IdField an Exception will be thrown.


After that in your program, you should create an instance of TextFileORM and through .addTable() pass an instance of a class you want to be saved in .txt files. You can add as many classes as you want, as long as they follow the rules above.

```java
public class Main {
    public static void main(String[] args) throws Exception {
        TextFileORM textFileORM = new TextFileORM();
        
        textFileORM.addTable(new CarDAO());
        textFileORM.connect();
        
        }

}

```

After adding all the classes simply call the .connect() method on the textFileORM instance.
If you run your program you will see that a .txt file with the Name of the class will appear in your working directory.CarDAO class will be used as an example.

Inside CarDAO.txt
```
CarDAO&[id:IDFIELD,model:STRINGFIELD,horsePowers:INTFIELD,priceInUSD:FLOATFIELD,isInStock:BOOLEANFIELD]
nextIndex:0
###
```

3 Lines above are metadata of your table and every .txt file will have it, since this information is used for inernal purposes.

Lets add a new record to the CarDAO table


```java
public class Main {
    public static void main(String[] args) throws Exception {
        TextFileORM textFileORM = new TextFileORM();

        textFileORM.addTable(new CarDAO());
        textFileORM.connect();
        
        
        CarDAO newRecordOfCarDAO = new CarDAO();
        newRecordOfCarDAO.model = "Audi A5";
        newRecordOfCarDAO.horsePowers = 300;
        newRecordOfCarDAO.priceInUSD = 30_000f;
        newRecordOfCarDAO.isInStock = true;
        
        TableWriter tableWriter = textFileORM.getTableWriter();
        tableWriter.writeNew(newRecordOfCarDAO);
        

        }

}
```

In Order to write a record to a .txt file, you should retrieve a TableWriter from TextFileORM using the .getTableWriter() method.
To write a new record, simply call the .writeNew() method and pass an instance of BaseClass child (in this case CarDao child).
> If you will pass an instance BaseClass child that was not added to the tables pool before calling the .connect() method, an  Exception will be thrown.


After running this code, a new record to CarDao.txt will be added
```
#CarDao.txt

CarDAO&[id:IDFIELD,model:STRINGFIELD,horsePowers:INTFIELD,priceInUSD:FLOATFIELD,isInStock:BOOLEANFIELD]
nextIndex:1
###
0`Audi A5`300`30000.0`true
```


In order to read the records, you should retrieve TableReader from TextFileORM

```java

public class Main {
    public static void main(String[] args) throws Exception {
        TextFileORM textFileORM = new TextFileORM();

        textFileORM.addTable(new CarDAO());
        textFileORM.connect();

        TableReader tableReader = textFileORM.getTableReader();

        CarDAO carDAOinstanceWithIDZero = (CarDAO) tableReader.readById(new CarDAO(), 0);

        System.out.println(carDAOinstanceWithIDZero.model);
        System.out.println(carDAOinstanceWithIDZero.horsePowers);
        System.out.println(carDAOinstanceWithIDZero.priceInUSD);
        System.out.println(carDAOinstanceWithIDZero.isInStock);

        }

}

#OUTPUT

Audi A5
300
30000.0
true
```







```java
tableReader.readById(BaseClass table, int ID)
```
will return you a BaseClass instance and it should be explicitly down-casted to the child class (CarDAO in this case) in order to access the data inside.


```java
tableReader.readAll(BaseClass table);
```
follows the same principle, but it returns BaseClass[]

<h2>Additional features and Implementations</h2>


__Get All records in table__
```
#CarDao.txt


CarDAO&[id:IDFIELD,model:STRINGFIELD,horsePowers:INTFIELD,priceInUSD:FLOATFIELD,isInStock:BOOLEANFIELD]
nextIndex:3
###
0`Audi A5`300`30000.0`true
1`tesla model x`500`50000.0`false
2`Honda civic`200`15000.0`true

```

```java
public class Main {
    public static void main(String[] args) throws Exception {
        TextFileORM textFileORM = new TextFileORM();

        textFileORM.addTable(new CarDAO());
        textFileORM.connect();



        TableReader tableReader = textFileORM.getTableReader();

        BaseClass[] cars = tableReader.readAll(new CarDAO());

        for (BaseClass upcastedCar: cars){
            System.out.println( ( (CarDAO)upcastedCar).toString() );
        }
  }

}

#OUTPUT
CarDAO{model='Audi A5', horsePowers=300, priceInUSD=30000.0, isInStock=true}
CarDAO{model='tesla model x', horsePowers=500, priceInUSD=50000.0, isInStock=false}
CarDAO{model='Honda civic', horsePowers=200, priceInUSD=15000.0, isInStock=true}

```

__EDIT RECORD__

```
#CarDAO.txt

CarDAO&[id:IDFIELD,model:STRINGFIELD,horsePowers:INTFIELD,priceInUSD:FLOATFIELD,isInStock:BOOLEANFIELD]
nextIndex:3
###
0`Audi A5`300`30000.0`true
1`tesla model x`500`50000.0`false
2`Honda civic`200`15000.0`true
```

```java
public class Main {
    public static void main(String[] args) throws Exception {
        TextFileORM textFileORM = new TextFileORM();

        textFileORM.addTable(new CarDAO());
        textFileORM.connect();


        TableWriter tableWriter = textFileORM.getTableWriter();
        
        CarDAO carToWrite = new CarDAO();

        carToWrite.setId(2);
        carToWrite.model = "Hundai";
        carToWrite.isInStock = true;
        carToWrite.priceInUSD = 15_000f;
        carToWrite.horsePowers = 150;

        tableWriter.writeToID(carToWrite);
        }

}

```

```
#CarDao.txt

CarDAO&[id:IDFIELD,model:STRINGFIELD,horsePowers:INTFIELD,priceInUSD:FLOATFIELD,isInStock:BOOLEANFIELD]
nextIndex:3
###
0`Audi A5`300`30000.0`true
1`tesla model x`500`50000.0`false
2`Hundai`150`15000.0`true
```

__DELETE RECORD___
```
#CarDAO.txt

CarDAO&[id:IDFIELD,model:STRINGFIELD,horsePowers:INTFIELD,priceInUSD:FLOATFIELD,isInStock:BOOLEANFIELD]
nextIndex:3
###
0`Audi A5`300`30000.0`true
1`tesla model x`500`50000.0`false
2`Hundai`150`15000.0`true
```

```java
public class Main {
    public static void main(String[] args) throws Exception {
        TextFileORM textFileORM = new TextFileORM();

        textFileORM.addTable(new CarDAO());
        textFileORM.connect();

        TableRecordDeleter tableRecordDeleter = textFileORM.getTableRecordDeleter();
        tableRecordDeleter.deleteRecordById(new CarDAO(), 2);
        }

}
```

```
#CarDAO.txt

CarDAO&[id:IDFIELD,model:STRINGFIELD,horsePowers:INTFIELD,priceInUSD:FLOATFIELD,isInStock:BOOLEANFIELD]
nextIndex:3
###
0`Audi A5`300`30000.0`true
1`tesla model x`500`50000.0`false
```











