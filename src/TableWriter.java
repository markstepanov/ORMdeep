import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TableWriter {

    private TableFileFetcher tableFileFetcher;

    protected TableWriter(TableFileFetcher tableFileFetcher) {
        this.tableFileFetcher = tableFileFetcher;
    }

    public <T> void writeNew(T tableToWrite) throws Exception {
        checkIfGenericIsBaseClassChild(tableToWrite);
        TextDbTable textDbTable = createTextDbTable((BaseClass) tableToWrite);
        File fileToWrite = getTableFile(textDbTable);
        String record = createDbTableRecord((BaseClass) tableToWrite, textDbTable);
        writeNewRecord(fileToWrite, record);
    }


    private void writeNewRecord(File fileToWrite, String record) throws Exception {

        ArrayList<String> fileContent = new ArrayList<>(Arrays.asList(Files.readString(fileToWrite.toPath()).split("\n")));
        int maxIndex = Integer.parseInt(fileContent.get(1).split(":")[1]);
        String newRecord = maxIndex + "," + record + "\n";
        fileContent.add(newRecord);
        incrementTableFileNextIndex(fileContent);
        FileWriter fileWriter = new FileWriter(fileToWrite);
        fileWriter.write(String.join("\n",fileContent));
        fileWriter.close();


    }

    private void incrementTableFileNextIndex(List<String> fileContent){
        String nextIndexLine =  fileContent.get(1);
        int incrementedNextIndex = Integer.parseInt(nextIndexLine.split(":")[1]) + 1;
        String newNextIndexLine = "nextIndex:" +  incrementedNextIndex;
        fileContent.set(1, newNextIndexLine);
    }

    private File getTableFile(TextDbTable currentTextDbTable) throws Exception {
        List<TextDbTable> existingDbTables = tableFileFetcher.getImmutableDbTables();

        for (TextDbTable table : existingDbTables) {
            if (table.generateMeta().equals(currentTextDbTable.generateMeta())) {
                return table.getFile();
            }
        }
        throw new Exception("There is no such table " + currentTextDbTable.getName());
    }


    private TextDbTable createTextDbTable(BaseClass tableClass) throws Exception {
        TextDbTableFactory textDbTableFactory = new TextDbTableFactory();
        return textDbTableFactory.assembleTable(tableClass);
    }

    private <T> void checkIfGenericIsBaseClassChild(T tableToWrite) throws Exception {
        if (!(tableToWrite instanceof BaseClass)) {
            throw new Exception(tableToWrite.getClass().toString() + " is not a child of a BaseClass , thus cannot be written to a text file");
        }
    }

    private String createDbTableRecord(BaseClass tableClass, TextDbTable currentTable) throws Exception {

        StringBuilder tableRecord = new StringBuilder();


        for (int i = 1; i < currentTable.getFields().length; i++) {
            TableField field = currentTable.getFields()[i];
            tableRecord.append(getFieldValue(field, tableClass)).append(",");
        }
        return tableRecord.substring(0, tableRecord.length() - 1);

    }


    private String getFieldValue(TableField field, BaseClass tableClass) throws Exception {
        Field classField = tableClass.getClass().getDeclaredField(field.getName());
        return String.valueOf(classField.get(tableClass));
    }

}




