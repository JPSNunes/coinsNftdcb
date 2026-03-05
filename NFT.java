package dcb.dti;

import java.io.Serializable;

public class NFT implements Serializable {

    private long id;
    private int owner;
    private String name;
    private String uri;
    private long value;

    public NFT(long id, int owner, String name, String uri, long value) {
        this.id = id;
        this.owner = owner;
        this.name = name;
        this.uri = uri;
        this.value = value;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getOwner() {
        return owner;
    }

    public void setOwner(int owner) {
        this.owner = owner;
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

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "NFT{id=" + id + ", owner=" + owner + ", name='" + name + "', uri='" + uri + "', value=" + value + "}";
    }
}