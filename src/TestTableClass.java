import Annotations.*;

import java.time.LocalTime;

public class TestTableClass extends BaseClass{

    @StringField
    String name;
    @FloatField
    float price;
    @LocalTImeField
    LocalTime timeOfAdding;
    @IntegerField
    int some_int_field;
}
