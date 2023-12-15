package fr.ensma.a3.ia.be.blockchain.database.api.transactions;

public abstract class MyTransaction implements ITransaction {

    private String type_tx;
    private int date;
    private String id_client;

    public MyTransaction(String type, int n_date, String client) {

        if (type == null) {
            throw new IllegalArgumentException("type must not be null");

        }
        if (n_date < 0) {
            throw new IllegalArgumentException("date must not be null");
        }
        if (client == null) {
            throw new IllegalArgumentException("client must not be null");
        }

        type_tx = type;
        date = n_date;
        id_client = client;
    }

    public String getType() {
        return type_tx;
    }

    public String getClient() {
        return id_client;
    }

    public int getDate() {
        return date;
    }

    public String toString() {
        return "\\\"TypeTx\\\":\\\"" + type_tx + "\\\",\\\"Date\\\":" + date + ",\\\"IdClient\\\":\\\"" + id_client +"\\\"";
    }

}
