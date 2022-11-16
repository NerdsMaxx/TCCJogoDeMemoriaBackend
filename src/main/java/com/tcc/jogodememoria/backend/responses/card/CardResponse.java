package com.tcc.jogodememoria.backend.responses.card;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CardResponse {
	public CardResponse(String question, String answer) {
		this.question = question;
		this.answer = answer;
	}

	private String question;

	private String answer;
}
