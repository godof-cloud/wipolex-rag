package org.wipo.wipolex.rag.model;

import java.util.List;

public record LLMResponse(String answer, List<Reference> references, AnswerDetails details ) {

}
