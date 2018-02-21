package com.kverchi.diary.domain;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Liudmyla Melnychuk on 20.2.2018.
 */
public class MessageSearchAttributes extends SearchAttributes {
    public enum MessageSearchType {
        BY_USER_ID,
        BY_COMPANION_ID,
        BY_CONVERSATION_ID,
        BY_MESSAGE_TEXT
    };
    private Map<MessageSearchType, Object> searchCriteria = new HashMap<>();
    private MessageSearchType messageSearchType;

    public Map<MessageSearchType, Object> getSearchCriteria() {
        return searchCriteria;
    }

    public void setSearchCriteria(Map<MessageSearchType, Object> searchCriteria) {
        this.searchCriteria = searchCriteria;
    }
    public void addSearchCriteria(MessageSearchType messageSearchType, Object value) {
        this.searchCriteria.put(messageSearchType, value);
    }

    public MessageSearchType getMessageSearchType() {
        return messageSearchType;
    }

    public void setMessageSearchType(MessageSearchType messageSearchType) {
        this.messageSearchType = messageSearchType;
    }
}
