package fr.ensma.a3.ia.be.blockchain.database.api.transactions.transactionsTypes;

import fr.ensma.a3.ia.be.blockchain.database.api.transactions.MyTransaction;

public class MyConvTransaction extends MyTransaction{
    
    private String idConv;
    private String nameConv;

    public MyConvTransaction(String type, int n_date, String client, String conv, String title) {
        super(type, n_date, client);
        if (conv == null || title == null){
            throw new IllegalArgumentException();
        }
        
        idConv = conv;
        nameConv = title;
    }

    public String getClientId() {
        return idConv;
    }

    public String getName() {
        return nameConv;
    }

    @Override
    public String toStore() {
        return "{ " + super.toString() + ", \"IdConv\" : \"" + idConv + "\", \"Cont\" : \"" + nameConv + "\" }";
    }

    
}
