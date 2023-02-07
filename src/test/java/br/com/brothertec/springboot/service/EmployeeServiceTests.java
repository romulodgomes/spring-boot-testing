package br.com.brothertec.springboot.service;

import br.com.brothertec.springboot.exception.ResourceNotFoundException;
import br.com.brothertec.springboot.model.Employee;
import br.com.brothertec.springboot.repository.EmployeeRepository;
import br.com.brothertec.springboot.service.impl.EmployeeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTests {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private Employee employee;

    @BeforeEach
    public void setup() {
//        employeeRepository = Mockito.mock(EmployeeRepository.class);
//        employeeService = new EmployeeServiceImpl(employeeRepository);

        employee = Employee.builder()
                .firstName("Romulo")
                .lastName("Gomes")
                .email("romulo@teste.com")
                .build();
    }

    @DisplayName("Test saveEmployee method")
    @Test
    public void givenEmployeeObject_whenSaveEmployee_thenReturnEmployeeObject() {
        //given - precondition or setup
        given(employeeRepository.findByEmail(employee.getEmail())).willReturn(Optional.empty());

        given(employeeRepository.save(employee)).willReturn(employee);

        System.out.println(employeeRepository);
        System.out.println(employeeService);
        //when - action or the behavior that we are going test
        Employee savedEmployee = employeeService.saveEmployee(employee);
        System.out.println(savedEmployee);

        //then - verify the output
        assertThat(savedEmployee).isNotNull();

    }

    @DisplayName("Test saveEmployee already exists ")
    @Test
    public void givenEmployeeSaved_whenSaveEmployee_thenThrowsResourceNotFoundException() {
        //given - precondition or setup
        given(employeeRepository.findByEmail(employee.getEmail())).willReturn(Optional.of(employee));

        System.out.println(employeeRepository);
        System.out.println(employeeService);
        //when - action or the behavior that we are going test
        assertThrows(ResourceNotFoundException.class, () -> {
            employeeService.saveEmployee(employee);
        });

        //then - verify the output
        verify(employeeRepository, never()).save(any(Employee.class));
    }

    @DisplayName("Test getAllEmployees method")
    @Test
    public void givenEmployeesList_whenGetAllEmployeesEmployee_thenReturnEmployeesList() {
        //given - precondition or setup
        var employee1 = Employee.builder()
                .id(2l)
                .firstName("Tony")
                .lastName("Stark")
                .email("toony@teste.com")
                .build();

        given(employeeRepository.findAll()).willReturn(List.of(employee, employee1));

        //when - action or the behavior that we are going test
        List<Employee> employeeList = employeeService.getAllEmployees();

        //then - verify the output
        assertThat(employeeList).isNotNull();
        assertThat(employeeList.size()).isEqualTo(2);
        assertThat(employeeList.get(0).getFirstName()).isEqualTo("Romulo");
        assertThat(employeeList.get(0).getLastName()).isEqualTo("Gomes");
        assertThat(employeeList.get(1).getFirstName()).isEqualTo("Tony");
    }

    @DisplayName("Test getAllEmployees method (negative scenario)")
    @Test
    public void givenEmptyEmployeesList_whenGetAllEmployeesEmployee_thenReturnEmployeesList() {
        //given - precondition or setup
        var employee1 = Employee.builder()
                .id(2l)
                .firstName("Tony")
                .lastName("Stark")
                .email("toony@teste.com")
                .build();

        given(employeeRepository.findAll()).willReturn(Collections.emptyList());

        //when - action or the behavior that we are going test
        List<Employee> employeeList = employeeService.getAllEmployees();

        //then - verify the output
        assertThat(employeeList).isEmpty();
        assertThat(employeeList.size()).isEqualTo(0);
    }

    @DisplayName("Get by id")
    @Test
    public void givenEmployeeId_whenGetEmployeeById_thenReturnEmployeeObject() {
        //given - precondition or setup
        given(employeeRepository.findById(any())).willReturn(Optional.of(employee));

        //when - action or the behavior that we are going test
        Employee savedEmployee = employeeService.getEmployeeById(1l).get();

        //then - verify the output
        assertThat(savedEmployee).isNotNull();
    }


    @DisplayName("updateEmployee method")
    @Test
    public void givenEmployeeObject_whenUpdateEmployee_thenReturnEmployeeObject() {
        //given - precondition or setup
        given(employeeRepository.save(employee)).willReturn(employee);

        //when - action or the behavior that we are going test
        employee.setEmail("rominho@gmail.com");
        employee.setFirstName("Rominho");
        Employee updateEmployee = employeeService.updateEmployee(employee);

        //then - verify the output
        assertThat(updateEmployee).isNotNull();
        assertThat(updateEmployee.getEmail()).isEqualTo("rominho@gmail.com");
        assertThat(updateEmployee.getFirstName()).isEqualTo("Rominho");
    }

    @DisplayName("deleteEmployee method")
    @Test
    public void givenEmployeeID_whenDeleteEmployee_thenNothing() {
        //given - precondition or setup
        long employeeId = 1L;
        willDoNothing().given(employeeRepository).deleteById(employeeId);

        //when - action or the behavior that we are going test
        employeeService.deleteEmployee(employeeId);

        //then - verify the output
        verify(employeeRepository, times(1)).deleteById(employeeId);

    }

}  
