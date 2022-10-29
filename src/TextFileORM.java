import java.util.ArrayList;

public class TextFileORM {

    private ArrayList<BaseClass> tablesToInitialize;
    private TableFileFetcher tableFileFetcher;
    private TableWriter tableWriter;

    private TableReader tableReader;

    private TableRecordDeleter tableRecordDeleter;

    private  TableFIleIOController tableFIleIOController;
    private  TextDbTable[] dbTables;

    public TableRecordDeleter getTableRecordDeleter() {
        return tableRecordDeleter;
    }

    public TextFileORM() {
        this.tablesToInitialize = new ArrayList<>();
        this.tableFileFetcher = new TableFileFetcher();
        this.tableFIleIOController = new TableFIleIOController(this.tableFileFetcher);
        this.tableWriter = new TableWriter(this.tableFIleIOController);
        this.tableReader = new TableReader(this.tableFIleIOController);
        this.tableRecordDeleter = new TableRecordDeleter(this.tableFIleIOController);

    }

    public TableWriter getTableWriter(){
        return  this.tableWriter;
    }

    public  TableReader getTableReader(){
        return this.tableReader;
    }

    public void addTable(BaseClass table) {
        this.tablesToInitialize.add(table);
    }

    public void connect() throws Exception {
       this.dbTables = getAllTables();
       for (TextDbTable table: dbTables){
          this.tableFileFetcher.addTextDbTable(table);
       }

    }



    private TextDbTable[] getAllTables() throws Exception {
        ArrayList<TextDbTable> dbTables = new ArrayList<>();
        TextDbTableFactory textDbTableFactory = new TextDbTableFactory();
        for (BaseClass table : this.tablesToInitialize) {
            dbTables.add(textDbTableFactory.assembleTable(table));
        }
        TextDbTable[] dbTablesArray = new TextDbTable[dbTables.size()];
        dbTablesArray = dbTables.toArray(dbTablesArray);
        return dbTablesArray;
    }


}



