package no.kristiania.ProjectManager;

import org.flywaydb.core.Flyway;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

class MemberDAOTest {

    private MemberDAO dao;

    @BeforeEach
    void setup(){
        JdbcDataSource dataSource = createDataSource();
        dao = new MemberDAO(dataSource);
    }

    static JdbcDataSource createDataSource(){
        JdbcDataSource dataSource = new JdbcDataSource();
        dataSource.setUrl("jdbc:h2:mem:memberTest;DB_CLOSE_DELAY=-1");
        Flyway.configure().dataSource(dataSource).load().migrate();
        return dataSource;
    }

    @Test
    void shouldListSavedMembers() throws SQLException{
        Member member = sampleMember();
        dao.insert(member);
        assertThat(dao.listAll())
                .extracting(Member::getName)
                .contains(member.getName());
    }

    @Test
    public void shouldRetrieveSavedMember() throws SQLException{
        Member member = sampleMember();
        dao.insert(member);
        assertThat(member).hasNoNullFieldsOrProperties();
        System.out.println(member);
        assertThat((member));
    }


    public static Member sampleMember() {
        Member member = new Member();
        member.setId(1);
        member.setName("NameExample");
        member.setEmail("Email@Example.com");
        return member;
    }


}