package com.alexandergonzalez.libraryProject;

import com.alexandergonzalez.libraryProject.controllers.LoanController;
import com.alexandergonzalez.libraryProject.dto.loan.LoanDto;
import com.alexandergonzalez.libraryProject.factory.loanFactory.LoanFactory;
import com.alexandergonzalez.libraryProject.factory.loanFactory.LoanService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LoanControllerTests {


    @Mock
    private LoanFactory loanFactory;

    @Mock
    private LoanService loanService;

    @InjectMocks
    private LoanController loanController;


    @BeforeEach
    void setUp() {
        when(loanFactory.getLoanService()).thenReturn(loanService);
    }

    @Test
    void testSaveLoan_Success() {
        // Mock data
        LoanDto loanDto = new LoanDto("456", "789", LocalDateTime.now());
        LoanDto savedLoan = new LoanDto("456", "789", LocalDateTime.now());

        // Configuramos el comportamiento esperado del servicio
        when(loanService.saveLoan(loanDto)).thenReturn(savedLoan);

        // Ejecutamos el método
        ResponseEntity<Object> response = (ResponseEntity<Object>) loanController.saveLoan(loanDto);

        // Verificamos el resultado
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Map<String, Object> responseBody = (HashMap<String, Object>) response.getBody();
        assertEquals(savedLoan, responseBody.get("Libro prestado"));

        // Verificamos que el servicio fue invocado correctamente
        verify(loanService, times(1)).saveLoan(loanDto);
    }

    @Test
    void testSaveLoan_BadRequest() {
        // Mock data
        LoanDto savedLoan = new LoanDto("456", "789", LocalDateTime.now());

        // Configuramos el comportamiento esperado del servicio
        when(loanService.saveLoan(savedLoan)).thenReturn(null);

        // Ejecutamos el método
        ResponseEntity<Object> response = (ResponseEntity<Object>) loanController.saveLoan(savedLoan);

        // Verificamos el resultado
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Map<String, Object> responseBody = (HashMap<String, Object>) response.getBody();
        assertEquals("No se pudo realizar el prestamo, verifica que el libro esté disponible", responseBody.get("message"));

        // Verificamos que el servicio fue invocado correctamente
        verify(loanService, times(1)).saveLoan(savedLoan);
    }

    @Test
    void testGetAllLoans_Success() {
        // Mock data
        List<LoanDto> loanDtos = List.of(
                new LoanDto("456", "789", LocalDateTime.now()),
                new LoanDto("457", "789", LocalDateTime.now())
        );

        // Configuramos el comportamiento esperado del servicio
        when(loanService.getLoans()).thenReturn(loanDtos);

        // Ejecutamos el método
        ResponseEntity<Object> response = loanController.getAllLoans();

        // Verificamos el resultado
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Map<String, Object> responseBody = (HashMap<String, Object>) response.getBody();
        assertEquals(loanDtos, responseBody.get("Todos los préstamos encontrados"));

        // Verificamos que el servicio fue invocado correctamente
        verify(loanService, times(1)).getLoans();
    }

    @Test
    void testReturnBook_Success() {
        // Mock data
        String bookId = "789";
        LoanDto returnedLoan = new LoanDto("456", "789", LocalDateTime.now());

        // Configuramos el comportamiento esperado del servicio
        when(loanService.returnBook(bookId)).thenReturn(returnedLoan);

        // Ejecutamos el método
        ResponseEntity<Object> response = loanController.returnBook(bookId);

        // Verificamos el resultado
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Map<String, Object> responseBody = (HashMap<String, Object>) response.getBody();
        assertEquals(returnedLoan, responseBody.get("Libro devuelto"));

        // Verificamos que el servicio fue invocado correctamente
        verify(loanService, times(1)).returnBook(bookId);
    }

    @Test
    void testReturnBook_NotFound() {
        // Mock data
        String bookId = "789";

        // Configuramos el comportamiento esperado del servicio
        when(loanService.returnBook(bookId)).thenReturn(null);

        // Ejecutamos el método
        ResponseEntity<Object> response = loanController.returnBook(bookId);

        // Verificamos el resultado
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Map<String, Object> responseBody = (HashMap<String, Object>) response.getBody();
        assertEquals("No se encontró el préstamo para este libro.", responseBody.get("Error"));

        // Verificamos que el servicio fue invocado correctamente
        verify(loanService, times(1)).returnBook(bookId);
    }

    @Test
    void testReturnLoanByUser_Success() {
        // Mock data
        String userId = "456";
        List<LoanDto> returnedLoans = List.of(
                new LoanDto("456", "789", LocalDateTime.now()),
                new LoanDto("457", "789", LocalDateTime.now())
        );

        // Configuramos el comportamiento esperado del servicio
        when(loanService.getLoansByUserId(userId)).thenReturn(returnedLoans);

        // Ejecutamos el método
        ResponseEntity<Object> response = loanController.returnLoanByUser(userId);

        // Verificamos el resultado
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Map<String, Object> responseBody = (HashMap<String, Object>) response.getBody();
        assertEquals(returnedLoans, responseBody.get("Préstamos encontrados para este usuario"));

        // Verificamos que el servicio fue invocado correctamente
        verify(loanService, times(1)).getLoansByUserId(userId);
    }
}
