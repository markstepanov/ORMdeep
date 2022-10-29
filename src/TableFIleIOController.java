import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TableFIleIOController {

    private  TableFileFetcher tableFileFetcher;

    protected  TableFIleIOController(TableFileFetcher tableFileFetcher){
        this.tableFileFetcher = tableFileFetcher;
    }
    protected TextDbTable createTextDbTable(BaseClass tableClass) throws Exception {
        TextDbTableFactory textDbTableFactory = new TextDbTableFactory();
        return textDbTableFactory.assembleTable(tableClass);
    }

    protected TextDbTable getTable(TextDbTable currentTextDbTable) throws Exception {

        List<TextDbTable> existingDbTables = tableFileFetcher.getImmutableDbTables();

        for (TextDbTable table : existingDbTables) {
            if (table.generateMeta().equals(currentTextDbTable.generateMeta())) {
                return table;
            }
        }
        throw new Exception("There is no such table " + currentTextDbTable.getName());
    }

    protected ArrayList<String> getFIleContent(File file) throws Exception {
        return new ArrayList<String>(Arrays.asList(Files.readString(file.toPath()).split("\n")));
    }

    protected int getMaxIndex(ArrayList<String> fileContent) {
        return Integer.parseInt(fileContent.get(1).split(":")[1]);
    }
}
