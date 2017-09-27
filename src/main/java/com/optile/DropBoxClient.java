package com.optile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.optile.model.Content;
import com.optile.model.ContentFile;
import com.optile.model.ContentFolder;
import com.optile.model.User;


/**
 * @author Emin
 *
 */
public class DropBoxClient {
	private static final  Logger logger = Logger.getLogger(DropBoxClient.class);
//	public static String appKey = "vkob3aqvetuzm35";
//	public static String appSecret = "iunnhtg83d0yj7n";
	
	public void dene(String[] args){}
	
	public static void printHelpMsg(){
		System.out.println("\n **************** HELP ******************");
		System.out.println("# To authenticate and authorize Dropbox user, Type : \n  auth {appKey} {appSecret}");
		System.out.println("# To retrieves information about Dropbox user's	account, Type : \n  info {accessToken} {locale}");
		System.out.println("# To Prints the list of files and folders within the specified directory, Type : \n  list {accessToken} {path} {locale} \n");
	}
	
	public static void main(String[] args) {

		String commandLine = "";
		for (int i = 0; i < args.length; i++) {
			commandLine += " " + args[i];
		}
		commandLine = commandLine.trim(); //"list lfxGZ0Bt0GIAAAAAAAAEBPcD_m9zRzI0RTEKaoJzusFZTa22nRMw7e3eZsSXD_8T \"/photos/sample album\"";
		
		DropBoxProcessor dbxProcessor = new DropBoxProcessor("optileDboxClient");
		boolean fromJar = true;
		while(true){
			try {
				if(!fromJar)
					commandLine = new BufferedReader(new InputStreamReader(System.in)).readLine();
				
				String command = commandLine.split(" ")[0].trim();
		
				if(command.equalsIgnoreCase("auth")){
					
					System.out.println("################ AUTH #################");
					String accessToken = dbxProcessor.getAccessToken(commandLine.split(" ")[1].trim(), commandLine.split(" ")[2].trim(), null);
					System.out.println("----------------------------------------------------------------------------------------------");
					System.out.println("Your access-token: " + accessToken);
					System.out.println("----------------------------------------------------------------------------------------------");
				} else if(command.equalsIgnoreCase("info")){
					
					System.out.println("################ INFO #################");
					String pars[] = commandLine.split(" ");
					User user = dbxProcessor.getInfo(pars[1].trim(), pars.length > 2 ? pars[2].trim() : "en");
					System.out.println("----------------------------------------------------------------------------------------------");
					System.out.println(user);
					System.out.println("----------------------------------------------------------------------------------------------");
				} else if(command.equalsIgnoreCase("list")){
					
					System.out.println("################ LIST #################");
					
					Pattern pattern = Pattern.compile("\"([^\"]*)\"");
					Matcher matcher = pattern.matcher(commandLine);
					String path = null;

					while (matcher.find())
						path = matcher.group(1);
					commandLine = commandLine.replace("\"" + path + "\"", "");
					
					String pars[] = commandLine.split(" ");
					List<Content> contents = dbxProcessor.listDbxContent(pars[1].trim(), path, pars.length > 2 ? pars[2].trim() : "en");
					System.out.println("----------------------------------------------------------------------------------------------");
					printPathContent(contents);
					System.out.println("----------------------------------------------------------------------------------------------");
				}else {
					printHelpMsg();
				}
			} catch (Exception e) {
				logger.error("Error Occured : " + e.getMessage());
				System.err.println("\n Error Occured : " + e.getMessage());
				printHelpMsg();
			} finally {
				fromJar = false;
			}
		}
	
	}
	
	private static void printPathContent(List<Content> contents){
		for (int i = 0; i < contents.size(); i++) {
			if(i == 0){
				 System.out.println((contents.get(i) instanceof ContentFile) ? (ContentFile)contents.get(i) : (ContentFolder)contents.get(i));
			} else {
				System.out.println((contents.get(i) instanceof ContentFile) ? "  - " + (ContentFile)contents.get(i) : "  - " + (ContentFolder)contents.get(i));
			}
		}
	}

}
