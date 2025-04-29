package com.b1tf0m0.reddit.feeder.save;

import com.b1tf0m0.common.broker.EventPublisher;
import com.b1tf0m0.common.event.EventFileSaver;
import com.b1tf0m0.reddit.database.RedditEventInserter;
import com.b1tf0m0.reddit.processor.RedditRawProcessor;

public class SaveRedditEvent {
    public void saveFeederEvent(String eventJsonString, String SOURCE_SYSTEM, String TOPIC_NAME){
        if (eventJsonString != null) {
            System.out.println("Saving feeder events...");
            EventFileSaver fileSaver = new EventFileSaver();
            fileSaver.saveEvent(TOPIC_NAME, SOURCE_SYSTEM, eventJsonString);
            RedditEventInserter inserter = new RedditEventInserter();
            RedditRawProcessor processor = new RedditRawProcessor();
            inserter.insertEvent(processor.processRawToObject(eventJsonString));

            EventPublisher.publishEvent(TOPIC_NAME, eventJsonString, SOURCE_SYSTEM);
            System.out.println("✅ Evento de Reddit publicado en topic: " + TOPIC_NAME);
        }
    }
}
