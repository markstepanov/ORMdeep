import Annotations.BooleanField;
import Annotations.FloatField;
import Annotations.IdField;
import Annotations.StringField;


public class SecondTableClass extends BaseClass{

    @FloatField
    float some_val;

    @StringField
    String namee;

    @BooleanField
    boolean isActive;

}
