import Annotations.*;

import java.lang.reflect.Field;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Optional;

public class TextDbTableFactory {

    public TextDbTable assembleTable(BaseClass table) throws Exception {
        return new TextDbTable(assembleTableFields(table), table.getClass().getName());
    }

    private TableField[] assembleTableFields(BaseClass table) throws Exception {
        Field[] annotatedFields = this.getAllAnnotatedClassFields(table);

        ArrayList<TableField> tableFields = new ArrayList<>();

        for (int fieldNumber = 0; fieldNumber < annotatedFields.length; fieldNumber++) {
            String fieldName = annotatedFields[fieldNumber].getName();
            DbField fieldType = getAnnotationEnumValue(annotatedFields[fieldNumber]).orElseThrow();
            TableField currentTableFiled = new TableField(fieldName, fieldType, fieldNumber);
            tableFields.add(currentTableFiled);
        }

        TableField[] tableFieldsArray = new TableField[tableFields.size()];
        tableFieldsArray = tableFields.toArray(tableFieldsArray);
        return tableFieldsArray;

    }

    private Field[] getAllAnnotatedClassFields(BaseClass table) throws Exception {
        ArrayList<Field> annotatedFields = new ArrayList<>();

        annotatedFields.add(table.getClass().getSuperclass().getDeclaredField("id"));
        for (Field field : table.getClass().getDeclaredFields()) {
            if (getAnnotationEnumValue(field).isPresent()) {
                checkIfNotIdField(field);
                checkIfFieldHasCorrectType(field);
                annotatedFields.add(field);
            }
        }

        Field[] annotatedFieldsArray = new Field[annotatedFields.size()];
        annotatedFieldsArray = annotatedFields.toArray(annotatedFieldsArray);
        return annotatedFieldsArray;

    }

    private void checkIfFieldHasCorrectType(Field field) throws Exception {
        DbField annotationType = getAnnotationEnumValue(field).orElseThrow();

        if (!field.getType().isAssignableFrom(this.getSupposedFieldType(annotationType))) {
            String className = field.getDeclaringClass().getName();
            String fieldName = field.getName();
            throw new Exception("Field " + fieldName + " in " + className + " does not satisfy "
                    + annotationType.toString() + " annotation. Change " + fieldName +
                    " field type with the one stated in annotation");
        }


    }

    private void checkIfNotIdField(Field field) throws Exception {
        if (getAnnotationEnumValue(field).orElseThrow() == DbField.IDFIELD) {
            // TODO SPECIFY EXACT METHOD BY WHICH ID COULD BE RETRIEVED
            throw new Exception("Second ID field was specified.All tables have ID Field by default");
        }
    }

    private Optional<DbField> getAnnotationEnumValue(Field field) {
        if (field.isAnnotationPresent(IntegerField.class)) {
            return Optional.of(DbField.INTFIELD);
        } else if (field.isAnnotationPresent(FloatField.class)) {
            return Optional.of(DbField.FLOATFIELD);
        } else if (field.isAnnotationPresent(StringField.class)) {
            return Optional.of(DbField.STRINGFIELD);
        } else if (field.isAnnotationPresent(LocalTImeField.class)) {
            return Optional.of(DbField.LOCALTIMEFIELD);
        } else if (field.isAnnotationPresent(IdField.class)) {
            return Optional.of(DbField.IDFIELD);
        } else if (field.isAnnotationPresent(BooleanField.class)){
            return Optional.of(DbField.BOOLEANFIELD);
        }

        return Optional.empty();
    }

    private Class<?> getSupposedFieldType(DbField dbField) {
        return switch (dbField) {
            case IDFIELD, INTFIELD -> int.class;
            case FLOATFIELD -> float.class;
            case STRINGFIELD -> String.class;
            case LOCALTIMEFIELD -> LocalTime.class;
            case BOOLEANFIELD ->  boolean.class;
        };

    }
}
