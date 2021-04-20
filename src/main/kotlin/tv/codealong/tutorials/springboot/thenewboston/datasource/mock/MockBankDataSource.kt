package tv.codealong.tutorials.springboot.thenewboston.datasource.mock

import org.springframework.stereotype.Repository
import tv.codealong.tutorials.springboot.thenewboston.datasource.BankDataSource
import tv.codealong.tutorials.springboot.thenewboston.model.Bank
import java.lang.IllegalArgumentException

@Repository
class MockBankDataSource: BankDataSource {
    val banks = mutableListOf(
            Bank("1234", 3.14, 7),
            Bank("1010", 17.0, 0),
            Bank("5678", 0.0, 100),
    )

    override fun retrieveBanks(): Collection<Bank> = banks

    override fun retrieveBank(accountNumber: String): Bank =
            banks.firstOrNull() { it.accountNumber == accountNumber }
                    ?: throw NoSuchElementException("Could not find a bank with account Number $accountNumber")

    override fun createBank(bank: Bank): Bank {
        if(banks.any { it.accountNumber == bank.accountNumber }) {
            throw IllegalArgumentException("Bank with account Number ${bank.accountNumber} already exists")
        }
        banks.add(bank)

        return bank
    }

    override fun updataBank(bank: Bank): Bank {
        val currentBank =  banks.firstOrNull() { it.accountNumber == bank.accountNumber }
                ?: throw NoSuchElementException("Could not find a bank with account Number $bank.accountNumber")
        banks.remove(currentBank)
        banks.add(bank)

        return bank
    }

}