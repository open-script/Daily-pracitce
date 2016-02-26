package action;

import com.opensymphony.xwork2.ActionSupport;
import entity.Account;
import entity.Consume;
import entity.User;
import remote.JNDIFactory;
import service.AccountService;
import service.ConsumeService;

import java.util.List;

/**
 * Created by zzt on 2/13/16.
 * <p>
 * Usage:
 * Struts 2 Action objects are instantiated for each request, so there are no thread-safety issues.
 */
public class AccountAction extends ActionSupport {

    User user;
    Account account;
    Consume consume;


    public AccountAction() {
    }

    @Override
    public String execute() throws Exception {
        try {
            AccountService accountService =
                    (AccountService) JNDIFactory.getResource("ejb:/javaeeHomeworkEJB_exploded//UserInfoEJB!service.AccountService");
            int uid = SessionManagement.getUid();
            assert accountService != null;
            user = accountService.getUser(uid);
            account = user.getAccount();
            consume = user.getConsume();
        } catch (Exception e) {
            e.printStackTrace();
            return ERROR;
        }
        return SUCCESS;
    }

    private List<Consume> records;
    private String result;
    private String message;

    private int totalRecordCount;
    // Holds Start Page Index
    private int jtStartIndex;
    // Hold records to be displayed per Page
    private int jtPageSize;

    public List<Consume> getRecords() {
        return records;
    }

    public void setRecords(List<Consume> records) {
        this.records = records;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getTotalRecordCount() {
        return totalRecordCount;
    }

    public void setTotalRecordCount(int totalRecordCount) {
        this.totalRecordCount = totalRecordCount;
    }

    public int getJtStartIndex() {
        return jtStartIndex;
    }

    public void setJtStartIndex(int jtStartIndex) {
        this.jtStartIndex = jtStartIndex;
    }

    public int getJtPageSize() {
        return jtPageSize;
    }

    public void setJtPageSize(int jtPageSize) {
        this.jtPageSize = jtPageSize;
    }

    public String userList() throws Exception {
        try {
            ConsumeService consumeService
                    = (ConsumeService) JNDIFactory.getResource("ejb:/javaeeHomeworkEJB_exploded//UserInfoEJB!service.ConsumeService");
            records = consumeService.userBalanceList();
            totalRecordCount = consumeService.countUserBalanceList();
            result = JTableHelper.OK;
        } catch (Exception e) {
            e.printStackTrace();
            result = JTableHelper.ERROR;
            return ERROR;
        }
        return SUCCESS;
    }
}

