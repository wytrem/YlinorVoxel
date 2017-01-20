package com.ylinor.library.api;

public class EntityTest
{
    static class Aze
    {
        synchronized void foo()
        {
            System.out.println("toto");
        }
        
        synchronized void faa()
        {
            System.out.println("tata");
            foo();
        }
    }
    
    
    public static void main(String[] args) throws InterruptedException
    {
        Aze aze = new Aze();
        aze.faa();
    }
}
