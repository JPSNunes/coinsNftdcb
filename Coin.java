package dcb.dti;

import java.io.Serializable;

public class Coin implements Serializable {

    private long id;
    private int owner;
    private long value;

    public Coin(long id, int owner, long value) {
        this.id = id;
        this.owner = owner;
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

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Coin{id=" + id + ", owner=" + owner + ", value=" + value + "}";
    }
}