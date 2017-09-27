package com.optile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.dropbox.core.DbxAppInfo;
import com.dropbox.core.DbxAuthFinish;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.DbxWebAuth;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.FolderMetadata;
import com.dropbox.core.v2.files.ListFolderResult;
import com.dropbox.core.v2.files.Metadata;
import com.dropbox.core.v2.users.FullAccount;
import com.optile.model.Content;
import com.optile.model.ContentFile;
import com.optile.model.ContentFolder;
import com.optile.model.User;

/**
 * Locale attribute for DropBox Api has been deprecated
 * 
 * @author Emin
 *
 */
public class DropBoxProcessor {
	private static final  Logger logger = Logger.getLogger(DropBoxProcessor.class);
	private String clientIdentifier;


	
	public DropBoxProcessor(String clientIdentifier) {
		this.clientIdentifier = clientIdentifier;
	}

	
	/** authenticate user by Oauth over DropBox App,
	 * @param appKey
	 * @param appSecret
	 * * @param authCode (it is used only for unit test)
	 * @return Access Token <String>
	 * @throws Exception
	 */
	public String getAccessToken(String appKey, String appSecret, String dBxAuthCode) throws Exception{
		
		DbxAppInfo appInfo = new DbxAppInfo(appKey, appSecret);
        DbxRequestConfig regConf = new DbxRequestConfig(this.clientIdentifier);
        DbxWebAuth authDbx = new DbxWebAuth(regConf, appInfo);
        
        DbxWebAuth.Request authReqDbx = DbxWebAuth.newRequestBuilder().withNoRedirect().build();
        String authUrl = authDbx.authorize(authReqDbx);
        
        if(dBxAuthCode == null)
        	System.out.println("Go to " + authUrl + " \n and then click \"Allow\" (you might have to log in first)");
        
        DbxAuthFinish authFinish;
        try {
        	if(dBxAuthCode == null){
	        	System.out.print("Copy the authorization code and paste it here:");
	        	dBxAuthCode = new BufferedReader(new InputStreamReader(System.in)).readLine();
        	} 
        	authFinish = authDbx.finishFromCode(dBxAuthCode);
        	if(authFinish == null)
        		throw new Exception("Authentication is empty!");
		} catch (Exception e) {
			logger.error("Error <Auth> :", e);
			throw new Exception(e.getMessage());
		}

		return authFinish.getAccessToken();
	}
	
	/** Retrieve Dbx user information.
	 * @param accessToken
	 * @param locale
	 * @return User
	 * @throws Exception
	 */
	public User getInfo(String accessToken, String locale) throws Exception{
		User user = null;
		 try {
			 DbxClientV2 client = new DbxClientV2(new DbxRequestConfig(this.clientIdentifier), accessToken);
			 FullAccount acc = client.users().getCurrentAccount();

	        user = new User(acc.getAccountId(), acc.getName().getDisplayName(), 
	        		acc.getName().getGivenName() + " " + acc.getName().getSurname() + "(" + acc.getName().getFamiliarName() + ")", 
	        		acc.getEmail(), acc.getCountry(), acc.getReferralLink());
		} catch (Exception e) {
			logger.error("Error <Info> :", e);
			throw new Exception(e.getMessage());
		}
		 return user;
	}
	
	/** The first item in the List<Content>, is always parent
	 * @param accessToken
	 * @param path
	 * @return
	 * @throws Exception
	 */
	public List<Content> listDbxContent(String accessToken, String path, String locale) throws Exception{
		List<Content> contents = new ArrayList<Content>();
		DbxClientV2 dbxClient = new DbxClientV2(new DbxRequestConfig(this.clientIdentifier), accessToken);
		
		if(path == null || path.equals("/"))
			path = "";
		try {
			ListFolderResult result = null;
			try {
				result = dbxClient.files().listFolder(path);
			} catch (Exception e) {
				FileMetadata fmdd = (FileMetadata)dbxClient.files().getMetadata(path);
				contents.add(new ContentFile("file", fmdd.getPathLower() , 
        				new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(fmdd.getServerModified()), 
        				fmdd.getSize() / 1000, "-"));
				return contents;
			}
			if(!path.equals("")){
				FolderMetadata fmdd = (FolderMetadata)dbxClient.files().alphaGetMetadata(path);
				contents.add(new ContentFolder("dir", fmdd.getPathLower(), "-"));
			} else
				contents.add(new ContentFolder("dir", "/", "-"));

	        while (true) {
	        	Content cnt = null;
	            for (Metadata metadata : result.getEntries()) {
	            	if (metadata instanceof FileMetadata) {
	            		FileMetadata fmd = (FileMetadata)metadata;
	            		//Unfortunately there is no file info (.zip .rar .exe etc) in the response
	            	
	            		cnt = new ContentFile("file", fmd.getPathLower() , 
	            				new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(fmd.getServerModified()), 
	            				fmd.getSize() / 1000, "-");
	            	} else if (metadata instanceof FolderMetadata){
	            		FolderMetadata fmd = (FolderMetadata)metadata;
	            		// No modified date property for folder 
	            		cnt = new ContentFolder("dir", fmd.getPathLower(), "-");
	            	}
	            	contents.add(cnt);
	            }
	            if (!result.getHasMore())
	                break;
	            result = dbxClient.files().listFolderContinue(result.getCursor());
	        }
		} catch (Exception e) {
			logger.error("Error <Info> :", e);
			throw new Exception(e.getMessage());
		}

		return contents;
	}
	
}
