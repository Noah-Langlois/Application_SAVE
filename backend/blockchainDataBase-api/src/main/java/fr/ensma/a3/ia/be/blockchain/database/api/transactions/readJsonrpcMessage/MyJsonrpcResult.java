package fr.ensma.a3.ia.be.blockchain.database.api.transactions.readJsonrpcMessage;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MyJsonrpcResult {
    private List<MyJsonrpcTx> txs;

    @JsonProperty("total_count")
    private String totalCount;
    
    public MyJsonrpcResult(){}

    public String getTotalCount(){
        return totalCount;
    }

    public List<MyJsonrpcTx> gettxs(){
        return txs;
    }

 
}
