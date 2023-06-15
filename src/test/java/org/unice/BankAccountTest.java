package org.unice;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import junit.framework.TestCase;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;
import org.unice.model.BankAccount;
import org.unice.model.Customer;
import org.unice.model.Operation;
import org.unice.service.BankAccountService;
import org.unice.service.CustomerService;
import org.unice.service.OperationService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class BankAccountTest extends TestCase{

    public void test1CreateBankAccount(){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            File file = Paths.get("src", "main", "resources", "accounts.json").toFile();
            List<BankAccount> bankAccounts = objectMapper
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .readValue(file, objectMapper.getTypeFactory().constructCollectionType(List.class, BankAccount.class));
            for (BankAccount bankAccount : bankAccounts){
                BankAccountService.delete(bankAccount.getId());
                BankAccountService.save(bankAccount);
                }
            }catch (IOException e){
                e.printStackTrace();
            }catch (Exception e){
                e.printStackTrace();
                throw new RuntimeException();
            }

            assertTrue(true);
        }

    public void test2GetAllBankAccounts() throws RuntimeException{
        try{
            List<BankAccount> bankAccounts = BankAccountService.getAll();
            assertEquals(200, bankAccounts.size());
        }catch (Exception e){
                System.out.println(e);
                e.printStackTrace();
                throw new RuntimeException();
            }
    }

    public void test3GetByID() throws RuntimeException{
        try{
            BankAccount bankAccount = BankAccountService.getById(17);
            assertEquals( "MU34 RXCZ 6149 3663 3178 8778 993L OE", bankAccount.getAccountNumber());
        }catch (Exception e){
                e.printStackTrace();
                throw new RuntimeException();
            }
    }

    public void test4GetByAccountNumber() throws RuntimeException{
        try{
            List<BankAccount> bankAccounts = BankAccountService.getByAccountNumber("BA20 5295 8284 1701 1432");
            assertEquals(1,bankAccounts.size());
            assertEquals(197, bankAccounts.get(0).getId());
        }catch (Exception e){
                e.printStackTrace();
                throw new RuntimeException();
            }

    }

    public void test5UpdateAll(){
        BankAccount previousBankAccount = BankAccountService.getById(10);
        BankAccountService.updateValueForAllCustomers("accountType", "Cherry");
        BankAccount currentOperation = BankAccountService.getById(10);
        assertNotSame(previousBankAccount, currentOperation);
        assertEquals("Cherry", currentOperation.getAccountType());

    }

    public void test6Delete() {
        BankAccountService.delete(13);
        assertEquals(199, BankAccountService.getAll().size());
    }

}
