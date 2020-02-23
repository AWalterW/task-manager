package no.kristiania.ProjectManager;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProjectMemberDAO extends AbstractDAO<ProjectMember> {



    public ProjectMemberDAO(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    protected void insertObject(ProjectMember projectMember, PreparedStatement statement) throws SQLException {
        statement.setLong(1, projectMember.getProjectId());
        statement.setLong(2, projectMember.getMemberId());
    }

    public void insert(ProjectMember projectMember) throws SQLException {
        insert(projectMember, "INSERT INTO projectmembers (memberid, projectid) VALUES (?, ?)");
    }


    @Override
    protected ProjectMember readObject(ResultSet resultSet) throws SQLException {
        ProjectMember projectMember  = new ProjectMember();
        projectMember.setMemberId(resultSet.getInt("memberid"));
        projectMember.setProjectId(resultSet.getInt("projectid"));
        projectMember.setProjectName(resultSet.getString("projectname"));
        projectMember.setProjectStatus(resultSet.getString("status"));
        projectMember.setMemberName(resultSet.getString("membername"));
        return projectMember;
    }


    public ProjectMember delete(String memberId, String projectId) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            if (!memberId.isEmpty() && !projectId.isEmpty()) {
                try (PreparedStatement statement = connection.prepareStatement( "DELETE FROM projectmembers WHERE projectmembers.memberid = ? AND projectmembers.projectid = ?")) {
                    statement.setLong(1, Integer.parseInt(memberId));
                    statement.setLong(2, Integer.parseInt(projectId));
                    try (ResultSet resultSet = statement.executeQuery()) {
                        if (resultSet.rowUpdated()) {
                            return readObject(resultSet);
                        } else {
                            return null;
                        }
                    }
                }
            } else if (!memberId.isEmpty()) {
                try (PreparedStatement statement = connection.prepareStatement("DELETE FROM projectmembers WHERE projectmembers.memberid = ?")) {
                    statement.setLong(1, Integer.parseInt(memberId));
                    try (ResultSet resultSet = statement.executeQuery()) {
                        System.out.println(resultSet);
                        if (resultSet.rowUpdated()) {
                            return readObject(resultSet);
                        } else {
                            return null;
                        }
                    }
                }
            } else if (!projectId.isEmpty()) {
                try (PreparedStatement statement = connection.prepareStatement("DELETE FROM projectmembers WHERE projectmembers.projectid = ?")) {
                    statement.setLong(1, Integer.parseInt(projectId));
                    try (ResultSet resultSet = statement.executeQuery()) {
                        if (resultSet.rowUpdated()) {
                            return readObject(resultSet);
                        } else {
                            return null;
                        }
                    }
                }
            } else {
                return null;
            }
        }
    }

    String joinSelect = "SELECT projectmembers.memberid, projectmembers.projectid, projects.name as projectname, projects.status, members.name AS membername\n" +
            "FROM projects, members, projectmembers WHERE projects.id = projectmembers.projectid AND members.id = projectmembers.memberid";

    public List<ProjectMember> listAll(String memberId, String projectId) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            if (!memberId.isEmpty() && !projectId.isEmpty()) {
                try (PreparedStatement statement = connection.prepareStatement(joinSelect + " AND projectmembers.memberid = ? AND projectmembers.projectid = ?")) {
                    statement.setLong(1, Integer.parseInt(memberId));
                    statement.setLong(2, Integer.parseInt(projectId));
                    try (ResultSet resultSet = statement.executeQuery()) {
                        List<ProjectMember> projectMembers = new ArrayList<>();
                        while (resultSet.next()) {
                            projectMembers.add(readObject(resultSet));
                        }
                        return projectMembers;
                    }
                }
            } else if (!memberId.isEmpty()) {
                try (PreparedStatement statement = connection.prepareStatement(joinSelect + " AND projectmembers.memberid = ?")) {
                    statement.setLong(1, Integer.parseInt(memberId));
                    try (ResultSet resultSet = statement.executeQuery()) {
                        List<ProjectMember> projectMembers = new ArrayList<>();
                        while (resultSet.next()) {
                            projectMembers.add(readObject(resultSet));
                        }
                        return projectMembers;
                    }
                }
            } else if (!projectId.isEmpty()) {
                try (PreparedStatement statement = connection.prepareStatement(joinSelect + " AND projectmembers.projectid = ?")) {
                    statement.setLong(1, Integer.parseInt(projectId));
                    try (ResultSet resultSet = statement.executeQuery()) {
                        List<ProjectMember> projectMembers = new ArrayList<>();
                        while (resultSet.next()) {
                            projectMembers.add(readObject(resultSet));
                        }
                        return projectMembers;
                    }
                }
            } else {
                return listAll(joinSelect);
            }
        }
    }
}