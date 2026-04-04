package pl.comp.generatorfaktur.entities;

public class InvoiceRequest {

    private String companyName;
    private String address;
    private String nip;
    private String description;
    private int amount;
    private double netPrice;
    private double grossPrice;

    public InvoiceRequest(String address, String companyName, String nip, String description, int amount, double netPrice) {
        this.address = address;
        this.companyName = companyName;
        this.nip = nip;
        this.description = description;
        this.amount = amount;
        this.netPrice = netPrice;
    }

    public double getNetPrice() {
        return netPrice;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getAddress() {
        return address;
    }

    public String getNip() {
        return nip;
    }

    public String getDescription() {
        return description;
    }

    public int getAmount() {
        return amount;
    }
}
