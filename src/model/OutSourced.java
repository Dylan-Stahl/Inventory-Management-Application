package model;

/**
 * OutSourced class is a subclass of Part and inherits part's methods.
 * @author Dylan Stahl
 */
public class OutSourced extends Part {
    private String companyName;

    /**
     * OutSourced class constructor.
     * @param id
     * @param name
     * @param price
     * @param stock
     * @param min
     * @param max
     * @param companyName
     */
    public OutSourced(int id, String name, double price, int stock, int min, int max, String companyName) {
        super(id, name, price, stock, min, max);
        this.companyName = companyName;
    }

    /**
     * Returns company name for part.
     * @return
     */
    public String getCompanyName() {
        return companyName;
    }

    /**
     * Sets the companyName for a part.
     * @param companyName
     */
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

}
