package org.wipo.wipolex.rag.model;

import java.util.List;

public record LLMResponse(String answer, List<String> references, AnswerDetails details ) {

}
