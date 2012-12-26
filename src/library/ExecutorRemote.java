/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package library;
import java.io.Serializable;
import java.rmi.Remote;

/**
 *
 * @author Asish
 */
public interface ExecutorRemote extends Remote, Serializable{
    int MAX_COMMODITY = 20;
    Object invoke(int userID, int itemNumber, int op, Object obj) throws Exception;
}
