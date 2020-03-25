package com.cartonwale.common.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming()
public class SMSRequestBody{

	@JsonProperty("TemplateName")
	private final String templateName;
	
	@JsonProperty("To")
	private final String to;
	
	@JsonProperty("From")
	private final String from = "PACKFY";
	
	@JsonProperty("VAR1")
	private final String var1;
	
	@JsonProperty("VAR2")
	private final String var2;

	@JsonProperty("VAR3")
	private final String var3;
	
	@JsonProperty("VAR4")
	private final String var4;
	
	@JsonProperty("VAR5")
	private final String var5;

	private SMSRequestBody(SMSBodyBuilder builder) {
		super();
		templateName = builder.TemplateName;
		to = builder.To;
		var1 = builder.VAR1;
		var2 = builder.VAR2;
		var3 = builder.VAR3;
		var4 = builder.VAR4;
		var5 = builder.VAR5;
	}
	
	public String getTemplateName() {
		return templateName;
	}



	public String getTo() {
		return to;
	}



	public String getFrom() {
		return from;
	}



	public String getVar1() {
		return var1;
	}



	public String getVar2() {
		return var2;
	}



	public String getVar3() {
		return var3;
	}



	public String getVar4() {
		return var4;
	}



	public String getVar5() {
		return var5;
	}



	public static class SMSBodyBuilder {
		
		private final String TemplateName;
		
		private final String To;
		
		private String VAR1;
		
		private String VAR2;

		private String VAR3;
		
		private String VAR4;
		
		private String VAR5;
		
		public SMSBodyBuilder(String TemplateName, String To) {
			this.TemplateName = TemplateName;
			this.To = To;
		}
		
		public SMSBodyBuilder VAR1(String var1){
			this.VAR1 = var1;
			return this;
		}
		
		public SMSBodyBuilder VAR2(String var2){
			this.VAR2 = var2;
			return this;
		}
		
		public SMSBodyBuilder VAR3(String var3){
			this.VAR3 = var3;
			return this;
		}
		
		public SMSBodyBuilder VAR4(String var4){
			this.VAR4 = var4;
			return this;
		}
		
		public SMSBodyBuilder VAR5(String var5){
			this.VAR5 = var5;
			return this;
		}
		
		public SMSRequestBody build(){
			SMSRequestBody body = new SMSRequestBody(this);
			
			return body;
		}
	}
}
