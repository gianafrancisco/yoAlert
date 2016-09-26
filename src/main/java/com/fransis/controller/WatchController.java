package com.fransis.controller;

import com.fransis.model.Email;
import com.fransis.model.FbFilter;
import com.fransis.model.FbGroup;
import com.fransis.model.Watcher;
import com.fransis.repository.EmailRepository;
import com.fransis.repository.FilterRepository;
import com.fransis.repository.GroupRepository;
import com.fransis.repository.WatcherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;

/**
 * Created by francisco on 26/09/2016.
 */
@RestController
@RequestMapping("/w")
@Component("watchController")
public class WatchController {

    @Autowired
    private WatcherRepository watcherRepository;
    @Autowired
    private EmailRepository emailRepository;
    @Autowired
    private FilterRepository filterRepository;
    @Autowired
    private GroupRepository groupRepository;

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
        return (ResponseEntity.status(HttpStatus.OK)).body(watcherRepo);
    }

    @RequestMapping(value = "/{id}/emails", method = RequestMethod.GET)
    public ResponseEntity<Collection<Email>> getEmails(@PathVariable Long id){
        Watcher watcherRepo = watcherRepository.findOne(id);
        Collection<Email> emails = watcherRepo.getEmails();
        return (ResponseEntity.status(HttpStatus.OK)).body(emails);
    }

    @RequestMapping(value = "/{id}/emails", method = RequestMethod.POST)
    public ResponseEntity<Email> createEmail(@PathVariable Long id, @RequestBody Email email){
        Watcher watcherRepo = watcherRepository.findOne(id);
        Email emailRepo = emailRepository.saveAndFlush(email);
        Collection<Email> emails = watcherRepo.getEmails();
        emails.add(emailRepo);
        watcherRepository.saveAndFlush(watcherRepo);

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
        Collection<FbFilter> filters = watcherRepo.getFilters();
        return (ResponseEntity.status(HttpStatus.OK)).body(filters);
    }

    @RequestMapping(value = "/{id}/filters", method = RequestMethod.POST)
    public ResponseEntity<FbFilter> createFilter(@PathVariable Long id, @RequestBody FbFilter filter){
        Watcher watcherRepo = watcherRepository.findOne(id);
        FbFilter repo = filterRepository.saveAndFlush(filter);
        Collection<FbFilter> collection = watcherRepo.getFilters();
        collection.add(repo);
        watcherRepository.saveAndFlush(watcherRepo);
        URI location = null;
        try {
            location = new URI("/w/" + watcherRepo.getId() + "/filters/" + repo.getId());
        } catch (URISyntaxException e) {
            return (ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)).body(null);
        }
        return (ResponseEntity.status(HttpStatus.CREATED)).location(location).body(repo);
    }

    @RequestMapping(value = "/{id}/groups", method = RequestMethod.GET)
    public ResponseEntity<Collection<FbGroup>> getGroups(@PathVariable Long id){
        Watcher watcherRepo = watcherRepository.findOne(id);
        Collection<FbGroup> groups = watcherRepo.getGroups();
        return (ResponseEntity.status(HttpStatus.OK)).body(groups);
    }

    @RequestMapping(value = "/{id}/groups", method = RequestMethod.POST)
    public ResponseEntity<FbGroup> createGroup(@PathVariable Long id, @RequestBody FbGroup group){
        Watcher watcherRepo = watcherRepository.findOne(id);
        FbGroup repo = groupRepository.saveAndFlush(group);
        Collection<FbGroup> collection = watcherRepo.getGroups();
        collection.add(repo);
        watcherRepository.saveAndFlush(watcherRepo);
        URI location = null;
        try {
            location = new URI("/w/" + watcherRepo.getId() + "/groups/" + repo.getId());
        } catch (URISyntaxException e) {
            return (ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)).body(null);
        }
        return (ResponseEntity.status(HttpStatus.CREATED)).location(location).body(repo);
    }

}
