import Annotations.*;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Optional;

public class TextDbTable {
    private TableField[] fields;
    private File file;
    private String name;

    public void setFile(File file) {
        this.file = file;
    }

    public TextDbTable(TableField[] tableFields, String name){
        this.fields = tableFields;
        this.name = name;
    }

    public TableField[] getFields() {
        return fields;
    }

    public File getFile() {
        return file;
    }

    public String getName() {
        return name;
    }

    public String generateMeta() {
        String metaData = this.name + "&[";
        for (int i = 0; i < this.fields.length; i++) {
            metaData += this.fields[i].getName() + ":" + this.fields[i].getFieldType();
            if (i != this.fields.length - 1) {
                metaData += ",";
            }
        }

        return metaData + "]";
    }

}