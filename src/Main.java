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

        SecondTableClass someDataToWrite = new SecondTableClass();
        someDataToWrite.isActive = false;
        someDataToWrite.some_val = 3.1f;
        someDataToWrite.namee = "hi";
        someDataToWrite.setId(6);
//        tableWriter.writeNew(someDataToWrite);
        tableWriter.writeToID(someDataToWrite);


    }







}


