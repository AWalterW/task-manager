package no.kristiania.ProjectManager;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractDAO<T> {

    protected DataSource dataSource;

    public AbstractDAO(DataSource dataSource){
        this.dataSource = dataSource;
    }

    public long insert(T member, String sql) throws SQLException{
        try(Connection connection = dataSource.getConnection()) {
            try(PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
                insertObject(member, statement);
                statement.executeUpdate();

                ResultSet generatedKeys = statement.getGeneratedKeys();
                generatedKeys.next();
                return generatedKeys.getLong(1);
            }
        }
    }

    public long alter(T member, String sql) throws SQLException{
        try(Connection connection = dataSource.getConnection()){
            PreparedStatement statement = connection.prepareStatement(sql);{
                insertObject(member, statement);
                statement.executeUpdate();

                ResultSet generatedKeys = statement.getGeneratedKeys();
                generatedKeys.next();
                return generatedKeys.getLong(1);

            }
        }
    }

    protected abstract void insertObject(T member, PreparedStatement statement) throws SQLException;

    public List<T> listAll(String sql) throws SQLException{
        try(Connection connection = dataSource.getConnection()){
            try(PreparedStatement statement = connection.prepareStatement(sql)){
                try(ResultSet resultSet = statement.executeQuery()){
                    List<T> members = new ArrayList<>();
                    while (resultSet.next()){
                        members.add(readObject(resultSet));
                    }
                    return members;
                }
            }
        }
    }

    protected abstract T readObject(ResultSet resultSet) throws SQLException;


}
