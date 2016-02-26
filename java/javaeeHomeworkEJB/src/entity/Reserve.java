package entity;

import service.RDtemp;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Set;

/**
 * Created by zzt on 2/16/16.
 * <p>
 * Usage:
 */
@Entity()
@Table(name = "reserve")
@NamedQueries(
        {
                @NamedQuery(name = Reserve.USER_RESERVE, query = "select r from Reserve r where r.user.uid = ?1 and r.state = false "),
                @NamedQuery(name = Reserve.COUNT_USER_RESERVE, query = "select COUNT(r) from Reserve r where r.user.uid = ?1 and r.state = false "),
                @NamedQuery(name = Reserve.PAY_RESERVE, query = "select r from Reserve r where r.user.uid = ?1"),
                @NamedQuery(name = Reserve.COUNT_USER_PAYMENT, query = "select COUNT(r) from Reserve r where r.user.uid = ?1"),
                @NamedQuery(name = Reserve.BRANCH_RESERVE, query = "select r from Reserve r where r.branch.bid = ?1 and r.state = false"),
                @NamedQuery(name = Reserve.COUNT_BRANCH_RESERVE, query = "select COUNT(r) from Reserve r where r.branch.bid = ?1 and r.state = false"),
                @NamedQuery(name = Reserve.BRANCH_USER_RESERVE, query = "select r from Reserve r where r.branch.bid = ?1 and r.user.uid = ?2 and r.state = false"),
                @NamedQuery(name = Reserve.COUNT_BRANCH_USER_RESERVE, query = "select COUNT(r) from Reserve r where r.branch.bid = ?1 and r.user.uid = ?2 and r.state = false"),
        }
)
public class Reserve implements Serializable {
    public static final long serialVersionUID = 42L;


    public static final String USER_RESERVE = "user reserve";
    public static final String COUNT_USER_RESERVE = "count user reserve";
    public static final String PAY_RESERVE = "pay reserve";
    public static final String COUNT_USER_PAYMENT = "count pay reserve";
    public static final String BRANCH_RESERVE = "branch reserve";
    public static final String COUNT_BRANCH_RESERVE = "count branch reserve";
    public static final String BRANCH_USER_RESERVE = "branch user reserve";
    public static final String COUNT_BRANCH_USER_RESERVE = "count branch user reserve";

    private int rid;

    private String bdate;
    private boolean state;

    private User user;
    private Branch branch;

    public Reserve() {
    }

    public Reserve(User user, Branch branch, String bdate) {
        this.user = user;
        this.bdate = bdate;
        this.branch = branch;
    }

    public Reserve(int uid, int bid, String bdate, ArrayList<RDtemp> temps) {
        this.bdate = bdate;
    }

    @Id
    @GeneratedValue
    public int getRid() {
        return rid;
    }

    public void setRid(int rid) {
        this.rid = rid;
    }

    public String getBdate() {
        return bdate;
    }

    public void setBdate(String bdate) {
        this.bdate = bdate;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    @ManyToOne(optional = false)
    @JoinColumn(name = "uid")
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @ManyToOne(optional = false)
    @JoinColumn(name = "bid")
    public Branch getBranch() {
        return branch;
    }

    public void setBranch(Branch branch) {
        this.branch = branch;
    }

    private Set<ReserveDetail> details;

    @OneToMany(mappedBy = "reserve")
    public Set<ReserveDetail> getDetails() {
        return details;
    }

    public void setDetails(Set<ReserveDetail> details) {
        this.details = details;
        for (ReserveDetail detail : details) {
            addDetail(detail);
        }
    }

    public void addDetail(ReserveDetail detail) {
        detail.setReserve(this);
    }

    public double payment() {
        return details
                .stream()
                .mapToDouble(detail -> detail.getNum() * detail.getPrice())
                .sum();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Reserve reserve = (Reserve) o;

        return rid == reserve.rid;

    }

    @Override
    public int hashCode() {
        return rid;
    }
}