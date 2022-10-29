
public class Main {
    public static void main(String[] args) throws Exception {
        TextFileORM textDb = new TextFileORM();
        TestTableClass testTableClass = new TestTableClass();
        SecondTableClass secondTableClass = new SecondTableClass();


        textDb.addTable(testTableClass);
        textDb.addTable(secondTableClass);
        textDb.connect();



        TableReader tableReader = textDb.getTableReader();

















    }







}


