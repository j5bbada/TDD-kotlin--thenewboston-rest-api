package tv.codealong.tutorials.springboot.thenewboston.controller

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.*
import tv.codealong.tutorials.springboot.thenewboston.model.Bank

@SpringBootTest
@AutoConfigureMockMvc
internal class BankControllerTest @Autowired constructor(
      val mockMvc: MockMvc,
      val objectMapper: ObjectMapper
) {

    val baseUrl = "/api/banks"

    @Nested
    @DisplayName("GET /api/banks")
    @TestInstance(Lifecycle.PER_CLASS)
    inner class GetBanks {

        @Test
        fun `should return all banks`() {
            // Given
            // When
            // Then
            mockMvc.get("$baseUrl")
                    .andDo { print() }
                    .andExpect {
                        status { isOk() }
                        content { contentType(MediaType.APPLICATION_JSON) }
                        jsonPath("$[0].accountNumber") {
                            value("1234")
                        }
                    }

        }

    }

    @Nested
    @DisplayName("GET /api/bank")
    @TestInstance(Lifecycle.PER_CLASS)
    inner class GetBank {
        @Test
        fun `should return bank with thr given account number`() {
            // Given
            val accountNumber = 1234

            // When
            // Then
            mockMvc.get("$baseUrl/$accountNumber")
                    .andDo { print() }
                    .andExpect {
                        status { isOk() }
                        content { contentType(MediaType.APPLICATION_JSON) }
                        jsonPath("$.trust") { value("3.14")}
                        jsonPath("$.transactionFee") { value("7")}
                    }
        }
        
        @Test
        fun `should return Not Found id the account number does not exist`() {
            // Given
            val accountNumber = "dose_not_exist"
        
            // When
            // Then
            mockMvc.get("$baseUrl/$accountNumber")
                    .andDo { print() }
                    .andExpect {
                        status { isNotFound() }
                    }
        }

    }
    
    
    @Nested
    @DisplayName("POST /api/banks")
    @TestInstance(Lifecycle.PER_CLASS)
    inner class PostNewBank {
        @Test
        fun `should add the new bank`() {
            // Given
            val newBank = Bank("abc123", 4.12,1)

            // When
            val performPost = mockMvc.post(baseUrl) {
                contentType =  MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(newBank)
            }

            // Then
            performPost
                .andDo { print() }
                .andExpect {
                    status { isCreated() }
                    content {
                        contentType(MediaType.APPLICATION_JSON)
                        json(objectMapper.writeValueAsString(newBank))
                    }
//                    jsonPath("$.accountNumber") { value("abc123")}
//                    jsonPath("$.trust") { value("4.12")}
//                    jsonPath("$.transactionFee") { value("1")}
                }

            mockMvc.get("$baseUrl/${newBank.accountNumber}")
                    .andExpect { content { json(objectMapper.writeValueAsString(newBank)) } }
        }
        
        @Test
        fun `should return BAD REQUEST id bank with given account number alerady exists`() {
            // Given
            val invalidBank = Bank("1234", 1.0,1)
        
            // When
            val performPost = mockMvc.post(baseUrl) {
                contentType =  MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(invalidBank)
            }
        
            // Then
            performPost
                .andDo { print() }
                .andExpect {
                    status { isBadRequest() }
                }

        }
    
    }

    @Nested
    @DisplayName("PATCH /api/banks")
    @TestInstance(Lifecycle.PER_CLASS)
    inner class PatchBanks {

        @Test
        fun `should update an existing bank `() {
            // Given
            val updateBank = Bank("1234", 1.0,1)

            // When
            val performPatch = mockMvc.patch(baseUrl) {
                contentType =  MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(updateBank)
            }

            // Then
            performPatch
                .andDo { print() }
                .andExpect {
                    status { isOk() }
                    content {
                        contentType(MediaType.APPLICATION_JSON)
                    }
                }

            mockMvc.get("$baseUrl/${updateBank.accountNumber}")
                    .andExpect { content { json(objectMapper.writeValueAsString(updateBank)) } }

        }
        
        @Test
        fun `should return BAD REQUEST if no bank given account number exists`() {
            // Given
            val invalidBank = Bank("dios_notexist", 1.0, 1)
        
            // When
            val performPatch = mockMvc.patch(baseUrl) {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(invalidBank)
            }

            // Then
            performPatch
                    .andDo {  print() }
                    .andExpect { status { isNotFound() } }

        }

    }





}