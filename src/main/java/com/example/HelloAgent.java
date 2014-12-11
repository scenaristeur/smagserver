package com.example;

import org.janusproject.kernel.agent.Agent;
import org.janusproject.kernel.status.Status;
 
public class HelloAgent extends Agent  {
  public Status live() {
    print("bonjour Agent!\n");

    return null;
  }
}