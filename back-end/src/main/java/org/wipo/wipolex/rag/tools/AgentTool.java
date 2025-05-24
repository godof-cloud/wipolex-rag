package org.wipo.wipolex.rag.tools;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

@Component
public class AgentTool {

	@Tool(description = "Search for more information regarding the question that has been done in the WIPO Lex web site")
	String searchForMoreInfoInWipolex(@ToolParam(description = "Question about the documents") String question) {
		return "";
	}
}
