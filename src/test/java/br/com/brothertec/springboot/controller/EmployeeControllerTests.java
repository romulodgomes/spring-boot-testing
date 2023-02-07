package br.com.brothertec.springboot.controller;

import br.com.brothertec.springboot.model.Employee;
import br.com.brothertec.springboot.service.EmployeeService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
public class EmployeeControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

    @Autowired
    private ObjectMapper objectMapper;

    @DisplayName("Create Employee")
    @Test
    public void givenEmployeeObject_whenCreateEmployee_thenReturnSavedEmployee() throws Exception {
        //given - precondition or setup
        Employee employee = Employee.builder()
                .firstName("Romulo")
                .lastName("Gomes")
                .email("romulo@teste.com")
                .build();

        given(employeeService.saveEmployee(any(Employee.class)))
                .willAnswer(invocation -> invocation.getArgument(0));
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
        given(employeeService.getAllEmployees()).willReturn(listOfEmployees);

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
                .id(1l)
                .firstName("Romulo")
                .lastName("Gomes")
                .email("romulo@teste.com")
                .build();
        given(employeeService.getEmployeeById(employee.getId())).willReturn(Optional.of(employee));

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
        given(employeeService.getEmployeeById(2l)).willReturn(Optional.empty());

        //when - action or the behavior that we are going test
        ResultActions response = mockMvc.perform(get("/api/employees/{id}", 2l));

        //then - verify the output
        response.andDo(print())
                .andExpect(status().isNotFound());

    }

    @DisplayName("Update employee - positive")
    @Test
    public void givenUpdatedEmployee_whenUpdateEmployee_thenReturnUpdateEmployeeObject() throws Exception {
        //given - precondition or setup
        var employee = Employee.builder()
                .id(1l)
                .firstName("Romulo")
                .lastName("Gomes")
                .email("romulo@teste.com")
                .build();

        var updatedEmployee = Employee.builder()
                .id(1l)
                .firstName("Rominho")
                .lastName("Gomez")
                .email("rominho@teste.com")
                .build();

        given(employeeService.getEmployeeById(employee.getId())).willReturn(Optional.of(employee));
        given(employeeService.updateEmployee(any(Employee.class)))
                .willAnswer(invocationOnMock -> invocationOnMock.getArgument(0));

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

        var updatedEmployee = Employee.builder()
                .id(1l)
                .firstName("Rominho")
                .lastName("Gomez")
                .email("rominho@teste.com")
                .build();

        given(employeeService.getEmployeeById(updatedEmployee.getId())).willReturn(Optional.empty());

        //when - action or the behavior that we are going test
        ResultActions response = mockMvc.perform(put("/api/employees/{id}", 2l)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEmployee)));

        //then - verify the output
        response.andExpect(status().isNotFound()).
                andDo(print());
    }

    @DisplayName("Delete employee")
    @Test
    public void givenEmployeeId_whenDeleteEmployee_thenRetun200() throws Exception {
        //given - precondition or setup
        willDoNothing().given(employeeService).deleteEmployee(any(Long.class));

        //when - action or the behavior that we are going test
        ResultActions response = mockMvc.perform(delete("/api/employees/{id}", 1l));

        //then - verify the output
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().string("Employee deleted successfully!"));
    }

}
