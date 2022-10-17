
public class TableField {
    private String name;
    private DbField fieldType;
    private int fieldNumber;

    public TableField(String name, DbField fieldType, int fieldNumber) {
        this.name = name;
        this.fieldType = fieldType;
        this.fieldNumber = fieldNumber;
    }

    public int getFieldNumber() {
        return fieldNumber;
    }

    public void setFieldNumber(int fieldNumber) {
        this.fieldNumber = fieldNumber;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DbField getFieldType() {
        return fieldType;
    }

    public void setFieldType(DbField fieldType) {
        this.fieldType = fieldType;
    }

}


enum DbField {
    IDFIELD,
    STRINGFIELD,
    INTFIELD,
    FLOATFIELD,
    LOCALTIMEFIELD,

    BOOLEANFIELD
}
