package com.techelevator.controller;
import com.techelevator.dao.FamilyDao;
import com.techelevator.dao.JdbcPrizeDao;
import com.techelevator.dao.PrizeDao;
import com.techelevator.dao.UserDao;
import com.techelevator.model.Prize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import javax.validation.Valid;
import java.util.List;
@CrossOrigin
@RestController
@PreAuthorize("isAuthenticated()")
public class PrizeController {

    private final PrizeDao prizeDao;
    private final FamilyDao familyDao;
    private final UserDao userDao;

    @Autowired
    public PrizeController(PrizeDao prizeDao, FamilyDao familyDao, UserDao userDao) {
        this.prizeDao = prizeDao;
        this.familyDao = familyDao;
        this.userDao = userDao;
    }

    @RequestMapping(path = "/prize/{prizeId}", method = RequestMethod.GET)
    public Prize getPrizeById(@PathVariable int prizeId) {
        return prizeDao.getPrizeById(prizeId);
    }
    @RequestMapping(path = "/prize/family/{familyId}", method = RequestMethod.GET)
    public List<Prize> getPrizesByFamilyId(@PathVariable int familyId) {
        return prizeDao.getPrizesByFamilyId(familyId);
    }
    @PreAuthorize("permitAll")
    @RequestMapping(path = "/prize/user/{userId}", method = RequestMethod.GET)
    public List<Prize> getPrizesByWinnerUserId(@PathVariable int userId) {
        return prizeDao.getPrizesByWinnerUserId(userId);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(path = "/prize", method = RequestMethod.POST)
    public ResponseEntity<Prize> createPrize(@RequestBody Prize prize) {
        if (prize == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Prize Not Found :(");
        } else {
            Prize createdPrize = prizeDao.createPrize(prize);
            return new ResponseEntity<>(createdPrize, HttpStatus.CREATED);
        }
    }
    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(path = "/prize/{id}", method = RequestMethod.PUT)
    public boolean editPrize(@RequestBody Prize prize,@PathVariable Integer id) {
        if (id == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID is required");
        }
        Prize existingPrize = prizeDao.getPrizeById(id);
        if (existingPrize == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Prize information not found");
        } else {
            return prizeDao.editPrize(prize, id);
        }
    }
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RequestMapping(path = "/prize/{id}", method = RequestMethod.DELETE)
    public boolean deletePrizeById(@PathVariable Integer id) {
        Prize prize = prizeDao.getPrizeById(id);
        if(prize == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The prize with id " + id + " does not exist.");
        }
        return prizeDao.deletePrizeById(id);
    }
}
