package no.kristiania.ProjectManager;

import org.flywaydb.core.Flyway;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

class ProjectDAOTest {

    private static Random random = new Random();
    private JdbcDataSource dataSource;
    private ProjectDAO projectDAO;

    @BeforeEach
    void setUp(){
        dataSource = createDataSource();
        projectDAO = new ProjectDAO(dataSource);
    }

    static JdbcDataSource createDataSource(){
        JdbcDataSource dataSource = new JdbcDataSource();
        dataSource.setUrl("jdbc:h2:mem:taskTest;DB_CLOSE_DELAY=-1");
        Flyway.configure().dataSource(dataSource).load().migrate();
        return dataSource;
    }

    @Test
    void shouldFindSavedTasks() throws SQLException{

        Project project = new Project();
        project.setName("ProjectExample");
        project.setStatus("ExampleStatus");
        project.setDescription("DescriptionExample");
        ProjectDAO projectDAO = new ProjectDAO(dataSource);

        projectDAO.insert(project);
        assertThat(projectDAO.listAll().contains(project));

    }

    @Test
    void shouldRetrieveStoredProject() throws SQLException{
        ProjectDAO projectDAO = new ProjectDAO(dataSource);
        Project project = sampleProject();
        projectDAO.insert(project);
        assertThat(projectDAO.listAll()).contains(project);

    }

    private Project sampleProject() {
        Project project = new Project();
        project.setId(1);
        project.setName("ProjectExample");
        project.setStatus("ExampleStatus");
        project.setDescription("DescriptionExample");
        return project;
    }
}