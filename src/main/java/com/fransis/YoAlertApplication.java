package com.fransis;

import com.fransis.model.Email;
import com.fransis.model.FbFilter;
import com.fransis.model.FbUsername;
import com.fransis.model.FbGroup;
import com.fransis.repository.EmailRepository;
import com.fransis.repository.FilterRepository;
import com.fransis.repository.UsernameRepository;
import com.fransis.repository.GroupRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@ComponentScan("com.fransis")
public class YoAlertApplication {

	public static void main(String[] args) {
		SpringApplication.run(YoAlertApplication.class, args);
	}

	@Bean
	public CommandLineRunner loadData(GroupRepository groupRepository, EmailRepository emailRepository, UsernameRepository usernameRepository, FilterRepository filterRepository) {
		return (args) -> {
			Email email = new Email("gianafrancisco@gmail.com", "Francisco Giana");
			emailRepository.saveAndFlush(email);

			FbGroup fbGroup = new FbGroup("1027119427344209", "Prueba API");
			groupRepository.saveAndFlush(fbGroup);

			FbUsername fbUsername = new FbUsername("franciscogiana@hotmail.com", "EAACEdEose0cBABZBByBomUXU9PbiKemuliLF6texJLZCskOYXfXOnxAGdjr0ZAmzdL1gnZCnNjfyABlxYM3new7MjdKE79LWxdacI2evnx2YXGIpRh5rYX2g8YCqydhcFZBlEXzZC8ExD4W0ZACNyXKVrNxOQyw7sBTZAo8haoVTsgZDZD");
			usernameRepository.saveAndFlush(fbUsername);

			FbFilter fbFilter = new FbFilter("Nueva publicacion");
			filterRepository.saveAndFlush(fbFilter);

		};
	}

}
