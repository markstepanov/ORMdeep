import Annotations.BooleanField;
import Annotations.FloatField;
import Annotations.IdField;
import Annotations.StringField;


public class SecondTableClass extends BaseClass{

   public SecondTableClass (){}

    @Override
    public String toString() {
        return "SecondTableClass{" +
                "some_val=" + some_val +
                ", namee='" + namee + '\'' +
                ", isActive=" + isActive +
                ", id=" + this.getId() +
                '}';
    }

    public  SecondTableClass(float some_val, String name, boolean isActive){
        this.some_val = some_val;
        this.namee =name;
        this.isActive = isActive;
    }

    @FloatField
    float some_val;

    @StringField
    String namee;

    @BooleanField
    boolean isActive;

}
