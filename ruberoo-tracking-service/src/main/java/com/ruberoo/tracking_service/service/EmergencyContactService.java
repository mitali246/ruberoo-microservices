package com.ruberoo.tracking_service.service;

import com.ruberoo.tracking_service.entity.EmergencyContact;
import com.ruberoo.tracking_service.repository.EmergencyContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmergencyContactService {

    @Autowired
    private EmergencyContactRepository contactRepository;

    public EmergencyContact createContact(EmergencyContact contact) {
        return contactRepository.save(contact);
    }

    public List<EmergencyContact> getAllContacts() {
        return contactRepository.findAll();
    }

    public EmergencyContact getContactById(Long id) {
        return contactRepository.findById(id).orElse(null);
    }

    public EmergencyContact updateContact(Long id, EmergencyContact contactDetails) {
        EmergencyContact contact = contactRepository.findById(id).orElse(null);
        if (contact != null) {
            contact.setUserId(contactDetails.getUserId());
            contact.setContactName(contactDetails.getContactName());
            contact.setContactNumber(contactDetails.getContactNumber());
            return contactRepository.save(contact);
        }
        return null;
    }

    public void deleteContact(Long id) {
        contactRepository.deleteById(id);
    }
}
