package dcb.dti;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.List;

public class GenericMessage implements Serializable {

    public enum Type {
        MY_COINS,MINT,SPEND,
        MY_NFTS,MINT_NFT,SET_NFT_PRICE,SEARCH_NFT,BUY_NFT
    };

    private Type type;
    private long value = 0;
    private long tokenId = -1;
    private long[] coinIds;
    private int receiver;
    private long nftId;
    private String name;
    private String uri;
    private List<Coin> coins;
    private List<NFT> nfts;

    public GenericMessage(Type type){
        this.type = type;
    }

    public static byte[] toBytes(GenericMessage message) throws IOException {
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        ObjectOutputStream objOut = new ObjectOutputStream(byteOut);
        objOut.writeObject(message);

        objOut.flush();
        byteOut.flush();

        return byteOut.toByteArray();
    }

    public static GenericMessage fromBytes(byte[] rep) throws IOException, ClassNotFoundException {
        ByteArrayInputStream byteIn = new ByteArrayInputStream(rep);
        ObjectInputStream objIn = new ObjectInputStream(byteIn);
        return (GenericMessage) objIn.readObject();
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }

    public long getTokenId() {
        return tokenId;
    }

    public void setTokenId(long tokenId) {
        this.tokenId = tokenId;
    }

    public long[] getCoinIds() {
        return coinIds;
    }

    public void setCoinIds(long[] coinIds) {
        this.coinIds = coinIds;
    }

    public int getReceiver() {
        return receiver;
    }

    public void setReceiver(int receiver) {
        this.receiver = receiver;
    }

    public long getNftId() {
        return nftId;
    }

    public void setNftId(long nftId) {
        this.nftId = nftId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public List<Coin> getCoins() {
        return coins;
    }

    public void setCoins(List<Coin> coins) {
        this.coins = coins;
    }

    public List<NFT> getNfts() {
        return nfts;
    }

    public void setNfts(List<NFT> nfts) {
        this.nfts = nfts;
    }
}