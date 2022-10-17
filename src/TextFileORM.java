import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;

public class TextFileORM {

    private ArrayList<BaseClass> tablesToInitialize;
    private TableFileFetcher tableFileFetcher;
    private TableWriter tableWriter;
    private  TextDbTable[] dbTables;

    public TextFileORM() {
        this.tablesToInitialize = new ArrayList<>();
        this.tableFileFetcher = new TableFileFetcher();
        this.tableWriter = new TableWriter(this.tableFileFetcher);
    }

    public TableWriter getTableWriter(){
        return  this.tableWriter;
    }

    public void addTable(BaseClass table) {
        this.tablesToInitialize.add(table);
    }

    public void connect() throws Exception {
       this.dbTables = getAllTables();
       for (TextDbTable table: dbTables){
          this.tableFileFetcher.addTextDbTable(table);
       }

//       for (TextDbTable table: dbTables){
//           System.out.println(table.getName() + " " + table.getFile().getPath().toString());
//       }
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



