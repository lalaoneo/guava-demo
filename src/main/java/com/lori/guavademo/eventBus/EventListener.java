package com.lori.guavademo.eventBus;

import com.google.common.eventbus.Subscribe;

public class EventListener {

    private String name;

    public EventListener(String name){
        this.name = name;
    }

    private int lastNum = 0;

    public int getLastNum() {
        return lastNum;
    }

    @Subscribe
    public void listen(TestEvent event){
        lastNum = event.getNum();
        System.out.println(name + " listen num : "+lastNum);
    }
}
