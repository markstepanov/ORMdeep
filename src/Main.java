import java.time.LocalTime;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        TextFileORM textDb = new TextFileORM();
        TestTableClass testTableClass = new TestTableClass();
        SecondTableClass secondTableClass = new SecondTableClass();

        textDb.addTable(testTableClass);
        textDb.addTable(secondTableClass);
        textDb.connect();

        TableWriter tableWriter = textDb.getTableWriter();
        TestTableClass someDataToWrite = new TestTableClass();
        someDataToWrite.name = "hello";
        someDataToWrite.some_int_field = 3;
        someDataToWrite.price = 3.0f;
        someDataToWrite.timeOfAdding = LocalTime.now();
        tableWriter.writeNew(someDataToWrite);


    }







}


