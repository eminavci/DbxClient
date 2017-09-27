package com.optile;

import java.util.List;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import com.optile.model.Content;
import com.optile.model.User;

public class DropBoxProcessorTest {

	static DropBoxProcessor dbxProcessor;
	static String accessToken = "lfxGZ0Bt0GIAAAAAAAAEBPcD_m9zRzI0RTEKaoJzusFZTa22nRMw7e3eZsSXD_8T";
	
	@BeforeClass
	public static void initDbxProcessor(){
		dbxProcessor = new DropBoxProcessor("optileDboxClient");
	}
	
	@Test
	public void authTest() throws Exception {
		String appKey = "vkob3aqvetuzm35";
		String secretKey = "iunnhtg83d0yj7n";
		String dBxAuthCode = "lfxGZ0Bt0GIAAAAAAAAEBzPL8C2LZ_O7xoz5JK9AkA8";
		
		String accessToken = dbxProcessor.getAccessToken(appKey, secretKey, dBxAuthCode);
		Assert.assertNotNull(accessToken);
	}

	@Test
	public void userInfoTest() throws Exception {
		User user = dbxProcessor.getInfo(accessToken, "en");
		Assert.assertNotNull(user);
	}
	
	@Test
	public void metaDataListTest() throws Exception {
		List<Content> contents = dbxProcessor.listDbxContent(accessToken, "", "en");
		Assert.assertNotNull(contents);
	}
}
