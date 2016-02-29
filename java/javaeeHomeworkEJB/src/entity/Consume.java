package entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by zzt on 2/23/16.
 * <p>
 * Usage:
 */
@Entity()
@Table(name = "consume")
@NamedQueries(
        {
                @NamedQuery(name = Consume.OWNING_MONEY_USER, query = "select c from Consume c where c.balance < 0"),
                @NamedQuery(name = Consume.COUNT_OWNING_MONEY_USER, query = "select count (c) from Consume c where c.balance < 0")

        }
)
public class Consume implements Serializable {
    public static final long serialVersionUID = 42L;
    public static final String OWNING_MONEY_USER = "owning money user";
    public static final String COUNT_OWNING_MONEY_USER = "count user";

    private int uid;

    private byte state;
    private String nDate;
    private double balance;
    private byte rank;
    private int credit;

    private User user;

    public Consume() {
    }

    public Consume(User user) {
        this.user = user;
        /*
        @see {Account}
         */
        //        uid = user.getUid();
    }

    @MapsId
    @OneToOne
    @JoinColumn(name = "uid")
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Id
    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public byte getState() {
        return state;
    }

    public void setState(byte state) {
        this.state = state;
    }

    public String getnDate() {
        return nDate;
    }

    public void setnDate(String nDate) {
        this.nDate = nDate;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public byte getRank() {
        return rank;
    }

    public void setRank(byte rank) {
        this.rank = rank;
    }

    public int getCredit() {
        return credit;
    }

    public void setCredit(int credit) {
        this.credit = credit;
    }
}