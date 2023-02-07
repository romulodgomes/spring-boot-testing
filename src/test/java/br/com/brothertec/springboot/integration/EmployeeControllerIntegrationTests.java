package br.com.brothertec.springboot.integration;

import br.com.brothertec.springboot.model.Employee;
import br.com.brothertec.springboot.repository.EmployeeRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class EmployeeControllerIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup(){
        employeeRepository.deleteAll();
    }

    @DisplayName("Create Employee")
    @Test
    public void givenEmployeeObject_whenCreateEmployee_thenReturnSavedEmployee() throws Exception {
        //given - precondition or setup
        Employee employee = Employee.builder()
                .firstName("Romulo")
                .lastName("Gomes")
                .email("romulo@teste.com")
                .build();

        //when - action or the behavior that we are going test
        ResultActions response = mockMvc.perform(post("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)));

        //then - verify the output
        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName",
                        is(employee.getFirstName())))
                .andExpect((jsonPath("$.lastName",
                        is(employee.getLastName()))))
                .andExpect((jsonPath("$.email",
                        is(employee.getEmail()))));
    }

    @DisplayName("Get All employees")
    @Test
    public void givenListOfEmployees_whenGetAllEmployees_thenReturnEmployeesList() throws Exception {
        //given - precondition or setup
        List<Employee> listOfEmployees = List.of(
                Employee.builder()
                        .id(1l)
                        .firstName("Romulo")
                        .lastName("Gomes")
                        .email("romulo@teste.com")
                        .build(),
                Employee.builder()
                        .id(2l)
                        .firstName("Nayra")
                        .lastName("Cruz")
                        .email("nayra@teste.com")
                        .build()
        );
        employeeRepository.saveAll(listOfEmployees);

        //when - action or the behavior that we are going test
        ResultActions response = mockMvc.perform(get("/api/employees"));

        //then - verify the output
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()",
                        is(listOfEmployees.size())));

    }

    @DisplayName("Get employee by id - positive")
    @Test
    public void givenEmployeeId_whenGetEmployeeById_thenReturnEmployeeObject() throws Exception {
        //given - precondition or setup
        var employee = Employee.builder()
                .firstName("Romulo")
                .lastName("Gomes")
                .email("romulo@teste.com")
                .build();

        employeeRepository.save(employee);

        //when - action or the behavior that we are going test
        ResultActions response = mockMvc.perform(get("/api/employees/{id}", employee.getId()));

        //then - verify the output
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is(employee.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(employee.getLastName())))
                .andExpect(jsonPath("$.email", is(employee.getEmail())));
    }

    @DisplayName("Get employee by id - negative")
    @Test
    public void givenEmployeeId_whenGetEmployeeById_thenReturnEmpty() throws Exception {
        //given - precondition or setup
        var employee = Employee.builder()
                .firstName("Romulo")
                .lastName("Gomes")
                .email("romulo@teste.com")
                .build();

        employeeRepository.save(employee);

        //when - action or the behavior that we are going test
        ResultActions response = mockMvc.perform(get("/api/employees/{id}", employee.getId()-1));

        //then - verify the output
        response.andDo(print())
                .andExpect(status().isNotFound());
    }

    @DisplayName("Update employee - positive")
    @Test
    public void givenUpdatedEmployee_whenUpdateEmployee_thenReturnUpdateEmployeeObject() throws Exception {
        //given - precondition or setup
        var employee = Employee.builder()
                .firstName("Romulo")
                .lastName("Gomes")
                .email("romulo@teste.com")
                .build();

        employeeRepository.save(employee);

        var updatedEmployee = Employee.builder()
                .firstName("Rominho")
                .lastName("Gomez")
                .email("rominho@teste.com")
                .build();


        //when - action or the behavior that we are going test
        ResultActions response = mockMvc.perform(put("/api/employees/{id}", employee.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEmployee)));

        //then - verify the output
        response.andExpect(status().isOk()).
                andDo(print())
                .andExpect(jsonPath("$.firstName", is(updatedEmployee.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(updatedEmployee.getLastName())))
                .andExpect(jsonPath("$.email", is(updatedEmployee.getEmail())));
    }

    @DisplayName("Update employee - negative")
    @Test
    public void givenUpdatedEmployeeNotExists_whenUpdateEmployee_thenReturnEmpty() throws Exception {
        //given - precondition or setup

        var employee = Employee.builder()
                .firstName("Romulo")
                .lastName("Gomes")
                .email("romulo@teste.com")
                .build();

        employeeRepository.save(employee);

        //when - action or the behavior that we are going test
        ResultActions response = mockMvc.perform(put("/api/employees/{id}", employee.getId()+1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)));

        //then - verify the output
        response.andExpect(status().isNotFound()).
                andDo(print());
    }

    @DisplayName("Delete employee")
    @Test
    public void givenEmployeeId_whenDeleteEmployee_thenRetun200() throws Exception {
        //given - precondition or setup
        var employee = Employee.builder()
                .firstName("Romulo")
                .lastName("Gomes")
                .email("romulo@teste.com")
                .build();

        employeeRepository.save(employee);

        //when - action or the behavior that we are going test
        ResultActions response = mockMvc.perform(delete("/api/employees/{id}", employee.getId()));

        //then - verify the output
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().string("Employee deleted successfully!"));
    }

}
