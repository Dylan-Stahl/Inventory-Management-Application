package model;

/**
 * InHouse class is a subclass of Part and inherits part's methods.
 * @author Dylan Stahl
 */
public class InHouse extends Part{
    private int machineid;

    /**
     * InHouse class constructor.
     * @param id
     * @param name
     * @param price
     * @param stock
     * @param min
     * @param max
     * @param machineid
     */
    public InHouse(int id, String name, double price, int stock, int min, int max, int machineid) {
        super(id, name, price, stock, min, max);
    this.machineid = machineid;
    }

    /**
     * Method returns given InHouse part's machine id.
     * @return
     */
    public int getMachineId() {
        return machineid;
    }

    /**
     * Method sets the InHouse part's machine id.
     * @param machineid
     */
    public void setMachineId(int machineid) {
        this.machineid = machineid;
    }
}
