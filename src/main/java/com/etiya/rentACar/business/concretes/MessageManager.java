package com.etiya.rentACar.business.concretes;

import com.etiya.rentACar.business.abstracts.LanguageService;
import com.etiya.rentACar.business.abstracts.MessageService;
import com.etiya.rentACar.business.constants.messages.Messages;
import com.etiya.rentACar.business.request.messageRequests.CreateMessageRequest;
import com.etiya.rentACar.business.request.messageRequests.DeleteMessageRequest;
import com.etiya.rentACar.business.request.messageRequests.UpdateMessageRequest;
import com.etiya.rentACar.core.utilities.mapping.ModelMapperService;
import com.etiya.rentACar.core.utilities.results.Result;
import com.etiya.rentACar.core.utilities.results.SuccessResult;
import com.etiya.rentACar.dataAccess.abstracts.MessageDao;
import com.etiya.rentACar.entities.multipleLanguageMessages.Language;
import com.etiya.rentACar.entities.multipleLanguageMessages.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Service
public class MessageManager implements MessageService {

    private MessageDao messageDao;
    private ModelMapperService modelMapperService;
    private MessageService messageService;
    private Environment environment;
    private LanguageService languageService;


    @Autowired
    public MessageManager(MessageDao messageDao, ModelMapperService modelMapperService, @Lazy MessageService messageService, Environment environment,
                          @Lazy LanguageService languageService) {
        this.messageDao = messageDao;
        this.modelMapperService = modelMapperService;
        this.messageService=messageService;
        this.environment = environment;
        this.languageService = languageService;
    }

    @Override
    public Result save(CreateMessageRequest createMessageRequest) {
        Message message = modelMapperService.forRequest().map(createMessageRequest, Message.class);
        this.messageDao.save(message);
        return new SuccessResult(messageService.getMessage(Messages.addMessage));
    }

    @Override
    public Result delete(DeleteMessageRequest deleteMessageRequest) {
        Message message = modelMapperService.forRequest().map(deleteMessageRequest, Message.class);
        this.messageDao.delete(message);
        return new SuccessResult(messageService.getMessage(Messages.deleteMessage));
    }

    @Override
    public Result update(UpdateMessageRequest updateMessageRequest) {
        Message message = modelMapperService.forRequest().map(updateMessageRequest, Message.class);
        this.messageDao.save(message);
        return new SuccessResult(messageService.getMessage(Messages.updateMessage));
    }

    @Override
    public String getMessage(String messageKey) {
        int languageId = Integer.parseInt(environment.getProperty("language.id"));
        Language language = languageService.getByLanguageId(languageId);
        if (language == null){
            languageId = 1;
        }
        String message = this.messageDao.getMessage(languageId,messageKey);
        if (message==null){
            message = messageKey;
        }
        return message;
    }
}
