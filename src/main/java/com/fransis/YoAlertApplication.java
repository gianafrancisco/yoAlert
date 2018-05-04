package com.fransis;

import com.fransis.model.*;
import com.fransis.repository.*;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.Arrays;

@SpringBootApplication
@EnableScheduling
@ComponentScan("com.fransis")
@ConfigurationProperties(locations = "classpath:application.properties")
@EnableAutoConfiguration
public class YoAlertApplication {

	@Value("${fb.appId}")
	private String MY_APP_ID = "";
	@Value("${fb.appSecret}")
	private String MY_APP_SECRET = "";

	public static void main(String[] args) {
		SpringApplication.run(YoAlertApplication.class, args);
	}

	@Bean
	public CommandLineRunner loadData(GroupRepository groupRepository, EmailRepository emailRepository, UsernameRepository usernameRepository, FilterRepository filterRepository, WatcherRepository watcherRepository) {
		return (args) -> {


			if(watcherRepository.findAll().size() == 0){

				Watcher watcher = new Watcher("NZ");
				watcherRepository.save(watcher);

				Email email = new Email("gianafrancisco@gmail.com", "Francisco Giana");
				email.setWatcher(watcher);
				email = emailRepository.saveAndFlush(email);

				email = new Email("berna@yomeanimoyvos.com", "Bernardo Cari");
				email.setWatcher(watcher);

				email = emailRepository.saveAndFlush(email);

				FbGroup fbGroup = new FbGroup("223419024459146", "Nueva Zelanda - yomeanimoyvos.com");
				fbGroup.setWatcher(watcher);
				fbGroup = groupRepository.saveAndFlush(fbGroup);

				String filters = System.getProperty("filters", "orbit,protect,manualdemanuel,argentinoporelmundo,assist,asegura,aseguratuviaje,whatsap,whatsapp,chanta,greattrips,great,trips,1500,cantone,seguros,seguro,351,agreguen,agreguenme,sinlimites,agencia,caradura,estafa,universal");

				Arrays.stream(filters.split(",")).forEach(s -> {
					FbFilter fbFilter = new FbFilter(s.trim());
					fbFilter.setWatcher(watcher);
					fbFilter = filterRepository.saveAndFlush(fbFilter);
				});



			}

		};
	}

}
