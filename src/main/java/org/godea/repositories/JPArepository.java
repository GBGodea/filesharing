package org.godea.repositories;

import java.util.List;
import java.util.Optional;

public abstract class JPArepository<T, ID> implements GenericRepository<T, ID> {
    @Override
    public T save(T entity) {
        return null;
    }

    @Override
    public Optional<T> findById(ID id) {
        return Optional.empty();
    }

    @Override
    public List<T> findAll() {
        return null;
    }

    @Override
    public void delete(T entity) {

    }


//    default void findBy(String to, ServletContext context) {
//        DataSource source = (DataSource) context.getAttribute("myDataSource");
//
//        try (Connection connection = source.getConnection()) {
//            Statement statement = connection.createStatement();
//
//            String find = "";
//            if(to.equals("*")) {
//                find = "SELECT * from users";
//            } else {
//                find = "SELECT * FROM users WHERE email = " + "'" + to + "'";
//                System.out.println(find);
//            }
//            ResultSet rs = statement.executeQuery(find);
//            ResultSetMetaData md = rs.getMetaData();
//            int columnCount = md.getColumnCount();
//
//            while (rs.next()) {
//                StringBuilder row = new StringBuilder();
//                for (int i = 1; i <= columnCount; i++) {
//                    String colName = md.getColumnName(i);
//                    String colValue = rs.getString(i);
//                    row.append(colName)
//                            .append("=")
//                            .append(colValue)
//                            .append(";     ");
//                }
//                System.out.println(row);
//            }
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//    }
}
