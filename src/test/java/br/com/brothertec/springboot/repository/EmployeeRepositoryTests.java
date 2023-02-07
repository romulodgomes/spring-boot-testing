package br.com.brothertec.springboot.repository;

import br.com.brothertec.springboot.model.Employee;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

@DataJpaTest
public class EmployeeRepositoryTests {

    @Autowired
    private EmployeeRepository employeeRepository;
    private Employee employee;

    @BeforeEach
    public void setup() {
        //given - precondition or setup
        employee = Employee.builder()
                .firstName("Romulo")
                .lastName("Gomes")
                .email("romulo@teste.com")
                .build();
    }

    @DisplayName("JUnit test for save employee operation")
    @Test
    public void givenEmployeeObject_whenSave_thenReturnSavedEmployee() {
        //given - precondition or setup


        //when - action or the behavior that we are going test
        Employee savedEmployee = employeeRepository.save(employee);

        //then - verify the output
        assertThat(savedEmployee).isNotNull();
        assertThat(savedEmployee.getId()).isGreaterThan(0);
    }

    @DisplayName("Get all the employess operation")
    @Test
    public void givenEmployeesList_whenFindAll_thenEmployeesList() {
        //given - precondition or setup
//        var employee1 = Employee.builder()
//                .firstName("Romulo")
//                .lastName("Gomes")
//                .email("romulo@teste.com")
//                .build();

        var employee2 = Employee.builder()
                .firstName("John")
                .lastName("Cena")
                .email("cena@teste.com")
                .build();

        employeeRepository.save(employee);
        employeeRepository.save(employee2);

        //when - action or the behavior that we are going test
        var employeeList = employeeRepository.findAll();

        //then - verify the output
        assertThat(employeeList).isNotNull();
        assertThat(employeeList.size()).isEqualTo(2);

    }

    @DisplayName("Get employee by id operation")
    @Test
    public void givenEmployeeId_whenFindById_thenReturnEmployee() {
        //given - precondition or setup
//        var employee = Employee.builder()
//                .firstName("Romulo")
//                .lastName("Gomes")
//                .email("romulo@teste.com")
//                .build();

        employeeRepository.save(employee);
        Long id = employee.getId();

        //when - action or the behavior that we are going test
        Employee employeeDB = employeeRepository.getReferenceById(id);

        //then - verify the output
        assertThat(employeeDB).isNotNull();
    }

    @DisplayName("Get emloyee by email operation")
    @Test
    public void givenSavedEmployeeEmail_whenFindByEmail_thenReturnEmployeeObject() {
        //given - precondition or setup
//        var employee = Employee.builder()
//                .firstName("Romulo")
//                .lastName("Gomes")
//                .email("romulo@teste.com")
//                .build();

        employeeRepository.save(employee);
        String email = employee.getEmail();

        //when - action or the behavior that we are going test
        Employee employeeDB = employeeRepository.findByEmail(email).get();

        //then - verify the output
        assertThat(employeeDB).isNotNull();
    }

    @DisplayName("Update employee")
    @Test
    public void givenEmployeeObject_whenUpdateEmployee_thenReturnUpdatedEmployee() {
        //given - precondition or setup
//        var employee = Employee.builder()
//                .firstName("Romulo")
//                .lastName("Gomes")
//                .email("romulo@teste.com")
//                .build();
        employeeRepository.save(employee);

        //when - action or the behavior that we are going test
        Employee employeeDB = employeeRepository.findById(employee.getId()).get();
        employeeDB.setEmail("new_email@teste.com");
        employeeDB.setFirstName("Rominho");

        Employee updatedEmployee = employeeRepository.findById(employee.getId()).get();

        //then - verify the output
        assertThat(updatedEmployee.getEmail()).isEqualTo("new_email@teste.com");
        assertThat(updatedEmployee.getFirstName()).isEqualTo("Rominho");
    }

    @DisplayName("Delete employee")
    @Test
    public void givenEmployeeObject_whenDeleteEmployee_thenRemoveEmployee() {
        //given - precondition or setup
//        var employee = Employee.builder()
//                .firstName("Romulo")
//                .lastName("Gomes")
//                .email("romulo@teste.com")
//                .build();
        employeeRepository.save(employee);

        //when - action or the behavior that we are going test
        employeeRepository.delete(employee);
        Optional<Employee> employeeOptional = employeeRepository.findById(employee.getId());

        //then - verify the output
        assertThat(employeeOptional).isEmpty();
    }

    @DisplayName("Custom JPQL query with index")
    @Test
    public void givenEmployeeFirstNameAndLastName_whenFindByJPQL_thenReturnEmployeeObject() {
        //given - precondition or setup
//        var employee = Employee.builder()
//                .firstName("Romulo")
//                .lastName("Gomes")
//                .email("romulo@teste.com")
//                .build();
        employeeRepository.save(employee);

        //when - action or the behavior that we are going test
        var savedEmployee = employeeRepository.findByJPQL("Romulo", "Gomes");

        //then - verify the output
        assertThat(savedEmployee).isNotNull();
    }

    @DisplayName("Custom JPQL query with named params")
    @Test
    public void givenEmployeeFirstNameAndLastName_whenFindByJPQLNamedParams_thenReturnEmployeeObject() {
        //given - precondition or setup
//        var employee = Employee.builder()
//                .firstName("Romulo")
//                .lastName("Gomes")
//                .email("romulo@teste.com")
//                .build();
        employeeRepository.save(employee);

        //when - action or the behavior that we are going test
        var savedEmployee = employeeRepository.findByJPQLNamedParams("Romulo", "Gomes");

        //then - verify the output
        assertThat(savedEmployee).isNotNull();
    }

    @DisplayName("Custom Native query with index param")
    @Test
    public void givenEmployeeFirstNameAndLastName_whenFindByNativeSQL_thenReturnEmployeeObject() {
        //given - precondition or setup
//        var employee = Employee.builder()
//                .firstName("Romulo")
//                .lastName("Gomes")
//                .email("romulo@teste.com")
//                .build();
        employeeRepository.save(employee);

        //when - action or the behavior that we are going test
        var savedEmployee = employeeRepository.findByNativeSQL("Romulo", "Gomes");

        //then - verify the output
        assertThat(savedEmployee).isNotNull();
    }

    @DisplayName("Custom Native query with named param")
    @Test
    public void givenEmployeeFirstNameAndLastName_whenFindByNativeSQLNamedParams_thenReturnEmployeeObject() {
        //given - precondition or setup
//        var employee = Employee.builder()
//                .firstName("Romulo")
//                .lastName("Gomes")
//                .email("romulo@teste.com")
//                .build();
        employeeRepository.save(employee);

        //when - action or the behavior that we are going test
        var savedEmployee = employeeRepository.findByNativeSQLNamedParams("Romulo", "Gomes");

        //then - verify the output
        assertThat(savedEmployee).isNotNull();
    }
}
