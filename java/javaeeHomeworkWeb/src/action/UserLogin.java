package action;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.validator.annotations.RequiredFieldValidator;
import com.opensymphony.xwork2.validator.annotations.Validations;
import com.opensymphony.xwork2.validator.annotations.ValidatorType;
import org.apache.struts2.ServletActionContext;
import org.hornetq.utils.JNDIUtil;
import remote.JNDIFactory;
import service.AccountService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Created by zzt on 2/11/16.
 * <p>
 * Usage:
 * Your browser sends to the web server a request for the URL http://localhost:8080/tutorial/HelloWorld.action.
 * <p>
 * The container receives from the web server a request for the resource HelloWorld.action. According to the settings
 * loaded from the web.xml, the container finds that all requests are being routed to
 * org.apache.struts2.dispatcher.FilterDispatcher, including the *.action requests. The FilterDispatcher is the entry
 * point into the framework.
 * The framework looks for an action mapping named "HelloWorld", and it finds that this mapping corresponds to the
 * class
 * "HelloWorld". The framework instantiates the Action and calls the Action's execute method.
 * The execute method sets the message and returns SUCCESS. The framework checks the action mapping to see what page to
 * load if SUCCESS is returned. The framework tells the container to render as the response to the request, the
 * resource
 * HelloWorld.jsp.
 * As the page HelloWorld.jsp is being processed, the <s:property value="message" /> tag calls the getter getMessage of
 * the HelloWorld Action, and the tag merges into the response the value of the message.
 * A pure HTML response is sent back to the browser.
 */
public class UserLogin extends ActionSupport {

    public static final String UID = "uid";
    private String name;
    private String pw;
    private final AccountService accountService;

    public UserLogin() {
        accountService =
                (AccountService) JNDIFactory.getResource("ejb:/javaeeHomeworkEJB_ejb exploded//UserInfoEJB!service.AccountService");
    }

    @Validations(
            requiredFields =
                    {
                            @RequiredFieldValidator(type = ValidatorType.SIMPLE, fieldName = "name", message = "You must enter a value for field."),
                            @RequiredFieldValidator(type = ValidatorType.SIMPLE, fieldName = "pw", message = "You must enter a value for field.")
                    }
    )
    @Override
    public String execute() throws Exception {
        String name = getName();
        String pw = getPw();

        Integer uid = accountService.login(name, pw);
        if (uid == null) {
            addFieldError("name", "user name or password is wrong.");
            return INPUT;
        }
        SessionManagement.setUserSession(uid);
        return SUCCESS;
    }

    public String logOut() throws Exception {
        HttpSession session = SessionManagement.getSession();
        assert session != null;
        session.invalidate();
        return SUCCESS;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setPw(String pw) {
        this.pw = pw;
    }

    public String getPw() {
        return pw;
    }
}
