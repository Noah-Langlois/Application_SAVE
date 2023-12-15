package fr.ensma.a3.ia.be.blockchain.database.api.transactions.transactionsTypes;

import fr.ensma.a3.ia.be.blockchain.database.api.transactions.MyTransaction;

public class MyConvSupprTransaction extends MyTransaction {

    private String idConv;

    public MyConvSupprTransaction(String type, int n_date, String client, String conv) {
        super(type, n_date, client);
        if (conv == null) {
            throw new IllegalArgumentException("conv must not be null");
        }

        idConv = conv;
    }

    public String getConvId() {
        return idConv;
    }

    @Override
    public String toStore() {

        return "{ " + super.toString() + ", \"IdConv\" : \"" + idConv + "\"}";
    }

}
