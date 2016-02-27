package entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Set;

/**
 * Created by zzt on 2/17/16.
 * <p>
 * Usage:
 */
@Entity()
@Table(name = "plan")
@NamedQueries(
        {
                @NamedQuery(query = "select p from Plan p where p.state = 0", name = Plan.NEW_PLAN),
                @NamedQuery(query = "select p from Plan p where p.state = 1 and p.branch.bid = ?1", name = Plan.BRANCH_PLAN),
                @NamedQuery(query = "select p from Plan p where p.staff.sid = ?1 and p.state <> 1", name = Plan.STAFF_PLAN),
        }
)
public class Plan implements Serializable {
    public static final long serialVersionUID = 42L;

    public static final String NEW_PLAN = "new plan";
    public static final String BRANCH_PLAN = "branch plan";
    public static final String STAFF_PLAN = "staff submitted plan";
    private int planId;

    private byte state;
    private String pdate;

    private Set<PlanDetail> details;

    private Branch branch;

    private Staff staff;

    public Plan() {
    }

    public Plan(Branch branch, String planDate) {
        this.branch = branch;
        this.pdate = planDate;
    }

    @Id
    @GeneratedValue
    public int getPlanId() {
        return planId;
    }

    public void setPlanId(int planId) {
        this.planId = planId;
    }

    public byte isState() {
        return state;
    }

    public void setState(byte state) {
        this.state = state;
    }

    public String getPdate() {
        return pdate;
    }

    public void setPdate(String pdate) {
        this.pdate = pdate;
    }

    @OneToMany(mappedBy = "plan", cascade = CascadeType.ALL)
    public Set<PlanDetail> getDetails() {
        return details;
    }

    public void setDetails(Set<PlanDetail> details) {
        this.details = details;
        for (PlanDetail detail : details) {
            addDetail(detail);
        }
    }

    public void addDetail(PlanDetail detail) {
        detail.setPlan(this);
    }

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "bid")
    public Branch getBranch() {
        return branch;
    }

    public void setBranch(Branch branch) {
        this.branch = branch;
    }

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "sid")
    public Staff getStaff() {
        return staff;
    }

    public void setStaff(Staff staff) {
        this.staff = staff;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Plan plan = (Plan) o;

        return planId == plan.planId;

    }

    @Override
    public int hashCode() {
        return planId;
    }
}