package com.cartonwale.common.model;

public class SMSRequestBody{

	private final String TemplateName;
	
	private final String To;
	
	private final String From = "PACKFY";
	
	private final String VAR1;
	
	private final String VAR2;

	private final String VAR3;
	
	private final String VAR4;
	
	private final String VAR5;

	private SMSRequestBody(SMSBodyBuilder builder) {
		super();
		TemplateName = builder.TemplateName;
		To = builder.To;
		VAR1 = builder.VAR1;
		VAR2 = builder.VAR2;
		VAR3 = builder.VAR3;
		VAR4 = builder.VAR4;
		VAR5 = builder.VAR5;
	}

	public String getTemplateName() {
		return TemplateName;
	}

	public String getTo() {
		return To;
	}

	public String getFrom() {
		return From;
	}

	public String getVAR1() {
		return VAR1;
	}

	public String getVAR2() {
		return VAR2;
	}

	public String getVAR3() {
		return VAR3;
	}

	public String getVAR4() {
		return VAR4;
	}

	public String getVAR5() {
		return VAR5;
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
