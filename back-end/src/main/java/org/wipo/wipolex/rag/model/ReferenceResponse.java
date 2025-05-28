package org.wipo.wipolex.rag.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ReferenceResponse(List<ContentItem> content) {

}
