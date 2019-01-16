package com.lori.guavademo.eventBus;

public class TestEvent {

    private int num;

    public TestEvent(int numParam){
        this.num = numParam;
        System.out.println("event num : "+num);
    }

    public int getNum(){
        return num;
    }
}
