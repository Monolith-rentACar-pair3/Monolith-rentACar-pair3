package com.etiya.rentACar.dataAccess.abstracts;


import com.etiya.rentACar.entities.multipleLanguageMessages.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MessageDao extends JpaRepository<Message, Integer> {

    @Query(value = "select m.message from messages as m inner join message_keys as mk on mk.message_key_id=m.message_key_id" +
            " where m.language_id=?1 and mk.message_key = ?2", nativeQuery = true)
    String getMessage(int languageId, String messageKey);
}
