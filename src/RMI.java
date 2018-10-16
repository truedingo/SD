//package hello;
package com.company;

import java.rmi.*;
public interface RMI extends Remote {
	public void sayHello() throws java.rmi.RemoteException;
}