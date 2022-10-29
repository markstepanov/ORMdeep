import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public class TableRecordDeleter {
    private  TableFIleIOController tableFIleIOController;

   protected  TableRecordDeleter(TableFIleIOController tableFIleIOController){
       this.tableFIleIOController = tableFIleIOController;
   }


   public  void deleteRecordById(BaseClass table, int id) throws Exception{
       TextDbTable textDbTable = this.tableFIleIOController.createTextDbTable(table);
       TextDbTable existingDbTable  = this.tableFIleIOController.getTable(textDbTable);
       File tableFile =  existingDbTable.getFile();
       ArrayList<String> fileContent = tableFIleIOController.getFIleContent(tableFile);
       if (id < 0  || id > tableFIleIOController.getMaxIndex(fileContent)){
           throw  new Exception("Could not parse value with " + id + " id.It's either lower than zero or bigger than maxIndex in the Table");
       }
       String splitter = "`";
       for (int i = 0; i < fileContent.size(); i++) {
           if (fileContent.get(i).split(splitter)[0].equals(String.valueOf(id))) {
               fileContent.remove(i);
               FileWriter fileWriter = new FileWriter(tableFile);
               fileWriter.write(String.join("\n", fileContent));
               fileWriter.close();
               return;
           }
       }

       throw new Exception("Record with ID " + id + " is not exist");

   }
}
