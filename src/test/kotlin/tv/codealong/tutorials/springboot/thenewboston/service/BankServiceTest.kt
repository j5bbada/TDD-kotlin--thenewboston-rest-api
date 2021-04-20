package tv.codealong.tutorials.springboot.thenewboston.service

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import tv.codealong.tutorials.springboot.thenewboston.datasource.BankDataSource

internal class BankServiceTest {

    private val dataSource:BankDataSource = mockk(relaxed = true)

    private val bankService = BankService(dataSource)

    @Test
    fun `should call ins data source to retrieve banks`() {
        // Given
//        every { dataSource.retrieveBanks() } returns emptyList()

        // When
        bankService.getBanks()

        // Then
        verify(exactly = 1) { dataSource.retrieveBanks() }
        
    }
}