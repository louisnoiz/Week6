package com.example.week6.contoller;

import com.example.week6.pojo.Wizard;
import com.example.week6.repository.WizardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.aggregation.ConvertOperators;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class WizardController {
    @Autowired
    private WizardService wizardService;

    @RequestMapping(value = "/wizards", method = RequestMethod.GET)
    public ResponseEntity<?> getWizard(){
        List<Wizard> wizards = wizardService.retrieveWizard();
        return ResponseEntity.ok(wizards);
    }

    @RequestMapping(value = "/addWizard", method = RequestMethod.POST)
    public ResponseEntity<?> createWizard(@RequestBody Wizard wizard){
        Wizard n = wizardService.createWizard(wizard);
        return ResponseEntity.ok(n);
    }

    @RequestMapping(value = "/deleteWizard", method = RequestMethod.POST)
    public ResponseEntity<?> deleteWizard(@RequestBody String id){
        Wizard n = wizardService.retriveWizardById(id);
        boolean status = wizardService.deleteWizard(n);
        return ResponseEntity.ok(status);
    }

    @RequestMapping(value = "/updateWizard", method = RequestMethod.POST)
    public ResponseEntity<?> updateWizard(@RequestBody Wizard wizard){
        Wizard n = wizardService.updateWizard(wizard);
        return ResponseEntity.ok(n);

    }
}
