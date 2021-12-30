package com.etiya.rentACar.business.concretes;

import com.etiya.rentACar.business.abstracts.LanguageService;
import com.etiya.rentACar.business.abstracts.MessageService;
import com.etiya.rentACar.business.request.languageRequests.CreateLanguageRequest;
import com.etiya.rentACar.business.request.languageRequests.DeleteLanguageRequest;
import com.etiya.rentACar.business.request.languageRequests.UpdateLanguageRequest;
import com.etiya.rentACar.core.utilities.mapping.ModelMapperService;
import com.etiya.rentACar.core.utilities.results.Result;
import com.etiya.rentACar.core.utilities.results.SuccessResult;
import com.etiya.rentACar.dataAccess.abstracts.LanguageDao;
import com.etiya.rentACar.entities.multipleLanguageMessages.Language;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import com.etiya.rentACar.business.constants.messages.Messages;

@Service
public class LanguageManager implements LanguageService {
    private LanguageDao languageDao;
    private ModelMapperService modelMapperService;
    private MessageService messageService;

    @Autowired
    public LanguageManager(LanguageDao languageDao, ModelMapperService modelMapperService, MessageService messageService) {
        this.languageDao = languageDao;
        this.modelMapperService = modelMapperService;
        this.messageService = messageService;
    }

    @Override
    public Result save(CreateLanguageRequest createLanguageRequest) {
        Language language = modelMapperService.forRequest().map(createLanguageRequest, Language.class);
        this.languageDao.save(language);
        return new SuccessResult(messageService.getMessage(Messages.addLanguage));
    }

    @Override
    public Result delete(DeleteLanguageRequest deleteLanguageRequest) {
        Language language = modelMapperService.forRequest().map(deleteLanguageRequest, Language.class);
        this.languageDao.delete(language);
        return new SuccessResult(messageService.getMessage(Messages.deleteLanguage));
    }

    @Override
    public Result update(UpdateLanguageRequest updateLanguageRequest) {
        Language language = modelMapperService.forRequest().map(updateLanguageRequest, Language.class);
        this.languageDao.save(language);
        return new SuccessResult(messageService.getMessage(Messages.updateLanguage));
    }
}
