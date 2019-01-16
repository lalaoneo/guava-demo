package com.lori.guavademo.eventBus;

import com.google.common.eventbus.EventBus;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EventBusTest {

    @Test
    public void testEventBus() {

        EventBus eventBus = new EventBus("test");

        EventListener listener = new EventListener("listener");

        EventListener listener1 = new EventListener("listener1");

        EventListener listener2 = new EventListener("listener2");

        eventBus.register(listener);
        eventBus.register(listener1);
        eventBus.register(listener2);

        eventBus.post(new TestEvent(100));
    }
}
