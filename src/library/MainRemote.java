/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package library;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author Asish
 */
public interface MainRemote extends Serializable, Remote {
    EveryoneRef register(String name)  throws RemoteException;
    MagicalItemInfo[] getAllMagicalItems()  throws RemoteException;
}
