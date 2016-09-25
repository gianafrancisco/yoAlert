package com.fransis;

import com.fransis.model.*;
import com.fransis.repository.*;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@ComponentScan("com.fransis")
@ConfigurationProperties(locations = "classpath:application.properties")
public class YoAlertApplication {

	private static final String MY_APP_ID = "1334300239928512";
	private static final String MY_APP_SECRET = "fd26d7bc50496527912c4abcee2bf172";

	public static void main(String[] args) {
		SpringApplication.run(YoAlertApplication.class, args);
	}

	@Bean
	public CommandLineRunner loadData(GroupRepository groupRepository, EmailRepository emailRepository, UsernameRepository usernameRepository, FilterRepository filterRepository, WatcherRepository watcherRepository) {
		return (args) -> {
			Email email = new Email("gianafrancisco@gmail.com", "Francisco Giana");
			email = emailRepository.saveAndFlush(email);

			FbGroup fbGroup = new FbGroup("1027119427344209", "Prueba API");
			fbGroup = groupRepository.saveAndFlush(fbGroup);

			String accessToken = System.getProperty("access_token","");

			FacebookClient.AccessToken accessTokenExtended =
					new DefaultFacebookClient().obtainExtendedAccessToken(MY_APP_ID,
							MY_APP_SECRET, accessToken);

			System.out.println("Access Token: " + accessTokenExtended);
			FbUsername fbUsername = new FbUsername("franciscogiana@hotmail.com", accessTokenExtended.getAccessToken());
			fbUsername = usernameRepository.saveAndFlush(fbUsername);

			FbFilter fbFilter = new FbFilter("Nueva publicacion");
			fbFilter = filterRepository.saveAndFlush(fbFilter);


			Watcher watcher = new Watcher("NZ Group");
			watcher.getEmails().add(email);
			watcher.getFilters().add(fbFilter);
			watcher.getGroups().add(fbGroup);

			watcherRepository.save(watcher);

		};
	}

}
