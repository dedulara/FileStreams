import java.io.Serializable;

public class Product implements Serializable
{
    private String idString;
    private String nameString;
    private String descriptString;
    private double costDouble;

    public Product(String idString, String nameString, String descriptString, double costDouble)
    {
        this.idString = idString;
        this.nameString = nameString;
        this.descriptString = descriptString;
        this.costDouble = costDouble;
    }

        public String getIdString() {return idString;}                                      //Get ID

        public String getNameString() {return nameString;}                                  //Get product name

        public String getDescriptString() {return descriptString;}                          //Get product description

        public double getCostDouble() {return costDouble;}                                  //Get product cost
        public void setCostDouble(double costDouble) {this.costDouble = costDouble;}        //Set product cost

        public String toCSVDataRecord()
        {
            return (idString + ", " + nameString + ", " + descriptString + ", " + costDouble);
        }

        @Override
        public String toString()
        {
            return "Product{" +
                    "idString='" + idString + '\'' +
                    ", nameString='" + nameString + '\'' +
                    ", descriptString='" + descriptString + '\'' +
                    ", costDouble=" + costDouble +
                    '}';
        }
}