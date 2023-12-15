package fr.ensma.a3.ia.be.blockchain.database.api.transactions.readJsonrpcMessage;


public class MyJsonrpcMessage {
    
    private String jsonrpc;
    private Integer id;
    private MyJsonrpcResult result;

    public MyJsonrpcMessage(){}

    public String getTotalCount(){
        return result.getTotalCount();
    }

}
