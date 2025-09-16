package com.ruberoo.tracking_service.controller;

import com.ruberoo.tracking_service.entity.EmergencyContact;
import com.ruberoo.tracking_service.service.EmergencyContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/emergency-contacts")
public class EmergencyContactController {

    @Autowired
    private EmergencyContactService contactService;

    @PostMapping
    public EmergencyContact createContact(@RequestBody EmergencyContact contact) {
        return contactService.createContact(contact);
    }

    @GetMapping
    public List<EmergencyContact> getAllContacts() {
        return contactService.getAllContacts();
    }

    @GetMapping("/{id}")
    public EmergencyContact getContactById(@PathVariable Long id) {
        return contactService.getContactById(id);
    }

    @PutMapping("/{id}")
    public EmergencyContact updateContact(@PathVariable Long id, @RequestBody EmergencyContact contactDetails) {
        return contactService.updateContact(id, contactDetails);
    }

    @DeleteMapping("/{id}")
    public void deleteContact(@PathVariable Long id) {
        contactService.deleteContact(id);
    }
}
