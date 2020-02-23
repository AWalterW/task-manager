package no.kristiania.ProjectManager;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MemberDAO extends AbstractDAO<Member> {

    public MemberDAO(DataSource dataSource) {
        super(dataSource);
    }

    public long insert(Member member) throws SQLException {
        return insert(member, "INSERT INTO members (NAME, EMAIL) VALUES (?, ?)");
    }


    @Override
    protected void insertObject(Member member, PreparedStatement statement) throws SQLException {
        statement.setString(1, member.getName());
        statement.setString(2, member.getEmail());
    }

    @Override
    protected Member readObject(ResultSet resultSet) throws SQLException {
        Member member = new Member();
        member.setId(resultSet.getInt("id"));
        member.setName(resultSet.getString("name"));
        member.setEmail(resultSet.getString("email"));

        return member;
    }

    public Member retrieve(long id) throws SQLException {
        try(Connection connection = dataSource.getConnection()){
            try(PreparedStatement statement = connection.prepareStatement("SELECT * FROM members WHERE ID = ?")) {
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
    public Member edit(long id, String name, String email) throws SQLException {
        try(Connection connection = dataSource.getConnection()){
            try(PreparedStatement statement = connection.prepareStatement("UPDATE Members SET name = ?, email = ? WHERE id = ?")) {
                statement.setString(1, name);
                statement.setString(2, email);
                statement.setLong(3, id);
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

    public Member delete(long id) throws SQLException {
        try(Connection connection = dataSource.getConnection()){
            try(PreparedStatement statement = connection.prepareStatement("DELETE FROM members WHERE id = ?")) {
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


    public List<Member> listAll() throws SQLException {
        return listAll("SELECT * FROM members ORDER BY id");
    }

    String listExceptStatement = "SELECT * from members AS m WHERE NOT EXISTS(SELECT * FROM projectmembers AS pm WHERE pm.memberid = m.id AND pm.projectid = ?)";

    public List<Member> listAllExcept(int projectId) throws SQLException {
        try(Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(listExceptStatement)) {
                statement.setLong(1, projectId);
                try (ResultSet resultSet = statement.executeQuery()) {
                    List<Member> Members = new ArrayList<>();
                    while (resultSet.next()) {
                        Members.add(readObject(resultSet));
                    }
                    return Members;
                }
            }
        }
    }
}
