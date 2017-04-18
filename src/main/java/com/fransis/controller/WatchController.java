package com.fransis.controller;

import com.fransis.model.*;
import com.fransis.repository.*;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.List;

/**
 * Created by francisco on 26/09/2016.
 */
@RestController
@RequestMapping("/w")
@Component("watchController")
public class WatchController {

    private static final String MY_APP_ID = "1334300239928512";
    private static final String MY_APP_SECRET = "fd26d7bc50496527912c4abcee2bf172";

    @Autowired
    private WatcherRepository watcherRepository;
    @Autowired
    private EmailRepository emailRepository;
    @Autowired
    private FilterRepository filterRepository;
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private UsernameRepository usernameRepository;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Watcher> create(@RequestBody Watcher watcher){
        Watcher watcherRepo = watcherRepository.saveAndFlush(watcher);
        URI location = null;
        try {
            location = new URI("/w/" + watcherRepo.getId());
        } catch (URISyntaxException e) {
            return (ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)).body(null);
        }
        return (ResponseEntity.status(HttpStatus.CREATED)).location(location).body(watcherRepo);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<Watcher> getWatcher(@PathVariable Long id){
        Watcher watcherRepo = watcherRepository.findOne(id);
        watcherRepo.setGroups(groupRepository.findByWatcher(watcherRepo));
        watcherRepo.setEmails(emailRepository.findByWatcher(watcherRepo));
        watcherRepo.setFilters(filterRepository.findByWatcher(watcherRepo));
        List<FbUsername> usernameList = usernameRepository.findByWatcher(watcherRepo);
        FbUsername username = null;
        if(usernameList.size() > 0) {
            username = usernameList.get(0);
            watcherRepo.setUsername(username);
        }
        return (ResponseEntity.status(HttpStatus.OK)).body(watcherRepo);
    }

    @RequestMapping(value = "/{id}/emails", method = RequestMethod.GET)
    public ResponseEntity<Collection<Email>> getEmails(@PathVariable Long id){
        Watcher watcherRepo = watcherRepository.findOne(id);
        List<Email> emails = emailRepository.findByWatcher(watcherRepo);
        return (ResponseEntity.status(HttpStatus.OK)).body(emails);
    }

    @RequestMapping(value = "/{id}/emails", method = RequestMethod.POST)
    public ResponseEntity<Email> createEmail(@PathVariable Long id, @RequestBody Email email){
        Watcher watcherRepo = watcherRepository.findOne(id);
        email.setWatcher(watcherRepo);
        Email emailRepo = emailRepository.saveAndFlush(email);


        URI location = null;
        try {
            location = new URI("/w/" + watcherRepo.getId() + "/emails/" + emailRepo.getEmail());
        } catch (URISyntaxException e) {
            return (ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)).body(null);
        }

        return (ResponseEntity.status(HttpStatus.CREATED)).location(location).body(email);
    }

    @RequestMapping(value = "/{id}/filters", method = RequestMethod.GET)
    public ResponseEntity<Collection<FbFilter>> getFilters(@PathVariable Long id){
        Watcher watcherRepo = watcherRepository.findOne(id);
        List<FbFilter> filters = filterRepository.findByWatcher(watcherRepo);
        return (ResponseEntity.status(HttpStatus.OK)).body(filters);
    }

    @RequestMapping(value = "/{id}/filters", method = RequestMethod.POST)
    public ResponseEntity<FbFilter> createFilter(@PathVariable Long id, @RequestBody FbFilter filter){
        Watcher watcherRepo = watcherRepository.findOne(id);
        filter.setWatcher(watcherRepo);
        FbFilter repo = filterRepository.saveAndFlush(filter);

        URI location = null;
        try {
            location = new URI("/w/" + watcherRepo.getId() + "/filters/" + repo.getId());
        } catch (URISyntaxException e) {
            return (ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)).body(null);
        }
        return (ResponseEntity.status(HttpStatus.CREATED)).location(location).body(repo);
    }

    @RequestMapping(value = "/{id}/filters/{idFilter}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteFilter(@PathVariable Long id, @PathVariable Long idFilter){

        FbFilter filter = filterRepository.findOne(idFilter);
        if(filter != null){
            filterRepository.delete(idFilter);
            filterRepository.flush();
            return (ResponseEntity.status(HttpStatus.NO_CONTENT)).build();
        }else{
            return (ResponseEntity.status(HttpStatus.NOT_FOUND)).build();
        }

    }

    @RequestMapping(value = "/{id}/groups", method = RequestMethod.GET)
    public ResponseEntity<Collection<FbGroup>> getGroups(@PathVariable Long id){
        Watcher watcherRepo = watcherRepository.findOne(id);
        List<FbGroup> groups = groupRepository.findByWatcher(watcherRepo);
        return (ResponseEntity.status(HttpStatus.OK)).body(groups);
    }

    @RequestMapping(value = "/{id}/groups", method = RequestMethod.POST)
    public ResponseEntity<FbGroup> createGroup(@PathVariable Long id, @RequestBody FbGroup group){
        Watcher watcherRepo = watcherRepository.findOne(id);
        group.setWatcher(watcherRepo);
        FbGroup repo = groupRepository.saveAndFlush(group);

        URI location = null;
        try {
            location = new URI("/w/" + watcherRepo.getId() + "/groups/" + repo.getId());
        } catch (URISyntaxException e) {
            return (ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)).body(null);
        }
        return (ResponseEntity.status(HttpStatus.CREATED)).location(location).body(repo);
    }

    @RequestMapping(value = "/{id}/users", method = RequestMethod.GET)
    public ResponseEntity<FbUsername> getUser(@PathVariable Long id){
        Watcher watcherRepo = watcherRepository.findOne(id);
        List<FbUsername> usernameList = usernameRepository.findByWatcher(watcherRepo);
        FbUsername username = null;
        if(usernameList.size() > 0) {
            username = usernameList.get(0);
        }
        return (ResponseEntity.status(HttpStatus.OK)).body(username);
    }

    @RequestMapping(value = "/{id}/users", method = RequestMethod.POST)
    public ResponseEntity<FbUsername> createUser(@PathVariable Long id, @RequestBody FbUsername user){
        Watcher watcherRepo = watcherRepository.findOne(id);

        FacebookClient.AccessToken accessTokenExtended =
                new DefaultFacebookClient().obtainExtendedAccessToken(MY_APP_ID,
                        MY_APP_SECRET, user.getAccessToken());
        FbUsername user2 = new FbUsername(user.getUsername(), accessTokenExtended.getAccessToken(), user.getUsuarioId());
        user2.setWatcher(watcherRepo);
        FbUsername repo = usernameRepository.saveAndFlush(user2);

        URI location = null;
        try {
            location = new URI("/w/" + watcherRepo.getId() + "/users/" + repo.getUsername());
        } catch (URISyntaxException e) {
            return (ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)).body(null);
        }
        return (ResponseEntity.status(HttpStatus.CREATED)).location(location).body(user2);
    }

}
