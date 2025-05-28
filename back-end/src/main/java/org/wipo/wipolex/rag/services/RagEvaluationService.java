package org.wipo.wipolex.rag.services;

import java.util.List;

public interface RagEvaluationService {

	String generateEvaluationFile(List<String> questions);
}
