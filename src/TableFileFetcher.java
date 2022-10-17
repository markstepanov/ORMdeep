import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class TableFileFetcher {


    private ArrayList<TextDbTable> dbTables;

    protected  TableFileFetcher(){
        this.dbTables = new ArrayList<>();
    }


    protected void addTextDbTable(TextDbTable table) throws Exception{
        File tableFile = getOrCreateTableFile(table);
        if (checkIfMetaDataIsTheSame(tableFile, table)){
            table.setFile(tableFile);
            dbTables.add(table);
        }
        else  {
            throw new Exception(table.getName() + " file and table has different metadata, try to delete existing TableFile");
        }
    }

    protected List<TextDbTable>  getImmutableDbTables(){
        return (List<TextDbTable>)Collections.unmodifiableList(this.dbTables);
    }

    private File getOrCreateTableFile(TextDbTable table) throws Exception{
        Optional<File> tableFileBox = getFileIfExist(table);
        File tableFile;
        if (tableFileBox.isEmpty()) {
            tableFile =  createNewTableFile(table).orElseThrow();
            writeMetadata(tableFile, table);
        } else {
            tableFile = tableFileBox.get();
        }

        return  tableFile;
    }

    private  boolean checkIfMetaDataIsTheSame(File tableFile, TextDbTable table) throws Exception{
       String[] fileLines = Files.readString(tableFile.toPath()).split("\n");
       int metaDataIndex = 0;

       if (fileLines[metaDataIndex].equals(table.generateMeta())){
           return true;
       }
       return false;
    }

    private Optional<File> createNewTableFile(TextDbTable table){
        File tableFile= new File(table.getName());
        try {
            if (tableFile.createNewFile()) {
                writeMetadata(tableFile, table);
                return Optional.of(tableFile);
            }
        }  catch (Exception e){
            e.printStackTrace();
        }
        return Optional.empty();
    }

    private Optional<File> getFileIfExist(TextDbTable table) {
        File tableFile = new File(table.getName());
        if (tableFile.exists()){
            return Optional.of(tableFile);
        }
        return Optional.empty();
    }

    private void writeMetadata (File tableFile, TextDbTable table) throws Exception{
        FileWriter fileWriter = new FileWriter(tableFile);
        fileWriter.write(table.generateMeta() + "\n");
        fileWriter.write("nextIndex:0\n");
        fileWriter.write("###\n");
        fileWriter.close();
    }

}
