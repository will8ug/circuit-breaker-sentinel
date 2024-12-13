package net.will.circuitbreaker.resource;

import net.will.circuitbreaker.application.AccountApplicationService;
import net.will.circuitbreaker.domain.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
@RestController
@RequestMapping("/accounts")
public class AccountResource {
    @Autowired
    private AccountApplicationService accountService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpEntity<List<Account>> list() {
        List<Account> list = accountService.list();
        list.forEach(AccountResource::populateHateoasProperties);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping(path = "/slow-list", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpEntity<List<Account>> slowList() {
        List<Account> list = accountService.slowList();
        list.forEach(AccountResource::populateHateoasProperties);

        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping(path = "/exception-list", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpEntity<List<Account>> exceptionList() {
        List<Account> list = accountService.listWithPossibleException();
        list.forEach(AccountResource::populateHateoasProperties);

        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpEntity<Account> get(@PathVariable String id) {
        Optional<Account> optAccount = accountService.get(id);
        if (optAccount.isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        populateHateoasProperties(optAccount.get());
        return new ResponseEntity<>(optAccount.get(), HttpStatus.OK);
    }

    private static void populateHateoasProperties(Account act) {
        act.add(linkTo(methodOn(AccountResource.class).get(act.getId())).withSelfRel());
    }
}
