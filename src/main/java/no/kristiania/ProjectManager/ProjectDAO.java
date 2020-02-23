package no.kristiania.ProjectManager;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProjectDAO extends AbstractDAO<Project> {

    public ProjectDAO(DataSource dataSource){
        super(dataSource);
    }

    public void insert(Project project) throws SQLException{
        insert(project, "INSERT INTO projects (NAME, DESCRIPTION, STATUS) VALUES (?, ?, ?)");
    }

    @Override
    protected void insertObject(Project project, PreparedStatement statement) throws SQLException{
        statement.setString(1, project.getName());
        statement.setString(2, project.getDescription());
        statement.setString(3, project.getStatus());
    }

    public Project retrieve(long id) throws SQLException {
        try(Connection connection = dataSource.getConnection()){
            try(PreparedStatement statement = connection.prepareStatement("SELECT * FROM projects WHERE ID = ?")) {
                statement.setLong(1, id);
                try(ResultSet resultSet = statement.executeQuery()) {
                    if(resultSet.next()) {
                        return readObject(resultSet);
                    } else {
                        return null;
                    }
                }
            }
        }
    }

    public Project edit(long id, String name, String description, String status) throws SQLException {
        try(Connection connection = dataSource.getConnection()){
            try(PreparedStatement statement = connection.prepareStatement("UPDATE projects SET name = ?, description = ?, status = ? WHERE id = ?")) {
                statement.setString(1, name);
                statement.setString(2, description);
                statement.setString(3, status);
                statement.setLong(4, id);
                try(ResultSet resultSet = statement.executeQuery()) {
                    if(resultSet.rowUpdated()) {
                        return readObject(resultSet);
                    } else {
                        return null;
                    }
                }
            }
        }
    }

    public Project delete(long id) throws SQLException {
        try(Connection connection = dataSource.getConnection()){
            try(PreparedStatement statement = connection.prepareStatement("DELETE FROM projects WHERE id = ?")) {
                statement.setLong(1, id);
                try(ResultSet resultSet = statement.executeQuery()) {
                    if(resultSet.rowDeleted()) {
                        return readObject(resultSet);
                    } else {
                        return null;
                    }
                }
            }
        }
    }

    public List<Project> listAll() throws SQLException {
        return listAll("SELECT * FROM projects");
    }

    String listExceptStatement = "SELECT * from projects AS p\n" +
            "                WHERE NOT EXISTS(SELECT * FROM projectmember AS pm\n" +
            "                        WHERE pm.projectid = p.id AND pm.memberid = ?)";

    public List<Project> listAllExcept(int memberId) throws SQLException {
    try(Connection connection = dataSource.getConnection()) {
        try (PreparedStatement statement = connection.prepareStatement(listExceptStatement)) {
            statement.setLong(1, memberId);
            try (ResultSet resultSet = statement.executeQuery()) {
                List<Project> projects = new ArrayList<>();
                while (resultSet.next()) {
                    projects.add(readObject(resultSet));
                }
                return projects;
                }
            }
        }
    }

    @Override
    protected Project readObject(ResultSet resultSet) throws SQLException{
        Project project = new Project();
        project.setId(resultSet.getInt("Id"));
        project.setName(resultSet.getString("Name"));
        project.setDescription(resultSet.getString("Description"));
        project.setStatus(resultSet.getString("Status"));
        return project;
    }
}
