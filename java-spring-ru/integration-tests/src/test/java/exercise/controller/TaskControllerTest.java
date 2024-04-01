package exercise.controller;

import org.junit.jupiter.api.Test;
import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;
import org.instancio.Instancio;
import org.instancio.Select;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

import java.util.HashMap;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.datafaker.Faker;
import exercise.repository.TaskRepository;
import exercise.model.Task;

// BEGIN
@SpringBootTest
@AutoConfigureMockMvc
// END
class ApplicationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Faker faker;

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TaskRepository taskRepository;


    @Test
    public void testWelcomePage() throws Exception {
        var result = mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();
        assertThat(body).contains("Welcome to Spring!");
    }

    @Test
    public void testIndex() throws Exception {
        var result = mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();
        assertThatJson(body).isArray();
    }


    private Task generateTask() {
        return Instancio.of(Task.class)
                .ignore(Select.field(Task::getId))
                .supply(Select.field(Task::getTitle), () -> faker.lorem().word())
                .supply(Select.field(Task::getDescription), () -> faker.lorem().paragraph())
                .create();
    }

    // BEGIN
    @Test
    public void testShow() throws Exception {
        var task = generateTask();
        taskRepository.save(task);

        var request = get("/tasks/" + task.getId());
        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();
        assertThatJson(body).and(
                a -> a.node("id").isEqualTo(task.getId()),
                a -> a.node("title").isEqualTo(task.getTitle()),
                a -> a.node("description").isEqualTo(task.getDescription()),
                a -> a.node("updatedAt").isEqualTo(task.getUpdatedAt().toString()),
                a -> a.node("createdAt").isEqualTo(task.getCreatedAt().toString())
        );
    }

    @Test
    public void testCreate() throws Exception {

        var data = new HashMap<>();
        data.put("title", faker.lorem().word());
        data.put("description",faker.lorem().paragraph());

        var request = post("/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(data));

        var result = mockMvc.perform(request)
                .andExpect(status().isCreated())
                .andReturn();

//        var body = result.getResponse().getContentAsString();
//
//        Task task = taskRepository.findById()
//
//        assertThatJson(body).and(
//                a -> a.node("id").isEqualTo(task.getId()),
//                a -> a.node("title").isEqualTo(task.getTitle()),
//                a -> a.node("description").isEqualTo(task.getDescription()),
//                a -> a.node("updatedAt").isEqualTo(task.getUpdatedAt().toString()),
//                a -> a.node("createdAt").isEqualTo(task.getCreatedAt().toString())
//        );

    }

    @Test
    public void testUpdate() throws Exception{

        var task = generateTask();
        taskRepository.save(task);

        var data = new HashMap<>();
        data.put("title", faker.lorem().word());
        data.put("description",faker.lorem().paragraph());

        var request = put("/tasks/" + task.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(data));

        mockMvc.perform(request)
                .andExpect(status().isOk());

        task = taskRepository.findById(task.getId()).get();
        assertThat(task.getTitle()).isEqualTo(data.get("title"));
        assertThat(task.getDescription()).isEqualTo(data.get("description"));
    }

    @Test
    public void testDelete() throws Exception{
        var task = generateTask();
        taskRepository.save(task);

        var request = delete("/tasks/" + task.getId());

        mockMvc.perform(request)
                .andExpect(status().isOk());
        assertThat(taskRepository.findById(task.getId())).isEmpty();
    }
    // END
}
