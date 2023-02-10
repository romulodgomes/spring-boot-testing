package br.com.brothertec.springboot.bdd.steps;

import br.com.brothertec.springboot.model.Employee;
import br.com.brothertec.springboot.repository.EmployeeRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.Before;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Entao;
import io.cucumber.java.pt.Quando;
import org.assertj.core.api.Assertions;
import org.hamcrest.CoreMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

public class EmployeeSteps extends CucumberSpringIntegration {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Employee employee1;
    private Employee employee2;
    private ResultActions resultActions;

    @Before
    public void setup() {
        employeeRepository.deleteAll();
    }

    @Dado("um funcionario")
    public void um_funcionario() {
        employee1 = Employee.builder()
                .firstName("Everson")
                .lastName("Santos")
                .email("rhevs@teste.com")
                .build();
    }

    @Quando("enviar uma requisicao do tipo POST para o recurso {string}")
    public void enviar_uma_requisicao_do_tipo_post_para_o_recurso(String recurso) throws Exception {
        resultActions =
                mockMvc.perform(MockMvcRequestBuilders.post(recurso)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employee1)));
    }

    @Entao("deve ser retornado o stutas code {int}")
    public void deve_ser_retornado_o_stutas_code(Integer status) throws Exception {
        resultActions.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is(status));
    }

    @Entao("no response deve conter o funcionario salvo")
    public void no_response_deve_conter_o_funcionario_salvo() throws Exception {
        resultActions
                .andExpect(MockMvcResultMatchers.jsonPath("$.id",
                        CoreMatchers.notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName",
                        CoreMatchers.is(employee1.getFirstName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName",
                        CoreMatchers.is(employee1.getLastName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email",
                        CoreMatchers.is(employee1.getEmail())));
    }

    @Entao("deve ser persistido na base de dados o funcionario")
    public void deve_ser_persistido_na_base_de_dados_o_funcionario() {
        Assertions.assertThat(
                        employeeRepository.findByEmail(employee1.getEmail()).get())
                .isNotNull();
    }

    @Dado("dois funcionarios salvos")
    public void dois_funcionarios_salvos() {
        employee1 = Employee.builder()
                .firstName("Everson")
                .lastName("Santos")
                .email("rhevs@teste.com")
                .build();

        employee2 = Employee.builder()
                .firstName("Romulo")
                .lastName("Gomes")
                .email("romulo@teste.com")
                .build();

        List<Employee> listaEmployees = List.of(employee1, employee2);

        employeeRepository.saveAll(listaEmployees);
    }

    @Entao("no response deve conter a lista de funcionarios")
    public void no_response_deve_conter_a_lista_de_funcionarios() throws Exception {
        resultActions
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()",
                        CoreMatchers.is(2)));
    }

    @Quando("enviar uma requisicao do tipo GET para o recurso {string}")
    public void enviar_uma_requisicao_do_tipo_get_para_o_recurso(String recurso) throws Exception {
        resultActions =
                mockMvc.perform(MockMvcRequestBuilders.get(recurso));
    }
}
