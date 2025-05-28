package org.wipo.wipolex.rag.model;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record Evaluation(ConversationTurn conversationTurn) {

}
