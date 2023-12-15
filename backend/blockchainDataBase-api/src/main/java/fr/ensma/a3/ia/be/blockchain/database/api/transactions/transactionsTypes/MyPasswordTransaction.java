package fr.ensma.a3.ia.be.blockchain.database.api.transactions.transactionsTypes;

import fr.ensma.a3.ia.be.blockchain.database.api.transactions.MyTransaction;

public class MyPasswordTransaction extends MyTransaction{

    private String hash;

    public MyPasswordTransaction(String type, int n_date, String client,String h) {
        super(type, n_date, client);

        if (h == null){
            throw new IllegalArgumentException("h must not be null");
        }

        hash = h;
        
    }

    public String getHash() {
        return hash;
    }

    @Override
    public String toStore() {
        return "{ " + super.toString() + ", \"Cont\" : \"" + hash +"\"}";
    }
    
}
