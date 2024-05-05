package hello.hellospring.repository;

import hello.hellospring.domain.Member;
import org.springframework.jdbc.datasource.DataSourceUtils;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcMemberRepository implements MemberRepository {
    private final DataSource dataSource; //DB에 붙기 위해서 DataSource가 필요하다

    public JdbcMemberRepository(DataSource dataSource) { //스프링을 통해 DataSource를 주입받을 수 있다
        this.dataSource = dataSource;
    }

    @Override
    public Member save(Member member) {
        String sql = "insert into member(name) values(?)";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null; //ResultSet은 결과를 받는 역할이다

        try {
            conn = getConnection(); //getConnection을 통해 커넥션을 가져온다
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS); //prepareStatement에 sql과 옵션을 넘겨준다.
            //RETURN_GENERATED_KEYS : DB에 insert해야만 알 수 있는 id 값을 얻을 때 사용한다

            pstmt.setString(1, member.getName());
            //setString에서 parameterIndex 위치에 1을 넣어주면 위 String sql의 첫번째 물음표와 매칭되고 그 위치에 member.getName()으로 값을 넣는다

            pstmt.executeUpdate(); //executeUpdate를 통해 실제 DB에 쿼리가 날라간다
            rs = pstmt.getGeneratedKeys(); //getGeneratedKeys는 RETURN_GENERATED_KEYS를 통해 얻은 id값을 반환해준다

            if (rs.next()) { //만약 rs 다음 값이 있다면 getLong을 통해 값을 꺼내 id 값을 세팅한다
                member.setId(rs.getLong(1));
            } else { //없으면 Exception 처리한다
                throw new SQLException("id 조회 실패");
            }
            return member;
        } catch (Exception e) {
            throw new IllegalStateException(e);
        } finally { //할 일이 끝나면 연결 세션을 종료한다
            close(conn, pstmt, rs);
        }
    }

    @Override
    public Optional<Member> findById(Long id) {
        String sql = "select * from member where id = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, id);

            rs = pstmt.executeQuery(); //executeQuery를 통해 result 값을 받아온다

            if (rs.next()) { //값이 있으면 멤버 객체를 만들고 반환을 해준다
                Member member = new Member();
                member.setId(rs.getLong("id"));
                member.setName(rs.getString("name"));
                return Optional.of(member);
            } else {
                return Optional.empty();
            }
        } catch (Exception e) {
            throw new IllegalStateException(e);
        } finally {
            close(conn, pstmt, rs);
        }
    }

    @Override
    public List<Member> findAll() {
        String sql = "select * from member";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);

            rs = pstmt.executeQuery();

            List<Member> members = new ArrayList<>(); //리스트를 사용한다

            while (rs.next()) { //루프를 돌면서 add를 통해 멤버를 담은 후 반환한다
                Member member = new Member();
                member.setId(rs.getLong("id"));
                member.setName(rs.getString("name"));
                members.add(member);
            }
            return members;
        } catch (Exception e) {
            throw new IllegalStateException(e);
        } finally {
            close(conn, pstmt, rs);
        }
    }

    @Override
    public Optional<Member> findByName(String name) {
        String sql = "select * from member where name = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, name);

            rs = pstmt.executeQuery();

            if (rs.next()) { //값이 있으면 객체를 생성한다
                Member member = new Member();
                member.setId(rs.getLong("id"));
                member.setName(rs.getString("name"));
                return Optional.of(member);
            }
            return Optional.empty();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        } finally {
            close(conn, pstmt, rs);
        }
    }

    private Connection getConnection() {
        //DataSourceUtils를 사용하면 DB 트랜잭션에 걸렸을 때 DB 커넥션을 똑같이 유지시켜 준다
        return DataSourceUtils.getConnection(dataSource);
    }

    private void close(Connection conn, PreparedStatement pstmt, ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (pstmt != null) {
                pstmt.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (conn != null) {
                close(conn);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void close(Connection conn) throws SQLException {
        DataSourceUtils.releaseConnection(conn, dataSource);
    }
}
