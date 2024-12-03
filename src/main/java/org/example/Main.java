package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Main {
    public static Connection connection;
    public static Statement statement;

    private static void connect() {
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "Pwt4950z");
            statement = connection.createStatement();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    public static void disconnect() {
        try {
            statement.close();
        } catch (SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }

        try {
            connection.close();
        } catch (SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        connect();

        try {
//            statement.execute("drop table progress; drop table students; drop table subjects;");

//            statement.execute("create table students (id serial primary key not null, name text not null, seriya smallint not null, number int not null, check (seriya::text ~ '^\\d{4}$'), check (number::text ~ '^\\d{6}$'))");
//
//            statement.execute("create table subjects (id serial primary key not null, name text not null);");
//
//            statement.execute("create table progress (id serial primary key not null, student_id integer not null references students (id) on delete cascade on update cascade, subject_id integer not null references subjects (id), mark integer null, check (mark >= 2 and mark <= 5));");
//
//            statement.execute("insert into students (id, name, seriya, number) values (1, 'semyon', '1111', '222222'), (2, 'oleg', '2222', '123123'), (3, 'egor', '3333', '123132'), (4, 'alex', '4444', '333222'), (5, 'nikita', '5555', '112233'), (6, 'yuliya', '6666', '123123'), (7, 'nataliya', '7777', '345345'), (8, 'daniil', '8888', '666666'), (9, 'nikolay', '9999', '777777'), (10, 'igor', '9988', '888888');");
//
//            statement.execute("insert into subjects (id, name) values (1, 'русский'), (2, 'английский язык'), (3, 'математика'), (4, 'биология'), (5, 'физика');");

//            statement.execute("insert into progress(id, student_id, subject_id, mark) values " +
//                    "(1, 1, 1, '2'), (2, 1, 2, '3'), (3, 1, 3, '5'), (4, 1, 4, '4'),(5, 1, 5, '5')," +
//                    "(6, 2, 1, '5'), (7, 2, 2, '4'), (8, 2, 3, '4'), (9, 2, 4, '5'), (10, 2, 5, '4'), " + // 45 oleg
//                    "(11, 3, 1, '4'), (12, 3, 2, '4'), (13, 3, 3, '3'), (14, 3, 4, '3'), (15, 3, 5, '5'), " +
//                    "(16, 4, 1, '5'), (17, 4, 2, '5'), (18, 4, 3, '5'), (19, 4, 4, '5'), (20, 4, 5, '5'), " + // 5 alex
//                    "(21, 5, 1, '3'), (22, 5, 2, '3'), (23, 5, 3, '3'), (24, 5, 4, '3'), (25, 5, 5, '3'), " +
//                    "(26, 6, 1, '4'), (27, 6, 2, '4'), (28, 6, 3, '4'), (29, 6, 4, '4'), (30, 6, 5, '4'), " + // 4 yuliya
//                    "(31, 7, 1, '2'), (32, 7, 2, '2'), (33, 7, 3, '4'), (34, 7, 4, '2'), (35, 7, 5, '2'), " +
//                    "(36, 8, 1, '5'), (37, 8, 2, '2'), (38, 8, 3, '3'), (39, 8, 4, '4'), (40, 8, 5, '4');");
//
////уник серия и номер паспорта
//            statement.execute("alter table students add constraint unic unique (seriya, number);");

            System.out.println("Студенты, претендующие на стипендию");
            var result = statement.executeQuery(
                    "SELECT st.id, st.name " +
                            "FROM students st " +
                            "LEFT JOIN progress p ON st.id = p.student_id " +
                            "GROUP BY st.id, st.name " +
                            "HAVING COUNT(CASE WHEN p.mark IS NULL OR p.mark < 4 THEN 1 END) = 0 " +
                            "   AND COUNT(p.id) = (SELECT COUNT(*) FROM subjects);"
            );

            while (result.next()) {
                int id = result.getInt("id");
                String name = result.getString("name");
                System.out.println("ID: " + id + ", Name: " + name);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        disconnect();
    }
}
