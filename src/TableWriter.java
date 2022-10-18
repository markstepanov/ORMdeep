import java.io.File;
import java.io.FileWriter;
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


    public <T> void writeToID(T tableToWrite) throws Exception {
        checkIfGenericIsBaseClassChild(tableToWrite);
        BaseClass currentTableToWrite = (BaseClass) tableToWrite;
        TextDbTable textDbTable = createTextDbTable(currentTableToWrite);

        File fileToWrite = getTableFile(textDbTable);
        ArrayList<String> fileContent = getFIleContent(fileToWrite);

        int idToWrite = currentTableToWrite.getId();
        int tableMaxIndex = getMaxIndex(fileContent);

        String record = createDbTableRecord((BaseClass) tableToWrite, textDbTable);
        checkIfNewRecordHasLegitimateID(idToWrite, tableMaxIndex);
        String recordToWrite = concatenateIDWithRecord(idToWrite, record);

        writeRecordToId(fileToWrite, recordToWrite, idToWrite, fileContent);
    }


    private void writeRecordToId(File fileToWrite, String record, int idToWrite, ArrayList<String> fileContent) throws Exception {

        String splitter = "`";
        for (int i = 0; i < fileContent.size(); i++) {

            if (fileContent.get(i).split(splitter)[0].equals(String.valueOf(idToWrite))) {

                fileContent.set(i, record);
                FileWriter fileWriter = new FileWriter(fileToWrite);
                fileWriter.write(String.join("\n", fileContent));
                fileWriter.close();
            }
        }


    }


    private void checkIfNewRecordHasLegitimateID(int idToWrite, int maxIndex) throws Exception {
        if (!(idToWrite > 0 && idToWrite < maxIndex)) {
            throw new Exception("You specified wrong ID. The ID should be more than 0 and less than nextIndex " +
                    "(specified in metadata).Current maxIndex is " + maxIndex);
        }
    }

    private void writeNewRecord(File fileToWrite, String record) throws Exception {
        ArrayList<String> fileContent = getFIleContent(fileToWrite);
        int maxIndex = getMaxIndex(fileContent);
        String newRecord = concatenateIDWithRecord(maxIndex, record);
        fileContent.add(newRecord);
        incrementTableFileNextIndex(fileContent);
        FileWriter fileWriter = new FileWriter(fileToWrite);
        fileWriter.write(String.join("\n", fileContent));
        fileWriter.close();
    }


    private String concatenateIDWithRecord(int recordId, String record) {
        return recordId + "`" + record + "\n";

    }

    private ArrayList<String> getFIleContent(File fileToWrite) throws Exception {
        return new ArrayList<String>(Arrays.asList(Files.readString(fileToWrite.toPath()).split("\n")));
    }

    private int getMaxIndex(ArrayList<String> fileContent) {
        return Integer.parseInt(fileContent.get(1).split(":")[1]);
    }

    private void incrementTableFileNextIndex(List<String> fileContent) {
        String nextIndexLine = fileContent.get(1);
        int incrementedNextIndex = Integer.parseInt(nextIndexLine.split(":")[1]) + 1;
        String newNextIndexLine = "nextIndex:" + incrementedNextIndex;
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
            tableRecord.append(getFieldValue(field, tableClass)).append("`");
        }
        return tableRecord.substring(0, tableRecord.length() - 1);

    }


    private String getFieldValue(TableField field, BaseClass tableClass) throws Exception {
        Field classField = tableClass.getClass().getDeclaredField(field.getName());
        return String.valueOf(classField.get(tableClass));
    }

}




