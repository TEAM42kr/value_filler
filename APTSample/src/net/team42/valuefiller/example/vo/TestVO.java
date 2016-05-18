package net.team42.valuefiller.example.vo;

import net.team42.valuefiller.annotation.ValueFiller;

public class TestVO {
	@ValueFiller(path = "http://mafia42.co.kr/channel.php", sourceType = ValueFiller.SOURCETYPE_HTTP)
	String httpTestContent;
	@ValueFiller(path = "C:\\Windows\\system.ini", sourceType = ValueFiller.SOURCETYPE_FILE)
	String fileTestContent;

	public String getHttpTestContent() {
		return httpTestContent;
	}

	public void setHttpTestContent(String httpTestContent) {
		this.httpTestContent = httpTestContent;
	}

	public String getFileTestContent() {
		return fileTestContent;
	}

	public void setFileTestContent(String fileTestContent) {
		this.fileTestContent = fileTestContent;
	}
}
