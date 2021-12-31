package com.etiya.rentACar.business.constants.messages;

import com.etiya.rentACar.business.abstracts.MessageService;
import org.springframework.core.env.Environment;

public class Messages {

    private Environment environment;
    private MessageService messageService;

    public Messages(Environment environment, MessageService messageService) {
        this.environment = environment;
        this.messageService = messageService;
    }

    public static String addAdditionalService = "AddAdditionalService";
    public static String deleteAdditionalService = "DeleteAdditionalService";
    public static String updateAdditionalService = "UpdateAdditionalService";
    public static String existingServiceName = "ExistingServiceName";
    public static String serviceIdDoesNotExist = "ServiceIdDoesNotExist";

    public static String addBrand = "AddBrand";
    public static String deleteBrand = "DeleteBrand";
    public static String updateBrand = "UpdateBrand";
    public static String brandDuplicationError = "BrandDuplicationError";
    public static String brandIdNotFound = "BrandIdNotFound";

    public static String addCarDamage = "AddCarDamage";
    public static String deleteCarDamage = "DeleteCarDamage";
    public static String updateCarDamage = "UpdateCarDamage";
    public static String carDamageNotFound = "CarDamageNotFound";
    public static String carDamageIdNotFound = "CarDamageIdNotFound";

    public static String addCarImage = "AddCarImage";
    public static String deleteCarImage = "DeleteCarImage";
    public static String updateCarImage = "UpdateCarImage";
    public static String upperImageLimit = "UpperImageLimit";
    public static String noImage = "NoImage";

    public static String addCar = "AddCar";
    public static String deleteCar = "DeleteCar";
    public static String updateCar = "UpdateCar";
    public static String carNotFound = "CarNotFound";
    public static String invalidModelYearFormat = "InvalidModelYearFormat";
    public static String carListIsEmpty = "CarListIsEmpty";

    public static String deleteUser = "DeleteUser";
    public static String updateUser = "UpdateUser";
    public static String emailDuplicate = "EmailDuplicate";
    public static String userDoesNotExist = "UserDoesNotExist";

    public static String addColor = "AddColor";
    public static String deleteColor = "DeleteColor";
    public static String updateColor = "UpdateColor";
    public static String colorDuplicationError = "ColorDuplicationError";
    public static String colorIdNotFound = "ColorIdNotFound";

    public static String addCorporateCustomer = "AddCorporateCustomer";
    public static String deleteCorporateCustomer = "DeleteCorporateCustomer";
    public static String updateCorporateCustomer = "UpdateCorporateCustomer";
    public static String invalidTaxNumberFormat = "InvalidTaxNumberFormat";

    public static String addCreditCard = "AddCreditCard";
    public static String deleteCreditCard = "DeleteCreditCard";
    public static String updateCreditCard = "UpdateCreditCard";
    public static String invalidCardNumberFormat = "InvalidCardNumberFormat";
    public static String invalidCvcFormat = "InvalidCvcFormat";
    public static String cardIdDoesNotExist = "CardIdDoesNotExist";

    public static String wrongInput = "WrongInput";
    public static String argumentTypeMismatch = "ArgumentTypeMismatch";
    public static String validationErrors = "ValidationErrors";
    public static String noSuchElementException = "NoSuchElementException";


    public static String findexPointIsNotEnough = "FindexPointIsNotEnough";
    public static String paymentUnsuccessful = "PaymentUnsuccessful";

    public static String addIndividualCustomer = "AddIndividualCustomer";
    public static String deleteIndividualCustomer = "DeleteIndividualCustomer";
    public static String updateIndividualCustomer = "UpdateIndividualCustomer";

    public static String loginSuccessful = "LoginSuccessful";
    public static String wrongPassword = "WrongPassword";
    public static String wrongEmail = "WrongEmail";
    public static String passwordConfirmationError = "PasswordConfirmationError";

    public static String addMaintenance = "AddMaintenance";
    public static String deleteMaintenance = "DeleteMaintenance";
    public static String updateMaintenance = "UpdateMaintenance";
    public static String carIsOnMaintenance = "CarIsOnMaintenance";
    public static String maintenanceCarIsOnRental = "MaintenanceCarIsOnRental";
    public static String maintenanceIdNotFound = "MaintenanceIdNotFound";

    public static String addRental = "AddRental";
    public static String deleteRental = "DeleteRental";
    public static String updateRental = "UpdateRental";
    public static String updateAndCreateBill= "UpdateAndCreateBill";
    public static String rentalCarIsOnRental = "RentalCarIsOnRental";
    public static String additionalServiceDeclaration = "AdditionalServiceDeclaration";
    public static String dateAccordance = "DateAccordance";
    public static String rentalIdDoesNotExist = "RentalIdDoesNotExist";
    public static String rentalAlreadyCreated = "RentalAlreadyCreated";
    public static String invalidReturnKilometer = "InvalidReturnKilometer";

    public static String addRentingBill = "AddRentingBill";
    public static String deleteRentingBill = "DeleteRentingBill";
    public static String updateRentingBill = "UpdateRentingBill";
    public static String rentingBillIdDoesNotExist = "RentingBillIdDoesNotExist";

    public static String addLanguage = "AddLanguage";
    public static String deleteLanguage = "DeleteLanguage";
    public static String updateLanguage = "UpdateLanguage";

    public static String addMessageKey = "AddMessageKey";
    public static String deleteMessageKey = "DeleteMessageKey";
    public static String updateMessageKey = "UpdateMessageKey";

    public static String addMessage = "AddMessage";
    public static String deleteMessage = "DeleteMessage";
    public static String updateMessage = "UpdateMessage";

    public static String addDemandedAdditional="AddDemandedAdditional";
    public static String deleteDemandedAdditional="DeleteDemandedAdditional";
    public static String updateDemandedAdditional= "UpdateDemandedAdditional";
    public static String demandedAdditionalDoesNotExist = "DemandedAdditionalDoesNotExist";

}
