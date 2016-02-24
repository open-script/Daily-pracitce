package service;

import entity.Message;

import javax.ejb.Remote;
import java.util.ArrayList;

/**
 * Created by zzt on 2/23/16.
 * <p>
 * Usage:
 */
@Remote
public interface MessageService {

    void addMsg(String msg, int toUser);
    void deleteMsg(int mid);
    ArrayList<Message> allMsg(int uid);
}
